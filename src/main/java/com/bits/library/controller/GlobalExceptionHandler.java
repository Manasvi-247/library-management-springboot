package com.bits.library.controller;

import com.bits.library.service.AuthorNotFoundException;
import com.bits.library.service.BookNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleIntegrity(DataIntegrityViolationException ex,
                                        HttpServletRequest request) {
        log.warn("Data integrity violation at {}: {}", request.getRequestURI(), ex.getMostSpecificCause().getMessage());
        ModelAndView mv = new ModelAndView("error");
        mv.setStatus(HttpStatus.CONFLICT);
        mv.addObject("title", "Duplicate or invalid data");
        mv.addObject("message",
                "This save would violate a uniqueness or foreign-key constraint. " +
                "Most likely the email or (title + author) combination already exists. " +
                "Please go back and adjust the values.");
        mv.addObject("detail", ex.getMostSpecificCause().getMessage());
        return mv;
    }

    @ExceptionHandler({AuthorNotFoundException.class, BookNotFoundException.class})
    public ModelAndView handleNotFound(RuntimeException ex) {
        ModelAndView mv = new ModelAndView("error");
        mv.setStatus(HttpStatus.NOT_FOUND);
        mv.addObject("title", "Record not found");
        mv.addObject("message", ex.getMessage());
        mv.addObject("detail", null);
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        ModelAndView mv = new ModelAndView("error");
        mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        mv.addObject("title", "Something went wrong");
        mv.addObject("message", "An unexpected error occurred while processing your request.");
        mv.addObject("detail", ex.getMessage());
        return mv;
    }
}
