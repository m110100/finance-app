package com.mono.app.utils.mapper;

import com.mono.app.dto.response.CategoryResponse;
import com.mono.app.dto.response.SubCategoryResponse;
import com.mono.app.model.Category;
import com.mono.app.model.SubCategory;
import com.mono.app.utils.mapper.types.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Component("categoryMapper")
public class CategoryMapper implements Mapper<Category, CategoryResponse> {
    private final Mapper<SubCategory, SubCategoryResponse> subCategoryMapper;

    public CategoryMapper(
            @Qualifier("subCategoryMapper")
            Mapper<SubCategory, SubCategoryResponse> subCategoryMapper
    ) {
        this.subCategoryMapper = subCategoryMapper;
    }

    @Override
    public CategoryResponse toDTO(Category dao) {
        return new CategoryResponse(
                dao.getName(),
                dao.getSubCategories()
                        .stream()
                        .map(subCategoryMapper::toDTO)
                        .collect(toSet())
        );
    }
}
