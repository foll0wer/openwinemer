# ---------------------------------------------------------
# github_data.py
# Version propre et adaptée pour : foll0wer/openwinemer
# - Récupère les issues via GitHub GraphQL
# - Fournit : auteur + avatar, assignés + avatars, labels
# - Compatible avec gen_issue_cards.py (avatars sur la droite)
# ---------------------------------------------------------

import os
import requests
import logging
import json
from dataclasses import dataclass
from typing import List, Optional, Dict, Any

# ---------------------------------------------------------
# CONFIGURATION DU REPO
# ---------------------------------------------------------

REPO_OWNER = "foll0wer"
REPO_NAME = "openwinemer"

GITHUB_TOKEN = os.environ.get("GITHUB_TOKEN")
if not GITHUB_TOKEN:
    raise ValueError("La variable d'environnement GITHUB_TOKEN n'est pas définie.")

API_GRAPHQL = "https://api.github.com/graphql"

HEADERS = {
    "Authorization": f"Bearer {GITHUB_TOKEN}",
    "Content-Type": "application/json"
}

logging.basicConfig(level=logging.INFO, format='[%(levelname)s] %(message)s')


# ---------------------------------------------------------
# STRUCTURES DE DONNÉES
# ---------------------------------------------------------

@dataclass
class Label:
    name: str
    color: str

@dataclass
class Issue:
    number: int
    title: str
    state: str
    url: str

    author: str
    author_avatar: str

    created_at: str
    comment_count: int
    last_commented_at: Optional[str]

    labels: List[Label]
    assignees: List[Dict]   # [{"login":..., "avatar":...}]


@dataclass
class StatusLabel:
    name: str
    color: str
    issue_count: int


# ---------------------------------------------------------
# PARSEUR D'ISSUE
# ---------------------------------------------------------

def parse_issue(data: Dict[str, Any]) -> Issue:
    """ Convertit un noeud GraphQL en Issue Python """

    # Auteur
    author = data["author"]["login"] if data["author"] else "unknown"
    author_avatar = data["author"]["avatarUrl"] if data["author"] else ""

    # Labels
    labels = [
        Label(lbl["name"], f"#{lbl['color']}")
        for lbl in data["labels"]["nodes"]
    ]

    # Assignés
    assignees = [
        {"login": a["login"], "avatar": a["avatarUrl"]}
        for a in data["assignees"]["nodes"]
    ]

    # Dernier commentaire
    last_comment_date = None
    if data["comments"]["nodes"]:
        last_comment_date = data["comments"]["nodes"][0]["createdAt"]

    return Issue(
        number=data["number"],
        title=data["title"],
        state=data["state"],
        url=data["url"],

        author=author,
        author_avatar=author_avatar,

        created_at=data["createdAt"],
        comment_count=data["comments"]["totalCount"],
        last_commented_at=last_comment_date,

        labels=labels,
        assignees=assignees
    )


# ---------------------------------------------------------
# FETCH : STATUS LABELS (status:xxx)
# ---------------------------------------------------------

def fetch_status_labels() -> List[StatusLabel]:
    logging.info("Récupération des labels status:* …")

    query = f"""
    query {{
      repository(owner: "{REPO_OWNER}", name: "{REPO_NAME}") {{
        labels(first: 50) {{
          nodes {{
            name
            color
            issues {{ totalCount }}
          }}
        }}
      }}
    }}
    """

    resp = requests.post(API_GRAPHQL, headers=HEADERS, json={"query": query})
    resp.raise_for_status()

    raw = resp.json()["data"]["repository"]["labels"]["nodes"]

    return [
        StatusLabel(item["name"], f"#{item['color']}", item["issues"]["totalCount"])
        for item in raw
        if item["name"].startswith("status:")
    ]


# ---------------------------------------------------------
# FETCH : TOP ISSUES (ordre décroissant de mise à jour)
# ---------------------------------------------------------

def fetch_top_issues(count: int = 6) -> List[Issue]:
    logging.info("Récupération des issues…")

    query = f"""
    query {{
      repository(owner: "{REPO_OWNER}", name: "{REPO_NAME}") {{
        issues(
          first: {count},
          orderBy: {{field: UPDATED_AT, direction: DESC}}
        ) {{
          nodes {{
            number
            title
            state
            url
            createdAt
            updatedAt

            author {{
              login
              avatarUrl(size: 40)
            }}

            assignees(first: 10) {{
              nodes {{
                login
                avatarUrl(size: 40)
              }}
            }}

            labels(first: 10) {{
              nodes {{
                name
                color
              }}
            }}

            comments(first: 50, orderBy: {{field: UPDATED_AT, direction: DESC}}) {{
              totalCount
              nodes {{
                createdAt
              }}
            }}
          }}
        }}
      }}
    }}
    """

    resp = requests.post(API_GRAPHQL, headers=HEADERS, json={"query": query})
    resp.raise_for_status()

    issues_raw = resp.json()["data"]["repository"]["issues"]["nodes"]

    issues = [parse_issue(node) for node in issues_raw]

    return issues
