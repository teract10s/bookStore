package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/delete-all-books.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/categories/delete-all-categories.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/categories/insert-two-categories.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/insert-five-books.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/books_categories/insert-books-and-categories.sql"));
        }
    }

    @Test
    @DisplayName(value = "Get books by non-existent category id")
    void getBooksByCategoryId_NonExistingCategory_EmptyList() {
        List<Book> actual = bookRepository.getBooksByCategoryId(100L);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName(value = "Get books by existent category id")
    void getBooksByCategoryId_ExistingCategory_ListOfFourBooks() {
        List<Book> actual = bookRepository.getBooksByCategoryId(1L);
        Assertions.assertEquals(3, actual.size());
    }
}
