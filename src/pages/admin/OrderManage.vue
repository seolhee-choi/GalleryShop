<template>
  <main class="main-content">
    <h1 class="h2">
      주문 조회
      <button class="btn btn-primary save-btn" @click="saveChanges">
        저장
      </button>
    </h1>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th scope="col" style="width: 50px">주문ID</th>
            <th scope="col" style="width: 50px">회원ID</th>
            <th scope="col" style="width: 80px">받는사람</th>
            <th scope="col" style="width: 200px">받는주소</th>
            <th scope="col" style="width: 50px">결제방식</th>
            <th scope="col" style="width: 50px">수량</th>
            <th scope="col" style="width: 100px">주문상품</th>
            <th scope="col" style="width: 50px">원가</th>
            <th scope="col" style="width: 50px">최종구매가격(수량*할인가)</th>
          </tr>
        </thead>
        <tbody>
          <!--          <tr v-for="(o, idx) in state.orders" :key="idx">-->
          <!--            <td>{{ o.id }}</td>-->
          <!--            <td>{{ o.memberId }}</td>-->
          <!--            <td>{{ o.name }}</td>-->
          <!--            <td>{{ o.address }}</td>-->
          <!--            <td>{{ o.payment }}</td>-->
          <!--            <td>{{ o.quantity }}</td>-->
          <!--            <td>-->
          <!--              <div v-for="(i, index) in o.items" :key="index">-->
          <!--                {{ i.name }} / {{ i.price }}-->
          <!--              </div>-->
          <!--            </td>-->
          <!--          </tr>-->
          <template v-for="(o, idx) in state.orders" :key="idx">
            <!-- 첫 번째 tr에 주문 공통 정보 + 첫 번째 상품 -->
            <tr>
              <td :rowspan="o.items.length">{{ o.id }}</td>
              <td :rowspan="o.items.length">{{ o.memberId }}</td>
              <td :rowspan="o.items.length">{{ o.name }}</td>
              <td :rowspan="o.items.length">{{ o.address }}</td>
              <td :rowspan="o.items.length">{{ o.payment }}</td>

              <!-- 첫 번째 상품 -->
              <td>{{ o.items[0].quantity }}</td>
              <td>{{ o.items[0].name }}</td>
              <td>{{ o.items[0].price }}</td>
              <td>{{ orderItemPrices[idx][0] }}</td>
            </tr>

            <!-- 두 번째 이후 상품 출력 -->
            <tr v-for="(i, iIdx) in o.items.slice(1)" :key="iIdx">
              <td>{{ i.quantity }}</td>
              <td>{{ i.name }}</td>
              <td>{{ i.price }}</td>
              <td>{{ orderItemPrices[idx][iIdx + 1] }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import axios from "@/axios.js";
import { computed, reactive } from "vue";

const state = reactive({
  orders: [],
});

// 수량 * 구매금액의 합 계산 함수
const orderItemPrices = computed(() =>
  state.orders.map((order) =>
    order.items.map(({ quantity = 0, price = 0, discountPer = 0 }) => {
      const total = quantity * price * (1 - discountPer / 100);
      return new Intl.NumberFormat().format(total);
    }),
  ),
);
const loadOrderList = () => {
  axios.get("/api/admin/orders").then((res) => {
    const data = res.orders;
    state.orders = [];

    for (let d of data) {
      if (d.order && d.items) {
        state.orders.push({
          ...d.order,
          items: d.items,
        });
      }
    }
  });
};

loadOrderList();
</script>

<stlye scoped></stlye>
