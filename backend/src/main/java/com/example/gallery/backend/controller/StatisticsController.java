package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Statistics;
import com.example.gallery.backend.mapper.StatisticsMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatisticsController {

    @Autowired
    StatisticsMapper statisticsMapper;

    @GetMapping("/api/admin/statistics/daily")
    public ResponseEntity<ApiResponse<List<Statistics>>> getDailyStats() {
        List<Statistics> response = statisticsMapper.findDailyStats();

        return ResponseFactory.success(response);

    }

    @GetMapping("/api/admin/statistics/monthly")
    public ResponseEntity<ApiResponse<List<Statistics>>> findMonthlyStats() {
        List<Statistics> response = statisticsMapper.findMonthlyStats();

        return ResponseFactory.success(response);
    }

    @GetMapping("/api/admin/statistics/product")
    public ResponseEntity<ApiResponse<List<Statistics>>> getProductStats() {
        List<Statistics> response = statisticsMapper.findProductStats();

        return ResponseFactory.success(response);
    }
}
