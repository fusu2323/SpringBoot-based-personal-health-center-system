<template>
  <div class="analytics-page">
    <el-card class="analytics-card">
      <!-- Page header -->
      <div class="page-header">
        <h2>健康数据分析</h2>
        <div class="date-picker-wrapper">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateRangeChange"
          />
        </div>
      </div>

      <!-- Metric category tabs -->
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="身体指标" name="indicator"></el-tab-pane>
        <el-tab-pane label="运动" name="sport"></el-tab-pane>
        <el-tab-pane label="饮食" name="diet"></el-tab-pane>
        <el-tab-pane label="睡眠" name="sleep"></el-tab-pane>
      </el-tabs>

      <!-- Tab content -->
      <div class="tab-content">
        <!-- Period toggle pills -->
        <div class="period-toggle">
          <button
            v-for="period in ['7天', '30天']"
            :key="period"
            :class="['period-pill', { active: selectedPeriod === period }]"
            @click="handlePeriodChange(period)"
          >
            {{ period }}
          </button>
        </div>

        <!-- Chart card -->
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title">{{ chartTitle }}</span>
            <span class="chart-period">{{ selectedPeriod }}趋势</span>
          </div>
          <div id="analytics-chart" class="chart-container"></div>
        </div>

        <!-- Stats row -->
        <div class="stats-row" v-if="statsData">
          <div class="stat-card">
            <div class="stat-value">{{ statsData.avg?.toFixed(1) || '--' }}</div>
            <div class="stat-label">平均</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ statsData.min?.toFixed(1) || '--' }}</div>
            <div class="stat-label">最低</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ statsData.max?.toFixed(1) || '--' }}</div>
            <div class="stat-label">最高</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ statsData.total?.toFixed(1) || '--' }}</div>
            <div class="stat-label">总计</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request'

const activeTab = ref('indicator')
const dateRange = ref([])
const selectedPeriod = ref('7天')
const chartInstance = ref(null)
const statsData = ref(null)

// Chart title by tab
const chartTitleMap = {
  indicator: '血压/心率/血糖',
  sport: '运动时长',
  diet: '每日摄入',
  sleep: '睡眠时长'
}
const chartId = 'analytics-chart'

const chartTitle = computed(() => chartTitleMap[activeTab.value] || '')

// Initialize date range to last 7 days
const initDateRange = () => {
  const end = new Date()
  const start = new Date()
  start.setDate(end.getDate() - 6)
  dateRange.value = [
    start.toISOString().split('T')[0],
    end.toISOString().split('T')[0]
  ]
}

const loadAnalyticsData = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  const userId = user.id
  if (!userId) return

  request.get(`/analytics/${activeTab.value}`, {
    params: {
      userId,
      startDate: dateRange.value[0],
      endDate: dateRange.value[1]
    }
  }).then(res => {
    if (res.code === '200') {
      statsData.value = res.data
      renderChart(res.data)
    }
  })
}

const renderChart = (data) => {
  if (!data || !data.dates || !data.values) return

  nextTick(() => {
    const chartDom = document.getElementById(chartId)
    if (!chartDom) return

    if (chartInstance.value) {
      chartInstance.value.dispose()
    }

    chartInstance.value = echarts.init(chartDom)

    // Series config by tab
    let seriesConfig = {}
    if (activeTab.value === 'indicator') {
      seriesConfig = {
        name: '数值',
        type: 'line',
        data: data.values,
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { color: '#8B9A7D', width: 3 },
        itemStyle: { color: '#8B9A7D', borderWidth: 2, borderColor: '#fff' },
        areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(139, 154, 125, 0.25)' }, { offset: 1, color: 'rgba(139, 154, 125, 0)' }] } }
      }
    } else if (activeTab.value === 'sport') {
      seriesConfig = {
        name: '运动时长',
        type: 'bar',
        data: data.values,
        barWidth: '50%',
        itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: '#C47D5E' }, { offset: 1, color: '#D4C4A8' }] }, borderRadius: [20, 20, 8, 8] }
      }
    } else {
      seriesConfig = {
        name: chartTitleMap[activeTab.value],
        type: 'line',
        data: data.values,
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { color: '#8B9A7D', width: 3 },
        itemStyle: { color: '#8B9A7D', borderWidth: 2, borderColor: '#fff' },
        areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(139, 154, 125, 0.25)' }, { offset: 1, color: 'rgba(139, 154, 125, 0)' }] } }
      }
    }

    chartInstance.value.setOption({
      backgroundColor: 'transparent',
      grid: { top: 20, right: 20, bottom: 30, left: 50 },
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: '#D4C4A8', textStyle: { color: '#3D3D3D' }, borderRadius: 12, padding: [12, 16] },
      xAxis: { type: 'category', data: data.dates, axisLine: { lineStyle: { color: '#D4C4A8' } }, axisLabel: { color: '#5A5A5A', fontFamily: 'DM Sans' } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#F5F1E8' } }, axisLine: { show: false }, axisLabel: { color: '#5A5A5A', fontFamily: 'DM Sans' } },
      series: [seriesConfig]
    })
  })
}

const handlePeriodChange = (period) => {
  selectedPeriod.value = period
  const end = new Date()
  const start = new Date()
  if (period === '7天') {
    start.setDate(end.getDate() - 6)
  } else {
    start.setDate(end.getDate() - 29)
  }
  dateRange.value = [
    start.toISOString().split('T')[0],
    end.toISOString().split('T')[0]
  ]
  loadAnalyticsData()
}

const handleDateRangeChange = () => {
  // Update selectedPeriod based on actual range
  if (dateRange.value && dateRange.value.length === 2) {
    const start = new Date(dateRange.value[0])
    const end = new Date(dateRange.value[1])
    const diffDays = Math.round((end - start) / (1000 * 60 * 60 * 24)) + 1
    selectedPeriod.value = diffDays <= 7 ? '7天' : '30天'
  }
  loadAnalyticsData()
}

const handleTabChange = () => {
  nextTick(() => {
    if (chartInstance.value) {
      chartInstance.value.resize()
    }
  })
  loadAnalyticsData()
}

onMounted(() => {
  initDateRange()
  loadAnalyticsData()
})

onUnmounted(() => {
  if (chartInstance.value) {
    chartInstance.value.dispose()
    chartInstance.value = null
  }
})
</script>

<style scoped>
.analytics-page {
  padding: var(--spacing-xl);
}

.analytics-card {
  background: var(--color-white);
  border-radius: var(--card-radius);
  padding: var(--card-padding-lg);
  box-shadow: var(--card-shadow);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.page-header h2 {
  font-family: var(--font-display);
  font-size: 28px;
  font-weight: 500;
  margin: 0;
}

.date-picker-wrapper {
  width: 280px;
}

.tab-content {
  margin-top: var(--spacing-lg);
}

.period-toggle {
  display: flex;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-lg);
}

.period-pill {
  padding: 6px 16px;
  border-radius: 100px;
  background: var(--color-cream);
  color: var(--color-text);
  cursor: pointer;
  border: none;
  font-size: 14px;
  font-family: var(--font-body);
  transition: all 0.2s ease;
}

.period-pill.active {
  background: var(--color-sage);
  color: white;
}

.chart-card {
  background: var(--color-white);
  border-radius: 24px;
  padding: 32px;
  box-shadow: var(--card-shadow);
  margin-bottom: var(--spacing-lg);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.chart-title {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 500;
}

.chart-period {
  font-size: 12px;
  background: var(--color-cream);
  padding: 6px 16px;
  border-radius: 100px;
}

.chart-container {
  width: 100%;
  height: 280px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--spacing-lg);
}

.stat-card {
  background: var(--color-white);
  border-radius: 20px;
  padding: 24px;
  box-shadow: var(--card-shadow);
  text-align: center;
}

.stat-value {
  font-family: var(--font-display);
  font-size: 48px;
  font-weight: 600;
  color: var(--color-sage);
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-muted);
}
</style>
