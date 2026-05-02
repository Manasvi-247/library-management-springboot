package com.bits.library.service;

import com.bits.library.entity.Author;
import com.bits.library.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock  AuthorRepository repo;
    @InjectMocks AuthorService service;

    @Test
    void findAll_delegates_to_repository() {
        when(repo.findAll()).thenReturn(List.of(new Author("A", "a@x", "IN", 1990)));
        assertThat(service.findAll()).hasSize(1);
        verify(repo).findAll();
    }

    @Test
    void findById_returns_when_present() {
        Author a = new Author("A", "a@x", "IN", 1990);
        when(repo.findById(7L)).thenReturn(Optional.of(a));
        assertThat(service.findById(7L)).isSameAs(a);
    }

    @Test
    void findById_throws_when_missing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_saves_through_repository() {
        Author input = new Author("New", "new@x", "IN", 2000);
        when(repo.save(any(Author.class))).thenAnswer(inv -> inv.getArgument(0));
        Author saved = service.create(input);
        assertThat(saved.getName()).isEqualTo("New");
        verify(repo).save(input);
    }

    @Test
    void update_copies_fields_onto_existing_record() {
        Author existing = new Author("Old", "old@x", "IN", 1980);
        existing.setId(5L);
        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Author.class))).thenAnswer(inv -> inv.getArgument(0));

        Author updates = new Author("New name", "new@x", "JP", 1985);
        Author result = service.update(5L, updates);

        assertThat(result.getName()).isEqualTo("New name");
        assertThat(result.getCountry()).isEqualTo("JP");
        assertThat(result.getBirthYear()).isEqualTo(1985);
        verify(repo).save(existing);
    }
}
