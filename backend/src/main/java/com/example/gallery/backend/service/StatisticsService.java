package com.example.gallery.backend.service;

import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.dto.statistics.Statistics;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.mapper.StatisticsMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatisticsService {

    @Autowired
    StatisticsMapper statisticsMapper;


    public Map<String, Object> getStatistics(String type, String date, int period) {
        Map<String, Object> result = new HashMap<>();

        String thisMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        Statistics totalStats = statisticsMapper.getMonthlyTotalStats(thisMonth);
        result.put("totalStats", totalStats);

        switch (type) {
            case "daily" -> result.put("dailyStats", statisticsMapper.getDailyStats(date, period));
            case "weekly" -> result.put("weeklyStats", statisticsMapper.getWeeklyStats(date, period));
            case "monthly" -> result.put("monthlyStats", statisticsMapper.getMonthlyStats(date, period));
            case "product" -> result.put("productStats", statisticsMapper.getProductStats(date, period));
            default -> result.put("dailyStats", statisticsMapper.getDailyStats(date, period));
        }

        return result;
    }
}
