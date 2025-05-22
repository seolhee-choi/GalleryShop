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

<style></style>
