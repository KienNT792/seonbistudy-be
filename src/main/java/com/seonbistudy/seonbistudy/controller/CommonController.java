package com.seonbistudy.seonbistudy.controller;

import com.seonbistudy.seonbistudy.dto.response.ApiResponse;
import com.seonbistudy.seonbistudy.model.entity.LevelThreshold;
import com.seonbistudy.seonbistudy.service.ILevelThresholdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Tag(name = "Common", description = "Common APIs")
public class CommonController {
    private final ILevelThresholdService levelThresholdService;

    @GetMapping("/levels")
    public ResponseEntity<ApiResponse<List<LevelThreshold>>> getLevels() {
        return ResponseEntity.ok(ApiResponse.success(levelThresholdService.getAllLevel(), "Get data successful"));
    }
}