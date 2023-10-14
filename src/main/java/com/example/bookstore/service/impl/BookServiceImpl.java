package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book bookFromDB = bookRepository.save(bookMapper.toModel(bookRequestDto));
        return bookMapper.toDto(bookFromDB);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Optional<Book> bookFromDB = bookRepository.findById(id);
        if (bookFromDB.isPresent()) {
            return bookMapper.toDto(bookFromDB.get());
        }
        throw new EntityNotFoundException("Can't find book by id: " + id);
    }
}
