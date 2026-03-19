import requests

API_URL = "https://api.github.com/repos/foll0wer/openwinemer/issues"

def make_svg(issue):
    title = issue["title"]
    number = issue["number"]
    state = issue["state"]

    return f"""
<svg width="600" height="120" xmlns="http://www.w3.org/2000/svg">
  <rect width="600" height="120" fill="#1e1e1e" rx="10"/>
  <text x="20" y="40" fill="white" font-size="20">Issue #{number}</text>
  <text x="20" y="80" fill="white" font-size="16">{title}</text>
  <text x="500" y="40" fill="orange" font-size="16">{state}</text>
</svg>
"""

issues = requests.get(API_URL).json()

for i, issue in enumerate(issues):
    with open(f".github/generated_svgs/issue_{i}.svg", "w", encoding="utf-8") as f:
        f.write(make_svg(issue))
