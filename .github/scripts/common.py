# ---------------------------------------------
# common.py
# Contient : STYLE CSS + fonction utilitaire de mesure du texte
# ---------------------------------------------

STYLE = """
<style>
.card {
    fill: #0d1117;
    stroke: #30363d;
    stroke-width: 1;
    rx: 10;
}
.label {
    font-family: Arial;
    font-size: 14px;
    font-weight: bold;
}
.title {
    font-family: Arial;
    font-size: 18px;
    font-weight: bold;
}
.counter {
    font-family: Arial;
    font-size: 28px;
    text-anchor: middle;
}
</style>
"""

# Approximation simple : 8px par caractère
def estimate_text_size(text: str) -> tuple[int, int]:
    return (len(text) * 8, 14)
