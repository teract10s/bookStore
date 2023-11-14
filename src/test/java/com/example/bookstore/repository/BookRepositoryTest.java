package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
                    new ClassPathResource("database/categories/add-two-categories.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/add-five-books.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books_categories/create-relations-between-books-and-categories.sql"));
        }
    }

    @Test
    void getBooksByCategoryId_NonExistingCategory_EmptyList() {
        List<Book> actual = bookRepository.getBooksByCategoryId(100L);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void getBooksByCategoryId_ExistingCategory_ListOfFourBooks() {
        List<Book> actual = bookRepository.getBooksByCategoryId(1L);
        Assertions.assertEquals(4, actual.size());
    }
}
