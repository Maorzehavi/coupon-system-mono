package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.dto.request.CategoryRequest;
import com.maorzehavi.couponSystem.model.dto.response.CategoryResponse;
import com.maorzehavi.couponSystem.model.entity.Category;
import com.maorzehavi.couponSystem.repository.CategoryRepository;
import com.maorzehavi.couponSystem.service.CategoryService;
import com.maorzehavi.couponSystem.service.CouponService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CouponService couponService;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               @Lazy CouponService couponService) {
        this.categoryRepository = categoryRepository;
        this.couponService = couponService;
    }

    @Override
    public Boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public Optional<Long> getIdByName(String name) {
        return categoryRepository.getIdByName(name);
    }

    @Override
    public Optional<CategoryResponse> getCategory(Long id) {
        return categoryRepository.findById(id).map(this::mapToCategoryResponse);
    }

    @Override
    public Optional<Category> getCategoryEntity(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<CategoryResponse> getCategory(String name) {
        return categoryRepository.findByName(name).map(this::mapToCategoryResponse);
    }

    @Override
    public Optional<CategoryResponse> createCategory(CategoryRequest categoryRequest) {
        if (!existsByName(categoryRequest.getName())) {
            Category category = mapToCategory(categoryRequest);
            return Optional.of(mapToCategoryResponse(categoryRepository.save(category)));
        }
        throw new SystemException("Category " + categoryRequest.getName() + " already exists");
    }

    @Override
    public Optional<CategoryResponse> updateCategory(Long id, CategoryRequest categoryRequest) {
        var category = getCategoryEntity(id);
        if (category.isPresent()) {
            category.get().setName(categoryRequest.getName());
            return Optional.of(mapToCategoryResponse(categoryRepository.save(category.get())));
        }
        throw new SystemException("Category with id '" + id + "' does not exist");
    }

    @Override
    public Optional<Boolean> deleteCategory(Long id) {
        var category = getCategoryEntity(id);
        if (category.isPresent()) {
            couponService.deleteAllByCategory(id);
            categoryRepository.delete(category.get());
            return Optional.of(true);
        }
        throw new SystemException("Category with id '" + id + "' does not exist");
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapToCategoryResponse).toList();
    }

    @Override
    public Category mapToCategory(CategoryResponse categoryResponse) {
        return Category.builder()
                .id(categoryResponse.getId())
                .name(categoryResponse.getName())
                .build();
    }

    @Override
    public Category mapToCategory(CategoryRequest categoryRequest) {
        return Category.builder().name(categoryRequest.getName()).build();
    }

    @Override
    public CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
    }
}
