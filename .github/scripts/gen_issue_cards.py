# ---------------------------------------------------------
# gen_issue_cards.py
# Génère les cartes SVG pour les issues de foll0wer/openwinemer
# Avatar = assigné principal si existant, sinon auteur
# Labels = taille dynamique ajustée au texte
# Style = common.py (GitHub-like, transparent + dark mode)
# Exports = .github/generated_svgs/
# ---------------------------------------------------------

import logging
import os
import base64
import requests

from github_data import fetch_top_issues
from common import STYLE, estimate_text_size

logging.basicConfig(level=logging.INFO, format='[%(levelname)s] %(message)s')

# ---------------------------------------------------------
# Dossier de sortie
# ---------------------------------------------------------
OUTPUT_DIR = os.path.join(".github", "generated_svgs")
os.makedirs(OUTPUT_DIR, exist_ok=True)


# ---------------------------------------------------------
# Fonction utilitaire : télécharger avatar → Base64
# ---------------------------------------------------------
def fetch_base64_image(url: str) -> str:
    """Télécharge une image et renvoie sa base64 ou '' si erreur."""
    if not url:
        return ""
    try:
        data = requests.get(url, timeout=6)
        data.raise_for_status()
        return base64.b64encode(data.content).decode("utf-8")
    except:
        return ""


# ---------------------------------------------------------
# Génération d'une carte issue → SVG
# ---------------------------------------------------------
def generate_issue_svg(issue):
    width = 680
    height = 130
    padding = 14

    # ---------------------------------------------------------
    # 1) Avatar → assigné principal ou auteur
    # ---------------------------------------------------------
    if issue.assignees and len(issue.assignees) > 0:
        avatar_url = issue.assignees[0]["avatar"]
        avatar_login = issue.assignees[0]["login"]
    else:
        avatar_url = issue.author_avatar
        avatar_login = issue.author

    avatar_b64 = fetch_base64_image(avatar_url)
    avatar_size = 42
    avatar_x = width - padding - avatar_size / 2
    avatar_y = height / 2

    # ---------------------------------------------------------
    # 2) Labels dynamiques
    # ---------------------------------------------------------
    labels_svg = ""
    lx = width - padding - avatar_size - 24  # marge avant avatar

    for lbl in issue.labels:
        # Estimation largeur texte avec ta fonction common.py
        text_w, text_h = estimate_text_size(lbl.name, 14)
        padding_lr = 24  # 12 px à gauche/droite comme GitHub
        label_w = text_w + padding_lr

        labels_svg += f"""
        <g transform="translate({lx - label_w}, {padding})">
            <rect class="labelBg"
                  x="0" y="0"
                  width="{label_w}" height="26" rx="13"
                  fill="{lbl.color}" stroke="{lbl.color}" />
            <text class="label"
                  x="{label_w/2}" y="17"
                  text-anchor="middle"
                  fill="white">{lbl.name}</text>
        </g>
        """
        lx -= label_w + 10  # espacement entre labels

    # ---------------------------------------------------------
    # 3) Construction du SVG complet
    # ---------------------------------------------------------
    return f"""
<svg width="{width}" height="{height}" xmlns="http://www.w3.org/2000/svg">
{STYLE}

<!-- Carte -->
<rect class="card" x="0" y="0" width="{width}" height="{height}"/>

<!-- Titre -->
<text class="title" x="{padding}" y="42">
    {issue.title} <tspan class="muted">#{issue.number}</tspan>
</text>

<!-- Sous-ligne : auteur + commentaires -->
<text class="muted" x="{padding}" y="72">
    Ouverte par {issue.author} · {issue.comment_count} commentaires
</text>

<!-- Labels -->
{labels_svg}

<!-- Avatar (cercle + clipping) -->
<defs>
    <clipPath id="avatar-clip">
        <circle cx="0" cy="0" r="{avatar_size/2}" />
    </clipPath>
</defs>

<g transform="translate({avatar_x}, {avatar_y})">
    <image x="{-(avatar_size/2)}" y="{-(avatar_size/2)}"
           width="{avatar_size}" height="{avatar_size}"
           href="data:image/png;base64,{avatar_b64}"
           clip-path="url(#avatar-clip)" />
    <circle cx="0" cy="0" r="{avatar_size/2}"
            fill="none" stroke="var(--border-color)" stroke-width="1.2"/>
</g>

</svg>
"""


# ---------------------------------------------------------
# Script principal
# ---------------------------------------------------------
if __name__ == "__main__":
    issues = fetch_top_issues(6)

    for i, issue in enumerate(issues):
        svg = generate_issue_svg(issue)
        filename = f"issue_{i}.svg"
        filepath = os.path.join(OUTPUT_DIR, filename)

        with open(filepath, "w") as f:
            f.write(svg)

        logging.info(f"[OK] SVG généré : {filepath}")
