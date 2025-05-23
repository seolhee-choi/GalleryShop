<template>
  <main class="main-content">
    <div class="dashboard-header">
      <h1 class="dashboard-title">Dashboard</h1>
      <div class="dashboard-actions"></div>
    </div>

    <canvas ref="chartRef" id="myChart" class="chart-canvas"></canvas>

    <h1 class="section-title">누적매출 TOP5 주문 상품</h1>
    <div class="table-container">
      <table class="top5-table">
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

.btn-dashboard {
  background-color: #f1f3f5;
  border: 1px solid #ccc;
  padding: 6px 12px;
  font-size: 14px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-dashboard:hover {
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

/* 대시보드 TOP5 테이블 스타일 */
.top5-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 12px;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.top5-table thead th {
  background-color: #2f405a; /* 사이드바 네이비톤과 맞춤 */
  color: #fff;
  font-weight: 700;
  padding: 12px 20px;
  text-align: left;
  border-radius: 10px 10px 0 0;
  user-select: none;
}

.top5-table tbody tr {
  background-color: #fdfdfd;
  box-shadow: 0 4px 6px rgb(0 0 0 / 0.1);
  border-radius: 10px;
  transition: background-color 0.3s ease;
  cursor: default;
}

.top5-table tbody tr:hover {
  background-color: #f0f4ff;
}

.top5-table tbody td {
  padding: 14px 20px;
  color: #333;
  vertical-align: middle;
}

.top5-table tbody td.number {
  text-align: right;
  font-weight: 600;
  color: #2f405a;
}

.top5-table tbody td.rank-badge {
  text-align: center;
  font-weight: 700;
  color: white;
  background: #a399f7; /* 라벤더 컬러 */
  width: 36px;
  height: 36px;
  line-height: 36px;
  border-radius: 50%;
  user-select: none;
}
</style>
