<template>
  <main class="main-content">
    <h1 class="section-title">주문 조회</h1>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th scope="col" style="width: 50px">주문ID</th>
            <th scope="col" style="width: 50px">회원ID</th>
            <th scope="col" style="width: 80px">받는 사람</th>
            <th scope="col" style="width: 200px">받는 주소</th>
            <th scope="col" style="width: 100px">주문 상품</th>
            <th scope="col" style="width: 50px">수량</th>
            <th scope="col" style="width: 50px">결제방식</th>
            <th scope="col" style="width: 50px">
              최종구매가격<br />(수량 * 할인가)
            </th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(o, idx) in state.orders" :key="idx">
            <!-- 첫 번째 tr에 주문 공통 정보 + 첫 번째 상품 -->
            <tr>
              <td class="number" :rowspan="o.items.length">{{ o.id }}</td>
              <td class="number" :rowspan="o.items.length">{{ o.memberId }}</td>
              <td :rowspan="o.items.length">{{ o.name }}</td>
              <td :rowspan="o.items.length">{{ o.address }}</td>

              <!-- 첫 번째 상품 -->
              <td>{{ o.items[0].name }}</td>
              <td class="number">{{ o.items[0].quantity }}</td>
              <td :rowspan="o.items.length">{{ o.payment }}</td>
              <td class="number">{{ orderItemPrices[idx][0] }}</td>
            </tr>

            <!-- 두 번째 이후 상품 출력 -->
            <tr v-for="(i, iIdx) in o.items.slice(1)" :key="iIdx">
              <!-- 숫자 오른쪽 정렬 깨져서 추가로 넣음 -->
              <td style="display: none" colspan="5"></td>
              <td>{{ i.name }}</td>
              <td class="number">{{ i.quantity }}</td>
              <td class="number">
                {{ orderItemPrices[idx][iIdx + 1] }}
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>

    <!-- 페이징 UI 시작 -->
    <div class="pagination">
      <button :disabled="page === 1" @click="changePage(page - 1)">이전</button>
      <span>페이지 {{ page }} / {{ totalPage }}</span>
      <button :disabled="page === totalPage" @click="changePage(page + 1)">
        다음
      </button>
    </div>
    <!-- 페이징 UI 끝 -->
  </main>
</template>

<script setup>
import axios from "@/axios.js";
import { computed, reactive, ref } from "vue";
import { usePaginationStore } from "@/scripts/usePaginationStore.js";
import { useInitPagination } from "@/scripts/useInitPagination.js";

const state = reactive({
  orders: [],
});
const pagination = usePaginationStore();
const page = computed(() => pagination.params.page);
const totalPage = computed(() => pagination.totalPage);

const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= pagination.totalPage) {
    pagination.changePage(newPage);
    loadOrderList(); // 부수 효과 처리
  }
};

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
  axios.get("/api/admin/orders", { params: pagination.params }).then((res) => {
    state.orders = [];

    for (let i of res.items) {
      if (i.order && i.items) {
        state.orders.push({
          ...i.order,
          items: i.items,
        });
      }
    }

    pagination.setPaginationInfo({
      page: res.currentPage,
      totalPage: res.totalPage,
      totalCount: res.totalCount,
    });
  });
};

loadOrderList();

useInitPagination(loadOrderList);
</script>

<style scoped></style>
