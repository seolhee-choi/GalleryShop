package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    Member findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    Member findByEmail(@Param("email") String email);

    boolean existsMemberByEmail(@Param("email") String email);

    void insert(Member member);
}
