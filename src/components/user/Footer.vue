<template>
  <footer
    class="text-body-secondary py-5"
    style="
      background-color: rgba(
        var(--bs-light-rgb),
        var(--bs-bg-opacity, 1)
      ) !important;
    "
  >
    <div class="container">
      <p class="float-end mb-1">
        <a href="#" @click="scrollToTop" style="color: #ee422d">Back to top</a>
      </p>
      <p class="mb-1">Gallery Artshop</p>
      <p class="mb-1">서울시 성동구 성수동 | 010-1111-2222</p>
    </div>
  </footer>
</template>
<script setup>
const scrollToTop = () => {
  const content = document.querySelector(".content");
  if (content) {
    content.scrollTo({ top: 0, behavior: "smooth" });
  }
};
</script>
<style></style>
