<template>
  <main class="main-content">
    <h1 class="h2">
      회원 조회
      <button class="btn btn-primary save-btn" @click="saveChanges">
        저장
      </button>
    </h1>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th>사용자ID</th>
            <th>이메일</th>
            <th>권한</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(m, idx) in state.members" :key="idx">
            <td class="number">{{ m.id }}</td>
            <td>{{ m.email }}</td>
            <td class="select-option">
              <select v-model="m.role">
                <option value="USER">사용자</option>
                <option value="ADMIN">관리자</option>
              </select>
            </td>
            <td class="select-option">
              <select v-model="m.status">
                <option value="0">활성화</option>
                <option value="1">비활성화</option>
              </select>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import axios from "@/axios.js";
import { reactive } from "vue";
import { useAlert, confirmAndSaveChanges } from "@/utils/alert.js";

const { vAlert, vSuccess } = useAlert();

const state = reactive({
  members: [],
});

const loadMember = () => {
  axios.get("/api/admin/members").then((res) => {
    state.members = [];

    for (let d of res) {
      if (d.items) {
        d.items = JSON.parse(d.items);
      }
      state.members.push(d);
    }
  });
};

const saveChanges = async () => {
  const isConfirmed = await confirmAndSaveChanges();
  if (isConfirmed) {
    try {
      const response = await axios.post("/api/admin/members", state.members);
      vSuccess("사용자 정보가 성공적으로 저장되었습니다.");
    } catch (err) {
      console.error(err);
      vAlert("사용자 정보 업데이트에 실패했습니다.");
    }
  }
};

loadMember();
</script>

<style scoped></style>
