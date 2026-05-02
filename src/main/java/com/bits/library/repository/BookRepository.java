package com.bits.library.repository;

import com.bits.library.dto.AuthorBookView;
import com.bits.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Custom JPQL inner join between Book and Author. Projects the result into
     * AuthorBookView so the view layer never touches lazy associations.
     */
    @Query("SELECT new com.bits.library.dto.AuthorBookView(" +
           "  a.name, a.country, b.title, b.genre, b.publishedYear, b.price) " +
           "FROM Book b INNER JOIN b.author a " +
           "ORDER BY a.name ASC, b.publishedYear DESC")
    List<AuthorBookView> findAllAuthorsWithBooks();

    @Query("SELECT new com.bits.library.dto.AuthorBookView(" +
           "  a.name, a.country, b.title, b.genre, b.publishedYear, b.price) " +
           "FROM Book b INNER JOIN b.author a " +
           "WHERE LOWER(b.genre) = LOWER(:genre) " +
           "ORDER BY b.publishedYear DESC")
    List<AuthorBookView> findByGenreJoined(String genre);
}
