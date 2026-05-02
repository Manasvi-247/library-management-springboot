package com.bits.library.service;

import com.bits.library.entity.Author;
import com.bits.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public Author create(Author author) {
        return authorRepository.save(author);
    }

    public Author update(Long id, Author updates) {
        Author existing = findById(id);
        existing.setName(updates.getName());
        existing.setEmail(updates.getEmail());
        existing.setCountry(updates.getCountry());
        existing.setBirthYear(updates.getBirthYear());
        return authorRepository.save(existing);
    }
}
