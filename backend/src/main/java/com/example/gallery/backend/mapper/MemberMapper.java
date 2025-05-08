package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MemberMapper {
    Member findByEmail(@Param("email") String email);

    boolean existsMemberByEmail(@Param("email") String email);

    void insert(Member member);

    void changePassword(Member member);
}
