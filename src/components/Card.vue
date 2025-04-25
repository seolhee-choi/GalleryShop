<template>
  <div class="card shadow-sm">
    <span
      class="img"
      :style="{ backgroundImage: `url(${item.imgPath}` }"
    ></span>
    <div class="card-body">
      <p class="card-text">
        <span class="item-name">{{ item.name }}</span>
        <span class="discount badge bg-danger"> {{ item.discountPer }}% </span>
      </p>

      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-primary" @click="addToCart(item.id)">
          <i class="fa fa-shopping-cart" aria-hidden="true"></i>
        </button>
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
</template>

<script setup>
import lib from "@/scripts/lib.js";
import axios from "axios";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import { useAlert } from "@/utils/alert.js";

const { vAlert } = useAlert();

const props = defineProps({
  item: Object,
});

const accountStore = useAccountStore();
const addToCart = (itemId) => {
  if (!accountStore.account.id) {
    vAlert("로그인 후 가능합니다");
    return;
  }
  axios.post(`/api/cart/items/${itemId}`).then(() => {
    console.log("success");
  });
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

.card-text {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.item-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: inline-block;
  max-width: calc(100% - 50px); /* discount badge 영역만큼 빼줌 */
}
</style>
