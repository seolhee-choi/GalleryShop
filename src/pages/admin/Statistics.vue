<template>
  <main class="main-content">
    <h1 class="h2">
      <span>통계 조회</span>
      <div class="stats-controls">
        <select v-model="type">
          <option value="daily">일별</option>
          <option value="monthly">월별</option>
          <option value="product">상품별</option>
        </select>
        <button class="btn btn-primary" @click="loadStats(type)">조회</button>
      </div>
    </h1>
    <!-- 📌 통계 카드 -->
    <div class="stats-summary">
      <div class="stat-card" v-for="stat in summaryStats" :key="stat.title">
        <h4>{{ stat.title }}</h4>
        <p>{{ stat.value }}</p>
      </div>
    </div>

    <!-- 📊 통계 차트 -->
    <div class="chart-container">
      <Line :data="chartData" :options="chartOptions" :key="type" />
    </div>
  </main>
</template>

<script setup>
import { ref } from "vue";
import { Line } from "vue-chartjs";
import "chart.js/auto";
import axios from "@/axios.js";

const type = ref("daily"); // 통계조회 '일별'
const chartData = ref({
  labels: ["1월", "2월", "3월", "4월"],
  datasets: [
    {
      label: "매출",
      data: [1000, 1500, 800, 1200],
      borderColor: "#007bff",
      backgroundColor: "rgba(0, 123, 255, 0.2)",
      fill: true,
    },
  ],
});

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: true,
    },
  },
};

// 통계 데이터 예시 (Vue 컴포넌트 내부)
const summaryStats = [
  { title: "총 매출", value: "₩3,400,000" },
  { title: "총 주문수", value: "1,280건" },
  { title: "상품 수", value: "230개" },
];

const loadDailyStatistics = () => {
  axios
    .get("/api/admin/statistics/daily")
    .then((res) => {
      console.log(res);
      console.log("일별 통계 조회 성공");
    })
    .catch((err) => {
      console.log(err);
      console.log("일별 통계 조회 실패");
    });
};

loadDailyStatistics();
</script>

<style scoped>
.chart-container {
  height: 300px;
  margin-top: 20px;
}
</style>
