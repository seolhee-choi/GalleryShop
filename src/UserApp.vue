<template>
  <div class="app-container">
    <Header />
    <main class="content">
      <RouterView />
    </main>
    <Alert />
    <Footer />
  </div>
</template>

<script setup>
import "@/assets/user.css";
import "@/assets/pagination.css";
import Header from "@/components/user/Header.vue";
import Footer from "@/components/user/Footer.vue";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import { onMounted } from "vue";

const accountStore = useAccountStore();

onMounted(() => {
  accountStore.check();
});
</script>

<style>
/* 만약 Header, Footer 컴포넌트가 내부에 별도 태그가 없다면, 위 header, footer 선택자는 직접 컴포넌트 이름으로 바꿔도 됩니다. */
</style>
