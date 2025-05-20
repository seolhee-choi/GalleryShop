package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.statistics.*;
import com.example.gallery.backend.mapper.StatisticsMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.example.gallery.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/api/admin/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics( @RequestParam String type,
                                                                        @RequestParam String date,
                                                                           @RequestParam(required = false, defaultValue = "7") int period) {


        return ResponseFactory.success(statisticsService.getStatistics(type, date, period));
    }
}
