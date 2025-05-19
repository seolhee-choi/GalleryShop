package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.statistics.*;
import com.example.gallery.backend.mapper.StatisticsMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
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
    StatisticsMapper statisticsMapper;

    @GetMapping("/api/admin/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics( @RequestParam String type,
                                                                        @RequestParam String date,
                                                                           @RequestParam(required = false, defaultValue = "7") int period) {

        Map<String, Object> result = new HashMap<>();

        // 기본 총합 통계
//        Statistics totalStats = statisticsMapper.getSalesStats(type, date, period);
//        result.put("totalStats", totalStats);


        // 1. 이번 달 날짜 계산
        String thisMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 2. 이번 달 기준 총합 통계 조회 (쿼리는 date LIKE '2025-05%' 식으로 구현 필요)
        Statistics totalStats = statisticsMapper.getMonthlyTotalStats(thisMonth);
        result.put("totalStats", totalStats);

        // 일별/주별/월별 상세 통계 - 차트 그려주기용
        switch (type) {
            case "daily" -> result.put("dailyStats", statisticsMapper.getDailyStats(date, period));
            case "weekly" -> result.put("weeklyStats", statisticsMapper.getWeeklyStats(date, period));
            case "monthly" -> result.put("monthlyStats", statisticsMapper.getMonthlyStats(date, period));
            case "product" -> result.put("productStats", statisticsMapper.getProductStats(date, period));
            default -> result.put("dailyStats", statisticsMapper.getDailyStats(date, period));
        }

        return ResponseFactory.success(result);

    }
}
