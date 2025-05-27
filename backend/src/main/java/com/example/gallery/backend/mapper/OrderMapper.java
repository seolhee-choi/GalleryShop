package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.OrderItem;
import com.example.gallery.backend.dto.Search;
import com.example.gallery.backend.dto.statistics.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper{

    List<Order> findByMemberIdOrderByIdDesc(@Param("memberId") int memberId);

    void save(Order order);
    List<Order> findAllOrder(@Param("search") Search search);

    int countOrder(Search params);

    void saveOrderItems(@Param("orderId") int orderId, @Param("items") List<OrderItem> items);

    List<Map<String, Object>> findTopProducts();

}
