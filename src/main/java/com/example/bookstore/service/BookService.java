package com.example.bookstore.service;

import com.example.bookstore.dto.book.BookResponseDto;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto book);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    void deleteById(Long id);

    List<BookResponseDto> getBookByCategoryId(Long id);

    BookResponseDto updateById(CreateBookRequestDto bookRequestDto, Long id);

    List<BookResponseDto> search(BookSearchParameters searchParameters, Pageable pageable);
}
