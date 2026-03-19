# ---------------------------------------------
# github_data.py
# Ce fichier interroge l'API GraphQL de GitHub pour récupérer :
#  - les labels de type "status:*"
#  - les issues (pinned + récentes)
# Les données sont utilisées par gen_status_cards.py et gen_issue_cards.py
# ---------------------------------------------

import os
import requests
import logging
import json
from dataclasses import dataclass
from enum import Enum, auto
from typing import List, Optional, Dict, Any, Tuple

# ---------------------------------------------
# CONFIGURATION DU REPO CIBLÉ
# ---------------------------------------------
REPO_OWNER = "foll0wer"
REPO_NAME = "openwinemer"

# Le token doit être dans GitHub Secrets → GITHUB_TOKEN
GITHUB_TOKEN = os.environ.get("GITHUB_TOKEN")
if not GITHUB_TOKEN:
    raise ValueError("La variable d'environnement GITHUB_TOKEN n'est pas définie")

# Endpoints GitHub (REST + GraphQL)
API_BASE_URL_REST = "https://api.github.com"
API_BASE_URL_GRAPHQL = "https://api.github.com/graphql"

# Headers des requêtes API
HEADERS_GRAPHQL = {
    "Authorization": f"Bearer {GITHUB_TOKEN}",
    "Content-Type": "application/json"
}

HEADERS_REST = {
    "Authorization": f"Bearer {GITHUB_TOKEN}",
    "Accept": "application/vnd.github.v3+json",
}

logging.basicConfig(level=logging.INFO, format='[%(levelname)s] %(message)s')


# ---------------------------------------------
# DÉFINITIONS DES TYPES DE DONNÉES
# ---------------------------------------------

class IssueType(Enum):
    BUG = auto()
    FEATURE = auto()
    OTHER = auto()

class IssueState(Enum):
    OPEN = auto()
    COMPLETED = auto()
    CLOSED = auto()

class PrState(Enum):
    OPEN = auto()
    CLOSED = auto()
    MERGED = auto()

@dataclass
class Label:
    name: str
    color: str

@dataclass
class Issue:
    number: int
    title: str
    type: IssueType
    state: IssueState
    url: str
    author: str
    created_at: str
    labels: List[Label]
    comment_count: int
    last_commented_at: Optional[str] = None

@dataclass
class StatusLabel:
    name: str
    color: str
    issue_count: int


# ---------------------------------------------
# RÉCUPÉRATION DES LABELS "status:*"
# ---------------------------------------------
def fetch_status_labels() -> List[StatusLabel]:
    """
    Récupère tous les labels dans le repo puis filtre ceux qui commencent par "status:".
    """
    logging.info("Récupération des labels de statut…")

    query = f"""
    query {{
      repository(owner: "{REPO_OWNER}", name: "{REPO_NAME}") {{
        labels(first: 50) {{
          nodes {{
            name
            color
            issues {{
              totalCount
            }}
          }}
        }}
      }}
    }}
    """

    resp = requests.post(API_BASE_URL_GRAPHQL, headers=HEADERS_GRAPHQL, json={"query": query})
    resp.raise_for_status()

    raw = resp.json()["data"]["repository"]["labels"]["nodes"]
    labels = []

    for item in raw:
        if item["name"].startswith("status:"):
            labels.append(
                StatusLabel(
                    name=item["name"],
                    color=f"#{item['color']}",
                    issue_count=item["issues"]["totalCount"]
                )
            )

    return labels


# ---------------------------------------------
# RÉCUPÉRATION DES ISSUES (les 10 dernières)
# ---------------------------------------------
def fetch_top_issues(count: int = 6) -> List[Issue]:
    """
    Récupère les issues dans l'ordre décroissant de mise à jour.
    """
    logging.info("Récupération des issues…")

    query = f"""
    query {{
      repository(owner: "{REPO_OWNER}", name: "{REPO_NAME}") {{
        issues(first: 20, orderBy: {{field: UPDATED_AT, direction: DESC}}) {{
          nodes {{
            number
            title
            url
            state
            createdAt
            author {{ login }}
            labels(first: 10) {{
              nodes {{
                name
                color
              }}
            }}
            comments {{
              totalCount
            }}
            updatedAt
          }}
        }}
      }}
    }}
    """

    resp = requests.post(API_BASE_URL_GRAPHQL, headers=HEADERS_GRAPHQL, json={"query": query})
    resp.raise_for_status()

    nodes = resp.json()["data"]["repository"]["issues"]["nodes"]

    issues: List[Issue] = []

    for item in nodes[:count]:
        # Détection du type
        title = item["title"]
        issue_type = IssueType.OTHER
        if title.startswith("[Bug]"):
            issue_type = IssueType.BUG
            title = title.replace("[Bug]", "").strip()
        elif title.startswith("[Feature]"):
            issue_type = IssueType.FEATURE
            title = title.replace("[Feature]", "").strip()

        # Détection état
        state = IssueState.OPEN if item["state"] == "OPEN" else IssueState.CLOSED

        # Labels
        labels = [Label(lbl["name"], f"#{lbl['color']}") for lbl in item["labels"]["nodes"]]

        issues.append(
            Issue(
                number=item["number"],
                title=title,
                type=issue_type,
                state=state,
                url=item["url"],
                author=item["author"]["login"],
                created_at=item["createdAt"],
                labels=labels,
                comment_count=item["comments"]["totalCount"],
                last_commented_at=item["updatedAt"],
            )
        )

    return issues
