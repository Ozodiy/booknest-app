# BookNest — UI Mockups

These six PNGs are **design mockups** of the BookNest screens, rendered from HTML
that mirrors the Jetpack Compose layouts. Each image carries a visible caption:
*"UI MOCKUP (design preview, not a live emulator screenshot)."*

| File | Screen |
|------|--------|
| `01_login.png` | Login |
| `02_register.png` | Register |
| `03_booklist.png` | Book list (search + covers + bookmark) |
| `04_detail.png` | Book detail / edit (owner view) |
| `05_addbook.png` | Add book + cover upload progress (Lab 3 Storage) |
| `06_saved.png` | Want-to-Read list with status toggle |

## How to use these honestly

- ✅ **Good:** presentation/design slides, the README, illustrating the intended UI.
- ❌ **Not** a substitute for the `[SCREENSHOT n]` evidence in the lab reports. Those
  must be **real** captures of your running app, Firebase Console, Trello, GitHub, and
  the passing test run — see `../SCREENSHOT_GUIDE.md`. Submitting a mockup as if it were
  a live run misrepresents your work.

## Regenerate

```
cd docs/mockups
python build_mockups.py
# then, per screen:
chrome --headless --disable-gpu --hide-scrollbars --window-size=460,940 \
       --screenshot=03_booklist.png 03_booklist.html
```
