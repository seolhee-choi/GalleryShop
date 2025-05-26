package com.example.gallery.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> items;      // 조회된 리스트
    private int totalCount;     // 전체 개수
    private int currentPage;    // 현재 페이지
    private int totalPage;      // 전체 페이지 수
}
