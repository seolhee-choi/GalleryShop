package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper{

    List<Order> findByMemberIdOrderByIdDesc(@Param("memberId") int memberId);

    void save(Order order);
    List<Order> findAllOrder();

    void saveOrderItems(@Param("orderId") int orderId, @Param("items") List<OrderItem> items);

}
