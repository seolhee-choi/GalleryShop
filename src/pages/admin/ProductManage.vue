<template>
  <main class="main-content">
    <h1 class="h2">
      상품 조회
      <button class="btn btn-primary save-btn" @click="saveChanges">
        저장
      </button>
    </h1>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th>상품ID</th>
            <th>상품명</th>
            <th>상품이미지</th>
            <th>상품원가</th>
            <th>할인율</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(i, idx) in state.items" :key="idx">
            <td>{{ i.id }}</td>
            <td>{{ i.name }}</td>
            <td>{{ i.imgPath }}</td>
            <td>{{ i.price }}</td>
            <td>{{ i.discountPer }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import axios from "@/axios.js";
import { reactive } from "vue";

const state = reactive({
  items: [],
});

const loadItems = () => {
  axios.get("/api/items").then((data) => {
    for (let d of data) {
      state.items.push(d);
    }
  });
};

loadItems();
</script>

<style scoped></style>
