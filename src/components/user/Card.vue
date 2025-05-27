<template>
  <div class="card-container" :class="statusClass">
    <!--    <div class="card shadow-sm">-->
    <div :class="['card shadow-sm', { 'sold-out': item.status === 1 }]">
      <span
        class="img"
        :style="{ backgroundImage: `url(${item.imgPath}` }"
      ></span>
      <span v-if="item.status === 1" class="soldout-label">품절</span>

      <div class="card-body">
        <p class="card-text">
          <span class="item-name" :title="item.name">{{ item.name }}</span>
          <button
            class="btn btn-link py-1 review-btn"
            @click="isOpen = true"
            :disabled="item.status === 1"
          >
            리뷰
          </button>
          <ReviewListModal
            :item="item"
            v-if="isOpen"
            @close-review="isOpen = false"
          />
        </p>

        <div class="d-flex justify-content-between align-items-center">
          <button
            class="btn btn-primary"
            @click="addToCart(item.id)"
            :disabled="item.status === 1"
          >
            <i class="fa fa-shopping-cart" aria-hidden="true"></i>
          </button>
          <div class="price-box">
            <span class="discount badge bg-danger">
              {{ item.discountPer }}%
            </span>
            <small class="price text-muted">
              {{ lib.getNumberFormatted(item.price) }}원
            </small>
            <small class="real text-danger">
              {{
                lib.getNumberFormatted(
                  item.price - (item.price * item.discountPer) / 100,
                )
              }}원
            </small>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { useAlert } from "@/utils/alert.js";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import lib from "@/scripts/lib.js";
import axios from "@/axios.js";
import ReviewListModal from "@/pages/user/ReviewListModal.vue";

const { vAlert, vSuccess } = useAlert();

const props = defineProps({
  item: Object,
});

const statusClass = computed(() => (props.item.status === 1 ? "sold-out" : ""));
const accountStore = useAccountStore();

const isOpen = ref(false);
const addToCart = (itemId) => {
  if (!accountStore.account.id) {
    vAlert("로그인 후 가능합니다");
    return;
  }
  axios.post(`/api/cart/items/${itemId}`).then(() => {
    vSuccess("장바구니에 상품이 담겼습니다.");
  });
};
</script>

<style>
.card-container {
  display: grid;
  grid-template-columns: repeat(
    auto-fill,
    minmax(300px, 1fr)
  ); /* 카드 크기 통일 */
  gap: 1rem;
  height: 100%;
}

.card {
  width: 100%; /* grid에서 꽉 채우기 */
  height: 100%; /* 내부 내용 높이에 맞추기 */
  border: 1.5px solid #f5c6cb; /* 연한 핑크 테두리 */
  border-radius: 8px;
  background-color: #fff0f6; /* 아주 연한 분홍빛 배경 */
  transition: box-shadow 0.3s ease;
}

.card:hover {
  box-shadow: 0 8px 16px rgba(238, 66, 45, 0.25); /* 포인트 진한 핑크 그림자 */
}

.card .img {
  display: inline-block;
  height: 200px;
  background-size: cover;
  background-position: center;
  border-bottom: 1px solid #f5c6cb;
  border-radius: 8px 8px 0 0;
  position: relative;
}

.card .card-body {
  color: #5a2a27; /* 차분한 살구빛 텍스트 */
  flex-grow: 1; /* 남은 공간을 차지하게 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card.sold-out {
  opacity: 0.5;
  pointer-events: none; /* 클릭도 막기 */
  user-select: none; /* 텍스트 선택도 안되게 */
}

.card.sold-out:hover {
  box-shadow: none;
}

.soldout-label {
  position: absolute;
  top: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.7); /* 반투명 검정 배경 */
  color: #fff;
  padding: 4px 8px;
  font-size: 0.875rem;
  border-radius: 4px;
  font-weight: bold;
  z-index: 1;
}

.card.sold-out .btn {
  cursor: not-allowed;
}

.card.sold-out .review-btn {
  color: #999;
  text-decoration: none;
  pointer-events: none;
}

.card.sold-out .real,
.card.sold-out .price,
.card.sold-out .discount {
  color: #aaa !important;
}

.price-box .discount {
  background-color: #ee422d; /* 포인트 진한 핑크 */
  font-weight: 600;
}

.price-box .price {
  text-decoration: line-through;
  color: #b0898f; /* 부드러운 회색빛 핑크 */
}

.price-box .real {
  color: #9b2c2c; /* 진한 핑크 레드 */
  font-weight: 700;
}

.review-btn {
  color: #ee422d;
  font-weight: 600;
  font-size: 0.875rem;
}

.review-btn:hover {
  text-decoration: underline;
}
</style>
