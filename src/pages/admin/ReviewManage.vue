<template>
  <main class="main-content">
    <div class="page-header">
      <h1 class="section-title">리뷰 조회</h1>
      <button class="btn btn-primary save-btn" @click="saveChanges">
        저장
      </button>
    </div>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th>리뷰ID</th>
            <th>상품ID</th>
            <th>리뷰 내용</th>
            <th>별점</th>
            <th>작성자</th>
            <th>리뷰등록일자</th>
            <th>활성화</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(r, idx) in state.reviews" :key="idx">
            <td class="number">{{ r.reviewId }}</td>
            <td class="number">{{ r.itemId }}</td>
            <td>{{ r.content }}</td>
            <td>
              <span v-for="i in r.rating" :key="i">💛</span>
            </td>
            <td>{{ r.email }}</td>
            <td>{{ formatDate(r.updatedAt) }}</td>
            <td class="select-option">
              <select v-model="r.status">
                <option value="0">활성화</option>
                <option value="1">비활성화</option>
              </select>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>
<script setup>
import axios from "@/axios.js";
import { reactive } from "vue";
import { formatDate } from "@/utils/date.js";
import { useAlert, confirmAndSaveChanges } from "@/utils/alert.js";

const { vAlert, vSuccess } = useAlert();
const state = reactive({
  reviews: [],
});

const loadReview = () => {
  axios.get("/api/admin/reviews").then((res) => {
    state.reviews = [];

    for (let d of res) {
      state.reviews.push(d);
    }
  });
};

const saveChanges = async () => {
  const isConfirmed = await confirmAndSaveChanges();
  if (isConfirmed) {
    try {
      const response = await axios.post("/api/admin/reviews", state.reviews);
      console.error(response);
      vSuccess("리뷰 상태가 성공적으로 변경되었습니다.");
    } catch (err) {
      console.error(err);
      vAlert("리뷰 상태변경에 실패했습니다.");
    }
  }
};

loadReview();
</script>
<style scoped></style>
