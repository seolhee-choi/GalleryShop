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
import axios from "@/axios.js";
import { ref, watch, reactive } from "vue";
import { useAlert } from "@/utils/alert.js";

const { vAlert, vSuccess } = useAlert();
const props = defineProps({
  itemId: "",
});
const emit = defineEmits(["close-review"]);

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
  const itemId = props.itemId;
  axios
    .post(`/api/reviews/register/${itemId}`, state.form)
    .then((res) => {
      vSuccess("리뷰가 성공적으로 등록되었습니다.");
      emit("close-review");
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
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
}

.modal-content {
  background-color: #ffffff;
  padding: 2rem;
  border-radius: 12px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-content h2 {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #333;
}

.mt-4 {
  margin-top: 1.5rem;
}

textarea {
  width: 100%;
  height: 80px;
  margin-top: 0.5rem;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  resize: none;
  font-size: 0.95rem;
  transition: border 0.2s ease;
}

textarea:focus {
  border-color: #ffa500;
  outline: none;
}

.modal-btn {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

.btn {
  padding: 0.5rem 1.25rem;
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-primary {
  background-color: #ffa500;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #ff8c00;
}

.btn-outline-secondary {
  background-color: transparent;
  border: 1px solid #ccc;
  color: #555;
}

.btn-outline-secondary:hover {
  background-color: #f5f5f5;
}

.star-rating {
  display: flex;
  gap: 0.5rem;
  font-size: 2rem;
  user-select: none;
}

.star span {
  transition: transform 0.2s ease;
}

.star span:hover {
  transform: scale(1.3);
}
</style>
