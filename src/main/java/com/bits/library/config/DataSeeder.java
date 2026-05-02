package com.bits.library.config;

import com.bits.library.entity.Author;
import com.bits.library.entity.Book;
import com.bits.library.repository.AuthorRepository;
import com.bits.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public DataSeeder(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        if (authorRepository.count() > 0) {
            log.info("Seed skipped — {} authors already present.", authorRepository.count());
            return;
        }

        List<Author> authors = List.of(
                new Author("Chetan Bhagat",      "chetan@inkwell.in",      "India",          1974),
                new Author("Jhumpa Lahiri",      "jhumpa@inkwell.in",      "India / USA",    1967),
                new Author("Arundhati Roy",      "arundhati@inkwell.in",   "India",          1961),
                new Author("Salman Rushdie",     "rushdie@inkwell.uk",     "United Kingdom", 1947),
                new Author("Haruki Murakami",    "murakami@inkwell.jp",    "Japan",          1949),
                new Author("J.K. Rowling",       "jkr@inkwell.uk",         "United Kingdom", 1965),
                new Author("George R. R. Martin","grrm@inkwell.us",        "United States",  1948),
                new Author("Margaret Atwood",    "atwood@inkwell.ca",      "Canada",         1939),
                new Author("Yuval Noah Harari",  "harari@inkwell.il",      "Israel",         1976),
                new Author("Paulo Coelho",       "coelho@inkwell.br",      "Brazil",         1947)
        );
        authorRepository.saveAll(authors);

        List<Book> books = List.of(
                new Book("Five Point Someone",        "Fiction",      2004, new BigDecimal("199.00"), authors.get(0)),
                new Book("The Namesake",              "Literary",     2003, new BigDecimal("349.00"), authors.get(1)),
                new Book("The God of Small Things",   "Literary",     1997, new BigDecimal("450.00"), authors.get(2)),
                new Book("Midnight's Children",       "Magical Realism",1981,new BigDecimal("499.00"), authors.get(3)),
                new Book("Norwegian Wood",            "Literary",     1987, new BigDecimal("550.00"), authors.get(4)),
                new Book("Harry Potter & the Sorcerer's Stone", "Fantasy", 1997, new BigDecimal("399.00"), authors.get(5)),
                new Book("A Game of Thrones",         "Fantasy",      1996, new BigDecimal("699.00"), authors.get(6)),
                new Book("The Handmaid's Tale",       "Dystopian",    1985, new BigDecimal("525.00"), authors.get(7)),
                new Book("Sapiens",                   "Non-Fiction",  2011, new BigDecimal("799.00"), authors.get(8)),
                new Book("The Alchemist",             "Fiction",      1988, new BigDecimal("299.00"), authors.get(9))
        );
        bookRepository.saveAll(books);

        log.info("Seeded {} authors and {} books.", authors.size(), books.size());
    }
}
