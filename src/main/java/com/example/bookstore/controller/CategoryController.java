package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Category management", description = "Endpoints for managing category")
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all categories")
    @PreAuthorize("hasAuthority('USER')")
    public List<CategoryResponseDto> getAll(@PageableDefault(sort = "name") Pageable pageable) {
        return categoryService.getAll(pageable);
    }

    @GetMapping("/{categoryId}/books")
    @Operation(summary = "Get books by category",
            description = "Getting books by receiving categoryId in path")
    @PreAuthorize("hasAuthority('USER')")
    public List<BookDtoWithoutCategoryIds> getBooksByCategory(@PathVariable Long categoryId) {
        return bookService.getBookByCategoryId(categoryId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    @PreAuthorize("hasAuthority('USER')")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    @Operation(summary = "Create new category")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseDto createCategory(
            @RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id, @RequestBody @Valid CreateCategoryRequestDto categoryDto
    ) {
        return categoryService.updateById(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete category by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
