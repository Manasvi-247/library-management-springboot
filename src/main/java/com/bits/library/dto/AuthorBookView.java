package com.bits.library.dto;

import java.math.BigDecimal;

public class AuthorBookView {

    private final String authorName;
    private final String authorCountry;
    private final String bookTitle;
    private final String genre;
    private final Integer publishedYear;
    private final BigDecimal price;

    public AuthorBookView(String authorName, String authorCountry, String bookTitle,
                          String genre, Integer publishedYear, BigDecimal price) {
        this.authorName = authorName;
        this.authorCountry = authorCountry;
        this.bookTitle = bookTitle;
        this.genre = genre;
        this.publishedYear = publishedYear;
        this.price = price;
    }

    public String getAuthorName() { return authorName; }
    public String getAuthorCountry() { return authorCountry; }
    public String getBookTitle() { return bookTitle; }
    public String getGenre() { return genre; }
    public Integer getPublishedYear() { return publishedYear; }
    public BigDecimal getPrice() { return price; }
}
