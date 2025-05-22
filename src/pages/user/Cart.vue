<template>
  <div class="cart">
    <div class="container">
      <ul>
        <li v-for="(i, idx) in cartStore.items" :key="idx">
          <img :src="i.imgPath" />
          <div class="info">
            <span class="name">{{ i.name }}</span>
            <span class="price"
              >{{
                lib.getNumberFormatted(
                  i.price - (i.price * i.discountPer) / 100,
                )
              }}원</span
            >
          </div>
          <!-- 오른쪽 영역을 하나의 wrapper로 -->
          <div class="action-box">
            <div class="quantity">
              <i
                class="fa fa-arrow-down"
                :class="{ disabled: i.quantity <= 1 }"
                @click="cartStore.updateQuantity(i.id, i.quantity - 1)"
              ></i>
              <input
                type="number"
                class="number"
                :value="i.quantity"
                disabled
              />
              <i
                class="fa fa-arrow-up"
                @click="cartStore.updateQuantity(i.id, i.quantity + 1)"
              ></i>
            </div>
            <i class="fa fa-trash" @click="remove(i.id)"></i>
          </div>
        </li>
      </ul>
      <router-link
        to="/order"
        v-if="cartStore.items.length > 0"
        class="btn btn-primary"
        >구매하기</router-link
      >
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

const localCartItems = (items = [], cartStore) => {
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
    const localData = JSON.parse(
      localStorage.getItem(`cart_${cartStore.userId}`) || "[]",
    );

    const mergedItems = localCartItems(res, {
      items: localData, // 우선 localStorage에서 수량 반영
    });

    cartStore.setItems(mergedItems); // 최종 세팅

    cartStore.setItems(localCartItems(res, cartStore));
  } catch (err) {
    console.log(err);
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
.cart {
  padding: 80px 0 40px;
}

.cart ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.cart ul li {
  display: flex;
  align-items: center;
  border: 1.5px solid #f5c6cb;
  background-color: #fff0f6;
  border-radius: 10px;
  margin: 20px 0;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(238, 66, 45, 0.1);
  transition: box-shadow 0.3s ease;
}

.cart ul li:hover {
  box-shadow: 0 8px 16px rgba(238, 66, 45, 0.2);
}

.cart ul li img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #eee;
}

.cart ul li .info {
  display: flex;
  flex-direction: column;
  margin-left: 20px;
}

.cart ul li .info .name {
  font-size: 16px;
  color: #5a2a27;
  margin-bottom: 5px;
}

.cart ul li .info .price {
  font-size: 16px;
  color: #9b2c2c;
  font-weight: 600;
}

.cart ul li .action-box {
  display: flex;
  align-items: center;
  margin-left: auto;
  gap: 20px;
}

.cart ul li .quantity {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
}

.cart ul li .fa {
  cursor: pointer;
  transition: color 0.2s ease;
}

.cart ul li .fa:hover {
  color: #ee422d;
}

.cart ul li .fa.disabled {
  color: #ccc;
  pointer-events: none;
}

.cart ul li .fa-trash {
  font-size: 20px;
  color: #c23523;
}

.cart ul li .fa-trash:hover {
  color: #ee422d;
}

.cart .btn {
  width: 300px;
  display: block;
  margin: 40px auto 0;
  padding: 14px;
  font-size: 18px;
  font-weight: bold;
  background-color: #ee422d;
  border-color: #ee422d;
}

.cart .btn:hover {
  background-color: #c23523;
  border-color: #c23523;
}

.number {
  width: 60px;
  text-align: center;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 4px 6px;
  font-size: 16px;
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
