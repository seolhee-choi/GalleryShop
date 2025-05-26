package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    Member findByEmail(@Param("email") String email);

    boolean existsMemberByEmail(@Param("email") String email);

    void insert(Member member);

    void changePassword(Member member);

    String findEmailById(@Param("id") int id);

    List<Member> findAllMember(@Param("search")Search search);

    int countMember(Search params);
//    void updateMembers(@Param("members") List<Member> members);
    void updateMember(Member member);
}
