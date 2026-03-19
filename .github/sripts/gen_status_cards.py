# ---------------------------------------------
# gen_status_cards.py
# Génère un SVG pour chaque label "status:*"
# Utilise github_data.fetch_status_labels()
# ---------------------------------------------

import logging
from github_data import fetch_status_labels
from common import STYLE, estimate_text_size
import os

OUTPUT_DIR = os.path.join(".github", "generated_svgs")

# Crée le dossier si inexistant
os.makedirs(OUTPUT_DIR, exist_ok=True)

logging.basicConfig(level=logging.INFO, format='[%(levelname)s] %(message)s')


def generate_status_svg(label_text: str, label_color: str, count: int) -> str:
    """
    Construit un SVG affichant :
    - un badge de couleur
    - le nom du label
    - le nombre d'issues associées
    """

    width = 130
    height = 110

    text_width, _ = estimate_text_size(label_text)
    label_width = text_width + 30

    return f"""
<svg width="{width}" height="{height}" xmlns="http://www.w3.org/2000/svg">
{STYLE}

<rect class="card" x="0" y="0" width="{width}" height="{height}" />

<g transform="translate({width/2}, 20)">
    <rect x="{-label_width/2}" y="0" width="{label_width}" height="24"
          rx="12" fill="{label_color}" stroke="{label_color}" stroke-width="0.5"/>
    <text class="label" x="0" y="17" text-anchor="middle" fill="white">{label_text}</text>
</g>

<text class="counter" x="{width/2}" y="90" text-anchor="middle">{count}</text>

</svg>
"""


if __name__ == "__main__":
    labels = fetch_status_labels()
    for label in labels:
        svg = generate_status_svg(label.name, label.color, label.issue_count)

        # Nom de fichier basé sur label
        filename = f"status_{label.name.replace(':','_')}.svg"
        filepath = os.path.join(OUTPUT_DIR, filename)
        with open(filepath, "w") as f:
            f.write(svg)

        logging.info(f"SVG généré : {filename}")
