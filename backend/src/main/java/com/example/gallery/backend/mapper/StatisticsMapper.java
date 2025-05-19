package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.statistics.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface StatisticsMapper {

    // 1) 총합 통계 (일/주/월)
    Statistics getSalesStats(@Param("type") String type, @Param("date") String date, @Param("period") int period);

    // 2) 일별 매출 통계
    List<DailyStat> getDailyStats(@Param("date") String date, @Param("period") int period);

    // 3) 주간별 매출 통계
    List<WeeklyStat> getWeeklyStats(@Param("date") String date, @Param("period") int period);

    // 4) 월별 매출 통계
    List<MonthlyStat> getMonthlyStats(@Param("date") String date, @Param("period") int period);

    // 5) 상품별 매출 통계
    List<ProductStat> getProductStats(@Param("date") String date, @Param("period") int period);

    // 카드용 통계(이번달 기준)
    Statistics getMonthlyTotalStats(@Param("yearMonth") String yearMonth);

}
