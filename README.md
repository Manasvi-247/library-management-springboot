# Inkwell — Library Management

A Spring Boot 3 (Jakarta) reference implementation of a **One‑to‑Many** JPA model
(`Author` 1—N `Book`) with **Create / Read / Update** flows rendered through
**JSP** views, an **inner‑join** custom query, validation, exception handling and
**JUnit 5 + Mockito** tests.

> Built for the BITS Pilani Spring Boot assignment.

---

## Stack

| Layer        | Choice                                              |
|--------------|-----------------------------------------------------|
| Framework    | Spring Boot 3.2 (Jakarta EE 10)                     |
| Language     | Java 17                                             |
| Build        | Maven (war packaging)                               |
| Persistence  | Spring Data JPA + Hibernate                         |
| Database     | MySQL (default profile) · H2 (demo / test profile)  |
| Web          | Spring MVC + JSP + JSTL                             |
| Validation   | Jakarta Bean Validation                             |
| Tests        | JUnit 5, Mockito, `@DataJpaTest`, AssertJ           |
| UI           | Custom CSS — glassmorphism + animated mesh gradient |

---

## Run it

### Option A — MySQL (default)
1. Make sure MySQL is running locally.
2. Edit credentials in [`src/main/resources/application-mysql.properties`](src/main/resources/application-mysql.properties) if needed (default: `root`/`root`).
3. The schema `library_db` is auto-created via `createDatabaseIfNotExist=true`.
4. Run:
   ```bash
   mvn spring-boot:run
   ```

### Option B — H2 (zero setup, recommended for screenshots)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```
- App: <http://localhost:8080/>
- H2 console: <http://localhost:8080/h2-console> (JDBC URL: `jdbc:h2:mem:librarydb`)

### Tests
```bash
mvn test
```

---

## Routes

| Method | Path                       | Purpose                                       |
|--------|----------------------------|-----------------------------------------------|
| GET    | `/`                        | Dashboard with counts                         |
| GET    | `/authors`                 | List authors (Read)                           |
| GET    | `/authors/new`             | New author form                               |
| POST   | `/authors`                 | Create author (Create)                        |
| GET    | `/authors/{id}/edit`       | Edit author form                              |
| POST   | `/authors/{id}`            | Update author (Update)                        |
| GET    | `/books`                   | List books (Read)                             |
| GET    | `/books/new`               | New book form                                 |
| POST   | `/books`                   | Create book (Create)                          |
| GET    | `/books/{id}/edit`         | Edit book form                                |
| POST   | `/books/{id}`              | Update book (Update)                          |
| GET    | `/books/joined`            | Inner‑join projection of Author × Book        |
| GET    | `/books/joined?genre=…`    | Filtered inner‑join                           |

---

## Project layout

```
src/main/java/com/bits/library/
  LibraryApplication.java
  config/DataSeeder.java          # CommandLineRunner — seeds 10+10 rows
  entity/Author.java              # @OneToMany side
  entity/Book.java                # @ManyToOne side
  repository/AuthorRepository.java
  repository/BookRepository.java  # custom @Query INNER JOIN
  service/AuthorService.java
  service/BookService.java
  controller/HomeController.java
  controller/AuthorController.java
  controller/BookController.java
  controller/GlobalExceptionHandler.java
  dto/AuthorBookView.java         # constructor projection target

src/main/webapp/WEB-INF/views/
  home.jsp · error.jsp
  authors/list.jsp · authors/form.jsp
  books/list.jsp · books/form.jsp · books/joined.jsp
  fragments/header.jsp · fragments/footer.jsp

src/main/webapp/resources/css/styles.css

src/test/java/com/bits/library/
  LibraryApplicationTests.java               (@SpringBootTest)
  repository/BookRepositoryTest.java         (@DataJpaTest)
  service/AuthorServiceTest.java             (Mockito)
  service/BookServiceTest.java               (Mockito)
```

---

## Pushing to GitHub

```bash
cd /path/to/bits
git init
git add .
git commit -m "Inkwell: Spring Boot Author/Book CRUD with JSP"
git branch -M main
git remote add origin https://github.com/<your-username>/inkwell-library.git
git push -u origin main
```

Then paste the GitHub URL into [`REPORT.md`](REPORT.md) under *Submission*.
