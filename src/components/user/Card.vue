<template>
  <div class="card shadow-sm">
    <span
      class="img"
      :style="{ backgroundImage: `url(${item.imgPath}` }"
    ></span>
    <div class="card-body">
      <p class="card-text">
        <span class="item-name">{{ item.name }}</span>
        <button class="btn btn-link py-1 review-btn" @click="isOpen = true">
          리뷰
        </button>
        <ReviewListModal
          :item="item"
          v-if="isOpen"
          @close-review="isOpen = false"
        />
      </p>

      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-primary" @click="addToCart(item.id)">
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
</template>

<script setup>
import { ref } from "vue";
import { useAlert } from "@/utils/alert.js";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import lib from "@/scripts/lib.js";
import axios from "@/axios.js";
import ReviewListModal from "@/pages/user/ReviewListModal.vue";

const { vAlert } = useAlert();

const props = defineProps({
  item: Object,
});

const accountStore = useAccountStore();

const isOpen = ref(false);
const addToCart = (itemId) => {
  if (!accountStore.account.id) {
    vAlert("로그인 후 가능합니다");
    return;
  }
  axios.post(`/api/cart/items/${itemId}`).then(() => {});
};
</script>

<style>
.card .img {
  display: inline-block;
  width: 100%;
  height: 250px;
  background-size: cover;
  background-position: center;
}

.card .card-body .price {
  text-decoration: line-through;
}

.price-box {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  line-height: 1.2;
}

.card-text {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
}

.item-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex-grow: 1;
  margin-right: 0.5rem;
}

.review-btn {
  white-space: nowrap;
  flex-shrink: 0;
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
}
</style>
