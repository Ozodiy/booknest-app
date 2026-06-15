# BookNest — Final Presentation (≈10 minutes)

**Student:** Ozodbek Tursunaliyev (ID 57264) — Akademia WSB
Suggested **10–15 slides**. Speaker notes in italics.

---

### Slide 1 — Title (0:30)
- **BookNest** — *Discover books recommended by students at your university.*
- Ozodbek Tursunaliyev · ID 57264 · individual project (all roles solo).

### Slide 2 — Problem & Target Users (1:00)
- *Students waste time on generic book lists that don't match their academic interests.*
- Target: university students 18–30, academic + leisure readers.
- *Say: BookNest makes recommendations peer-driven and relevant.*

### Slide 3 — Solution overview (1:00)
- One sentence: a peer feed of book recommendations + a personal reading list.
- Screenshot of the book list with covers.

### Slide 4 — Live demo: accounts (part of 4:00 demo block)
- Register → land on list → log out → log back in (show session persistence).

### Slide 5 — Live demo: CRUD
- Add a book **with a cover image** (show upload progress).
- Edit it, delete it (show confirmation dialog).
- Search + sort the list.

### Slide 6 — Live demo: advanced feature
- Emphasise **Firebase Storage**: cover picked from gallery → uploaded → shown.
- *Say why it adds value: instant visual recognition.*

### Slide 7 — Architecture (1:00)
- Diagram: Android (Compose, MVVM) ↔ Firebase Auth / Firestore / Storage.
- *Mention clean layering: UI → ViewModel → pure logic → data.*

### Slide 8 — Data model (0:45)
- 3 Firestore collections: `users`, `books`, `savedBooks`.
- Security rules: authenticated access; owner-only edit/delete.

### Slide 9 — Testing (0:45)
- 17 JUnit tests (validation + search/sort), all green.
- *Say: logic is pure functions, so it tests on the JVM with no emulator.*

### Slide 10 — Usability testing & improvements (0:45)
- Think-aloud test → 5 findings → fixed 4 (progress bar, validation, save icon, empty state).
- Show Trello board.

### Slide 11 — Challenges & solutions (0:30)
- Permission-denied → security rules; blank cover URL → await downloadUrl;
  back-stack → popUpTo(0).

### Slide 12 — Results & reflection (0:30)
- All deliverables met across 3 labs, solo.
- Future: genre dropdown, Cloud Function anti-spam, profile pictures, UI tests.

### Slide 13 — Thank you / Q&A (0:30)
- Repo + Trello links.
- Anticipated questions:
  - *"How do you stop duplicate recommendations?"* → planned Cloud Function.
  - *"What if two users edit the same book?"* → owner-only edit + last-write-wins; rules enforce ownership.
  - *"How did you test without teammates?"* → unit tests + structured self-usability test.

---

**Timing guide:** Intro 1 min · Problem/solution 2 min · Live demo 4 min ·
Technical (arch+data+testing) 2 min · Reflection 1 min.
