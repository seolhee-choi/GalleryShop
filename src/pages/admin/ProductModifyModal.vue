<template>
  <div class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2>상품 수정</h2>
      </div>
      <table class="edit-table" v-for="item in state.items" :key="item.id">
        <tr>
          <th>상품명</th>
          <td><input v-model="item.name" /></td>
        </tr>
        <tr>
          <th>상품 이미지 경로</th>
          <td><input v-model="item.imgPath" /></td>
        </tr>
        <tr>
          <th>상품원가</th>
          <td><input v-model="item.price" type="number" /></td>
        </tr>
        <tr>
          <th>할인율</th>
          <td><input v-model="item.discountPer" type="number" /></td>
        </tr>
      </table>
      <div class="modal-btn">
        <button
          class="btn btn-outline-secondary"
          type="button"
          @click="$emit('close-modal')"
        >
          취소
        </button>
        <button class="btn btn-primary" type="button" @click="updateItem">
          변경
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { nextTick, reactive } from "vue";
import axios from "@/axios.js";
import { useAlert } from "@/utils/alert.js";

const { vAlert, vSuccess } = useAlert();
const props = defineProps({
  itemId: "",
});
const emit = defineEmits(["close-modal"]);
const state = reactive({
  items: [],
});
const loadItem = () => {
  const itemId = props.itemId;

  axios
    .get(`/api/admin/items/${itemId}`)
    .then((data) => {
      state.items = data;
    })
    .catch((err) => alert(err));
};

const updateItem = () => {
  axios
    .post("/api/reviews/update", state.items)
    .then((res) => {
      vSuccess("상품정보가 변경되었습니다.");
      emit("close-modal");
      emit("refresh-data");
    })
    .catch((err) => {
      vAlert("상품 변경 중 에러가 발생했습니다.");
    });
};

loadItem();
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 어두운 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-header h2 {
  margin: 0 0 1rem;
  font-size: 20px;
  font-weight: bold;
}

.modal-content {
  background-color: white;
  padding: 1.5rem 2rem;
  border-radius: 8px;
  width: 800px;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
}

.modal-btn {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.edit-table {
  width: 100%;
  border-collapse: collapse;
}

.edit-table th {
  text-align: left;
  width: 30%;
  padding: 0.5rem;
  background-color: #f8f8f8;
  vertical-align: top;
}

.edit-table tr:not(:last-child) td {
  padding-bottom: 12px;
}

.edit-table td {
  padding: 0.5rem;
}

.edit-table input {
  width: 100%;
  padding: 6px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
</style>
