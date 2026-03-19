# FICHIER : generate_issue_cards.py
# -----------------------------------------------------------
# Génère des "issue cards" SVG avec le même design que
# brenocq/implot3d
# -----------------------------------------------------------

import requests
from datetime import datetime

API_URL = "https://api.github.com/repos/foll0wer/openwinemer/issues"


# -----------------------------------------------------------
# Style repris de brenocq/implot3d
# -----------------------------------------------------------
STYLE = """
<style>

:root {
    --fgColor-default: #e6edf3;
    --fgColor-muted: #7d8590;
    --fgColor-accent: #4493f8;
    --fgColor-danger: #f85149;
    --fgColor-success: #3fb950;

    --bgColor-card: #0d1117;
    --border-color: #30363d;

    --text-weight-normal: 400;
    --text-weight-semibold: 600;
}

/* Carte */
.card {
    fill: var(--bgColor-card);
    stroke: var(--border-color);
    stroke-width: 1;
    rx: 8;
}

/* Typo */
.title {
    fill: var(--fgColor-default);
    font-size: 16px;
    font-family: Arial, sans-serif;
    font-weight: var(--text-weight-semibold);
}

.small {
    fill: var(--fgColor-muted);
    font-size: 13px;
    font-family: Arial, sans-serif;
}

.muted {
    fill: var(--fgColor-muted);
}

/* Icônes */
.icon-open { fill: var(--fgColor-success); }
.icon-closed { fill: var(--fgColor-danger); }
.icon-completed { fill: var(--fgColor-accent); }
.icon-muted { fill: var(--fgColor-muted); }

/* Labels */
.labelBg-muted { fill: #21262d; }
.label-muted {
    fill: var(--fgColor-muted);
    font-size: 12px;
    font-family: Arial, sans-serif;
    dominant-baseline: middle;
}

/* Progress bar */
.progress-bg {
    fill: #21262d;
}
.progress-fill {
    fill: var(--fgColor-success);
}

</style>
"""


# -----------------------------------------------------------
# Icônes Octicons utilisées (https://primer.style/octicons/)
# -----------------------------------------------------------
ISSUE_OPEN_16 = "M8 9.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3Z M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0ZM1.5 8a6.5 6.5 0 1 0 13 0 6.5 6.5 0 0 0-13 0Z"
ISSUE_CLOSED_16 = "M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0ZM1.5 8a6.5 6.5 0 1 0 13 0 6.5 6.5 0 0 0-13 0Zm9.78-2.22-5.5 5.5a.749.749 0 0 1-1.275-.326.749.749 0 0 1 .215-.734l5.5-5.5a.751.751 0 0 1 1.042.018.751.751 0 0 1 .018 1.042Z"
ISSUE_COMPLETED_16 = "M11.28 6.78a.75.75 0 0 0-1.06-1.06L7.25 8.69 5.78 7.22a.75.75 0 0 0-1.06 1.06l2 2a.75.75 0 0 0 1.06 0l3.5-3.5Z M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0Zm-1.5 0a6.5 6.5 0 1 0-13 0 6.5 6.5 0 0 0 13 0Z"
BUG_16 = "M4.72.22a.75.75 0 0 1 1.06 0l1 .999a3.488 3.488 0 0 1 2.441 0l.999-1a.748.748 0 0 1 1.265.332.75.75 0 0 1-.205.729l-.775.776c.616.63.995 1.493.995 2.444v.327c0 .1-.009.197-.025.292.408.14.764.392 1.029.722l1.968-.787a.75.75 0 0 1 .556 1.392L13 7.258V9h2.25a.75.75 0 0 1 0 1.5H13v.5c0 .409-.049.806-.141 1.186l2.17.868a.75.75 0 0 1-.557 1.392l-2.184-.873A4.997 4.997 0 0 1 8 16a4.997 4.997 0 0 1-4.288-2.427l-2.183.873a.75.75 0 0 1-.558-1.392l2.17-.868A5.036 5.036 0 0 1 3 11v-.5H.75a.75.75 0 0 1 0-1.5H3V7.258L.971 6.446a.75.75 0 0 1 .558-1.392l1.967.787c.265-.33.62-.583 1.03-.722a1.677 1.677 0 0 1-.026-.292V4.5c0-.951.38-1.814.995-2.444L4.72 1.28a.75.75 0 0 1 0-1.06Zm.53 6.28a.75.75 0 0 0-.75.75V11a3.5 3.5 0 1 0 7 0V7.25a.75.75 0 0 0-.75-.75ZM6.173 5h3.654A.172.172 0 0 0 10 4.827V4.5a2 2 0 1 0-4 0v.327c0 .096.077.173.173.173Z"
PIN_16 = "m11.294.984 3.722 3.722a1.75 1.75 0 0 1-.504 2.826l-1.327.613a3.089 3.089 0 0 0-1.707 2.084l-.584 2.454c-.317 1.332-1.972 1.8-2.94.832L5.75 11.311 1.78 15.28a.749.749 0 1 1-1.06-1.06l3.969-3.97-2.204-2.204c-.968-.968-.5-2.623.832-2.94l2.454-.584a3.08 3.08 0 0 0 2.084-1.707l.613-1.327a1.75 1.75 0 0 1 2.826-.504ZM6.283 9.723l2.732 2.731a.25.25 0 0 0 .42-.119l.584-2.454a4.586 4.586 0 0 1 2.537-3.098l1.328-.613a.25.25 0 0 0 .072-.404l-3.722-3.722a.25.25 0 0 0-.404.072l-.613 1.328a4.584 4.584 0 0 1-3.098 2.537l-2.454.584a.25.25 0 0 0-.119.42l2.731 2.732Z"
COMMIT_16 = "M11.93 8.5a4.002 4.002 0 0 1-7.86 0H.75a.75.75 0 0 1 0-1.5h3.32a4.002 4.002 0 0 1 7.86 0h3.32a.75.75 0 0 1 0 1.5Zm-1.43-.75a2.5 2.5 0 1 0-5 0 2.5 2.5 0 0 0 5 0Z"
BRANCH_16 = "M9.5 3.25a2.25 2.25 0 1 1 3 2.122V6A2.5 2.5 0 0 1 10 8.5H6a1 1 0 0 0-1 1v1.128a2.251 2.251 0 1 1-1.5 0V5.372a2.25 2.25 0 1 1 1.5 0v1.836A2.493 2.493 0 0 1 6 7h4a1 1 0 0 0 1-1v-.628A2.25 2.25 0 0 1 9.5 3.25Zm-6 0a.75.75 0 1 0 1.5 0 .75.75 0 0 0-1.5 0Zm8.25-.75a.75.75 0 1 0 0 1.5.75.75 0 0 0 0-1.5ZM4.25 12a.75.75 0 1 0 0 1.5.75.75 0 0 0 0-1.5Z"
PULL_REQUEST_16 = "M1.5 3.25a2.25 2.25 0 1 1 3 2.122v5.256a2.251 2.251 0 1 1-1.5 0V5.372A2.25 2.25 0 0 1 1.5 3.25Zm5.677-.177L9.573.677A.25.25 0 0 1 10 .854V2.5h1A2.5 2.5 0 0 1 13.5 5v5.628a2.251 2.251 0 1 1-1.5 0V5a1 1 0 0 0-1-1h-1v1.646a.25.25 0 0 1-.427.177L7.177 3.427a.25.25 0 0 1 0-.354ZM3.75 2.5a.75.75 0 1 0 0 1.5.75.75 0 0 0 0-1.5Zm0 9.5a.75.75 0 1 0 0 1.5.75.75 0 0 0 0-1.5Zm8.25.75a.75.75 0 1 0 1.5 0 .75.75 0 0 0-1.5 0Z"
PULL_REQUEST_MERGED_16 = "M5.45 5.154A4.25 4.25 0 0 0 9.25 7.5h1.378a2.251 2.251 0 1 1 0 1.5H9.25A5.734 5.734 0 0 1 5 7.123v3.505a2.25 2.25 0 1 1-1.5 0V5.372a2.25 2.25 0 1 1 1.95-.218ZM4.25 13.5a.75.75 0 1 0 0-1.5.75.75 0 0 0 0 1.5Zm8.5-4.5a.75.75 0 1 0 0-1.5.75.75 0 0 0 0 1.5ZM5 3.25a.75.75 0 1 0 0 .005V3.25Z"
PULL_REQUEST_CLOSED_16 = "M3.25 1A2.25 2.25 0 0 1 4 5.372v5.256a2.251 2.251 0 1 1-1.5 0V5.372A2.251 2.251 0 0 1 3.25 1Zm9.5 5.5a.75.75 0 0 1 .75.75v3.378a2.251 2.251 0 1 1-1.5 0V7.25a.75.75 0 0 1 .75-.75Zm-2.03-5.273a.75.75 0 0 1 1.06 0l.97.97.97-.97a.748.748 0 0 1 1.265.332.75.75 0 0 1-.205.729l-.97.97.97.97a.751.751 0 0 1-.018 1.042.751.751 0 0 1-1.042.018l-.97-.97-.97.97a.749.749 0 0 1-1.275-.326.749.749 0 0 1 .215-.734l.97-.97-.97-.97a.75.75 0 0 1 0-1.06ZM2.5 3.25a.75.75 0 1 0 1.5 0 .75.75 0 0 0-1.5 0ZM3.25 12a.75.75 0 1 0 0 1.5.75.75 0 0 0 0-1.5Zm9.5 0a.75.75 0 1 0 0 1.5.75.75 0 0 0 0-1.5Z"
COMMENT_16 = "M1 2.75C1 1.784 1.784 1 2.75 1h10.5c.966 0 1.75.784 1.75 1.75v7.5A1.75 1.75 0 0 1 13.25 12H9.06l-2.573 2.573A1.458 1.458 0 0 1 4 13.543V12H2.75A1.75 1.75 0 0 1 1 10.25Zm1.75-.25a.25.25 0 0 0-.25.25v7.5c0 .138.112.25.25.25h2a.75.75 0 0 1 .75.75v2.19l2.72-2.72a.749.749 0 0 1 .53-.22h4.5a.25.25 0 0 0 .25-.25v-7.5a.25.25 0 0 0-.25-.25Z"


# -----------------------------------------------------------
# Génération d'une issue card complète
# -----------------------------------------------------------
def make_svg(issue):

    width = 670
    height = 100
    pad = 8
    icon_size = 16

    title = issue["title"]
    number = issue["number"]
    state = issue["state"]

    # Date formatée
    created_at = datetime.strptime(issue["created_at"], "%Y-%m-%dT%H:%M:%SZ")
    created_fmt = created_at.strftime("%b %d")

    # Choix de l’icône d’état
    if state == "open":
        icon = ISSUE_OPEN_16
        icon_class = "icon-open"
    elif state == "closed":
        icon = ISSUE_CLOSED_16
        icon_class = "icon-closed"
    else:
        icon = BUG_16
        icon_class = "icon-muted"

    # Raccourcissement du titre
    short_title = title[:40] + "..." if len(title) > 40 else title

    return f"""
<svg width="{width}" height="{height}" xmlns="http://www.w3.org/2000/svg">
{STYLE}

    <!-- Fond -->
    <rect class="card" x="0" y="0" width="{width}" height="{height}" />

    <!-- Icône -->
    <path class="{icon_class}"
          transform="translate({pad}, {pad})"
          d="{icon}" />

    <!-- Titre -->
    <text class="title"
          x="{pad + icon_size + 4}"
          y="{pad + icon_size/2 + 2}"
          dominant-baseline="middle">
        {short_title}
        <tspan class="small">  #{number}</tspan>
    </text>

    <!-- Sous‑titre -->
    <text class="small"
          x="{pad}"
          y="{height - pad - 2}">
        opened on {created_fmt}
    </text>

    <!-- État -->
    <text class="title {icon_class}"
          x="{width - pad - 60}"
          y="{pad + 12}">
        {state.upper()}
    </text>

</svg>
"""


# -----------------------------------------------------------
# Exécution (création des fichiers)
# -----------------------------------------------------------
if __name__ == "__main__":
    issues = requests.get(API_URL).json()

    for i, issue in enumerate(issues):
        with open(f".github/generated_svgs/issue_{i}.svg", "w", encoding="utf-8") as f:
            f.write(make_svg(issue))
