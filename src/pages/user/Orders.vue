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
            <td data-label="번호">{{ state.orders.length - idx }}</td>
            <td data-label="주문자명">{{ o.orderInfo.name }}</td>
            <td data-label="주소">{{ o.orderInfo.address }}</td>
            <td data-label="결제 수단">{{ o.orderInfo.payment }}</td>
            <td data-label="구입 항목">{{ o.item.name }}</td>
            <td data-label="구매일">{{ formatDate(o.item.createdAt) }}</td>
            <td data-label="리뷰 작성">
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
.orders {
  padding: 40px 0;
}

.orders table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 10px;
  box-shadow: 0 4px 12px rgba(238, 66, 45, 0.1);
  background-color: #fff8f7;
  border-radius: 12px;
  overflow: hidden;
}

.orders thead tr {
  background-color: #ee422d;
  color: #fff;
  font-weight: 600;
  text-align: center;
}

.orders thead th {
  padding: 14px 12px;
  border: none;
  user-select: none;
}

.orders tbody tr {
  background-color: #fff0f6;
  transition: box-shadow 0.3s ease;
  cursor: default;
}

.orders tbody tr:hover {
  box-shadow: 0 8px 20px rgba(238, 66, 45, 0.15);
}

.orders tbody td {
  padding: 12px;
  vertical-align: middle;
  text-align: center;
  color: #5a2a27;
  border: none;
  font-size: 15px;
}

.orders tbody td:first-child {
  font-weight: 700;
  color: #c23523;
}

.review-btn {
  font-size: 14px;
  padding: 6px 12px;
  border-color: #ee422d;
  color: #ee422d;
  transition:
    background-color 0.2s ease,
    color 0.2s ease;
}

.review-btn:hover {
  background-color: #ee422d;
  color: white;
}

@media (max-width: 768px) {
  .orders table,
  .orders thead,
  .orders tbody,
  .orders th,
  .orders td,
  .orders tr {
    display: block;
  }

  .orders thead tr {
    position: absolute;
    top: -9999px;
    left: -9999px;
  }

  .orders tbody tr {
    margin-bottom: 20px;
    background-color: #fff0f6;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(238, 66, 45, 0.1);
    padding: 15px;
  }

  .orders tbody td {
    text-align: right;
    padding-left: 50%;
    position: relative;
    border: none;
    font-size: 14px;
  }

  .orders tbody td::before {
    position: absolute;
    top: 12px;
    left: 12px;
    width: 45%;
    white-space: nowrap;
    font-weight: 600;
    color: #c23523;
    content: attr(data-label);
    text-align: left;
  }
}
</style>
