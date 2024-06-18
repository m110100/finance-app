package com.mono.app.dto.response;

import java.util.Set;

public record CategoryResponse(
        String name,
        Set<SubCategoryResponse> subCategories
) { }
