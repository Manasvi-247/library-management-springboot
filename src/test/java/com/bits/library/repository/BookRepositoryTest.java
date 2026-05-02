package com.bits.library.repository;

import com.bits.library.dto.AuthorBookView;
import com.bits.library.entity.Author;
import com.bits.library.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired AuthorRepository authorRepository;
    @Autowired BookRepository   bookRepository;

    private Author murakami;
    private Author rowling;

    @BeforeEach
    void setUp() {
        murakami = authorRepository.save(new Author("Murakami", "m@test.io", "Japan", 1949));
        rowling  = authorRepository.save(new Author("Rowling",  "r@test.io", "UK",    1965));

        bookRepository.save(new Book("Norwegian Wood", "Literary", 1987, new BigDecimal("550.00"), murakami));
        bookRepository.save(new Book("Kafka on the Shore", "Literary", 2002, new BigDecimal("620.00"), murakami));
        bookRepository.save(new Book("Harry Potter", "Fantasy", 1997, new BigDecimal("399.00"), rowling));
    }

    @Test
    void innerJoin_returns_one_row_per_book_with_author_data() {
        List<AuthorBookView> rows = bookRepository.findAllAuthorsWithBooks();
        assertThat(rows).hasSize(3);
        assertThat(rows).extracting(AuthorBookView::getAuthorName)
                .containsExactlyInAnyOrder("Murakami", "Murakami", "Rowling");
        assertThat(rows).extracting(AuthorBookView::getBookTitle)
                .contains("Norwegian Wood", "Kafka on the Shore", "Harry Potter");
    }

    @Test
    void innerJoin_excludes_authors_without_books() {
        authorRepository.save(new Author("Lonely", "lonely@test.io", "Atlantis", 1900));
        List<AuthorBookView> rows = bookRepository.findAllAuthorsWithBooks();
        assertThat(rows).hasSize(3);
        assertThat(rows).extracting(AuthorBookView::getAuthorName).doesNotContain("Lonely");
    }

    @Test
    void filtered_by_genre_is_case_insensitive() {
        List<AuthorBookView> rows = bookRepository.findByGenreJoined("fantasy");
        assertThat(rows).hasSize(1);
        assertThat(rows.get(0).getBookTitle()).isEqualTo("Harry Potter");
    }

    @Test
    void unique_email_constraint_is_enforced() {
        Author duplicate = new Author("Murakami Two", "m@test.io", "Japan", 1949);
        assertThatThrownBy(() -> {
            authorRepository.saveAndFlush(duplicate);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void unique_title_per_author_constraint_is_enforced() {
        Book dup = new Book("Norwegian Wood", "Literary", 1987, new BigDecimal("550.00"), murakami);
        assertThatThrownBy(() -> {
            bookRepository.saveAndFlush(dup);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
