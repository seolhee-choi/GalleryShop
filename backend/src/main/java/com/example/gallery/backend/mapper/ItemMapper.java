package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    // 여러 개의 itemId로 조회 (이미 존재하는 메서드)
    List<Item> findByIdIn(@Param("ids") List<Integer> ids, @Param("memberId") int memberId);

    // 모든 아이템 조회
    List<Item> findAll();

    // 여러 아이템 저장
    void saveAll(@Param("items") List<Item> items);
}
