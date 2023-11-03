package com.example.bookstore.service;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDtoWithoutCategoryIds save(CreateBookRequestDto book);

    List<BookDto> findAllWithCategories(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBookByCategoryId(Long id);

    BookDto updateById(CreateBookRequestDto bookRequestDto, Long id);

    List<BookDtoWithoutCategoryIds> search(BookSearchParameters searchParameters,
                                           Pageable pageable);
}
