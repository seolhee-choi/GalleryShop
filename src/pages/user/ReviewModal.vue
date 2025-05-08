<template>
  <div class="modal">
    <div class="modal-content">
      <h2>리뷰작성</h2>
      <div class="mt-4">
        <p>별점 등록</p>
        <div class="star-rating">
          <div class="star">
            <span
              v-for="index in 5"
              :key="index"
              @click="check(index)"
              @mouseover="hover(index)"
              @mouseleave="hover(0)"
            >
              {{ index <= (hovered || score) ? "💛" : "🤍" }}
            </span>
          </div>
        </div>
      </div>
      <div class="mt-4">
        <p>리뷰(50자 이내)</p>
        <textarea placeholder="리뷰내용을 작성하세요" v-model="text" />
        <p>{{ text.length }} / 50</p>
      </div>
      <div class="modal-btn">
        <button
          class="btn btn-outline-secondary"
          type="button"
          @click="$emit('close-review')"
        >
          취소
        </button>
        <button class="btn btn-primary" type="button" @click="register">
          등록
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, watch, reactive } from "vue";
import { useAlert } from "@/utils/alert.js";

const { vAlert, vSuccess } = useAlert();
const props = defineProps({
  item: Object,
});

const state = reactive({
  form: {
    rating: 1,
    content: "",
  },
});

const score = ref(0);
const hovered = ref(0);
const text = ref("");

watch(text, (newVal) => {
  if (newVal.length > 50) {
    alert("리뷰는 50자 이내로 작성해주세요.");
    text.value = newVal.slice(0, 50);
  }
});
const check = (index) => {
  score.value = index;
  state.form.rating = index;
  state.form.content = text;
};

const hover = (index) => {
  hovered.value = index;
};
const register = () => {
  const itemId = props.item.id;

  axios
    .post(`/api/reviews/register/${itemId}`, state.form)
    .then((res) => {
      vSuccess("리뷰가 성공적으로 등록되었습니다.");
      emit("close-review");
      emit("");
    })
    .catch((err) => {
      vAlert("리뷰 등록 중 에러가 발생했습니다.");
    });
};
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

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
}

.modal-btn {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

textarea {
  width: 100%;
  height: 100px;
  margin-top: 0.5rem;
  padding: 0.5rem;
  resize: none;
}

.star-rating {
  display: flex;
  flex-direction: row;
  gap: 5px;
  font-size: 2rem;
  cursor: pointer;
}

.star {
  transition: transform 0.2s ease;
}

.star:hover {
  transform: scale(1.2);
}
</style>
