<template>
  <main class="main-content">
    <div class="page-header">
      <h1 class="section-title">회원 조회</h1>
      <button class="btn btn-primary save-btn" @click="saveChanges">
        저장
      </button>
    </div>
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
import { ref, reactive, computed } from "vue";
import { useAlert, confirmAndSaveChanges } from "@/utils/alert.js";
import { usePaginationStore } from "@/scripts/usePaginationStore.js";
import { useInitPagination } from "@/scripts/useInitPagination.js";

const { vAlert, vSuccess } = useAlert();

const state = reactive({
  members: [],
});
const pagination = usePaginationStore();
const page = computed(() => pagination.params.page);
const totalPage = computed(() => pagination.totalPage);

const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= pagination.totalPage) {
    pagination.changePage(newPage);
    loadMember(); // 부수 효과 처리
  }
};

const loadMember = () => {
  axios.get("/api/admin/members", { params: pagination.params }).then((res) => {
    const data = res.items;
    state.members = [];

    for (let d of data) {
      if (d.items) {
        d.items = JSON.parse(d.items);
      }
      state.members.push(d);
    }

    pagination.setPaginationInfo({
      page: res.currentPage,
      totalPage: res.totalPage,
      totalCount: res.totalCount,
    });
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

useInitPagination(loadMember);
</script>

<style scoped>
/* 저장 버튼 별도 여유 주기 */
.save-btn {
  min-width: 80px;
  padding: 6px 12px;
}
</style>
