<template>
  <main class="main-content">
    <div class="page-header">
      <h1 class="section-title">상품 조회</h1>
      <div class="action-buttons">
        <button
          class="btn btn-outline-danger save-btn"
          @click="isBulkModalOpen = true"
        >
          대용량 업로드
        </button>
        <BulkDataModal
          v-if="isBulkModalOpen"
          @close-modal="isBulkModalOpen = false"
        />
        <button class="btn btn-outline-danger save-btn" @click="addRow">
          행 추가
        </button>
        <button class="btn btn-primary save-btn" @click="saveChanges">
          저장
        </button>
      </div>
    </div>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th>상품ID</th>
            <th>상품명</th>
            <th>상품 이미지 경로</th>
            <th>상품원가</th>
            <th>할인율</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="state.newRow">
            <td>
              <button
                class="btn btn-sm btn-outline-danger cancel-btn"
                @click="cancelAddRow"
              >
                취소
              </button>
            </td>
            <td><input class="table-input" v-model="state.newRow.name" /></td>
            <td>
              <input class="table-input" v-model="state.newRow.imgPath" />
            </td>
            <td>
              <input
                class="table-input"
                type="number"
                v-model="state.newRow.price"
              />
            </td>
            <td>
              <input
                class="table-input"
                type="number"
                v-model="state.newRow.discountPer"
              />
            </td>
          </tr>
          <tr v-for="(i, idx) in state.items" :key="idx">
            <td>{{ i.id }}</td>
            <td>
              <span
                class="clickable-id"
                @click="openItemModal(i.id)"
                title="수정하시려면 클릭하세요"
              >
                {{ i.name }}
              </span>
              <ProductModifyModal
                v-if="isOpen && selectedItemId === i.id"
                :itemId="i.id"
                @close-modal="isOpen = false"
                @refresh-data="loadItems"
              />
            </td>
            <td>{{ i.imgPath }}</td>
            <td class="number">{{ Intl.NumberFormat().format(i.price) }}</td>
            <td class="number">{{ i.discountPer }} %</td>
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
import { computed, reactive, ref } from "vue";
import { useAlert } from "@/utils/alert.js";
import { usePaginationStore } from "@/scripts/usePaginationStore.js";
import { useInitPagination } from "@/scripts/useInitPagination.js";
import ProductModifyModal from "@/pages/admin/ProductModifyModal.vue";
import BulkDataModal from "@/pages/admin/BulkDataModal.vue";

const { vAlert, vSuccess } = useAlert();
const state = reactive({
  items: [],
  newRow: null,
});
const isBulkModalOpen = ref(false);
const isOpen = ref(false);
const selectedItemId = ref(null);

const pagination = usePaginationStore();
const page = computed(() => pagination.params.page);
const totalPage = computed(() => pagination.totalPage);

const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= pagination.totalPage) {
    pagination.changePage(newPage);
    loadItems(); // 부수 효과 처리
  }
};

const openItemModal = (itemId) => {
  console.log(itemId);
  selectedItemId.value = itemId;
  isOpen.value = true;
};

const loadItems = () => {
  axios.get("/api/items", { params: pagination.params }).then((data) => {
    // for (let d of data) {
    //   state.items.push(d);
    // }
    state.items = data.items;

    pagination.setPaginationInfo({
      page: data.currentPage,
      totalPage: data.totalPage,
      totalCount: data.totalCount,
    });
  });
};

const addRow = () => {
  state.newRow = {
    name: "",
    imgPath: "",
    price: 0,
    discountPer: 0,
  };
};

const saveChanges = async () => {
  if (!state.newRow) {
    console.log("!state.newRow");
    return;
  }

  const values = Object.values(state.newRow);
  if (
    values.includes("") ||
    values.includes(null) ||
    values.includes(undefined)
  ) {
    vAlert("모든 항목을 입력해주세요.");
    return;
  }

  try {
    await axios.post("/api/admin/items/upload", [state.newRow]);
    vSuccess("상품 등록이 정상적으로 처리되었습니다.");
    state.newRow = [];
    await loadItems();
  } catch (e) {
    vAlert("상품 등록 실패");
    console.log(e);
  }
};
const cancelAddRow = () => {
  state.newRow = null;
};

loadItems();
useInitPagination(loadItems);
</script>

<style scoped>
.table-input {
  width: 100%;
  padding: 6px 10px;
  font-size: 14px;
  border: 1.5px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
  transition:
    border-color 0.3s,
    box-shadow 0.3s;
}

.table-input:focus {
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
  outline: none;
  background-color: #fff;
}
</style>
