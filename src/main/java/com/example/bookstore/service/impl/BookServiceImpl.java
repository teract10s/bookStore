package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;

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
    public BookDto updateById(CreateBookRequestDto bookRequestDto, Long id) {
        Book bookFromDb = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Can't find book by id: " + id));
        bookFromDb = updateField(bookFromDb, bookRequestDto);
        return bookMapper.toDto(bookRepository.save(bookFromDb));
    }

    @Override
    public BookDto findById(Long id) {
        Optional<Book> bookFromDB = bookRepository.findById(id);
        if (bookFromDB.isPresent()) {
            return bookMapper.toDto(bookFromDB.get());
        }
        throw new EntityNotFoundException("Can't find book by id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private Book updateField(Book bookFromDb, CreateBookRequestDto bookRequestDto) {
        if (bookRequestDto.getTitle() != null) {
            bookFromDb.setTitle(bookRequestDto.getTitle());
        }
        if (bookRequestDto.getAuthor() != null) {
            bookFromDb.setAuthor(bookRequestDto.getAuthor());
        }
        if (bookRequestDto.getIsbn() != null) {
            bookFromDb.setIsbn(bookRequestDto.getIsbn());
        }
        if (bookRequestDto.getPrice() != null) {
            bookFromDb.setPrice(bookRequestDto.getPrice());
        }
        if (bookRequestDto.getDescription() != null) {
            bookFromDb.setDescription(bookRequestDto.getDescription());
        }
        if (bookRequestDto.getCoverImage() != null) {
            bookFromDb.setCoverImage(bookRequestDto.getCoverImage());
        }
        return bookFromDb;
    }
}
