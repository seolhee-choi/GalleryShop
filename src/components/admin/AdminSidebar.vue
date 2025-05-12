<template>
  <div
    class="sidebar border border-right col-md-3 col-lg-2 p-0 bg-body-tertiary"
  >
    <div
      class="offcanvas-md offcanvas-end bg-body-tertiary"
      tabindex="-1"
      id="sidebarMenu"
      aria-labelledby="sidebarMenuLabel"
    >
      <div class="offcanvas-header">
        <h5 class="offcanvas-title" id="sidebarMenuLabel">Company name</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="offcanvas"
          data-bs-target="#sidebarMenu"
          aria-label="Close"
        ></button>
      </div>
      <div
        class="offcanvas-body d-md-flex flex-column p-0 pt-lg-3 overflow-y-auto"
      >
        <ul class="nav flex-column">
          <li class="nav-item" v-for="index in menu.items" :key="index">
            <router-link
              :to="index.to"
              class="nav-link d-flex align-items-center gap-2 active"
            >
              <svg class="bi" aria-hidden="true">
                <use :xlink:href="index.link"></use>
              </svg>
              {{ index.title }}
            </router-link>
          </li>
        </ul>

        <h6
          class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-body-secondary text-uppercase"
        >
          <span>Saved reports</span>
          <a class="link-secondary" href="#" aria-label="Add a new report">
            <svg class="bi" aria-hidden="true">
              <use xlink:href="#plus-circle"></use>
            </svg>
          </a>
        </h6>
        <ul class="nav flex-column mb-auto">
          <li class="nav-item">
            <a class="nav-link d-flex align-items-center gap-2" href="#">
              <svg class="bi" aria-hidden="true">
                <use xlink:href="#file-earmark-text"></use>
              </svg>
              이번 달 매출
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link d-flex align-items-center gap-2" href="#">
              <svg class="bi" aria-hidden="true">
                <use xlink:href="#file-earmark-text"></use>
              </svg>
              지난 달 매출
            </a>
          </li>
        </ul>

        <hr class="my-3" />

        <ul class="nav flex-column mb-auto">
          <li class="nav-item">
            <a class="nav-link d-flex align-items-center gap-2" href="#">
              <svg class="bi" aria-hidden="true">
                <use xlink:href="#gear-wide-connected"></use>
              </svg>
              환경설정
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link d-flex align-items-center gap-2" href="#">
              <svg class="bi" aria-hidden="true">
                <use xlink:href="#door-closed"></use>
              </svg>
              로그아웃
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const menu = reactive({
  items: [
    { title: "대시보드", to: "/admin/dashboard", link: "#house-fill" },
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

console.log(menu.items);
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
