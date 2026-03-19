# ---------------------------------------------
# gen_issue_cards.py
# Génère des cartes SVG complètes pour les issues
# ---------------------------------------------

import logging
from github_data import fetch_top_issues
from common import STYLE
import os

OUTPUT_DIR = os.path.join(".github", "generated_svgs")

# Crée le dossier si inexistant
os.makedirs(OUTPUT_DIR, exist_ok=True)

logging.basicConfig(level=logging.INFO, format='[%(levelname)s] %(message)s')


def generate_issue_svg(issue):
    """
    Construit un SVG simplifié d'une issue :
      - icône selon le type
      - titre + numéro
      - labels
      - compte de commentaires
    """

    width = 600
    height = 120

    labels_svg = ""
    x_offset = width - 10

    for label in issue.labels:
        labels_svg += f"""
        <rect x="{x_offset - 80}" y="10" width="70" height="20" rx="10" fill="{label.color}" />
        <text x="{x_offset - 45}" y="25" text-anchor="middle" font-size="12" fill="white">{label.name}</text>
        """
        x_offset -= 90

    return f"""
<svg width="{width}" height="{height}" xmlns="http://www.w3.org/2000/svg">
{STYLE}

<rect class="card" x="0" y="0" width="{width}" height="{height}" />

<text class="title" x="20" y="40" font-size="18">{issue.title} <tspan fill="#999">#{issue.number}</tspan></text>

<text x="20" y="70" fill="#666">Créé par {issue.author} · {issue.comment_count} commentaires</text>

{labels_svg}

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

        logging.info(f"SVG généré : {filename}")
``
