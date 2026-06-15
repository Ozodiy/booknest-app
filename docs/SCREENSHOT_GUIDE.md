# Screenshot Capture Guide

The Lab 2, Lab 3, and Final reports contain `[SCREENSHOT n]` markers. This guide
tells you exactly how to capture each one once the app is running. Budget ~30–40
minutes total.

> **Why you have to take these yourself:** screenshots are evidence that the app
> really runs on your machine with your Firebase project. They must be genuine — do
> not use fabricated images. The steps below make capturing them quick.

## How to capture

**From the Android emulator:** the emulator toolbar has a **camera icon** (or press
the camera button in the side panel). Screenshots save to your computer; in Android
Studio see **Device Manager → ⋮ → Show on Disk**, or drag the saved PNG out.

**From a physical device:** Power + Volume-Down, then pull the PNG via USB.

**From a browser (Firebase / GitHub / Trello):** Windows **Snipping Tool**
(`Win + Shift + S`).

Drop each PNG into a `docs/screenshots/` folder and rename to match (e.g. `01-auth.png`),
then insert into your DOCX/PDF where the marker is.

## Shot list

| # | Source | What to show |
|---|--------|--------------|
| 1 | Firebase Console | Authentication → Sign-in method, Email/Password = Enabled |
| 2 | Emulator | Login screen |
| 3 | Emulator | Register screen |
| 4 | Emulator | Login showing an error message (type a wrong password) |
| 5 | Emulator | Book list with a few recommendations |
| 6 | Emulator | Add-book form filled in |
| 7 | Emulator | Delete confirmation dialog |
| 8 | Emulator | Search box filtering the list |
| 9 | Emulator | Want-to-Read list with one book "Finished" |
| 10 | Trello | Sprint 1 cards in Done |
| 11 | GitHub | Commit history / PRs |
| 12 | Trello | Sprint 2 cards |
| 13 | Firebase Console | Storage tab enabled |
| 14 | Emulator | Add-book with cover preview + upload progress bar |
| 15 | Emulator | Book list showing cover thumbnails |
| 16 | Emulator | Book detail with large cover |
| 17 | Android Studio | Test runner: all tests passing |
| 18 | Trello | Usability bug/improvement cards with priority labels |
| 19 | Emulator | Add-book validation error (empty title) |
| A | draw.io / FigJam | Architecture diagram (or screenshot the ASCII one in Final_Report) |
| B | GitHub | Repository file list |
| C | Trello | Final board state |

## Getting the unit-test screenshot (#17)

1. In Android Studio, open `app/src/test/java/com/ozodbek/booknest/`.
2. Right-click the folder → **Run 'Tests in 'booknest''**.
3. Screenshot the green run panel showing **17 tests passed**.

Or from a terminal: `./gradlew test` and screenshot the
`BUILD SUCCESSFUL` output plus the HTML report at
`app/build/reports/tests/testDebugUnitTest/index.html`.
