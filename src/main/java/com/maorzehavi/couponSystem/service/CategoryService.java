package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.dto.request.CategoryRequest;
import com.maorzehavi.couponSystem.model.dto.response.CategoryResponse;
import com.maorzehavi.couponSystem.model.entity.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Boolean existsByName(String name);

    Optional<Long> getIdByName(String name);
    Optional<CategoryResponse> getCategory(Long id);
    Optional<Category> getCategoryEntity(Long id);
    Optional<CategoryResponse> getCategory(String name);
    @Modifying
    @Transactional
    Optional<CategoryResponse> createCategory(CategoryRequest categoryRequest);
    @Modifying
    @Transactional
    Optional<CategoryResponse> updateCategory(Long id, CategoryRequest categoryRequest);
    @Modifying
    @Transactional
    Optional<Boolean> deleteCategory(Long id);

    List<CategoryResponse> getAllCategories();


    Category mapToCategory(CategoryResponse categoryResponse);
    Category mapToCategory(CategoryRequest categoryRequest);
    CategoryResponse mapToCategoryResponse(Category category);
}