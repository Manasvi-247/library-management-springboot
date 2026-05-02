package com.bits.library.repository;

import com.bits.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
