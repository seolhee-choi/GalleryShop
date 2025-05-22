<template>
  <main class="main-content">
    <div class="page-header">
      <h1 class="section-title">통계 조회</h1>
      <div class="stats-controls">
        <select v-model="filterType">
          <option value="daily">일별</option>
          <option value="weekly">주간별</option>
          <option value="monthly">월별</option>
        </select>
        <div class="datepicker-wrapper">
          <DatePicker v-model="selectedDate" />
        </div>
        <button class="btn btn-primary" @click="onClickSearch">조회</button>
      </div>
    </div>

    <!-- 통계 카드 -->
    <div class="stats-summary">
      <div class="stat-card" v-for="(stat, idx) in state.statistics" :key="idx">
        <h4>{{ stat.title }}</h4>
        <h4>{{ stat.value }}</h4>
      </div>
    </div>

    <!-- 통계 차트 -->
    <div v-if="hasChartData" class="chart-container">
      <!--      <Line :data="chartData" :options="chartOptions" :key="filterType" />-->
      <Bar :data="chartData" :options="chartOptions" :key="filterType" />
    </div>
    <div v-else class="text-center text-gray-500 py-8">매출이 없습니다.</div>
  </main>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from "vue";
import { Line, Bar } from "vue-chartjs";
import { formatDate } from "@/utils/date.js";
import axios from "@/axios.js";
import DatePicker from "vue3-datepicker";
import "chart.js/auto";
import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek.js";
dayjs.extend(isoWeek);

const state = reactive({
  statistics: [],
});

const filterType = ref("daily");
const selectedDate = ref(new Date());
const hasChartData = ref(true);

const dateFormatMap = {
  daily: "YYYY-MM-DD",
  monthly: "YYYY-MM",
};

const formattedDisplayDate = computed(() => {
  const date = selectedDate.value ?? new Date();

  if (filterType === "weekly") {
    return dayjs(date).format("GGGG-ww"); // 예) "2025-20"
  }
  const format = dateFormatMap[filterType.value] ?? "YYYY-MM-DD";
  return dayjs(date).format(format);
});

/* 여기서부터 chart 관련 함수들 */
const periodMap = {
  daily: 7,
  weekly: 7,
  monthly: 7,
};
const period = computed(() => periodMap[filterType.value] ?? 7);

const highlightColor = "rgba(255, 99, 132, 0.8)";
const defaultColor = "rgba(54, 162, 235, 0.6)";
const highlightBorder = "rgba(255, 99, 132, 1)";
const defaultBorder = "rgba(54, 162, 235, 1)";

const dateLabelMap = {
  daily: dayjs(selectedDate.value).format("YYYY-MM-DD"),
  weekly: dayjs(selectedDate.value).format("GGGG-ww"), // 예: "2025-20"
  monthly: dayjs(selectedDate.value).format("YYYY-MM"), // 예: "2025-05"
};

const targetLabel = dateLabelMap[filterType.value];

const chartData = ref({
  labels: [],
  datasets: [
    {
      label: "매출",
      data: [],
      backgroundColor: "rgba(54, 162, 235, 0.6)",
      borderColor: "rgba(54, 162, 235, 1)",
      borderWidth: 3,
    },
  ],
});

const chartOptions = reactive({
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    y: {},
    x: {},
  },
});

const updateYAxisOptions = (maxValue) => {
  if (!chartOptions.scales.y.ticks) {
    chartOptions.scales.y.ticks = {}; // 안전하게 초기화
  }

  if (maxValue <= 0) {
    delete chartOptions.scales.y.max;
    delete chartOptions.scales.y.min;
    delete chartOptions.scales.y.ticks.stepSize;
    return;
  }

  chartOptions.scales.y.min = 0;
  chartOptions.scales.y.max = Math.ceil(maxValue * 1.1);
  chartOptions.scales.y.ticks.stepSize = Math.ceil(
    chartOptions.scales.y.max / 5,
  );
};

const chartConfigMap = {
  daily: {
    getStats: (data) => data.dailyStats,
    getLabels: (s) => s.day,
  },
  weekly: {
    getStats: (data) => data.weeklyStats,
    // getLabels: (s) => `${s.week}주차`,
    getLabels: (s) => {
      const [year, weekStr] = s.week.split("-");
      const week = parseInt(weekStr, 10);

      // ISO 주차 기준으로 시작 날짜 계산 (월요일 기준)
      const start = dayjs()
        .year(parseInt(year))
        .isoWeek(week)
        .startOf("week")
        .add(1, "day"); // 월요일
      const end = start.add(6, "day"); // 일요일

      const formatRange = `${start.format("MM.DD")} ~ ${end.format("MM.DD")}`;
      return `${s.week}주차 (${formatRange})`;
    },
  },
  monthly: {
    getStats: (data) => data.monthlyStats,
    getLabels: (s) => {
      const [year, month] = s.month.split("-");
      return `${parseInt(month, 10)}월`; // "2025-05" → "5월" 또는 "2025년 5월"
    },
  },
};

/* 차트 그려주는 항목들 */
const generateDateLabels = (baseDate, period) => {
  const labels = [];
  for (let i = period - 1; i >= 0; i--) {
    labels.push(dayjs(baseDate).subtract(i, "day").format("YYYY-MM-DD"));
  }
  return labels;
};

const buildChartData = (type, stats) => {
  if (!stats || stats.length === 0) return;

  const config = chartConfigMap[type];
  if (!config) return;

  let labels = [];
  let dataPoints = [];

  if (type === "daily") {
    const labelList = generateDateLabels(selectedDate.value, period.value);
    const dataMap = new Map(stats.map((s) => [s.day, s.totalPrice]));

    // labels = stats.map(config.getLabels);
    // dataPoints = stats.map((s) => s.totalPrice);
    labels = labelList;
    dataPoints = labelList.map((date) => dataMap.get(date) ?? 0);
  } else {
    // 주별/월별은 같은 라벨끼리 totalPrice 합산 후 표시
    const aggregateMap = new Map();

    stats.forEach((s) => {
      const label = config.getLabels(s);
      aggregateMap.set(label, (aggregateMap.get(label) ?? 0) + s.totalPrice);
    });
    labels = Array.from(aggregateMap.keys());
    dataPoints = Array.from(aggregateMap.values());
  }

  const backgroundColor = labels.map((label) =>
    label === targetLabel ? highlightColor : defaultColor,
  );
  const borderColor = labels.map((label) =>
    label === targetLabel ? highlightBorder : defaultBorder,
  );

  chartData.value = {
    labels,
    datasets: [
      {
        label: "매출",
        data: dataPoints,
        backgroundColor,
        borderColor,
        borderWidth: 1,
      },
    ],
  };
  // console.log(chartData.value.labels);
  // console.log(chartData.value.datasets[0].data);

  const maxValue = Math.max(...dataPoints);
  updateYAxisOptions(maxValue);
};

const onClickSearch = () => {
  loadStatistics();
};
const loadStatistics = async () => {
  try {
    const data = await axios.get("/api/admin/statistics", {
      params: {
        type: filterType.value,
        date: formattedDisplayDate.value,
        period: period.value,
      },
    });

    const total = data.totalStats;

    state.statistics = [
      {
        title: "이번달 총 매출",
        value: `₩${total.totalPrice?.toLocaleString() || 0}`,
      },
      {
        title: "이번달 총 주문수",
        value: `${total.totalOrders?.toLocaleString() || 0}건`,
      },
      {
        title: "이번달 판매 상품수",
        value: `${total.totalItems?.toLocaleString() || 0}개`,
      },
    ];

    const config = chartConfigMap[filterType.value];
    const chartStats = config?.getStats(data) ?? [];

    hasChartData.value = chartStats.length > 0;

    buildChartData(filterType.value, chartStats);
  } catch (err) {
    console.error("통계 조회 실패:", err);
    hasChartData.value = false; // 에러 시도 대비
  }
};

loadStatistics();
</script>

<style scoped>
.stats-controls select,
.datepicker-wrapper ::v-deep input {
  height: 32px;
  font-size: 14px;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.chart-container {
  height: 300px;
  margin-top: 20px;
}
</style>
