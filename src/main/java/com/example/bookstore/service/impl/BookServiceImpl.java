package com.example.bookstore.service.impl;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.BookSpecificationBuilder;
import com.example.bookstore.service.BookService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDtoWithoutCategoryIds save(CreateBookRequestDto bookRequestDto) {
        Book bookFromDB = bookRepository.save(bookMapper.toBook(bookRequestDto));
        return bookMapper.toDtoWithoutCategories(bookFromDB);
    }

    @Override
    public List<BookDto> findAllWithCategories(Pageable pageable) {
        return bookRepository.findAllWithCategories(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto updateById(CreateBookRequestDto bookRequestDto, Long id) {
        Book bookFromDb = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Can't find book by id: " + id));
        bookMapper.updateBook(bookRequestDto, bookFromDb);
        BookDto dto = bookMapper.toDto(bookRepository.save(bookFromDb));
        bookMapper.setCategoryIds(dto, bookFromDb);
        return dto;
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBookByCategoryId(Long id) {
        return bookRepository.getBooksByCategoryId(id)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> search(BookSearchParameters searchParameters,
                                                  Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .buildFrom(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
