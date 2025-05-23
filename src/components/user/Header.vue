<template>
  <!--  <header data-bs-theme="dark">-->
  <header>
    <div
      class="collapse"
      id="navbarHeader"
      style="background-color: #fff0f0; color: #ee422d"
    >
      <div class="container">
        <div class="row">
          <div class="col-sm-4 py-4">
            <h4 style="color: #ee422d">사이트맵</h4>
            <ul class="list-unstyled">
              <li v-if="accountStore.account.role === 'ADMIN'">
                <router-link to="/admin" style="color: #ee422d"
                  >관리자 페이지</router-link
                >
              </li>
              <li>
                <router-link to="/" style="color: #ee422d">홈</router-link>
              </li>
              <li v-if="accountStore.account.id">
                <router-link to="/orders" style="color: #ee422d"
                  >주문 내역</router-link
                >
              </li>
              <li v-if="accountStore.account.id">
                <router-link to="/mypage" style="color: #ee422d"
                  >비밀번호 변경</router-link
                >
              </li>
              <li>
                <router-link
                  to="/login"
                  style="color: #ee422d"
                  v-if="!accountStore.account.id"
                  >로그인</router-link
                >
                <a href="#" style="color: #ee422d" @click="logout()" v-else
                  >로그아웃</a
                >
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div class="navbar navbar-light" style="background-color: #ffffff">
      <div class="container">
        <router-link
          to="/"
          class="navbar-brand d-flex align-items-center"
          style="color: #ee422d"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            fill="none"
            stroke="#ee422d"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            aria-hidden="true"
            class="me-2"
            viewBox="0 0 24 24"
          >
            <path
              d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"
            />
            <circle cx="12" cy="13" r="4" />
          </svg>
          <strong style="color: #ee422d">Gallery Shop</strong>
        </router-link>

        <router-link
          to="/cart"
          class="cart btn"
          v-if="accountStore.account.id"
          style="color: #ee422d"
        >
          <i class="fa fa-shopping-cart" aria-hidden="true"></i>
        </router-link>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarHeader"
          aria-controls="navbarHeader"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span
            class="navbar-toggler-icon"
            style="
              filter: brightness(0) saturate(100%) invert(29%) sepia(83%)
                saturate(628%) hue-rotate(341deg) brightness(96%) contrast(97%);
            "
          ></span>
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { onMounted } from "vue";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import { useCartStore } from "@/scripts/useCartStore.js";
import router from "@/scripts/router.js";
import axios from "@/axios.js";

const accountStore = useAccountStore();
const cartStore = useCartStore();
let bsCollapse = null;
const logout = async () => {
  try {
    // await axios.post("/api/account/logout");
    // accountStore.setAccount({ id: 0, email: "", role: "" });
    await accountStore.logout();
    cartStore.$reset();
    router.push("/");
  } catch (err) {
    console.error("Logout failed", err);
  }
};

onMounted(() => {
  // 페이지 이동시 navbar 닫힘
  const collapseElement = document.getElementById("navbarHeader");
  // 이미 초기화된 Collapse 인스턴스가 있으면 가져오고, 없으면 새로 생성
  bsCollapse = bootstrap.Collapse.getOrCreateInstance(collapseElement, {
    toggle: false,
  });

  router.afterEach(() => {
    if (bsCollapse) {
      bsCollapse.hide(); // 페이지 이동 시 네비바 자동 닫기
    }
  });
});
</script>

<style>
header {
  position: relative; /* 위치 기준 만들기 */
  z-index: 1100; /* Bootstrap 네비바나 모달보다 높게 */
}
header ul li a {
  cursor: pointer;
}
header .navbar .cart {
  margin-left: auto;
  color: white;
}

#navbarHeader {
  position: relative; /* or fixed, absolute 필요에 따라 */
  z-index: 1050; /* Bootstrap 모달, 네비바가 보통 1000~1050 사이임 */
}
</style>
