package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class ItemController {

    @Autowired
    ItemMapper itemMapper;

    @GetMapping("/api/items")
    public ResponseEntity<ApiResponse<List<Item>>> getItems() {
        List<Item> items = itemMapper.findAll();

        return ResponseFactory.success(items);
    }

    @PostMapping("/api/items/upload")
    public ResponseEntity<ApiResponse<Void>> uploadJson(@RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Item> items = objectMapper.readValue(file.getInputStream(),
                    new TypeReference<List<Item>>() {});

            itemMapper.saveAll(items);

            return ResponseFactory.success(null);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(ErrorCode.ERROR_007);
        }
    }

    // 관리자 - 상품 등록
    @PostMapping("/api/admin/items/upload")
    public ResponseEntity<ApiResponse<Void>> uploadItem(@RequestBody List<Item> items) {
        if (items == null) {
            throw new BizException(ErrorCode.ERROR_004);
        }

        itemMapper.saveAll(items);
        return ResponseFactory.success(null);
    }

    // 관리자 - 상품 아이디로 상품 조회
    @GetMapping("/api/admin/items/{itemId}")
    public ResponseEntity<ApiResponse<List<Item>>> getItem(@PathVariable("itemId") int itemId) {
        List<Item> items = itemMapper.findItemById(itemId);

        return ResponseFactory.success(items);
    }

    // 관리자 - 아이템 업데이트
    @PostMapping("/api/reviews/update")
    public ResponseEntity<ApiResponse<Void>> updateItem(@RequestBody List<Item> items) {
        for (Item i : items) {
            itemMapper.updateItem(i);
        }

        return ResponseFactory.success(null);
    }
}
