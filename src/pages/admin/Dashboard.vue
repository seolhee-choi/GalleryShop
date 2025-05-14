<template>
  <main class="main-content">
    <div class="dashboard-header">
      <h1 class="dashboard-title">Dashboard</h1>
      <div class="dashboard-actions">
        <div class="button-group">
          <button class="btn">Share</button>
          <button class="btn">Export</button>
        </div>
        <button class="btn dropdown">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#calendar3"></use>
          </svg>
          This week
        </button>
      </div>
    </div>

    <canvas ref="chartRef" id="myChart" class="chart-canvas"></canvas>

    <h2 class="section-title">최근 주문 상품</h2>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th>상품명</th>
            <th>상품 가격</th>
            <th>누적 판매수</th>
            <th>순위</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>random</td>
            <td>data</td>
            <td>placeholder</td>
            <td>text</td>
          </tr>
          <tr>
            <td>random</td>
            <td>data</td>
            <td>placeholder</td>
            <td>text</td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import { Chart } from "chart.js/auto";
import { onMounted, ref } from "vue";

const chartRef = ref(null);

onMounted(() => {
  const ctx = chartRef.value.getContext("2d"); // 이 시점에는 DOM이 존재함

  new Chart(ctx, {
    type: "line",
    data: {
      labels: [
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
      ],
      datasets: [
        {
          data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
          lineTension: 0,
          backgroundColor: "transparent",
          borderColor: "#007bff",
          borderWidth: 4,
          pointBackgroundColor: "#007bff",
        },
      ],
    },
    options: {
      plugins: {
        legend: { display: false },
        tooltip: { boxPadding: 3 },
      },
    },
  });
});
</script>

<style scoped>
.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #ccc;
  margin-bottom: 24px;
}

.dashboard-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.dashboard-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.button-group {
  display: flex;
  gap: 8px;
}

.btn {
  background-color: #f1f3f5;
  border: 1px solid #ccc;
  padding: 6px 12px;
  font-size: 14px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn:hover {
  background-color: #e0e0e0;
}

.dropdown {
  display: flex;
  align-items: center;
  gap: 6px;
}

.icon {
  width: 16px;
  height: 16px;
  fill: currentColor;
}

.chart-canvas {
  width: 100%;
  height: 230px;
  margin-bottom: 40px;
}
</style>
