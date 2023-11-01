package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Override
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);

    @Query(value = "from Book b join fetch b.categories WHERE b.isDeleted = false")
    List<Book> findAllWithCategories(Pageable pageable);

    @Query("select b from Book b join fetch b.categories c where c.id = :id")
    List<Book> getBooksByCategoryId(Long id);
}
