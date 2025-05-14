<template>
  <aside class="sidebar">
    <ul>
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

<script setup>
import { reactive } from "vue";

const menu = reactive({
  items: [
    { title: "대시보드", to: "/admin/", link: "#house-fill" },
    { title: "회원관리", to: "/admin/member", link: "#people" },
    { title: "주문관리", to: "/admin/order", link: "#file-earmark" },
    { title: "상품관리", to: "/admin/product", link: "#cart" },
    { title: "통계", to: "/admin/statistics", link: "#graph-up" },
    // "이번달 매출",
    // "지난달 매출",
    // "환경설정",
    // "로그아웃",
  ],
});
</script>

<style scoped>
.bi {
  display: inline-block;
  width: 1rem;
  height: 1rem;
}

/*
 * Sidebar
 */

@media (min-width: 768px) {
  .sidebar .offcanvas-lg {
    position: -webkit-sticky;
    position: sticky;
    top: 48px;
  }
  .navbar-search {
    display: block;
  }
}

.sidebar .nav-link {
  font-size: 0.875rem;
  font-weight: 500;
}

.sidebar .nav-link.active {
  color: #2470dc;
}

.sidebar-heading {
  font-size: 0.75rem;
}
</style>
