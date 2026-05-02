# Library Management System — Project Report

**Course:** Spring Boot Web Application Development
**Institution:** BITS Pilani
**Author:** _Your Name_ &nbsp;·&nbsp; _Your ID_
**Submission date:** _DD MMM YYYY_
**GitHub URL:** _https://github.com/&lt;your-username&gt;/inkwell-library_

> This document is written in Markdown so it can be exported to PDF in one step
> (e.g. VS Code → *Markdown PDF*, or `pandoc REPORT.md -o REPORT.pdf`). Replace
> the `_…_` placeholders, drop screenshots into a `screenshots/` folder, and
> export.

---

## 1. Overview

**Inkwell** is a Spring Boot reference application that manages two related
entities — **Author** and **Book** — through a web UI built with JSP. The
application demonstrates:

- JPA entity modelling with a One-to-Many relationship.
- Repository, service, and controller layering with clear separation of concerns.
- Three CRUD operations per the assignment (**Create**, **Read**, **Update**).
- A custom **INNER JOIN** JPQL query projecting into a DTO.
- Form validation, integrity-violation handling, and a polished JSP UI.
- Unit tests for the repository and service layers.

---

## 2. Entity-Relationship Design

Two entities, related as **Author 1 — N Book**:

```
┌──────────────────┐  1     N  ┌────────────────────┐
│ Author           │───────────│ Book               │
│──────────────────│           │────────────────────│
│ id (PK)          │           │ id (PK)            │
│ name             │           │ title              │
│ email (UNIQUE)   │           │ genre              │
│ country          │           │ published_year     │
│ birth_year       │           │ price              │
└──────────────────┘           │ author_id (FK)     │
                               │  UNIQUE(title,     │
                               │         author_id) │
                               └────────────────────┘
```

### Constraints (also drive the integrity-violation demo)

| Constraint                                | Why                                                    |
|-------------------------------------------|--------------------------------------------------------|
| `Author.email` UNIQUE                     | Prevents two authors sharing an email.                 |
| `Book(title, author_id)` UNIQUE composite | Same author cannot have two books with the same title. |
| `Book.author_id` NOT NULL + FK            | Every book must reference an existing author.          |
| Bean Validation on form fields            | Client-side rejection before the DB is touched.        |

### JPA mapping highlights

- `Author.books` — `@OneToMany(mappedBy = "author", cascade = ALL, orphanRemoval = true)`
- `Book.author` — `@ManyToOne(fetch = LAZY, optional = false)` + `@JoinColumn(name = "author_id")`
- Bean Validation: `@NotBlank`, `@Email`, `@Min`, `@Positive`, `@NotNull`.

> **File references:** [`Author.java`](src/main/java/com/bits/library/entity/Author.java), [`Book.java`](src/main/java/com/bits/library/entity/Book.java)

---

## 3. Layered Architecture

```
JSP views ── Controllers ── Services ── Repositories ── Database
                              │
                              └─── @ControllerAdvice (GlobalExceptionHandler)
```

| Layer       | Responsibility                                                      |
|-------------|---------------------------------------------------------------------|
| Controller  | HTTP request/response, model binding, validation, flash messages.   |
| Service     | Transactional business logic, domain exceptions.                    |
| Repository  | Spring Data JPA — derived methods + custom JPQL.                    |
| View        | JSP + JSTL/EL with form-binding tags.                               |

---

## 4. Implementation Details

### 4.1 Populating the database

`config/DataSeeder.java` is a `CommandLineRunner` that inserts **10 authors and
10 books** on startup if the tables are empty. This guarantees the grader sees a
populated database the moment the app starts.

```java
@Component
public class DataSeeder implements CommandLineRunner {
    public void run(String... args) {
        if (authorRepository.count() > 0) return;
        // 10 authors + 10 books inserted via saveAll(...)
    }
}
```

> **Screenshot placeholder:** `screenshots/01-h2-tables-populated.png`

### 4.2 Create operation

- JSP form: [`authors/form.jsp`](src/main/webapp/WEB-INF/views/authors/form.jsp), [`books/form.jsp`](src/main/webapp/WEB-INF/views/books/form.jsp)
- Controller: `POST /authors` and `POST /books`
- `@Valid` triggers Bean Validation; errors are re-rendered into the same form via `BindingResult`.
- Successful creates redirect with a flash message ("Author 'X' created successfully").
- Duplicate email or duplicate `(title, author)` triggers `DataIntegrityViolationException`,
  caught by the global handler and rendered through `error.jsp`.

```java
@PostMapping
public String create(@Valid @ModelAttribute("author") Author author,
                     BindingResult bindingResult, ...) {
    if (bindingResult.hasErrors()) return "authors/form";
    Author saved = authorService.create(author);
    redirect.addFlashAttribute("flashSuccess", "Author '" + saved.getName() + "' created.");
    return "redirect:/authors";
}
```

> **Screenshot placeholders:**
> `screenshots/02-create-author-form.png`,
> `screenshots/03-create-validation-errors.png`,
> `screenshots/04-create-duplicate-error.png`

### 4.3 Read operation

- List views at `/authors`, `/books`.
- The **inner-join** projection lives at `/books/joined` and uses this JPQL:

```java
@Query("SELECT new com.bits.library.dto.AuthorBookView(" +
       "  a.name, a.country, b.title, b.genre, b.publishedYear, b.price) " +
       "FROM Book b INNER JOIN b.author a " +
       "ORDER BY a.name ASC, b.publishedYear DESC")
List<AuthorBookView> findAllAuthorsWithBooks();
```

The view layer never touches lazy associations because the query returns a flat
`AuthorBookView` DTO. A second variant (`findByGenreJoined`) supports filtering
by genre — wired up to a small filter bar in the joined view.

> **Screenshot placeholders:**
> `screenshots/05-authors-list.png`,
> `screenshots/06-books-list.png`,
> `screenshots/07-catalog-join.png`,
> `screenshots/08-catalog-join-filtered.png`

### 4.4 Update operation

- Edit forms at `/authors/{id}/edit` and `/books/{id}/edit`.
- Submitting `POST /authors/{id}` or `POST /books/{id}` calls
  `authorService.update(id, ...)` / `bookService.update(id, ...)`.
- The service fetches the existing entity, copies in the new field values, and
  re-saves — keeping the JPA-managed identity stable.

> **Screenshot placeholder:** `screenshots/09-edit-form.png`

### 4.5 Exception handling

`GlobalExceptionHandler` (`@ControllerAdvice`) handles:

| Exception                              | HTTP | User message                                                |
|----------------------------------------|------|-------------------------------------------------------------|
| `DataIntegrityViolationException`      | 409  | "Duplicate or invalid data — uniqueness/foreign-key broke." |
| `AuthorNotFoundException` / `Book…`    | 404  | "Record not found."                                         |
| Any other `Exception`                  | 500  | "Something went wrong" + cause detail.                      |

All errors render through a single styled [`error.jsp`](src/main/webapp/WEB-INF/views/error.jsp) page.

---

## 5. View Layer & UI

The UI is built on a small custom design system using only CSS (no framework):

- Animated mesh-gradient background with two overlapping radial layers.
- Glassmorphism cards (`backdrop-filter: blur`).
- Coloured "pill" badges for genre and country.
- Sticky frosted navbar with active-route highlighting.
- Responsive table that collapses into stacked rows on mobile.
- Flash messages, validation errors, and a friendly error page.

> **Screenshot placeholder:** `screenshots/10-dashboard-hero.png`

---

## 6. Testing & Validation

| Test class                        | Type             | What it proves                                                        |
|-----------------------------------|------------------|-----------------------------------------------------------------------|
| `BookRepositoryTest`              | `@DataJpaTest`   | Inner-join returns correct rows, excludes authors with no books, genre filter is case-insensitive, unique-email + unique-(title, author) constraints throw `DataIntegrityViolationException`. |
| `AuthorServiceTest`               | Mockito          | `findAll`, `findById` (success + miss), `create`, `update` field copy. |
| `BookServiceTest`                 | Mockito          | Author resolution on create, field copy + author reassignment on update. |
| `LibraryApplicationTests`         | `@SpringBootTest`| Full Spring context wires up cleanly.                                 |

Run with:
```bash
mvn test
```

---

## 7. Running the App

See [README.md](README.md) for full setup. Quick start (H2 — zero setup):

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```
Then visit <http://localhost:8080/>.

---

## 8. Challenges & Resolutions

| Challenge                                                                 | Resolution                                                                                                            |
|---------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| **JSP + Spring Boot 3 (Jakarta) JSTL imports**                            | Used `jakarta.tags.core` URI and the matching Glassfish `jakarta.servlet.jsp.jstl` 3.0.x runtime — `javax.*` is dead. |
| **`LazyInitializationException`** when rendering `${author.books}` in JSP | Two paths: keep `spring.jpa.open-in-view=true`, or project the joined data into a flat DTO. We use the DTO for the join page. |
| **Surfacing integrity violations as friendly UI errors**                  | `@ControllerAdvice` converts `DataIntegrityViolationException` into a styled 409 page instead of a stack trace.       |
| **Form-binding for the `Book → Author` foreign key**                      | Bound `authorId` as a separate request parameter; the service fetches the `Author` and assigns the relationship before save. |
| **Reproducible local demo without MySQL**                                 | Added an `h2` profile and an `application-h2.properties` so graders without MySQL can still run the app.              |
| **Tests had to run against the same schema as MySQL**                     | Test profile uses H2 in `MODE=MySQL` so unique-constraint behaviour matches production.                               |

---

## 9. Submission Checklist (Rubric Cross-Walk)

| Rubric                                     | Where to look                                                                  |
|--------------------------------------------|--------------------------------------------------------------------------------|
| **Entities & relationships (10%)**         | `entity/Author.java`, `entity/Book.java`                                       |
| **CRUD functionality (30%)**               | Controllers under `controller/`, JSPs under `WEB-INF/views/`                   |
| **Inner-join custom query**                | `BookRepository.findAllAuthorsWithBooks` + `/books/joined` page                |
| **Spring Boot layering (30%)**             | `controller/`, `service/`, `repository/` packages                              |
| **UI (10%)**                               | `webapp/resources/css/styles.css` + JSPs                                       |
| **Testing & validation (10%)**             | `src/test/java/...` and `GlobalExceptionHandler`                               |
| **Documentation (10%)**                    | This file + `README.md`                                                        |

---

## 10. Screenshots Index

Place these files into a `screenshots/` folder before exporting to PDF:

1. `01-h2-tables-populated.png` — H2 console showing 10 authors + 10 books.
2. `02-create-author-form.png` — New-author form (empty).
3. `03-create-validation-errors.png` — Form rejected with field errors.
4. `04-create-duplicate-error.png` — Friendly 409 page on duplicate email.
5. `05-authors-list.png` — Authors list view.
6. `06-books-list.png` — Books list view.
7. `07-catalog-join.png` — Inner-join projection page.
8. `08-catalog-join-filtered.png` — Same page filtered by genre.
9. `09-edit-form.png` — Update flow on an existing record.
9b. `09b-edit-success.png` — Update success: flash banner + country changed to Canada (proves the update persisted).
10. `10-dashboard-hero.png` — Home/dashboard.

---

_End of report._
