<template>
  <div class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2>리뷰 목록</h2>
        <button
          class="btn btn-close"
          type="button"
          @click="$emit('close-review')"
        >
          ✖
        </button>
      </div>

      <div v-if="state.reviews.length > 0" class="review-list">
        <div
          v-for="review in state.reviews"
          :key="review.reviewId"
          class="review-card"
          @click="toggleExpanded(review.reviewId)"
        >
          <div class="review-header">
            <div class="review-rating">
              <span v-for="i in 5" :key="i">
                {{ i <= review.rating ? "💛" : "🤍" }}
              </span>
            </div>
            <div class="review-meta">
              <span class="review-email">{{ review.email }}</span>
              <span class="review-date">{{
                review.createdAt.substr(0, 10)
              }}</span>
            </div>
          </div>
          <div
            class="review-content"
            :class="{ expanded: isExpanded(review.reviewId) }"
          >
            {{ review.content }}
          </div>
        </div>
      </div>

      <div v-else class="empty-review">등록된 리뷰가 없습니다.</div>
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
    .then((data) => {
      state.reviews = data;
    })
    .catch((err) => console.log(err));
};

const handleCloseReview = () => {
  isOpen.value = fasle; //리뷰작성 모달 닫기
  loadReview();
};

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
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 12px;
  width: 60%;
  max-height: 80%;
  overflow-y: auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.review-card {
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 1rem;
  background: #fdfdfd;
  transition: box-shadow 0.2s ease;
  cursor: pointer;
}

.review-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.review-rating {
  font-size: 1.2rem;
}

.review-meta {
  font-size: 0.85rem;
  color: #666;
  display: flex;
  gap: 0.75rem;
}

.review-content {
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: all 0.2s ease-in-out;
}

.review-content.expanded {
  white-space: normal;
}

.empty-review {
  text-align: center;
  color: #999;
  font-size: 1rem;
  padding: 2rem 0;
}
</style>
