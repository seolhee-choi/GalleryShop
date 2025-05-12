<template>
  <div class="access-denied-container">
    <h1>접근이 거부되었습니다</h1>
    <p>이 페이지에 접근할 권한이 없습니다.</p>
    <router-link to="/">홈으로 돌아가기</router-link>
  </div>
</template>

<script setup>
// 추가 로직이 필요하다면 여기에 작성
</script>

<style scoped>
.access-denied-container {
  text-align: center;
  margin-top: 100px;
}
.access-denied-container h1 {
  font-size: 2rem;
  color: #d9534f;
}
.access-denied-container p {
  margin: 20px 0;
}
</style>
