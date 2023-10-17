package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParameters;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.book.BookSpecificationBuilder;
import com.example.bookstore.service.BookService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book bookFromDB = bookRepository.save(bookMapper.toBook(bookRequestDto));
        return bookMapper.toDto(bookFromDB);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto updateById(CreateBookRequestDto bookRequestDto, Long id) {
        Book bookFromDb = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Can't find book by id: " + id));
        bookFromDb = updateField(bookFromDb, bookRequestDto);
        return bookMapper.toDto(bookRepository.save(bookFromDb));
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
    public List<BookDto> search(BookSearchParameters searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    private Book updateField(Book bookFromDb, CreateBookRequestDto bookRequestDto) {
        if (bookRequestDto.title() != null) {
            bookFromDb.setTitle(bookRequestDto.title());
        }
        if (bookRequestDto.author() != null) {
            bookFromDb.setAuthor(bookRequestDto.author());
        }
        if (bookRequestDto.isbn() != null) {
            bookFromDb.setIsbn(bookRequestDto.isbn());
        }
        if (bookRequestDto.price() != null) {
            bookFromDb.setPrice(bookRequestDto.price());
        }
        if (bookRequestDto.description() != null) {
            bookFromDb.setDescription(bookRequestDto.description());
        }
        if (bookRequestDto.coverImage() != null) {
            bookFromDb.setCoverImage(bookRequestDto.coverImage());
        }
        return bookFromDb;
    }
}
