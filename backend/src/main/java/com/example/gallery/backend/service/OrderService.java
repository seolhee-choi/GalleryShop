package com.example.gallery.backend.service;

import com.example.gallery.backend.dto.OrderItem;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final ObjectMapper objectMapper;

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
}
