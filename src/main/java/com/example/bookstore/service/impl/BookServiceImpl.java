package com.example.bookstore.service.impl;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.BookSpecificationBuilder;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.service.BookService;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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
    private final CategoryRepository categoryRepository;

    @Override
    public BookDtoWithoutCategoryIds save(CreateBookRequestDto bookRequestDto) {
        Book notSavedBook = bookMapper.toBook(bookRequestDto);
        Book book = bookRepository.save(notSavedBook);
        setCategories(book, bookRequestDto.categoryIds());
        return bookMapper.toDtoWithoutCategories(bookRepository.save(book));
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
        setCategories(bookFromDb, bookRequestDto.categoryIds());
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

    private void setCategories(Book book, List<Long> categoryIds) {
        if (categoryIds != null) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
            book.setCategories(categories);
        }
    }
}
