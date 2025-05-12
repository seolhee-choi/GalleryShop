<template>
  <div>
    <h2>고객 조회</h2>
    <div class="table-responsive small">
      <table class="table table-striped table-sm">
        <thead>
          <tr>
            <th scope="col">이메일</th>
            <th scope="col">권한</th>
            <th scope="col">가입일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(m, idx) in state.members" :key="idx">
            <td>{{ m.email }}</td>
            <td>{{ m.role }}</td>
            <td>{{ m.id }}</td>
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
  members: [],
});

const loadMember = () => {
  axios.get("/api/admin/members").then(({ data }) => {
    state.members = [];

    for (let d of data) {
      if (d.items) {
        d.items = JSON.parse(d.items);
      }
      state.members.push(d);
    }
  });
};

loadMember();
</script>

<style scoped></style>
