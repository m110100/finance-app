package com.mono.app.utils.mapper;

import com.mono.app.dto.request.SubCategoryRequest;
import com.mono.app.dto.response.SubCategoryResponse;
import com.mono.app.model.SubCategory;
import com.mono.app.utils.mapper.types.Mapper;
import org.springframework.stereotype.Component;

@Component("subCategoryMapper")
public class SubCategoryMapper implements Mapper<SubCategory, SubCategoryResponse> {
    @Override
    public SubCategoryResponse toDTO(SubCategory dao) {
        return new SubCategoryResponse(
                dao.getName()
        );
    }
}
