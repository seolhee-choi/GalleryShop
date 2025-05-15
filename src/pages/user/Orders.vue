<template>
  <div class="orders">
    <div class="container">
      <table class="table table-bordered">
        <thead>
          <tr>
            <th>번호</th>
            <th>주문자명</th>
            <th>주소</th>
            <th>결제 수단</th>
            <th>구입 항목</th>
            <th>리뷰 작성</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(o, idx) in state.orders" :key="idx">
            <td>{{ state.orders.length - idx }}</td>
            <td>{{ o.name }}</td>
            <td>{{ o.address }}</td>
            <td>{{ o.payment }}</td>
            <td>
              <div v-for="(i, idx2) in o.items" :key="idx2">
                {{ i.name }}
              </div>
            </td>
            <td>
              <button
                class="btn btn-outline-danger py-1 review-btn"
                @click="isOpen = true"
              >
                리뷰 작성
              </button>
              <ReviewModal
                :item="item"
                v-if="isOpen"
                @close-review="isOpen = false"
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import axios from "@/axios.js";
import ReviewModal from "@/pages/user/ReviewModal.vue";

const state = reactive({
  orders: [],
});
const load = () => {
  axios.get("/api/orders").then((data) => {
    state.orders = [];

    for (let d of data) {
      if (d.items) {
        d.items = JSON.parse(d.items);
      }
      state.orders.push(d);
    }
  });
};

load();
</script>

<style scoped>
table {
  margin-top: 30px;
}

.table > tbody {
  border-top: 1px solid #eee;
}
</style>
