package com.bits.library.controller;

import com.bits.library.entity.Author;
import com.bits.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("mode", "create");
        return "authors/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("author") Author author,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "authors/form";
        }
        Author saved = authorService.create(author);
        redirect.addFlashAttribute("flashSuccess",
                "Author '" + saved.getName() + "' created successfully.");
        return "redirect:/authors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.findById(id));
        model.addAttribute("mode", "edit");
        return "authors/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("author") Author author,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "authors/form";
        }
        Author saved = authorService.update(id, author);
        redirect.addFlashAttribute("flashSuccess",
                "Author '" + saved.getName() + "' updated successfully.");
        return "redirect:/authors";
    }
}
