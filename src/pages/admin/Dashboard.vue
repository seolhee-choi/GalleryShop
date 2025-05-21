<template>
  <main class="main-content">
    <div class="dashboard-header">
      <h1 class="dashboard-title">Dashboard</h1>
      <div class="dashboard-actions">
        <!--        <div class="button-group">-->
        <!--          <button class="btn">Share</button>-->
        <!--          <button class="btn">Export</button>-->
        <!--        </div>-->
        <!--        <button class="btn dropdown">-->
        <!--          <svg class="icon" aria-hidden="true">-->
        <!--            <use xlink:href="#calendar3"></use>-->
        <!--          </svg>-->
        <!--          This week-->
        <!--        </button>-->
      </div>
    </div>

    <canvas ref="chartRef" id="myChart" class="chart-canvas"></canvas>

    <h2 class="section-title">누적매출 TOP5 주문 상품</h2>
    <div class="table-container">
      <table class="custom-table">
        <thead>
          <tr>
            <th>순위</th>
            <th>상품명</th>
            <th>상품 가격</th>
            <th>누적 판매수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(i, idx) in state.items" :key="idx">
            <td class="number">{{ i.row_num }}</td>
            <td>{{ i.item_name }}</td>
            <td class="number">
              {{ Intl.NumberFormat().format(i.total_sales) }}
            </td>
            <td class="number">{{ i.total_quantity }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import {
  Chart,
  BarController,
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

Chart.register(
  BarController,
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
);

import { onMounted, reactive, ref, watch } from "vue";
import axios from "@/axios.js";

const state = reactive({
  items: [],
});
const chartRef = ref(null);
let chartInstance = null;

const loadtopOrderList = () => {
  axios.get("/api/admin/topOrderList").then((res) => {
    state.items = [];
    const data = res.items;
    for (let d of data) {
      state.items.push(d);
    }
  });
};

const renderChart = () => {
  if (chartInstance) {
    chartInstance.destroy();
  }
  const ctx = chartRef.value.getContext("2d");
  chartInstance = new Chart(ctx, {
    type: "bar",
    data: {
      labels: state.items.map((item) => item.item_name),
      datasets: [
        {
          label: "매출",
          data: state.items.map((item) => item.total_sales),
          backgroundColor: "#70a1ff", // 메인 블루 계열
          borderColor: "#3742fa", // 강조 컬러
          hoverBackgroundColor: "#5352ed",
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        legend: { display: true },
        title: {
          display: true,
          text: "누적매출 Top 5 상품",
          font: {
            size: 20, // 숫자로 지정 (px 단위)
          },
        },
      },
      scales: {
        x: {
          ticks: {
            font: {
              size: 16, // x축 숫자 크기 조절
            },
          },
        },
        y: {
          ticks: {
            font: {
              size: 16, // y축 라벨(상품명) 크기 조절
            },
          },
        },
      },
    },
  });
};

// 데이터가 바뀔 때마다 차트 다시 그림
watch(
  () => state.items,
  (newVal) => {
    if (newVal.length > 0) {
      renderChart();
    }
  },
);

onMounted(() => {
  loadtopOrderList();
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
  display: block;
  margin: 0 auto;
  padding-bottom: 60px;
}
</style>
