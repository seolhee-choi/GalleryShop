<template>
  <AdminHeader />
  <AdminSidebar />
  <RouterView />
  <Alert />
  <AdminFooter />
</template>

<script setup>
import "@/assets/admin.css";
import AdminHeader from "@/components/admin/AdminHeader.vue";
import AdminSidebar from "@/components/admin/AdminSidebar.vue";
import AdminFooter from "@/components/admin/AdminFooter.vue";
import axios from "@/axios.js";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import { onMounted, watch } from "vue";

const accountStore = useAccountStore();

onMounted(() => {
  accountStore.check();
});
</script>

<style>
body,
html {
  margin: 0;
  padding: 0;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f5f6fa;
  height: 100%;
}

.header {
  height: 60px;
  background-color: #2f3542;
  color: white;
  display: flex;
  align-items: center;
  padding: 0 20px;
  font-size: 18px;
  font-weight: bold;
  justify-content: space-between;
}

.header .title a {
  text-decoration-line: none;
  color: white;
}

/* Sidebar 고정 */
.sidebar {
  width: 220px;
  background-color: #3742fa;
  color: white;
  position: fixed;
  top: 60px; /* 헤더 높이 */
  bottom: 40px; /* 푸터 높이 */
  left: 0;
  padding: 20px 10px;
  box-sizing: border-box;
  height: calc(100vh - 60px - 40px); /* 전체 높이에서 헤더와 푸터 제외 */
  overflow-y: auto; /* 스크롤이 필요할 경우 사이드바 내부에서만 스크롤 */
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar li {
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.sidebar li a {
  color: white;
  text-decoration-line: none;
}

.sidebar li:hover {
  background-color: #1e90ff;
}

/* Main 컨텐츠 */
.main-content {
  margin-left: 220px; /* 사이드바 만큼 여백 */
  margin-top: 60px; /* 헤더 만큼 여백 */
  padding: 20px;
  min-height: calc(100vh - 100px); /* 헤더와 푸터 제외한 공간 */
  overflow-y: auto; /* 스크롤 가능 */
  padding-bottom: 40px; /* 푸터와 겹치지 않도록 여백 추가 */
}

.main-content h1 {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.save-btn {
  padding: 8px 15px;
  font-size: 16px;
  margin-left: auto;
}

/* Footer 고정 */
.footer {
  height: 40px;
  background-color: #2f3542;
  color: white;
  text-align: center;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
}
</style>
