package com.bits.library.service;

import com.bits.library.entity.Author;
import com.bits.library.entity.Book;
import com.bits.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock BookRepository  bookRepo;
    @Mock AuthorService   authorService;
    @InjectMocks BookService service;

    @Test
    void create_attaches_author_before_save() {
        Author author = new Author("A", "a@x", "IN", 1990);
        author.setId(3L);
        when(authorService.findById(3L)).thenReturn(author);
        when(bookRepo.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Book input = new Book("T", "Fiction", 2020, new BigDecimal("100"), null);
        Book saved = service.create(input, 3L);

        assertThat(saved.getAuthor()).isSameAs(author);
        verify(bookRepo).save(input);
    }

    @Test
    void update_overwrites_fields_and_reassigns_author() {
        Author oldAuthor = new Author("Old", "old@x", "IN", 1970);
        oldAuthor.setId(1L);
        Author newAuthor = new Author("New", "new@x", "JP", 1985);
        newAuthor.setId(2L);

        Book existing = new Book("Old Title", "Fiction", 2000, new BigDecimal("100"), oldAuthor);
        existing.setId(10L);

        when(bookRepo.findById(10L)).thenReturn(Optional.of(existing));
        when(authorService.findById(2L)).thenReturn(newAuthor);
        when(bookRepo.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Book updates = new Book("New Title", "Literary", 2021, new BigDecimal("250"), null);
        Book result = service.update(10L, updates, 2L);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getGenre()).isEqualTo("Literary");
        assertThat(result.getPublishedYear()).isEqualTo(2021);
        assertThat(result.getPrice()).isEqualByComparingTo("250");
        assertThat(result.getAuthor()).isSameAs(newAuthor);
    }

    @Test
    void findById_throws_when_missing() {
        when(bookRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(BookNotFoundException.class);
    }
}
