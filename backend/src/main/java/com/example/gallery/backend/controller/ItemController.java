package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.example.gallery.backend.service.ItemService;
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
    ItemService itemService;

    @GetMapping("/api/items")
    public ResponseEntity<ApiResponse<List<Item>>> getItems() {
        return ResponseFactory.success(itemService.getItems());
    }

    @PostMapping("/api/items/upload")
    public ResponseEntity<ApiResponse<Boolean>> uploadJson(@RequestParam("file") MultipartFile file) {
        return ResponseFactory.success(itemService.uploadJson(file));
    }

    // 관리자 - 상품 등록
    @PostMapping("/api/admin/items/upload")
    public ResponseEntity<ApiResponse<Boolean>> uploadItem(@RequestBody List<Item> items) {
        return ResponseFactory.success(itemService.uploadItem(items));
    }

    // 관리자 - 상품 아이디로 상품 조회
    @GetMapping("/api/admin/items/{itemId}")
    public ResponseEntity<ApiResponse<List<Item>>> getItem(@PathVariable("itemId") int itemId) {
        return ResponseFactory.success(itemService.getItemById(itemId));
    }

    // 관리자 - 아이템 업데이트
    @PostMapping("/api/reviews/update")
    public ResponseEntity<ApiResponse<Boolean>> updateItem(@RequestBody List<Item> items) {
        return ResponseFactory.success(itemService.updateItems(items));
    }
}
