package com.example.gallery.backend.service;

import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.dto.statistics.MonthlyStat;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        if ("monthly".equals(type)) {
            List<MonthlyStat> stats = statisticsMapper.getMonthlyStats(date, period);
            // 월별 리스트 채우기
            List<MonthlyStat> filledStats = fillMissingMonths(stats, date);
            result.put("monthlyStats", filledStats);
        } else {
            switch (type) {
                case "daily" -> result.put("dailyStats", statisticsMapper.getDailyStats(date, period));
                case "weekly" -> result.put("weeklyStats", statisticsMapper.getWeeklyStats(date, period));
//                case "monthly" -> result.put("monthlyStats", statisticsMapper.getMonthlyStats(date, period));
                case "product" -> result.put("productStats", statisticsMapper.getProductStats(date, period));
                default -> result.put("dailyStats", statisticsMapper.getDailyStats(date, period));
            }
        }

        return result;
    }


    // 해당 연도의 1월부터 12월까지 모든 월을 포함, 데이터 없으면 total_price 0으로 채움
    private List<MonthlyStat> fillMissingMonths(List<MonthlyStat> stats, String year) {
//        Map<String, MonthlyStat> map = stats.stream()
//                .collect(Collectors.toMap(MonthlyStat::getMonth, Function.identity()));

        Map<String, MonthlyStat> map = new HashMap<>();
        for (MonthlyStat stat : stats) {
            map.put(stat.getMonth(), stat);
        }

        List<MonthlyStat> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            String monthStr = String.format("%s-%02d", year.substring(0, 4), m);
            MonthlyStat stat = map.get(monthStr);
            if (stat == null) {
                stat = new MonthlyStat();
                stat.setMonth(monthStr);
                stat.setTotalPrice(BigDecimal.ZERO);
            }
            result.add(stat);
        }
        return result;
    }
}
