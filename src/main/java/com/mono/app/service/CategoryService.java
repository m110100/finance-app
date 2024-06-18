package com.mono.app.service;

import com.mono.app.dto.request.SubCategoryRequest;
import com.mono.app.dto.response.CategoryResponse;
import com.mono.app.dto.response.SubCategoryResponse;
import com.mono.app.exceptions.BusinessFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import com.mono.app.model.Category;
import com.mono.app.model.SubCategory;
import com.mono.app.model.User;
import com.mono.app.repository.CategoryRepository;
import com.mono.app.repository.SubCategoryRepository;
import com.mono.app.utils.mapper.types.Mapper;
import com.mono.app.utils.security.AuthenticatedUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    private final Mapper<SubCategory, SubCategoryResponse> subCategoryMapper;
    private final Mapper<Category, CategoryResponse> categoryMapper;
    private final AuthenticatedUserResolver resolver;

    @Autowired
    public CategoryService(
            SubCategoryRepository subCategoryRepository,
            CategoryRepository categoryRepository,
            @Qualifier("subCategoryMapper")
            Mapper<SubCategory, SubCategoryResponse> subCategoryMapper,
            @Qualifier("categoryMapper")
            Mapper<Category, CategoryResponse> categoryMapper,
            AuthenticatedUserResolver resolver
    ) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryMapper = subCategoryMapper;
        this.categoryMapper = categoryMapper;
        this.resolver = resolver;
    }

    public Set<CategoryResponse> getAllCategoriesWithUserSubCategories() {
        User user = resolver.getAuthenticatedUser();

        Set<Category> categories = categoryRepository.findAllCategories();
        Set<SubCategory> subCategories = getAllUserSubCategories(user.getId());

        categories.forEach(category -> {
            category.setSubCategories(
                    subCategories.stream()
                            .filter(subCategory -> subCategory.getCategory().getId().equals(category.getId()))
                            .collect(toSet())
            );
        });

        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(toSet());
    }

    public SubCategoryResponse addSubCategory(SubCategoryRequest request) {
        User user = resolver.getAuthenticatedUser();

        final Optional<SubCategory> existingSubCategory = subCategoryRepository.findUserSubCategory(request.name(), user.getId());

        if (existingSubCategory.isPresent()) {
            throw new BusinessFault(
                    String.format("Subcategory with name %s already exists", request.name()),
                    ErrorCode.E002,
                    ErrorType.ERROR);
        } else {
            Optional<Category> existingCategory = getCategoryByName(request.category());

            if (existingCategory.isEmpty()) {
                throw new BusinessFault(
                        "Category not found",
                        ErrorCode.E001,
                        ErrorType.ERROR
                );
            }

            Category category = existingCategory.get();

            SubCategory subCategory = SubCategory.builder()
                    .name(request.name())
                    .isEnabled(true)
                    .category(category)
                    .user(user)
                    .build();

            subCategory = subCategoryRepository.save(subCategory);

            return subCategoryMapper.toDTO(subCategory);
        }
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    public Set<SubCategory> getAllUserSubCategories(Long userId) {
        return subCategoryRepository.findAllUserSubCategories(userId);
    }

    public Optional<SubCategory> getSubCategoryByName(String name, Long userId) {
        return subCategoryRepository.findUserSubCategory(name, userId);
    }
}
