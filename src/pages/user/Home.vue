<template>
  <div class="home">
    <!--    <div class="album py-5" style="background-color: #fff6f6">-->
    <div class="album py-5">
      <div class="container">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          <div class="col" v-for="(item, idx) in state.items" :key="idx">
            <Card :item="item" />
          </div>
        </div>
      </div>
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
  </div>
</template>

<script setup>
import { computed, reactive } from "vue";
import { usePaginationStore } from "@/scripts/usePaginationStore.js";
import axios from "@/axios.js";
import Card from "@/components/user/Card.vue";

const state = reactive({
  items: [],
});
const pagination = usePaginationStore();
const page = computed(() => pagination.params.page);
const totalPage = computed(() => pagination.totalPage);

const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= pagination.totalPage) {
    pagination.changePage(newPage);
    api(); // 부수 효과 처리
  }
};
const api = () => {
  axios.get("/api/items", { params: pagination.params }).then((res) => {
    const data = res.items;
    state.items = data;

    pagination.setPaginationInfo({
      page: res.currentPage,
      totalPage: res.totalPage,
      totalCount: res.totalCount,
    });
  });
};

api();
</script>

<style></style>
