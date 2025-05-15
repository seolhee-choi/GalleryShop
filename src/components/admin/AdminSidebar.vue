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

<script setup></script>

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
