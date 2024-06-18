package com.mono.app.controller;

import com.mono.app.dto.request.SubCategoryRequest;
import com.mono.app.dto.response.SubCategoryResponse;
import com.mono.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {
    private final CategoryService service;

    @PostMapping("/new")
    public ResponseEntity<SubCategoryResponse> addNewSubcategory(@RequestBody SubCategoryRequest request) {
        SubCategoryResponse response = service.addSubCategory(request);

        return ResponseEntity.ok(response);
    }
}
