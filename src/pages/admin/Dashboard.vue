<template>
  <div class="container-fluid">
    <div class="row">
      <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
        <div
          class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom"
        >
          <h1 class="h2">Dashboard</h1>
          <div class="btn-toolbar mb-2 mb-md-0">
            <div class="btn-group me-2">
              <button type="button" class="btn btn-sm btn-outline-secondary">
                Share
              </button>
              <button type="button" class="btn btn-sm btn-outline-secondary">
                Export
              </button>
            </div>
            <button
              type="button"
              class="btn btn-sm btn-outline-secondary dropdown-toggle d-flex align-items-center gap-1"
            >
              <svg class="bi" aria-hidden="true">
                <use xlink:href="#calendar3"></use>
              </svg>
              This week
            </button>
          </div>
        </div>

        <canvas
          ref="chartRef"
          class="my-4 w-100"
          id="myChart"
          width="683"
          height="288"
          style="
            display: block;
            box-sizing: border-box;
            height: 230px;
            width: 546px;
          "
        ></canvas>

        <h2>최근 주문 상품</h2>
        <div class="table-responsive small">
          <table class="table table-striped table-sm">
            <thead>
              <tr>
                <th scope="col">상품명</th>
                <th scope="col">상품 가격</th>
                <th scope="col">누적 판매수</th>
                <th scope="col">순위</th>
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
    </div>
  </div>
</template>

<script setup>
import { Chart } from "chart.js/auto";
import { onMounted, ref } from "vue";
import AdminSidebar from "@/components/admin/AdminSidebar.vue";

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
.bi {
  display: inline-block;
  width: 1rem;
  height: 1rem;
}
/*
 * Navbar
 */

.navbar-brand {
  padding-top: 0.75rem;
  padding-bottom: 0.75rem;
  background-color: rgba(0, 0, 0, 0.25);
  box-shadow: inset -1px 0 0 rgba(0, 0, 0, 0.25);
}

.navbar .form-control {
  padding: 0.75rem 1rem;
}
</style>
