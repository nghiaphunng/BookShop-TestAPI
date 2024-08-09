package com.bookshop.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    Integer bookId;

    @Column(name = "book_title", nullable = false)
    String bookTitle;

    @Column(name = "book_author", nullable = false)
    String bookAuthor;

    @Column(name = "book_price", nullable = false)
    BigDecimal bookPrice;

    @Column(name = "book_quantity", nullable = false)
    Integer bookQuantity;

    @Column(name = "book_sold", nullable = false)
    Integer bookSold;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
