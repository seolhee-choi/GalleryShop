package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> findByMemberId(@Param("memberId") int memberId);
    Cart findByMemberIdAndItemId(@Param("memberId") int memberId, @Param("itemId") int itemId);

    void deleteByMemberId(@Param("memberId") int memberId);

    void deleteByMemberIdAndItemId(@Param("memberId") int memberId, @Param("itemId") int itemId);
    void insertCart(Cart cart);
}
