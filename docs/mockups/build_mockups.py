"""Generate labeled UI MOCKUPS of the BookNest screens as standalone HTML.
These mirror the Jetpack Compose layouts for use on presentation/design slides.
Each carries a visible 'UI mockup' caption — they are NOT live app screenshots.
Rendered to PNG by Chrome headless (see build_mockups.sh / .bat)."""
import os

HERE = os.path.dirname(os.path.abspath(__file__))

GREEN = "#1B5E20"
GREEN2 = "#2E7D32"

BASE_CSS = f"""
* {{ margin:0; padding:0; box-sizing:border-box; font-family:'Segoe UI',Roboto,Arial,sans-serif; }}
html, body {{ margin:0; }}
body {{ background:#e7ecee; display:flex; justify-content:center; overflow:hidden; }}
.phone {{ width:412px; min-width:412px; max-width:412px; height:892px; background:#fafafa;
         position:relative; overflow:hidden; display:flex; flex-direction:column; }}
.statusbar {{ height:28px; background:{GREEN}; color:#fff; font-size:12px;
             display:flex; align-items:center; justify-content:space-between; padding:0 14px; }}
.appbar {{ background:{GREEN}; color:#fff; height:56px; display:flex; align-items:center;
          padding:0 12px; font-size:18px; font-weight:600; gap:8px; overflow:hidden; }}
.appbar .icons {{ margin-left:auto; display:flex; gap:12px; font-size:18px; align-items:center; }}
.appbar .logout {{ font-size:13px; font-weight:500; white-space:nowrap; }}
.content {{ flex:1; padding:18px 18px; overflow:hidden; }}
.center {{ justify-content:center; align-items:center; display:flex; flex-direction:column; text-align:center; }}
.brand {{ font-size:34px; font-weight:800; color:{GREEN}; }}
.tag {{ color:#555; font-size:13px; margin:8px 0 26px; max-width:280px; }}
.field {{ width:100%; border:1.5px solid #b9c4ba; border-radius:8px; padding:13px 14px;
         font-size:14px; color:#222; margin-bottom:13px; background:#fff; }}
.field.label {{ color:#8a8a8a; }}
.btn {{ width:100%; background:{GREEN}; color:#fff; border:none; border-radius:10px;
       padding:14px; font-size:15px; font-weight:600; margin-top:6px; }}
.btn.outline {{ background:#fff; color:{GREEN}; border:1.5px solid {GREEN}; }}
.link {{ color:{GREEN2}; font-size:14px; margin-top:16px; font-weight:500; }}
.h {{ font-size:24px; font-weight:700; color:#222; margin-bottom:20px; }}
.card {{ background:#fff; border-radius:12px; box-shadow:0 1px 4px rgba(0,0,0,.12);
        padding:12px; margin-bottom:12px; display:flex; align-items:center; gap:12px; }}
.cover {{ width:56px; height:80px; border-radius:6px; flex:none; color:#fff; font-size:9px;
         display:flex; align-items:flex-end; padding:5px; font-weight:600; }}
.cover.big {{ width:100%; height:200px; border-radius:12px; font-size:14px; align-items:flex-end; padding:12px; }}
.bk-title {{ font-weight:700; font-size:15px; color:#1a1a1a; }}
.bk-author {{ font-size:13px; color:#555; margin-top:2px; }}
.chip {{ display:inline-block; background:#e3eee3; color:{GREEN2}; border-radius:14px;
        padding:3px 10px; font-size:11px; margin-top:6px; }}
.chip.filter {{ border:1.5px solid {GREEN}; background:#fff; padding:6px 12px; font-size:12px; }}
.chip.filter.on {{ background:{GREEN}; color:#fff; }}
.bm {{ margin-left:auto; font-size:22px; color:{GREEN}; }}
.search {{ width:100%; border:1.5px solid #b9c4ba; border-radius:8px; padding:11px 14px;
          font-size:13px; color:#8a8a8a; background:#fff; margin-bottom:14px; }}
.fab {{ position:absolute; right:20px; bottom:24px; width:56px; height:56px; border-radius:18px;
       background:{GREEN}; color:#fff; font-size:28px; display:flex; align-items:center;
       justify-content:center; box-shadow:0 3px 8px rgba(0,0,0,.3); }}
.progresswrap {{ margin:14px 0; }}
.progresslabel {{ font-size:13px; color:#333; margin-bottom:6px; }}
.progressbar {{ height:6px; background:#d6e2d6; border-radius:3px; overflow:hidden; }}
.progressbar > div {{ height:100%; width:64%; background:{GREEN}; }}
.struck {{ text-decoration:line-through; color:#888; }}
.area {{ height:78px; }}
.caption {{ position:absolute; bottom:0; left:0; right:0; background:rgba(0,0,0,.78);
           color:#fff; font-size:10px; text-align:center; padding:5px; letter-spacing:.3px; }}
.back {{ font-size:22px; }}
"""

PAGE = """<!DOCTYPE html><html><head><meta charset="utf-8"><style>{css}</style></head>
<body><div class="phone">{body}<div class="caption">BookNest — UI MOCKUP (design preview, not a live emulator screenshot)</div></div></body></html>"""

def statusbar():
    return '<div class="statusbar"><span>BookNest</span><span>12:30  ▾ ▾ 100%</span></div>'

def cover(grad, label):
    return f'<div class="cover" style="background:linear-gradient(135deg,{grad})">{label}</div>'

SCREENS = {}

SCREENS["01_login"] = statusbar() + """
<div class="content center">
  <div class="brand">BookNest</div>
  <div class="tag">Discover books recommended by students at your university</div>
  <input class="field label" value="Email" disabled>
  <input class="field label" value="Password" disabled>
  <button class="btn">Log in</button>
  <div class="link">No account? Register</div>
</div>"""

SCREENS["02_register"] = statusbar() + """
<div class="content">
  <div class="h" style="text-align:center">Create account</div>
  <input class="field label" value="Email" disabled>
  <input class="field label" value="Password (min 6 chars)" disabled>
  <input class="field label" value="Confirm password" disabled>
  <input class="field label" value="University (optional)" disabled>
  <button class="btn">Create account</button>
  <div class="link" style="text-align:center">Back to login</div>
</div>"""

def book_card(grad, label, title, author, genre, saved):
    bm = "🔖" if saved else "🔗"
    return f"""<div class="card">{cover(grad,label)}
      <div><div class="bk-title">{title}</div><div class="bk-author">{author}</div>
      <span class="chip">{genre}</span></div><div class="bm">{'★' if saved else '☆'}</div></div>"""

SCREENS["03_booklist"] = statusbar() + f"""
<div class="appbar">BookNest<div class="icons"><span>🔖</span><span>⇅</span><span class="logout">Log out</span></div></div>
<div class="content">
  <div class="search">Search title, author or genre</div>
  {book_card('#1e3a5f,#2e6da4','Dune','Dune','Frank Herbert','Science Fiction',True)}
  {book_card('#5b2333,#a23b5b','Clean Code','Clean Code','Robert C. Martin','Programming',False)}
  {book_card('#3a3a1e,#8a7a2e','Sapiens','Sapiens','Yuval N. Harari','History',True)}
  {book_card('#2e4a2e,#4a7a3a','Atomic','Atomic Habits','James Clear','Self-help',False)}
</div>
<div class="fab">+</div>"""

SCREENS["04_detail"] = statusbar() + f"""
<div class="appbar"><span class="back">←</span>Edit recommendation<div class="icons"><span>🗑</span></div></div>
<div class="content">
  <div class="cover big" style="background:linear-gradient(135deg,#1e3a5f,#2e6da4)">Dune</div>
  <input class="field" value="Dune" style="margin-top:14px">
  <input class="field" value="Frank Herbert">
  <input class="field" value="Science Fiction">
  <textarea class="field area">A landmark science-fiction novel about politics, religion and ecology on the desert planet Arrakis.</textarea>
  <button class="btn">Save changes</button>
</div>"""

SCREENS["05_addbook"] = statusbar() + f"""
<div class="appbar"><span class="back">←</span>Recommend a book</div>
<div class="content">
  <div class="cover big" style="background:linear-gradient(135deg,#5b2333,#a23b5b)">Selected cover preview</div>
  <button class="btn outline" style="margin-top:10px">Change cover</button>
  <input class="field" value="Clean Code" style="margin-top:14px">
  <input class="field" value="Robert C. Martin">
  <input class="field label" value="Genre">
  <div class="progresswrap">
    <div class="progresslabel">Uploading cover… 64%</div>
    <div class="progressbar"><div></div></div>
  </div>
  <button class="btn">Save recommendation</button>
</div>"""

SCREENS["06_saved"] = statusbar() + f"""
<div class="appbar"><span class="back">←</span>My Want-to-Read list</div>
<div class="content">
  <div class="card"><div><div class="bk-title">Sapiens</div><div class="bk-author">Yuval N. Harari</div></div>
    <span class="chip filter on" style="margin-left:auto">Finished</span></div>
  <div class="card"><div><div class="bk-title">Dune</div><div class="bk-author">Frank Herbert</div></div>
    <span class="chip filter" style="margin-left:auto">Want to read</span></div>
  <div class="card"><div><div class="bk-title struck">Atomic Habits</div><div class="bk-author">James Clear</div></div>
    <span class="chip filter on" style="margin-left:auto">Finished</span></div>
</div>"""

if __name__ == "__main__":
    for name, body in SCREENS.items():
        html = PAGE.format(css=BASE_CSS, body=body)
        with open(os.path.join(HERE, f"{name}.html"), "w", encoding="utf-8") as f:
            f.write(html)
        print("wrote", name + ".html")
