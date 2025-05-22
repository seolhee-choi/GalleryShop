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
      <p class="mb-1">
        Album example is &copy; Bootstrap, but please download and customize it
        for yourself!
      </p>
    </div>
  </footer>
</template>
<script setup>
defineProps({
  msg: {
    type: String,
    required: true,
  },
});
const scrollToTop = () => {
  const content = document.querySelector(".content");
  if (content) {
    content.scrollTo({ top: 0, behavior: "smooth" });
  }
};
</script>
<style></style>
