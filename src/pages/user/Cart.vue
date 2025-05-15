<template>
  <div class="cart">
    <div class="container">
      <ul>
        <li v-for="(i, idx) in cartStore.items" :key="idx">
          <img :src="i.imgPath" />
          <span class="name">{{ i.name }}</span>
          <span class="price"
            >{{
              lib.getNumberFormatted(i.price - (i.price * i.discountPer) / 100)
            }}원</span
          >
          <div>
            <i
              class="fa fa-arrow-down"
              :class="{ disabled: i.quantity <= 1 }"
              @click="cartStore.updateQuantity(i.id, i.quantity - 1)"
            ></i>
            <input type="number" class="number" :value="i.quantity" disabled />
            <i
              class="fa fa-arrow-up"
              @click="cartStore.updateQuantity(i.id, i.quantity + 1)"
            ></i>
          </div>
          <i class="fa fa-trash" @click="remove(i.id)"></i>
        </li>
      </ul>
      <router-link to="/order" class="btn btn-primary">구매하기</router-link>
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from "@/scripts/useCartStore.js";
import { useAlert } from "@/utils/alert.js";
import axios from "@/axios.js";
import lib from "@/scripts/lib.js";

const { vAlert } = useAlert();
const cartStore = useCartStore();

const checkCartItems = (items = [], cartStore) => {
  return items.map((item) => {
    const existing = cartStore.items.find((i) => i.id === item.id);
    return {
      ...item,
      // existing.quantity가 있으면 그걸쓰고, 없으면 item.quantity 그것도 없으면 1
      quantity: existing?.quantity ?? item.quantity ?? 1,
    };
  });
};

const load = async () => {
  try {
    const res = await axios.get("/api/cart/items");
    cartStore.setItems(checkCartItems(res, cartStore));
  } catch (err) {
    const errMsg = err.response?.data?.msg;
    if (errMsg) {
      cartStore.setItems([]);
      vAlert(errMsg);
    }
  }
};

const remove = async (itemId) => {
  await axios.delete(`api/cart/items/${itemId}`);
  cartStore.items = cartStore.items.filter((item) => item.id !== itemId);
};

load();
</script>

<style scoped>
.cart ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.cart ul li {
  display: flex;
  align-items: center;
  position: relative;
  border: 1px solid #eee;
  margin: 25px 0;
  padding: 15px 20px;
}

.cart ul li img {
  width: 100px;
  height: 100px;
  object-fit: cover;
}

.cart ul li .name,
.cart ul li .price {
  margin-left: 20px;
  font-size: 16px;
}

.cart ul li .price {
  color: #888;
}

.cart ul li > div {
  display: flex;
  align-items: center;
  margin-left: auto;
  margin-right: 50px;
  gap: 10px;
  font-size: 18px;
}

.cart ul li .fa {
  cursor: pointer;
}

.cart ul li .fa-trash {
  position: absolute;
  right: 20px;
  font-size: 20px;
  color: #888;
  //cursor: pointer;
}

.cart ul li .fa.disabled {
  color: #ccc;
  pointer-events: none;
  cursor: none;
}

.cart .btn {
  width: 300px;
  display: block;
  margin: 0 auto;
  padding: 30px 50px;
  font-size: 20px;
}

.number {
  width: 60px;
}
</style>

<style>
/*   !* 글로벌 스타일에서만 작동 *!*/
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>
