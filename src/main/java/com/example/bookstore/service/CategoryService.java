package com.example.bookstore.service;

import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> getAll(Pageable pageable);

    CategoryResponseDto getCategoryById(Long id);

    CategoryResponseDto save(CreateCategoryRequestDto categoryDto);

    CategoryResponseDto updateById(Long id, CreateCategoryRequestDto categoryDto);

    void deleteById(Long id);
}
