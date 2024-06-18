package com.mono.app.controller;

import com.mono.app.dto.request.LabelRequest;
import com.mono.app.dto.response.LabelResponse;
import com.mono.app.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService service;

    @GetMapping
    public ResponseEntity<Set<LabelResponse>> getAllUserLabels() {
        Set<LabelResponse> labels = service.getAllUserLabels();

        return ResponseEntity.ok(labels);
    }

    @PostMapping("/new")
    public ResponseEntity<Void> addLabel(@RequestBody LabelRequest request) {
        service.addLabel(request);

        return ResponseEntity.noContent().build();
    }
}
