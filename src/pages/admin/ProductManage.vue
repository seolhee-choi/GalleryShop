<template>
  <main class="main-content">
    <h1 class="h2">
      상품 조회
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
    </h1>
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
            <td><input v-model="state.newRow.name" /></td>
            <td><input v-model="state.newRow.imgPath" /></td>
            <td><input type="number" v-model="state.newRow.price" /></td>
            <td><input type="number" v-model="state.newRow.discountPer" /></td>
            <td></td>
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
  </main>
</template>

<script setup>
import axios from "@/axios.js";
import { reactive, ref } from "vue";
import { useAlert } from "@/utils/alert.js";
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

const openItemModal = (itemId) => {
  console.log(itemId);
  selectedItemId.value = itemId;
  isOpen.value = true;
};

const loadItems = () => {
  axios.get("/api/items").then((data) => {
    // for (let d of data) {
    //   state.items.push(d);
    // }
    state.items = data;
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

  console.log("try시작");
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
</script>

<style scoped></style>
