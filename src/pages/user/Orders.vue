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
            <th>구매일</th>
            <th>리뷰 작성</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(o, idx) in state.orders" :key="idx">
            <td>{{ state.orders.length - idx }}</td>
            <td>{{ o.orderInfo.name }}</td>
            <td>{{ o.orderInfo.address }}</td>
            <td>{{ o.orderInfo.payment }}</td>
            <td>{{ o.item.name }}</td>
            <td>{{ formatDate(o.item.createdAt) }}</td>
            <td>
              <button
                class="btn btn-outline-danger py-1 review-btn"
                @click="openReviewModal(o.item.id)"
              >
                리뷰 작성
              </button>
              <ReviewModal
                v-if="isOpen && selectedItemId === o.item.id"
                :itemId="o.item.id"
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
import { ref, reactive } from "vue";
import axios from "@/axios.js";
import ReviewModal from "@/pages/user/ReviewModal.vue";
import { formatDate } from "@/utils/date.js";

const state = reactive({
  orders: [],
});

const isOpen = ref(false);
const selectedItemId = ref(null);

const openReviewModal = (itemId) => {
  console.log(itemId);
  selectedItemId.value = itemId;
  isOpen.value = true;
};

const load = () => {
  axios.get("/api/orders").then((res) => {
    const orders = res.orders;
    const itemsList = [];

    for (let o of orders) {
      for (let item of o.items) {
        itemsList.push({
          orderInfo: o.order,
          item: item,
        });
      }
    }
    state.orders = itemsList;
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
