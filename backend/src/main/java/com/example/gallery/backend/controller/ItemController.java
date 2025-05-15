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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    ItemMapper itemMapper;

    @GetMapping("/api/items")
    public ResponseEntity<ApiResponse<List<Item>>> getItems() {
        List<Item> items = itemMapper.findAll();

//        return items;
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
}
