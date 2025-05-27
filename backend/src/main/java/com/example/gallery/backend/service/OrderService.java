package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.OrderItem;
import com.example.gallery.backend.dto.Search;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.CartMapper;
import com.example.gallery.backend.mapper.OrderMapper;
import com.example.gallery.backend.response.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final ObjectMapper objectMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    JwtService jwtService;

    public OrderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<OrderItem> parseItemJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<OrderItem>>() {});
        } catch (JsonProcessingException e) {
            throw new BizException(ErrorCode.ERROR_017);
        }
    }

    public Map<String, Object> getOrdersWithItems(String token) {
        int memberId = jwtService.getId(token);

        List<Order> orders = orderMapper.findByMemberIdOrderByIdDesc(memberId);

        if (orders.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_018);
        }

        List<Map<String, Object>> ordersWithItems = new ArrayList<>();
        for (Order order : orders) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("order", order);
            orderData.put("items", order.getItemList());

            ordersWithItems.add(orderData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("orders", ordersWithItems);

        return response;
    }

    @Transactional
    public boolean createOrder(Order dto, String token) {
        int memberId = jwtService.getId(token);
        List<OrderItem> items = parseItemJson(dto.getItems());

        Order newOrder = new Order();
        newOrder.setMemberId(memberId);
        newOrder.setName(dto.getName());
        newOrder.setAddress(dto.getAddress());
        newOrder.setPayment(dto.getPayment());
        newOrder.setCardNumber(dto.getCardNumber());
        newOrder.setItems(dto.getItems());

        orderMapper.save(newOrder);
        orderMapper.saveOrderItems(newOrder.getId(), items);
        cartMapper.deleteByMemberId(memberId);

        return true;
    }

    public PageResponse<Map<String, Object>> getAllOrdersWithItems(Search search) {
        int totalCount = orderMapper.countOrder(search);
        List<Order> orders = orderMapper.findAllOrder(search);

        if (orders.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_018);
        }

        List<Map<String, Object>> ordersWithItems = new ArrayList<>();

        for (Order order : orders) {
            String itemsJson = order.getItems();
            List<OrderItem> itemList = parseItemJson(itemsJson);

            Map<String, Object> orderData = new HashMap<>();
            orderData.put("order", order);
            orderData.put("items", itemList);

            ordersWithItems.add(orderData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("orders", ordersWithItems);

        int totalPage = (int)Math.ceil((double) totalCount / search.getRecordSize());

        //        return response;
        return new PageResponse<>(ordersWithItems, totalCount, search.getPage(), totalPage);
    }

    public Map<String, Object> getTopProducts() {
        List<Map<String, Object>> items = orderMapper.findTopProducts();

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);

        return response;
    }

}
