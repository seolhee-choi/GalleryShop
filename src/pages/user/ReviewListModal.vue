<template>
  <div class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2>리뷰 목록</h2>
        <button
          class="btn btn-close"
          type="button"
          @click="$emit('close-review')"
        ></button>
      </div>
      <table class="table">
        <thead>
          <tr>
            <th>내용</th>
            <th>별점</th>
            <th>작성자</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="review in state.reviews"
            :key="review.reviewId"
            class="review-row"
          >
            <td
              class="review-content"
              :class="{ expanded: isExpanded(review.reviewId) }"
              @click.stop="toggleExpanded(review.reviewId)"
            >
              {{ review.content }}
            </td>
            <td>
              <span v-for="i in review.rating" :key="i">💛</span>
            </td>
            <td>{{ review.email }}</td>
            <td>{{ review.createdAt.substr(0, 10) }}</td>
          </tr>
          <tr v-if="state.reviews.length === 0">
            <td colspan="5" style="text-align: center">
              등록된 리뷰가 없습니다.
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script setup>
import axios from "@/axios.js";
import { ref, reactive } from "vue";
import ReviewModal from "@/pages/user/ReviewModal.vue";

const isOpen = ref(false);
const props = defineProps({
  item: Object,
});
const state = reactive({
  reviews: [],
});
const expandedRows = ref(new Set());

//리뷰내용 클릭시 리뷰아이디별로 내용이 확장되는 함수
const toggleExpanded = (id) => {
  if (expandedRows.value.has(id)) {
    expandedRows.value.delete(id);
  } else {
    expandedRows.value.add(id);
  }
};

const isExpanded = (id) => expandedRows.value.has(id);
const loadReview = () => {
  const itemId = props.item.id;

  axios
    .get(`/api/reviews/${itemId}`)
    .then(({ data }) => {
      state.reviews = data;
      console.log(state.reviews);
    })
    .catch((err) => alert(err));
};

const handleCloseReview = () => {
  isOpen.value = fasle; //리뷰작성 모달 닫기
  loadReview();
};
// const test403 = () => {
//   axios
//     .get("http://localhost:8080/test-error")
//     .then((res) => console.log("403에러테스트"))
//     .catch((err) => console.log(err));
// };

loadReview();

// test403();
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 어두운 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem; /* 제목과 본문 사이 여백 */
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  width: 63%;
  height: 70%;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
}

.review-content {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-word;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.review-content.expanded {
  white-space: normal;
}
</style>
