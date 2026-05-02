package com.bits.library.service;

import com.bits.library.dto.AuthorBookView;
import com.bits.library.entity.Author;
import com.bits.library.entity.Book;
import com.bits.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<AuthorBookView> findAllJoined() {
        return bookRepository.findAllAuthorsWithBooks();
    }

    @Transactional(readOnly = true)
    public List<AuthorBookView> findByGenre(String genre) {
        return bookRepository.findByGenreJoined(genre);
    }

    public Book create(Book book, Long authorId) {
        Author author = authorService.findById(authorId);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public Book update(Long id, Book updates, Long authorId) {
        Book existing = findById(id);
        Author author = authorService.findById(authorId);
        existing.setTitle(updates.getTitle());
        existing.setGenre(updates.getGenre());
        existing.setPublishedYear(updates.getPublishedYear());
        existing.setPrice(updates.getPrice());
        existing.setAuthor(author);
        return bookRepository.save(existing);
    }
}
