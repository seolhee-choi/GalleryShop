package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.OrderItem;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toItemJson(List<OrderItem> items) {
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new BizException(ErrorCode.ERROR_017);
        }
    }

    @DisplayName("주문이 성공적으로 수행되야함")
    @Test
    void 주문생성성공() {
        // given : 테스트 사용자, 토큰 생성
        Member member = new Member();
        member.setEmail("testuser@test.com");
        member.setPassword(new BCryptPasswordEncoder().encode("test123456"));
        memberMapper.insert(member);

        String token = jwtService.getToken(member);

        // 주문 아이템 JSON 문자열
        OrderItem item1 = new OrderItem();
        item1.setItemId(1);
        item1.setItemName("상품 A");
        item1.setPriceAtOrder(10000);
        item1.setQuantity(2);
        item1.setDiscountPer(30);

        OrderItem item2 = new OrderItem();
        item2.setItemId(2);
        item2.setItemName("상품 B");
        item2.setPriceAtOrder(15000);
        item2.setQuantity(1);
        item2.setDiscountPer(10);

        List<OrderItem> itemList = List.of(item1, item2);

        // 주문 DTO 생성
        Order dto = new Order();
        dto.setName("홍길동");
        dto.setAddress("서울시 노원구");
        dto.setPayment("bank");
//        dto.setItems(itemsJson);
        dto.setItems(toItemJson(itemList)); // JSON 문자열로 변환해서 세팅


        // when: 주문 생성
        boolean result = orderService.createOrder(dto, token);

        // then: 생성된 주문이 잘 조회되는지 확인
        assertTrue(result);

        Map<String, Object> response = orderService.getOrdersWithItems(token);
        List<Map<String, Object>> orders = (List<Map<String, Object>>) response.get("orders");

        assertFalse(orders.isEmpty());
        Map<String, Object> orderMap = orders.get(0);

        Order saveOrder = (Order) orderMap.get("order");
        List<Map<String, Object>> orderItems = (List<Map<String, Object>>) orderMap.get("items");

        assertEquals("홍길동", saveOrder.getName());
        assertEquals(2, orderItems.size());
    }
}
