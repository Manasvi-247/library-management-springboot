package com.bits.library.controller;

import com.bits.library.entity.Book;
import com.bits.library.service.AuthorService;
import com.bits.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books/list";
    }

    @GetMapping("/joined")
    public String joinedView(@RequestParam(value = "genre", required = false) String genre,
                             Model model) {
        if (genre != null && !genre.isBlank()) {
            model.addAttribute("rows", bookService.findByGenre(genre));
            model.addAttribute("filterGenre", genre);
        } else {
            model.addAttribute("rows", bookService.findAllJoined());
        }
        return "books/joined";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("mode", "create");
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("book") Book book,
                         BindingResult bindingResult,
                         @RequestParam(value = "authorId", required = false) Long authorId,
                         Model model,
                         RedirectAttributes redirect) {
        if (authorId == null) {
            bindingResult.rejectValue("author", "author.required", "Author is required");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("mode", "create");
            return "books/form";
        }
        Book saved = bookService.create(book, authorId);
        redirect.addFlashAttribute("flashSuccess",
                "Book '" + saved.getTitle() + "' created successfully.");
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("selectedAuthorId", book.getAuthor().getId());
        model.addAttribute("mode", "edit");
        return "books/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("book") Book book,
                         BindingResult bindingResult,
                         @RequestParam(value = "authorId", required = false) Long authorId,
                         Model model,
                         RedirectAttributes redirect) {
        if (authorId == null) {
            bindingResult.rejectValue("author", "author.required", "Author is required");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("selectedAuthorId", authorId);
            model.addAttribute("mode", "edit");
            return "books/form";
        }
        Book saved = bookService.update(id, book, authorId);
        redirect.addFlashAttribute("flashSuccess",
                "Book '" + saved.getTitle() + "' updated successfully.");
        return "redirect:/books";
    }
}
