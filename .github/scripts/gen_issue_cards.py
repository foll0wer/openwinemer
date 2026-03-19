# ---------------------------------------------------------
# gen_issue_cards.py
# Génère les cartes SVG pour les issues de foll0wer/openwinemer
# Avatar affiché (assigné principal → fallback auteur)
# Carte transparente + style GitHub du fichier common.py
# ---------------------------------------------------------

import logging
import os
import base64
import requests
from github_data import fetch_top_issues
from common import STYLE

logging.basicConfig(level=logging.INFO, format='[%(levelname)s] %(message)s')

# Dossier de destination des SVG
OUTPUT_DIR = os.path.join(".github", "generated_svgs")
os.makedirs(OUTPUT_DIR, exist_ok=True)


def fetch_base64_image(url: str) -> str:
    """Télécharge l’image et la convertit en base64."""
    try:
        data = requests.get(url)
        data.raise_for_status()
        return base64.b64encode(data.content).decode("utf-8")
    except Exception:
        return ""  # fallback silencieux


def generate_issue_svg(issue):
    """
    Construit une carte SVG contenant :
    - Titre + numéro
    - Label(s)
    - Auteur / assigné principal
    - Avatar aligné à droite (cercle + clipping)
    """

    width = 650
    height = 120
    padding = 12

    # -------------------------------------------------------
    # 1) DÉTERMINATION DE LA PERSONNE À AFFICHER
    # -------------------------------------------------------
    # Issue.assignées est déjà fourni par github_data
    if hasattr(issue, "assignees") and issue.assignees:
        avatar_url = issue.assignees[0]["avatar"]
        avatar_user = issue.assignees[0]["login"]
    else:
        avatar_url = issue.author_avatar
        avatar_user = issue.author

    avatar_b64 = fetch_base64_image(avatar_url)

    # Position avatar
    avatar_size = 40
    avatar_x = width - padding - avatar_size / 2
    avatar_y = height / 2

    # -------------------------------------------------------
    # 2) LABELS
    # -------------------------------------------------------
    labels_svg = ""
    lx = width - padding - avatar_size - 20  # On laisse une marge avant avatar

    for lbl in issue.labels:
        label_w = 14 * len(lbl.name) + 20
        labels_svg += f"""
            <g transform="translate({lx - label_w}, {padding})">
                <rect class="labelBg" x="0" y="0" width="{label_w}" height="24" rx="12"
                      fill="{lbl.color}" stroke="{lbl.color}"/>
                <text class="label" x="{label_w/2}" y="16" text-anchor="middle" fill="white">
                    {lbl.name}
                </text>
            </g>
        """
        lx -= label_w + 10

    # -------------------------------------------------------
    # 3) SVG FINAL
    # -------------------------------------------------------
    return f"""
<svg width="{width}" height="{height}" xmlns="http://www.w3.org/2000/svg">
{STYLE}

<!-- Carte globale -->
<rect class="card" x="0" y="0" width="{width}" height="{height}"/>

<!-- Titre de l'issue -->
<text class="title" x="{padding}" y="40">
    {issue.title} <tspan class="muted">#{issue.number}</tspan>
</text>

<!-- Description: auteur + commentaires -->
<text class="muted" x="{padding}" y="70">
    Ouverte par {issue.author} · {issue.comment_count} commentaires
</text>

<!-- Labels à gauche -->
{labels_svg}

<!-- Avatar en cercle (clipPath) -->
<defs>
    <clipPath id="avatar-clip">
        <circle cx="0" cy="0" r="{avatar_size/2}" />
    </clipPath>
</defs>

<g transform="translate({avatar_x}, {avatar_y})">
    <image href="data:image/png;base64,{avatar_b64}"
           x="-{avatar_size/2}" y="-{avatar_size/2}"
           width="{avatar_size}" height="{avatar_size}"
           clip-path="url(#avatar-clip)" />
    <circle cx="0" cy="0" r="{avatar_size/2}" 
            fill="none" stroke="var(--border-color)" stroke-width="1.2"/>
</g>

</svg>
"""


if __name__ == "__main__":
    issues = fetch_top_issues(6)

    for i, issue in enumerate(issues):
        svg = generate_issue_svg(issue)
        filename = f"issue_{i}.svg"

        filepath = os.path.join(OUTPUT_DIR, filename)
        with open(filepath, "w") as f:
            f.write(svg)

        logging.info(f"SVG généré : {filepath}")
