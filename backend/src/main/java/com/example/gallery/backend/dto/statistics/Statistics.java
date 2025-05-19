package com.example.gallery.backend.dto.statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Statistics {
    /* int -> Integer로 변경 : nullable 허용 */
    // 총 매출
    private Integer totalPrice;

    // 총 주문수
    private int totalOrders;

    // 총 상품수
    private int totalItems;

    // 차트에 필요한 상세 통계
    private List<? extends ChartStat> chartStats;
}
