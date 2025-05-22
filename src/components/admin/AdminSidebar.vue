<template>
  <aside class="sidebar">
    <ul>
      <li class="sidebar-item user-page-link">
        <router-link to="/">사용자 페이지</router-link>
      </li>
      <li>
        <router-link to="/admin">대시보드</router-link>
      </li>
      <li>
        <router-link to="/admin/member">회원 관리</router-link>
      </li>
      <li>
        <router-link to="/admin/order">주문 관리</router-link>
      </li>
      <li>
        <router-link to="/admin/product">상품 관리</router-link>
      </li>
      <li>
        <router-link to="/admin/review">리뷰 관리</router-link>
      </li>
      <li>
        <router-link to="/admin/statistics">통계</router-link>
      </li>
    </ul>
  </aside>
</template>

<script setup></script>

<style scoped></style>
