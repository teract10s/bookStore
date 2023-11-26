package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing book")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of all available books")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER')")
    public List<BookDto> getAll(
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
        return bookService.findAllWithCategories(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one book by id", description = "Get a bookDto by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER')")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book")
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public BookDtoWithoutCategoryIds createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update book by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public BookDto updateBook(
            @PathVariable Long id, @RequestBody CreateBookRequestDto createBookRequestDto) {
        return bookService.updateById(createBookRequestDto, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Delete book by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Get all books by parameters",
            description = "Get list of all available books by parameters")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER')")
    public List<BookDtoWithoutCategoryIds> search(BookSearchParameters searchParameters,
                                                  @PageableDefault(sort = "title")
                                                  Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }
}
