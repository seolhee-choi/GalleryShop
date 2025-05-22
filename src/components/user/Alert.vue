<template>
  <div class="app-alert">
    <TransitionGroup name="slide" tag="div">
      <div
        v-for="({ message, type }, index) in alerts"
        :key="index"
        class="alert"
        :class="typeStyle(type)"
        role="alert"
      >
        {{ message }}
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { useAlert } from "@/utils/alert.js";
const { alerts } = useAlert();

const typeStyle = (type) =>
  type === "error" ? "alert-danger" : "alert-primary";
</script>

<style>
.app-alert {
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 2000; /* 더 크게 */
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 320px;
}

/* alert 개별 스타일 */
.app-alert .alert {
  padding: 12px 20px;
  border-radius: 6px;
  color: white;
  min-width: 250px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

/* Bootstrap 기본 alert 색상 사용 */
.alert-primary {
  background-color: #0d6efd;
}

.alert-danger {
  background-color: #dc3545;
}

/* 애니메이션 */
.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-30px);
}
.slide-enter-active,
.slide-leave-active {
  transition: all 0.5s ease;
}
.slide-enter-to,
.slide-leave-from {
  opacity: 1;
  transform: translateY(0);
}
</style>
