package com.mono.app.controller;

import com.mono.app.dto.response.CategoryResponse;
import com.mono.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<Set<CategoryResponse>> getAllCategoriesWithUserSubCategories() {
        Set<CategoryResponse> categories = service.getAllCategoriesWithUserSubCategories();

        return ResponseEntity.ok(categories);
    }
}
