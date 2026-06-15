"""Convert the BookNest Markdown reports to styled, standalone HTML files.
The HTML is then printed to PDF with Chrome/Edge headless (see build_pdfs.sh)."""
import os
import re
import markdown

HERE = os.path.dirname(os.path.abspath(__file__))

REPORTS = [
    ("Lab2_Report.md", "BookNest_Lab2_Report.html", "BookNest — Lab 2 Report"),
    ("Lab3_Report.md", "BookNest_Lab3_Report.html", "BookNest — Lab 3 Report"),
    ("Final_Report.md", "BookNest_Final_Report.html", "BookNest — Final Report"),
]

CSS = """
@page { size: A4; margin: 22mm 18mm; }
* { box-sizing: border-box; }
body {
  font-family: "Segoe UI", Calibri, Arial, sans-serif;
  font-size: 11pt; line-height: 1.55; color: #1a1a1a;
  max-width: 100%; margin: 0;
}
h1 { font-size: 23pt; color: #1B5E20; border-bottom: 3px solid #1B5E20;
     padding-bottom: 6px; margin-top: 0.2em; }
h2 { font-size: 16pt; color: #1B5E20; margin-top: 1.4em;
     border-bottom: 1px solid #cfe0cf; padding-bottom: 3px; }
h3 { font-size: 13pt; color: #2e7d32; margin-top: 1.1em; }
h4 { font-size: 11.5pt; color: #2e7d32; }
p { margin: 0.5em 0; }
a { color: #1565c0; text-decoration: none; }
code {
  font-family: "Consolas", "Courier New", monospace; font-size: 9.5pt;
  background: #f2f4f2; padding: 1px 5px; border-radius: 4px; color: #b1361e;
}
pre {
  background: #1e1e1e; color: #e6e6e6; padding: 12px 14px; border-radius: 8px;
  overflow-x: auto; font-size: 9pt; line-height: 1.45; page-break-inside: avoid;
}
pre code { background: none; color: inherit; padding: 0; font-size: 9pt; }
table {
  border-collapse: collapse; width: 100%; margin: 0.8em 0; font-size: 10pt;
  page-break-inside: avoid;
}
th { background: #1B5E20; color: #fff; text-align: left; padding: 7px 10px; }
td { border: 1px solid #d4d4d4; padding: 6px 10px; vertical-align: top; }
tr:nth-child(even) td { background: #f6f9f6; }
blockquote {
  border-left: 4px solid #1B5E20; background: #f3f8f3; margin: 0.8em 0;
  padding: 6px 14px; color: #33463a;
}
ul, ol { margin: 0.4em 0 0.8em 1.4em; }
li { margin: 0.25em 0; }
hr { border: none; border-top: 1px solid #d0d0d0; margin: 1.6em 0; }
strong { color: #15301a; }
"""

HTML_TEMPLATE = """<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>{title}</title>
<style>{css}</style>
</head>
<body>
{body}
</body>
</html>
"""

def convert(md_name, html_name, title):
    src = os.path.join(HERE, md_name)
    with open(src, encoding="utf-8") as f:
        text = f.read()
    # Render GitHub-style task list checkboxes as real symbols.
    text = re.sub(r"(?m)^(\s*[-*]) \[[xX]\] ", r"\1 ✅ ", text)
    text = re.sub(r"(?m)^(\s*[-*]) \[ \] ", r"\1 ⬜ ", text)
    html_body = markdown.markdown(
        text,
        extensions=["tables", "fenced_code", "sane_lists", "toc"],
    )
    out = HTML_TEMPLATE.format(title=title, css=CSS, body=html_body)
    dst = os.path.join(HERE, html_name)
    with open(dst, "w", encoding="utf-8") as f:
        f.write(out)
    print("wrote", html_name)

if __name__ == "__main__":
    for md, html, title in REPORTS:
        convert(md, html, title)
