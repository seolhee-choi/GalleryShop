package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.mapper.OrderMapper;
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
        String itemsJson =
                "[{\"itemId\":1,\"itemName\":\"상품 A\",\"priceAtOrder\":10000,\"quantity\":2,\"discountPer\":0}," +
                "{\"itemId\":2,\"itemName\":\"상품 B\",\"priceAtOrder\":15000,\"quantity\":1,\"discountPer\":10}]";


        // 주문 DTO 생성
        Order dto = new Order();
        dto.setName("홍길동");
        dto.setAddress("서울시 노원구");
        dto.setPayment("bank");
        dto.setItems(itemsJson);

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
