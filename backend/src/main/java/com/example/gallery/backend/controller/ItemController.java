package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.mapper.ItemMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<Item> getItems() {
        List<Item> items = itemMapper.findAll();

        return items;
    }

    @PostMapping("/api/items/upload")
    public ResponseEntity<String> uploadJson(@RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Item> items = objectMapper.readValue(file.getInputStream(),
                    new TypeReference<List<Item>>() {});

            itemMapper.saveAll(items);

            return ResponseEntity.ok("아이템 저장 완료");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
        }
    }
}
