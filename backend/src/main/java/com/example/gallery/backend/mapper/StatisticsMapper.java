package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Statistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatisticsMapper {
    List<Statistics> findDailyStats();
    List<Statistics> findMonthlyStats();
    List<Statistics> findProductStats();
}
