package com.example.bookstore.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.mapper.impl.BookMapperImpl;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Spy
    private BookMapper bookMapper = new BookMapperImpl();
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName(value = "Save book with valid input")
    void save_ValidInput_ReturnBookRequestDto() {
        Book notSavedBook = getDefaultBook();
        notSavedBook.setId(null);
        notSavedBook.setCategories(null);

        Book savedBookWithoutCategories = getDefaultBook();
        savedBookWithoutCategories.setCategories(Collections.emptySet());

        Book fullySavedBook = getDefaultBook();

        when(bookRepository.save(notSavedBook)).thenReturn(savedBookWithoutCategories);
        when(categoryRepository.findAllById(List.of(1L))).thenReturn(List.of(getDefaultCategory()));
        when(bookRepository.save(savedBookWithoutCategories)).thenReturn(fullySavedBook);

        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto("title", "author", "isbn",
                BigDecimal.TEN, "description", "cover_image", List.of(1L));
        BookDtoWithoutCategoryIds expected = new BookDtoWithoutCategoryIds(1L, "title", "author",
                "isbn", BigDecimal.TEN, "description", "cover_image");
        BookDtoWithoutCategoryIds actual = bookService.save(bookRequestDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Save book with invalid input")
    void findAllWithCategories_ReturnsBooksWithCategory() {
        Pageable pageable = Pageable.ofSize(10);
        BookDto bookDto = new BookDto(1L, "title", "author", "isbn",
                BigDecimal.TEN, "description", "cover_image", List.of(1L));
        List<BookDto> expected = List.of(bookDto);

        when(bookRepository.findAllWithCategories(pageable)).thenReturn(List.of(getDefaultBook()));
        List<BookDto> actual = bookService.findAllWithCategories(Pageable.ofSize(10));

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    @DisplayName(value = "Update book with valid input")
    void updateById_ValidInput_ReturnUpdatedBookDto() {
        Book bookFromDb = getDefaultBook();
        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto("updated title",
                "author", "isbn", BigDecimal.TEN, "description",
                "cover_image", List.of(1L));

        when(bookRepository.findById(bookFromDb.getId())).thenReturn(Optional.of(bookFromDb));
        bookFromDb.setTitle(updateBookRequestDto.title());
        when(categoryRepository.findAllById(List.of(1L))).thenReturn(List.of(getDefaultCategory()));
        when(bookRepository.save(bookFromDb)).thenReturn(bookFromDb);
        BookDto actual = bookService.updateById(updateBookRequestDto, bookFromDb.getId());

        BookDto expected = new BookDto(1L, "updated title", "author", "isbn",
                BigDecimal.TEN, "description", "cover_image", List.of(1L));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Update book by non-existent id")
    void updateById_NotExistingBook_ThrowException() {
        Long notExistingId = 100L;
        when(bookRepository.findById(notExistingId)).thenReturn(Optional.empty());
        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto("updated title",
                "author", "isbn", BigDecimal.TEN, "description",
                "cover_image", List.of(1L));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> bookService.updateById(updateBookRequestDto, notExistingId));
    }

    @Test
    @DisplayName(value = "Find book by id")
    void findById_ExistingId_ReturnBookDto() {
        Long id = 1L;
        Book bookFromDb = getDefaultBook();
        BookDto expected = new BookDto(1L, "title", "author", "isbn",
                BigDecimal.TEN, "description", "cover_image", List.of(1L));

        when(bookRepository.findById(id)).thenReturn(Optional.of(bookFromDb));
        BookDto actual = bookService.findById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Find book by non-existent id")
    void findById_NotExistingId_ThrowException() {
        Long id = 100L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(id));
    }

    @Test
    @DisplayName(value = "Delete book by existent id")
    void deleteById_ValidInput_DeleteBook() {
        Long id = 1L;
        bookService.deleteById(id);
        verify(bookRepository).deleteById(id);
    }

    @Test
    @DisplayName(value = "Getting books by existent category id")
    void getBookByCategoryId_ValidInput_ReturnList() {
        Long categoryId = 1L;
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds(1L, "title", "author",
                "isbn", BigDecimal.TEN, "description", "cover_image");
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDto);
        when(bookRepository.getBooksByCategoryId(categoryId)).thenReturn(List.of(getDefaultBook()));
        List<BookDtoWithoutCategoryIds> actual = bookService.getBookByCategoryId(categoryId);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    @DisplayName(value = "Getting books by non-existent category id")
    void getBookByCategoryId_NotValidInput_ReturnList() {
        Long categoryId = 100L;
        when(bookRepository.getBooksByCategoryId(categoryId)).thenReturn(Collections.emptyList());
        List<BookDtoWithoutCategoryIds> actual = bookService.getBookByCategoryId(categoryId);
        Assertions.assertEquals(0, actual.size());
    }

    private Book getDefaultBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("description");
        book.setCoverImage("cover_image");
        book.setCategories(Set.of(getDefaultCategory()));
        return book;
    }

    private Category getDefaultCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("category");
        category.setDescription("description");
        category.setDeleted(false);
        return category;
    }
}
