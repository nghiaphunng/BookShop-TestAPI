package com.bookshop.demo.response;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDTO {
	Long bookId;
	String bookTitle;
	String bookAuthor;
	BigDecimal bookPrice;
	Integer bookQuantity;
	Integer bookSold;
}
