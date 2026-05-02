package com.bits.library.controller;

import com.bits.library.repository.AuthorRepository;
import com.bits.library.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public HomeController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("authorCount", authorRepository.count());
        model.addAttribute("bookCount", bookRepository.count());
        model.addAttribute("joinedCount", bookRepository.findAllAuthorsWithBooks().size());
        return "home";
    }
}
