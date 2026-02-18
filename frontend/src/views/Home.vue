<template>
  <div class="home-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
        尊敬的 <span>{{ user.nickname }}</span> 您好！欢迎使用本系统，祝您今天有个好心情！
    </div>

    <!-- AI 简报 -->
    <el-card class="report-card" shadow="hover" v-loading="loadingReport">
        <template #header>
            <div class="card-header">
                <el-icon><DataAnalysis /></el-icon> 
                <span style="margin-left: 5px;">AI 每日健康简报</span>
                <el-button link type="primary" style="float: right;" @click="loadReport">刷新简报</el-button>
            </div>
        </template>
        <div class="report-content markdown-body" v-html="reportContent"></div>
    </el-card>

    <!-- 统计图表区 -->
    <el-row :gutter="20" class="chart-section">
        <el-col :span="12">
            <el-card shadow="hover">
                <div id="bp-chart" style="width: 100%; height: 350px;"></div>
            </el-card>
        </el-col>
        <el-col :span="12">
            <el-card shadow="hover">
                <div id="sport-chart" style="width: 100%; height: 350px;"></div>
            </el-card>
        </el-col>
    </el-row>

    <!-- 底部信息区 -->
    <el-row :gutter="20" class="info-section">
        <el-col :span="12">
            <el-card shadow="hover" class="info-card">
                <template #header>
                    <div class="card-header">
                        <span>系统公告</span>
                    </div>
                </template>
                <div class="notice-list">
                    <div v-for="item in noticeList" :key="item.id" class="notice-item">
                        <div class="notice-dot"></div>
                        <div class="notice-content">
                            <div class="notice-time">{{ item.createTime }}</div>
                            <div class="notice-title">{{ item.title }}</div>
                            <div class="notice-desc">{{ item.content }}</div>
                        </div>
                    </div>
                     <el-empty v-if="noticeList.length === 0" description="暂无公告" />
                </div>
            </el-card>
        </el-col>
        <el-col :span="12">
            <el-card shadow="hover" class="info-card">
                <template #header>
                    <div class="card-header">
                        <span>今日健康提醒</span>
                    </div>
                </template>
                <div class="reminder-list">
                    <div class="reminder-item">
                        <span>身体指标</span>
                        <span :class="status.indicator ? 'status-done' : 'status-todo'">
                            {{ status.indicator ? '已记录' : '未记录' }}
                        </span>
                    </div>
                     <div class="reminder-item">
                        <span>饮食情况</span>
                        <span :class="status.diet ? 'status-done' : 'status-todo'">
                            {{ status.diet ? '已记录' : '未记录' }}
                        </span>
                    </div>
                     <div class="reminder-item">
                        <span>运动情况</span>
                        <span :class="status.sport ? 'status-done' : 'status-todo'">
                            {{ status.sport ? '已记录' : '未记录' }}
                        </span>
                    </div>
                     <div class="reminder-item">
                        <span>睡眠情况</span>
                        <span :class="status.sleep ? 'status-done' : 'status-todo'">
                            {{ status.sleep ? '已记录' : '未记录' }}
                        </span>
                    </div>
                </div>
            </el-card>
        </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request'
import MarkdownIt from 'markdown-it'

const md = new MarkdownIt()
const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}
const noticeList = ref([])
const status = reactive({
    indicator: false,
    sport: false,
    diet: false,
    sleep: false
})
const reportContent = ref('')
const loadingReport = ref(false)

onMounted(() => {
  loadData()
  loadNotice()
  loadReport()
})

const loadData = () => {
    request.get(`/home/stats/${user.id}`).then(res => {
        if (res.code === '200') {
            const data = res.data
            // 渲染血压图表
            initBpChart(data.bloodPressure)
            // 渲染运动图表
            initSportChart(data.sport)
            // 更新今日状态
            Object.assign(status, data.todayStatus)
        }
    })
}

const loadNotice = () => {
    request.get('/home/notice/top').then(res => {
        if (res.code === '200') {
            noticeList.value = res.data
        }
    })
}

const loadReport = () => {
    loadingReport.value = true
    request.get('/ai/report/daily', { params: { userId: user.id } }).then(res => {
        loadingReport.value = false
        if (res.code === '200') {
            reportContent.value = md.render(res.data || '暂无数据，请多记录几天再来看看吧！')
        } else {
            reportContent.value = '简报生成失败，请稍后重试'
        }
    }).catch(() => {
        loadingReport.value = false
        reportContent.value = '网络错误，请稍后重试'
    })
}

const initBpChart = (data) => {
    const chart = echarts.init(document.getElementById('bp-chart'))
    const option = {
        title: { text: '近7天身体指标血压变化', left: 'center' },
        tooltip: { trigger: 'axis' },
        legend: { data: ['低压', '高压'], top: '30px' },
        xAxis: { type: 'category', data: data.dates },
        yAxis: { type: 'value' },
        series: [
            { name: '低压', type: 'line', data: data.low, smooth: true, itemStyle: { color: '#5470C6' } },
            { name: '高压', type: 'line', data: data.high, smooth: true, itemStyle: { color: '#91CC75' } }
        ]
    }
    chart.setOption(option)
}

const initSportChart = (data) => {
    const chart = echarts.init(document.getElementById('sport-chart'))
    const option = {
        title: { text: '近7天运动时长变化', left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.dates },
        yAxis: { type: 'value' },
        series: [
            { 
                name: '运动时长(分钟)', 
                type: 'line', 
                data: data.durations, 
                smooth: true, 
                areaStyle: { opacity: 0.3 },
                itemStyle: { color: '#73C0DE' },
                markPoint: {
                    data: [
                        { type: 'max', name: '最大值' },
                        { type: 'min', name: '最小值' }
                    ]
                }
            }
        ]
    }
    chart.setOption(option)
}
</script>

<style scoped>
.home-container {
    padding: 0;
}
.welcome-banner {
    background-color: white;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;
    font-size: 16px;
    color: #666;
    box-shadow: 0 1px 4px rgba(0,0,0,0.1);
}
.welcome-banner span {
    font-weight: bold;
    color: #333;
}
.report-card {
    margin-bottom: 20px;
    background: linear-gradient(to right, #ffffff, #f0f9eb);
}
.report-content {
    font-size: 15px;
    line-height: 1.6;
    color: #333;
    min-height: 80px;
}
.chart-section {
    margin-bottom: 20px;
}
.info-section .el-card {
    height: 400px;
    overflow-y: auto;
}
.card-header {
    font-weight: bold;
    font-size: 18px;
    display: flex;
    align-items: center;
}
.notice-item {
    display: flex;
    margin-bottom: 20px;
}
.notice-dot {
    width: 10px;
    height: 10px;
    background-color: #00b96b;
    border-radius: 50%;
    margin-top: 5px;
    margin-right: 10px;
    flex-shrink: 0;
}
.notice-time {
    color: #999;
    font-size: 12px;
    margin-bottom: 5px;
}
.notice-title {
    font-weight: bold;
    font-size: 16px;
    margin-bottom: 5px;
}
.notice-desc {
    color: #666;
    font-size: 14px;
}
.reminder-list {
    padding: 10px;
}
.reminder-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #eee;
    font-size: 16px;
}
.status-done {
    color: #67C23A;
    font-weight: bold;
}
.status-todo {
    color: #F56C6C;
}

/* Markdown 样式 */
:deep(.markdown-body p) {
    margin-bottom: 10px;
}
:deep(.markdown-body strong) {
    color: #409EFF;
}
</style>
