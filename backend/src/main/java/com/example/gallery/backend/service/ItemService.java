package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Cart;
import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.dto.Search;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.CartMapper;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.response.PageResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class ItemService {

    @Autowired
    ItemMapper itemMapper;


//    public PageResponse<List<Item>> getItems(Search search) {
    public PageResponse<Item> getItems(Search search) {
        int totalCount = itemMapper.countItem(search);
        int totalPage = (int)Math.ceil((double) totalCount / search.getRecordSize());

        List<Item> items = itemMapper.findAll(search);

//        return itemMapper.findAll(status, search);
        return new PageResponse<>(items, totalCount, search.getPage(), totalPage);

    }

    public boolean uploadJson(MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Item> items = objectMapper.readValue(file.getInputStream(),
                    new TypeReference<List<Item>>() {});
            itemMapper.saveAll(items);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(ErrorCode.ERROR_007);
        }
    }

    public boolean uploadItem(List<Item> items) {
        if (items == null) {
            throw new BizException(ErrorCode.ERROR_004);
        }
        itemMapper.saveAll(items);
        return true;
    }

    public List<Item> getItemById(int itemId) {
        return itemMapper.findItemById(itemId);
    }

    public boolean updateItems(List<Item> items) {
        for (Item i : items) {
            itemMapper.updateItem(i);
        }
        return true;
    }
}
