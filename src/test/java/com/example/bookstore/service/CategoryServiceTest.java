package com.example.bookstore.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.mapper.impl.CategoryMapperImpl;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName(value = "Get all categories")
    void getAll_ReturnList() {
        Pageable pageable = Pageable.ofSize(10);
        List<CategoryResponseDto> expected = List.of(getDefaultDto());
        List<Category> categories = List.of(getDefaultCategory());
        Page<Category> page = new PageImpl<>(categories, pageable, categories.size());
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        List<CategoryResponseDto> actual = categoryService.getAll(pageable);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    @DisplayName(value = "Get category by existent id")
    void getCategoryById_ValidId_ReturnDto() {
        Long id = 1L;
        Category category = getDefaultCategory();
        CategoryResponseDto expected = getDefaultDto();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryResponseDto actual = categoryService.getCategoryById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Save category with valid input")
    void save_ValidInput_ReturnDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("category",
                "description");
        Category category = new Category(null, "category", "description", false);
        when(categoryRepository.save(category)).thenReturn(getDefaultCategory());

        CategoryResponseDto actual = categoryService.save(requestDto);
        Assertions.assertEquals(getDefaultDto(), actual);
    }

    @Test
    @DisplayName(value = "Update category by non-existent id")
    void updateById_NotExistingCategory_ThrowException() {
        Long notExistingId = 100L;
        when(categoryRepository.findById(notExistingId)).thenReturn(Optional.empty());
        CreateCategoryRequestDto updateCategoryRequestDto =
                new CreateCategoryRequestDto("category", "description");
        Assertions.assertThrows(NoSuchElementException.class,
                () -> categoryService.updateById(notExistingId, updateCategoryRequestDto));
    }

    @Test
    @DisplayName(value = "Update category by existent id")
    void updateById_ExistingCategory_ReturnDto() {
        Long existingId = 1L;
        Category updatedCategory = getDefaultCategory();
        updatedCategory.setName("updated category");
        CreateCategoryRequestDto updateCategoryRequestDto =
                new CreateCategoryRequestDto("updated category",
                        "description");
        CategoryResponseDto expected = new CategoryResponseDto(1L,
                "updated category", "description");

        when(categoryRepository.findById(existingId)).thenReturn(Optional.of(getDefaultCategory()));
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        CategoryResponseDto actual = categoryService.updateById(existingId,
                updateCategoryRequestDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Delete category by existent id")
    void deleteById_ValidInput_DeleteCategory() {
        Long id = 1L;
        categoryService.deleteById(id);
        verify(categoryRepository).deleteById(id);
    }

    private CategoryResponseDto getDefaultDto() {
        return new CategoryResponseDto(1L, "category", "description");
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
