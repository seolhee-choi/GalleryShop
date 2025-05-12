<template>
  <div>
    <h2>주문 내역 조회</h2>
    <div class="table-responsive small">
      <table class="table table-striped table-sm">
        <thead>
          <tr>
            <th scope="col">주문ID</th>
            <th scope="col">회원ID</th>
            <th scope="col">받는사람</th>
            <th scope="col">받는주소</th>
            <th scope="col">결제방식</th>
            <th scope="col">수량</th>
            <th scope="col">주문내역</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(o, idx) in state.orders" :key="idx">
            <td>{{ o.id }}</td>
            <td>{{ o.memberId }}</td>
            <td>{{ o.name }}</td>
            <td>{{ o.address }}</td>
            <td>{{ o.payment }}</td>
            <td>{{ o.quantity }}</td>
            <td>{{ o.items }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { reactive } from "vue";

const state = reactive({
  orders: [],
});

const loadMember = () => {
  axios.get("/api/admin/orders").then(({ data }) => {
    state.orders = [];

    for (let d of data) {
      if (d.items) {
        d.items = JSON.parse(d.items);
      }
      state.orders.push(d);
    }
  });
};

loadMember();
</script>

<style scoped></style>
