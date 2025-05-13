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
          <tr>
            <td>random</td>
            <td>data</td>
            <td>placeholder</td>
          </tr>
          <tr>
            <td>random</td>
            <td>data</td>
            <td>placeholder</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import axios from "@/axios.js";
import { reactive } from "vue";

const state = reactive({
  members: [],
});

const loadMember = () => {
  axios.get("/admin/members").then(({ data }) => {
    console.log(data);
    state.members = data;
  });
};

loadMember();
</script>

<style scoped></style>
