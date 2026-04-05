<template>
  <div class="home-container" ref="homeContainer">
    <!-- Scroll Progress -->
    <div class="scroll-progress" :style="{ transform: `scaleX(${scrollProgress})` }"></div>

    <!-- Hero Section - Full Screen -->
    <section class="hero-section" ref="heroSection">
      <!-- Animated Background Grid -->
      <div class="hero-grid"></div>

      <!-- Floating Particles -->
      <div class="particles" ref="particles">
        <div v-for="i in 20" :key="i" class="particle" :class="`particle-${i}`"></div>
      </div>

      <!-- Decorative Shapes -->
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>

      <!-- Hero Content -->
      <div class="hero-content">
        <div class="hero-badge">
          <span class="badge-dot"></span>
          健康生活，从今天开始
        </div>

        <h1 class="hero-title">
          <span class="title-line">探索</span>
          <span class="title-line accent">生命之美</span>
          <span class="title-line">守护健康</span>
        </h1>

        <p class="hero-description">
          用数据记录生活，用科学守护健康。<br/>
          让每一次心跳都有意义，让每一天都充满活力。
        </p>

        <div class="hero-cta">
          <button class="cta-primary" @click="scrollToRecord">
            <span>开始记录</span>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </button>
          <button class="cta-secondary" @click="scrollToReport">
            <span>查看报告</span>
          </button>
        </div>

        <!-- Scroll Indicator -->
        <div class="scroll-indicator">
          <div class="scroll-mouse">
            <div class="scroll-wheel"></div>
          </div>
          <span>向下滚动</span>
        </div>
      </div>

      <!-- Hero Visual - Rotating Globe -->
      <div class="hero-visual">
        <div class="globe-container">
          <div class="globe">
            <div class="globe-ring ring-1"></div>
            <div class="globe-ring ring-2"></div>
            <div class="globe-ring ring-3"></div>
            <div class="globe-core">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
              </svg>
            </div>
          </div>
          <!-- Orbiting Elements -->
          <div class="orbit orbit-1">
            <div class="orbit-dot"></div>
          </div>
          <div class="orbit orbit-2">
            <div class="orbit-dot"></div>
          </div>
          <div class="orbit orbit-3">
            <div class="orbit-dot"></div>
          </div>
        </div>

        <!-- Stats Cards -->
        <div class="hero-stats">
          <div class="stat-card stat-1">
            <span class="stat-value">87</span>
            <span class="stat-label">健康评分</span>
          </div>
          <div class="stat-card stat-2">
            <span class="stat-value">42</span>
            <span class="stat-label">连续天数</span>
          </div>
          <div class="stat-card stat-3">
            <span class="stat-value">2.1k</span>
            <span class="stat-label">活跃用户</span>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section with Scroll Reveal -->
    <section class="features-section" ref="featuresSection">
      <div class="section-label">今日记录</div>
      <h2 class="section-title">每一个小坚持，都是健康的积累</h2>

      <div class="features-grid">
        <div
          v-for="(feature, index) in features"
          :key="feature.id"
          class="feature-card"
          :ref="el => featureCards[index] = el"
          @click="openQuickAdd(feature.id)"
        >
          <div class="feature-icon" :style="{ background: feature.bgColor }">
            <svg viewBox="0 0 24 24" fill="none" :stroke="feature.color" stroke-width="1.5" v-html="feature.icon"></svg>
          </div>
          <div class="feature-content">
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-status" :class="status[feature.id] ? 'done' : 'pending'">
              {{ status[feature.id] ? '已记录' : '未记录' }}
            </p>
          </div>
          <div class="feature-action">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="12" y1="5" x2="12" y2="19"/>
              <line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
          </div>
          <div class="feature-glow" :style="{ background: `radial-gradient(circle, ${feature.color}20 0%, transparent 70%)` }"></div>
        </div>
      </div>
    </section>

    <!-- Report Section - Parallax -->
    <section class="report-section" ref="reportSection" id="report">
      <div class="report-bg-element"></div>

      <div class="report-layout">
        <div class="report-main">
          <div class="report-card" :class="{ loaded: reportLoaded }">
            <div class="report-header">
              <div class="report-badge">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                </svg>
                AI 健康简报
              </div>
              <button class="refresh-btn" @click="loadReport">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="23 4 23 10 17 10"/>
                  <path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/>
                </svg>
              </button>
            </div>
            <div class="report-content markdown-body" v-html="reportContent" v-loading="loadingReport"></div>
          </div>
        </div>

        <div class="report-sidebar">
          <div class="mini-stat">
            <div class="mini-stat-value">{{ todayStats.sleep }}</div>
            <div class="mini-stat-label">睡眠时长 / 小时</div>
            <div class="mini-stat-bar">
              <div class="mini-stat-fill" :style="{ width: (todayStats.sleep / 10 * 100) + '%' }"></div>
            </div>
          </div>
          <div class="mini-stat">
            <div class="mini-stat-value">{{ todayStats.sport }}</div>
            <div class="mini-stat-label">运动时长 / 分钟</div>
            <div class="mini-stat-bar">
              <div class="mini-stat-fill sport" :style="{ width: (todayStats.sport / 120 * 100) + '%' }"></div>
            </div>
          </div>
          <div class="mini-stat">
            <div class="mini-stat-value">{{ todayStats.calories }}</div>
            <div class="mini-stat-label">摄入热量 / 卡</div>
            <div class="mini-stat-bar">
              <div class="mini-stat-fill calories" :style="{ width: (todayStats.calories / 2500 * 100) + '%' }"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Charts Section -->
    <section class="charts-section" ref="chartsSection">
      <div class="section-label">健康趋势</div>
      <h2 class="section-title">回顾过去，展望未来</h2>
      <div class="section-cta">
        <button class="view-analytics-btn" @click="$router.push('/layout/analytics')">
          <span>查看分析</span>
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
      </div>

      <div class="charts-grid">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title">血压变化</span>
            <span class="chart-period">近7天</span>
          </div>
          <div id="bp-chart" class="chart-container"></div>
        </div>
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title">运动时长</span>
            <span class="chart-period">近7天</span>
          </div>
          <div id="sport-chart" class="chart-container"></div>
        </div>
      </div>
    </section>

    <!-- Notice Section -->
    <section class="notice-section" ref="noticeSection">
      <div class="notice-header">
        <div class="notice-badge">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
            <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
          </svg>
          系统公告
        </div>
      </div>

      <div class="notice-grid">
        <div v-for="(item, index) in noticeList" :key="item.id" class="notice-card" :style="{ animationDelay: index * 0.1 + 's' }">
          <div class="notice-date">{{ formatDate(item.createTime) }}</div>
          <h4 class="notice-title">{{ item.title }}</h4>
          <p class="notice-desc">{{ item.content }}</p>
          <div class="notice-cta">
            <span>查看详情</span>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </div>
        </div>
      </div>

      <div v-if="noticeList.length === 0" class="notice-empty">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
        </svg>
        <span>暂无公告</span>
      </div>
    </section>

    <!-- Quick Add Dialog -->
    <el-dialog v-model="quickAddVisible" :title="quickAddTitle" width="480px" class="quick-add-dialog">
      <el-form :model="quickForm" label-width="100px" label-position="left">
        <el-form-item label="日期">
          <el-date-picker v-model="quickForm.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <template v-if="quickAddType === 'indicator'">
          <el-form-item label="体重(kg)"><el-input-number v-model="quickForm.weight" :precision="1" :step="0.1" style="width: 100%" /></el-form-item>
          <el-form-item label="血压"><el-input v-model="quickForm.bloodPressure" placeholder="例如: 120/80" /></el-form-item>
          <el-form-item label="血糖"><el-input-number v-model="quickForm.bloodSugar" :precision="1" :step="0.1" style="width: 100%" /></el-form-item>
          <el-form-item label="心率"><el-input-number v-model="quickForm.heartRate" style="width: 100%" /></el-form-item>
          <el-form-item label="体温"><el-input-number v-model="quickForm.temperature" :precision="1" :step="0.1" style="width: 100%" /></el-form-item>
        </template>

        <template v-if="quickAddType === 'sport'">
          <el-form-item label="运动类型"><el-input v-model="quickForm.sportType" placeholder="如: 跑步、游泳、瑜伽" /></el-form-item>
          <el-form-item label="时长(分钟)"><el-input-number v-model="quickForm.duration" style="width: 100%" /></el-form-item>
          <el-form-item label="消耗(kcal)"><el-input-number v-model="quickForm.calorie" style="width: 100%" /></el-form-item>
        </template>

        <template v-if="quickAddType === 'diet'">
          <el-form-item label="餐别">
            <el-select v-model="quickForm.mealType" placeholder="选择餐别" style="width: 100%">
              <el-option label="早餐" value="早餐" /><el-option label="午餐" value="午餐" /><el-option label="晚餐" value="晚餐" /><el-option label="加餐" value="加餐" />
            </el-select>
          </el-form-item>
          <el-form-item label="食物名称"><el-input v-model="quickForm.foodName" placeholder="如: 鸡胸肉、西兰花" /></el-form-item>
          <el-form-item label="卡路里"><el-input-number v-model="quickForm.calorie" style="width: 100%" /></el-form-item>
        </template>

        <template v-if="quickAddType === 'sleep'">
          <el-form-item label="入睡时间"><el-date-picker v-model="quickForm.sleepTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" /></el-form-item>
          <el-form-item label="起床时间"><el-date-picker v-model="quickForm.wakeTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" /></el-form-item>
          <el-form-item label="时长(小时)"><el-input-number v-model="quickForm.duration" :precision="1" :step="0.5" style="width: 100%" /></el-form-item>
          <el-form-item label="睡眠质量">
            <el-select v-model="quickForm.quality" placeholder="选择质量" style="width: 100%">
              <el-option label="优" value="优" /><el-option label="良" value="良" /><el-option label="中" value="中" /><el-option label="差" value="差" />
            </el-select>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="quickAddVisible = false">取消</el-button>
        <el-button type="primary" @click="saveQuickAdd" :loading="saving">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, nextTick, reactive, ref, computed, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import anime from 'animejs'
import request from '../utils/request'
import MarkdownIt from 'markdown-it'
import { ElMessage } from 'element-plus'

const md = new MarkdownIt()
const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}
const noticeList = ref([])
const status = reactive({ indicator: false, sport: false, diet: false, sleep: false })
const reportContent = ref('')
const loadingReport = ref(false)
const quickAddVisible = ref(false)
const quickAddType = ref('')
const quickAddTitle = ref('')
const quickForm = reactive({ recordDate: new Date().toISOString().split('T')[0] })
const saving = ref(false)
const reportLoaded = ref(false)
const todayStats = reactive({ sleep: 7.2, sport: 45, calories: 1850 })

const scrollProgress = ref(0)
const heroSection = ref(null)
const featuresSection = ref(null)
const reportSection = ref(null)
const chartsSection = ref(null)
const noticeSection = ref(null)
const featureCards = ref([])
const homeContainer = ref(null)

const features = [
  { id: 'indicator', title: '身体指标', color: '#C47D5E', bgColor: 'rgba(196, 125, 94, 0.1)', icon: '<path d="M22 12h-4l-3 9L9 3l-3 9H2"/>' },
  { id: 'sport', title: '运动情况', color: '#8B9A7D', bgColor: 'rgba(139, 154, 125, 0.1)', icon: '<circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/>' },
  { id: 'diet', title: '饮食情况', color: '#D4A574', bgColor: 'rgba(212, 165, 116, 0.1)', icon: '<path d="M18 8h1a4 4 0 0 1 0 8h-1"/><path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"/><line x1="6" y1="1" x2="6" y2="4"/><line x1="10" y1="1" x2="10" y2="4"/><line x1="14" y1="1" x2="14" y2="4"/>' },
  { id: 'sleep', title: '睡眠情况', color: '#7B68A6', bgColor: 'rgba(123, 104, 166, 0.1)', icon: '<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>' }
]

const typeTitles = { indicator: '身体指标 - 快速录入', sport: '运动记录 - 快速录入', diet: '饮食记录 - 快速录入', sleep: '睡眠记录 - 快速录入' }

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.split(' ')[0]
}

const scrollToRecord = () => {
  featuresSection.value?.scrollIntoView({ behavior: 'smooth' })
}

const scrollToReport = () => {
  reportSection.value?.scrollIntoView({ behavior: 'smooth' })
}

const handleScroll = () => {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  scrollProgress.value = docHeight > 0 ? scrollTop / docHeight : 0

  // Parallax effects
  if (heroSection.value) {
    const heroOffset = scrollTop * 0.5
    heroSection.value.style.setProperty('--parallax', heroOffset + 'px')
  }
}

const openQuickAdd = (type) => {
  quickAddType.value = type
  quickAddTitle.value = typeTitles[type]
  Object.keys(quickForm).forEach(key => {
    if (key !== 'recordDate') delete quickForm[key]
  })
  quickForm.userId = user.id
  quickForm.recordDate = new Date().toISOString().split('T')[0]

  anime({ targets: event.currentTarget, scale: [1, 0.95, 1], duration: 200, easing: 'easeOutQuad' })
  setTimeout(() => {
    quickAddVisible.value = true
    setTimeout(() => {
      anime({ targets: '.quick-add-dialog .el-dialog', opacity: [0, 1], scale: [0.95, 1], duration: 300, easing: 'easeOutQuad' })
    }, 50)
  }, 100)
}

const saveQuickAdd = () => {
  saving.value = true
  request.post(`/record/${quickAddType.value}/save`, quickForm).then(res => {
    saving.value = false
    if (res.code === '200') {
      ElMessage.success('保存成功')
      quickAddVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  }).catch(() => { saving.value = false; ElMessage.error('网络错误') })
}

onMounted(async () => {
  loadData()
  loadNotice()
  loadReport()
  await nextTick()
  setTimeout(() => animateHero(), 100)
  window.addEventListener('scroll', handleScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

function animateHero() {
  // Hero content reveal
  anime.timeline({ easing: 'easeOutExpo' })
    .add({ targets: '.hero-badge', opacity: [0, 1], translateY: [30, 0], duration: 800 })
    .add({ targets: '.title-line', opacity: [0, 1], translateY: [80, 0], rotateX: [-40, 0], delay: anime.stagger(150), duration: 1000 }, '-=400')
    .add({ targets: '.hero-description', opacity: [0, 1], translateY: [30, 0], duration: 800 }, '-=600')
    .add({ targets: '.hero-cta', opacity: [0, 1], translateY: [30, 0], scale: [0.9, 1], duration: 800 }, '-=400')
    .add({ targets: '.scroll-indicator', opacity: [0, 1], translateY: [20, 0], duration: 600 }, '-=200')

  // Globe animation
  anime({ targets: '.globe', rotateY: 360, duration: 20000, easing: 'linear', loop: true })
  anime({ targets: '.globe-core', scale: [1, 1.1, 1], duration: 3000, easing: 'easeInOutSine', loop: true })
  anime({ targets: '.ring-1', rotateZ: 360, duration: 15000, easing: 'linear', loop: true })
  anime({ targets: '.ring-2', rotateZ: -360, duration: 20000, easing: 'linear', loop: true })
  anime({ targets: '.ring-3', rotateY: 360, duration: 25000, easing: 'linear', loop: true })

  // Orbit animations
  anime({ targets: '.orbit-1', rotate: 360, duration: 8000, easing: 'linear', loop: true })
  anime({ targets: '.orbit-2', rotate: -360, duration: 12000, easing: 'linear', loop: true })
  anime({ targets: '.orbit-3', rotate: 360, duration: 10000, easing: 'linear', loop: true })

  // Particles float
  document.querySelectorAll('.particle').forEach((p, i) => {
    anime({
      targets: p,
      translateY: [-20, 20],
      translateX: [10, -10],
      scale: [0.8, 1.2],
      opacity: [0.3, 0.8],
      duration: 3000 + (i * 500),
      easing: 'easeInOutSine',
      loop: true,
      direction: 'alternate'
    })
  })

  // Stats cards
  anime({ targets: '.stat-card', opacity: [0, 1], translateY: [60, 0], scale: [0.8, 1], delay: anime.stagger(150, { start: 1500 }), duration: 1000, easing: 'easeOutBack' })

  // Scroll indicator bounce
  anime({ targets: '.scroll-wheel', translateY: [0, 8, 0], duration: 1500, easing: 'easeInOutSine', loop: true })

  // Intersection Observer for sections
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const el = entry.target
        if (el.classList.contains('features-section')) {
          anime({ targets: '.feature-card', opacity: [0, 1], translateY: [60, 0], scale: [0.9, 1], delay: anime.stagger(100), duration: 800, easing: 'easeOutBack' })
        } else if (el.classList.contains('report-section')) {
          reportLoaded.value = true
          anime({ targets: '.report-card', opacity: [0, 1], translateX: [-60, 0], rotateY: [-15, 0], duration: 1000, easing: 'easeOutQuart' })
          anime({ targets: '.mini-stat', opacity: [0, 1], translateX: [40, 0], delay: anime.stagger(150, { start: 400 }), duration: 800, easing: 'easeOutBack' })
        } else if (el.classList.contains('charts-section')) {
          anime({ targets: '.chart-card', opacity: [0, 1], translateY: [40, 0], scale: [0.95, 1], delay: anime.stagger(150), duration: 800, easing: 'easeOutQuart' })
        } else if (el.classList.contains('notice-section')) {
          anime({ targets: '.notice-card', opacity: [0, 1], translateY: [30, 0], rotateX: [-10, 0], delay: anime.stagger(100), duration: 600, easing: 'easeOutQuart' })
        }
        observer.unobserve(el)
      }
    })
  }, { threshold: 0.15 })

  if (featuresSection.value) observer.observe(featuresSection.value)
  if (reportSection.value) observer.observe(reportSection.value)
  if (chartsSection.value) observer.observe(chartsSection.value)
  if (noticeSection.value) observer.observe(noticeSection.value)
}

const loadData = () => {
  request.get(`/home/stats/${user.id}`).then(res => {
    if (res.code === '200') {
      const data = res.data
      initBpChart(data.bloodPressure)
      initSportChart(data.sport)
      Object.assign(status, data.todayStatus)
    }
  })
}

const loadNotice = () => {
  request.get('/home/notice/top').then(res => { if (res.code === '200') noticeList.value = res.data })
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
  }).catch(() => { loadingReport.value = false; reportContent.value = '网络错误，请稍后重试' })
}

const initBpChart = (data) => {
  const chart = echarts.init(document.getElementById('bp-chart'))
  chart.setOption({
    backgroundColor: 'transparent',
    grid: { top: 20, right: 20, bottom: 30, left: 50 },
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: '#D4C4A8', textStyle: { color: '#3D3D3D' }, borderRadius: 12, padding: [12, 16] },
    xAxis: { type: 'category', data: data.dates, axisLine: { lineStyle: { color: '#D4C4A8' } }, axisLabel: { color: '#5A5A5A', fontFamily: 'DM Sans' } },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#F5F1E8' } }, axisLine: { show: false }, axisLabel: { color: '#5A5A5A', fontFamily: 'DM Sans' } },
    series: [
      { name: '低压', type: 'line', data: data.low, smooth: true, symbol: 'circle', symbolSize: 8, lineStyle: { color: '#8B9A7D', width: 3 }, itemStyle: { color: '#8B9A7D', borderWidth: 2, borderColor: '#fff' }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(139, 154, 125, 0.25)' }, { offset: 1, color: 'rgba(139, 154, 125, 0)' }] } } },
      { name: '高压', type: 'line', data: data.high, smooth: true, symbol: 'circle', symbolSize: 8, lineStyle: { color: '#C47D5E', width: 3 }, itemStyle: { color: '#C47D5E', borderWidth: 2, borderColor: '#fff' }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(196, 125, 94, 0.25)' }, { offset: 1, color: 'rgba(196, 125, 94, 0)' }] } } }
    ]
  })
}

const initSportChart = (data) => {
  const chart = echarts.init(document.getElementById('sport-chart'))
  chart.setOption({
    backgroundColor: 'transparent',
    grid: { top: 20, right: 20, bottom: 30, left: 50 },
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: '#D4C4A8', textStyle: { color: '#3D3D3D' }, borderRadius: 12, padding: [12, 16] },
    xAxis: { type: 'category', data: data.dates, axisLine: { lineStyle: { color: '#D4C4A8' } }, axisLabel: { color: '#5A5A5A', fontFamily: 'DM Sans' } },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#F5F1E8' } }, axisLine: { show: false }, axisLabel: { color: '#5A5A5A', fontFamily: 'DM Sans' } },
    series: [{ name: '运动时长', type: 'bar', data: data.durations, barWidth: '50%', itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: '#C47D5E' }, { offset: 1, color: '#D4C4A8' }] }, borderRadius: [20, 20, 8, 8] } }]
  })
}
</script>

<style scoped>
.home-container {
  position: relative;
  z-index: 1;
}

/* Scroll Progress */
.scroll-progress {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--color-sage), var(--color-terracotta));
  transform-origin: left;
  z-index: 1000;
  transform: scaleX(0);
}

/* Hero Section */
.hero-section {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  padding: 0 80px;
  align-items: center;
  position: relative;
  overflow: hidden;
  --parallax: 0;
}

/* Animated Grid Background */
.hero-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(139, 154, 125, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(139, 154, 125, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  mask-image: radial-gradient(ellipse at center, black 20%, transparent 70%);
}

/* Particles */
.particles {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.particle {
  position: absolute;
  width: 8px;
  height: 8px;
  background: var(--color-sage);
  border-radius: 50%;
  opacity: 0.4;
}

.particle-1 { top: 20%; left: 10%; }
.particle-2 { top: 30%; left: 85%; width: 6px; height: 6px; }
.particle-3 { top: 60%; left: 5%; background: var(--color-terracotta); }
.particle-4 { top: 70%; left: 90%; width: 10px; height: 10px; }
.particle-5 { top: 40%; left: 50%; background: var(--color-terracotta); width: 4px; height: 4px; }
.particle-6 { top: 80%; left: 20%; }
.particle-7 { top: 15%; left: 70%; background: var(--color-terracotta); width: 6px; height: 6px; }
.particle-8 { top: 85%; left: 60%; width: 5px; height: 5px; }
.particle-9 { top: 25%; left: 30%; background: var(--color-terracotta); }
.particle-10 { top: 50%; left: 75%; width: 7px; height: 7px; }
.particle-11 { top: 65%; left: 40%; }
.particle-12 { top: 35%; left: 95%; width: 5px; height: 5px; background: var(--color-terracotta); }
.particle-13 { top: 90%; left: 85%; }
.particle-14 { top: 10%; left: 55%; width: 6px; height: 6px; background: var(--color-terracotta); }
.particle-15 { top: 75%; left: 35%; }
.particle-16 { top: 45%; left: 15%; width: 4px; height: 4px; background: var(--color-terracotta); }
.particle-17 { top: 55%; left: 92%; }
.particle-18 { top: 5%; left: 45%; width: 5px; height: 5px; background: var(--color-terracotta); }
.particle-19 { top: 95%; left: 10%; }
.particle-20 { top: 20%; left: 60%; width: 6px; height: 6px; }

/* Decorative Shapes */
.shape {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(80px);
  opacity: 0.4;
}

.shape-1 {
  top: -200px;
  right: -200px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(139, 154, 125, 0.3) 0%, transparent 70%);
  animation: float 20s ease-in-out infinite;
}

.shape-2 {
  bottom: -200px;
  left: -200px;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(196, 125, 94, 0.25) 0%, transparent 70%);
  animation: float 25s ease-in-out infinite reverse;
}

.shape-3 {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 800px;
  height: 800px;
  background: radial-gradient(circle, rgba(212, 165, 116, 0.1) 0%, transparent 60%);
  animation: pulse 15s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(30px, -30px); }
}

@keyframes pulse {
  0%, 100% { opacity: 0.3; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 0.5; transform: translate(-50%, -50%) scale(1.1); }
}

/* Hero Content */
.hero-content {
  position: relative;
  z-index: 2;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
  background: rgba(139, 154, 125, 0.1);
  border: 1px solid rgba(139, 154, 125, 0.2);
  border-radius: 100px;
  font-size: 13px;
  color: var(--color-sage-dark);
  margin-bottom: 32px;
  opacity: 0;
}

.badge-dot {
  width: 8px;
  height: 8px;
  background: var(--color-sage);
  border-radius: 50%;
  animation: blink 2s ease-in-out infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(0.8); }
}

.hero-title {
  display: flex;
  flex-direction: column;
  font-family: var(--font-display);
  font-size: clamp(48px, 6vw, 80px);
  font-weight: 500;
  line-height: 1.1;
  color: var(--color-ink);
  margin-bottom: 28px;
  perspective: 1000px;
}

.title-line {
  display: block;
  opacity: 0;
}

.title-line.accent {
  background: linear-gradient(135deg, var(--color-sage), var(--color-terracotta));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 18px;
  line-height: 1.8;
  color: var(--color-text);
  margin-bottom: 40px;
  opacity: 0;
}

.hero-cta {
  display: flex;
  gap: 16px;
  margin-bottom: 60px;
  opacity: 0;
}

.cta-primary {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 32px;
  background: linear-gradient(135deg, var(--color-sage), var(--color-sage-dark));
  color: white;
  border: none;
  border-radius: 100px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cta-primary svg {
  width: 20px;
  height: 20px;
  transition: transform 0.3s ease;
}

.cta-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(139, 154, 125, 0.4);
}

.cta-primary:hover svg {
  transform: translateX(4px);
}

.cta-secondary {
  display: flex;
  align-items: center;
  padding: 16px 32px;
  background: transparent;
  color: var(--color-text);
  border: 2px solid var(--color-sand);
  border-radius: 100px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cta-secondary:hover {
  border-color: var(--color-sage);
  color: var(--color-sage);
  background: rgba(139, 154, 125, 0.05);
}

/* Scroll Indicator */
.scroll-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  opacity: 0;
}

.scroll-mouse {
  width: 24px;
  height: 40px;
  border: 2px solid var(--color-sand);
  border-radius: 12px;
  display: flex;
  justify-content: center;
  padding-top: 8px;
}

.scroll-wheel {
  width: 4px;
  height: 8px;
  background: var(--color-sage);
  border-radius: 2px;
}

.scroll-indicator span {
  font-size: 11px;
  color: var(--color-text-muted);
  text-transform: uppercase;
  letter-spacing: 2px;
}

/* Hero Visual */
.hero-visual {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 2;
}

.globe-container {
  position: relative;
  width: 350px;
  height: 350px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.globe {
  position: relative;
  width: 200px;
  height: 200px;
}

.globe-ring {
  position: absolute;
  border: 1px solid rgba(139, 154, 125, 0.3);
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.ring-1 {
  width: 250px;
  height: 250px;
  border-style: dashed;
}

.ring-2 {
  width: 300px;
  height: 180px;
  border-radius: 50%;
  transform: translate(-50%, -50%) rotateX(60deg);
}

.ring-3 {
  width: 350px;
  height: 200px;
  border-radius: 50%;
  transform: translate(-50%, -50%) rotateY(60deg);
}

.globe-core {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 140px;
  height: 140px;
  background: linear-gradient(135deg, var(--color-sage-light), var(--color-sage));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 20px 60px rgba(139, 154, 125, 0.4);
}

.globe-core svg {
  width: 60px;
  height: 60px;
  stroke: white;
  opacity: 0.9;
}

/* Orbits */
.orbit {
  position: absolute;
  border-radius: 50%;
  border: 1px solid rgba(196, 125, 94, 0.2);
}

.orbit-1 {
  width: 280px;
  height: 280px;
  transform-origin: center;
}

.orbit-2 {
  width: 320px;
  height: 320px;
  transform-origin: center;
}

.orbit-3 {
  width: 360px;
  height: 360px;
  transform-origin: center;
}

.orbit-dot {
  position: absolute;
  top: -6px;
  left: 50%;
  transform: translateX(-50%);
  width: 12px;
  height: 12px;
  background: var(--color-terracotta);
  border-radius: 50%;
  box-shadow: 0 0 20px rgba(196, 125, 94, 0.5);
}

/* Stats Cards */
.hero-stats {
  display: flex;
  gap: 16px;
  margin-top: 40px;
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 28px;
  background: var(--color-white);
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(139, 154, 125, 0.15);
  opacity: 0;
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-value {
  font-family: var(--font-display);
  font-size: 36px;
  font-weight: 600;
  color: var(--color-ink);
  line-height: 1;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 12px;
  color: var(--color-text-muted);
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* Features Section */
.features-section {
  padding: 120px 80px;
  position: relative;
}

.section-label {
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 3px;
  color: var(--color-terracotta);
  margin-bottom: 16px;
  font-weight: 500;
}

.section-title {
  font-family: var(--font-display);
  font-size: clamp(32px, 4vw, 48px);
  font-weight: 500;
  color: var(--color-ink);
  margin-bottom: 60px;
  max-width: 600px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.feature-card {
  position: relative;
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 28px;
  background: var(--color-white);
  border-radius: 24px;
  box-shadow: var(--card-shadow);
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  opacity: 0;
  overflow: hidden;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--card-shadow-hover);
}

.feature-card:hover .feature-glow {
  opacity: 1;
}

.feature-glow {
  position: absolute;
  inset: 0;
  opacity: 0;
  transition: opacity 0.4s ease;
  pointer-events: none;
}

.feature-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: transform 0.3s ease;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1) rotate(5deg);
}

.feature-icon svg {
  width: 28px;
  height: 28px;
}

.feature-content {
  flex: 1;
  min-width: 0;
}

.feature-title {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 500;
  color: var(--color-ink);
  margin-bottom: 4px;
}

.feature-status {
  font-size: 13px;
  font-weight: 500;
}

.feature-status.done { color: var(--color-sage-dark); }
.feature-status.pending { color: var(--color-terracotta); }

.feature-action {
  width: 32px;
  height: 32px;
  background: var(--color-cream);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.feature-action svg {
  width: 16px;
  height: 16px;
  stroke: var(--color-text-muted);
}

.feature-card:hover .feature-action {
  background: var(--color-sage);
}

.feature-card:hover .feature-action svg {
  stroke: white;
}

/* Report Section */
.report-section {
  padding: 80px;
  position: relative;
  overflow: hidden;
}

.report-bg-element {
  position: absolute;
  top: 0;
  right: 0;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(139, 154, 125, 0.08) 0%, transparent 60%);
  border-radius: 50%;
  filter: blur(60px);
  pointer-events: none;
}

.report-layout {
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 40px;
  position: relative;
}

.report-card {
  background: var(--color-white);
  border-radius: 32px;
  padding: 48px;
  box-shadow: var(--card-shadow);
  opacity: 0;
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.report-badge {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 24px;
  background: linear-gradient(135deg, var(--color-terracotta), var(--color-sand));
  border-radius: 100px;
  color: white;
  font-weight: 500;
  font-size: 15px;
}

.report-badge svg {
  width: 18px;
  height: 18px;
}

.refresh-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: var(--color-cream);
  border: none;
  border-radius: 100px;
  color: var(--color-text);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.refresh-btn:hover {
  background: var(--color-sage);
  color: white;
}

.refresh-btn svg {
  width: 16px;
  height: 16px;
}

.report-content {
  font-size: 15px;
  line-height: 2;
  color: var(--color-text);
}

.report-content :deep(p) { margin-bottom: 16px; }
.report-content :deep(strong) { color: var(--color-ink); font-weight: 500; }

.report-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.mini-stat {
  background: var(--color-white);
  border-radius: 20px;
  padding: 24px;
  box-shadow: var(--card-shadow);
  opacity: 0;
}

.mini-stat-value {
  font-family: var(--font-display);
  font-size: 48px;
  font-weight: 600;
  color: var(--color-sage);
  line-height: 1;
  margin-bottom: 8px;
}

.mini-stat-label {
  font-size: 13px;
  color: var(--color-text-muted);
  margin-bottom: 16px;
}

.mini-stat-bar {
  height: 6px;
  background: var(--color-cream);
  border-radius: 100px;
  overflow: hidden;
}

.mini-stat-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--color-sage), var(--color-sage-light));
  border-radius: 100px;
  transition: width 1s ease;
}

.mini-stat-fill.sport {
  background: linear-gradient(90deg, var(--color-terracotta), var(--color-sand));
}

.mini-stat-fill.calories {
  background: linear-gradient(90deg, #7B68A6, #A594C0);
}

/* Charts Section */
.charts-section {
  padding: 80px;
}

.section-cta {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 24px;
}

.view-analytics-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
  background: var(--color-sage);
  color: white;
  border: none;
  border-radius: 100px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background var(--transition-fast);
}

.view-analytics-btn:hover {
  background: var(--color-sage-dark);
}

.view-analytics-btn svg {
  width: 18px;
  height: 18px;
}

.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
}

.chart-card {
  background: var(--color-white);
  border-radius: 24px;
  padding: 32px;
  box-shadow: var(--card-shadow);
  opacity: 0;
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
  color: var(--color-ink);
}

.chart-period {
  font-size: 12px;
  color: var(--color-text);
  background: var(--color-cream);
  padding: 6px 16px;
  border-radius: 100px;
}

.chart-container {
  width: 100%;
  height: 220px;
}

/* Notice Section */
.notice-section {
  padding: 80px;
  background: linear-gradient(180deg, transparent 0%, rgba(245, 241, 232, 0.5) 100%);
}

.notice-header {
  margin-bottom: 40px;
}

.notice-badge {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  padding: 12px 24px;
  background: var(--color-white);
  border-radius: 100px;
  box-shadow: var(--card-shadow);
  font-weight: 500;
  color: var(--color-ink);
}

.notice-badge svg {
  width: 20px;
  height: 20px;
  stroke: var(--color-terracotta);
}

.notice-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.notice-card {
  background: var(--color-white);
  border-radius: 24px;
  padding: 32px;
  box-shadow: var(--card-shadow);
  transition: all 0.3s ease;
  opacity: 0;
  cursor: pointer;
}

.notice-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--card-shadow-hover);
}

.notice-date {
  font-size: 12px;
  color: var(--color-terracotta);
  margin-bottom: 12px;
  font-weight: 500;
}

.notice-title {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 500;
  color: var(--color-ink);
  margin-bottom: 12px;
  line-height: 1.4;
}

.notice-desc {
  font-size: 14px;
  color: var(--color-text);
  line-height: 1.7;
  margin: 0 0 20px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.notice-cta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--color-sage);
  font-weight: 500;
  transition: gap 0.3s ease;
}

.notice-card:hover .notice-cta {
  gap: 12px;
}

.notice-cta svg {
  width: 16px;
  height: 16px;
}

.notice-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  background: var(--color-cream);
  border-radius: 24px;
  color: var(--color-text-muted);
  gap: 16px;
}

.notice-empty svg {
  width: 60px;
  height: 60px;
  opacity: 0.5;
}

/* Quick Add Dialog */
:deep(.quick-add-dialog .el-dialog) {
  border-radius: 20px;
  overflow: hidden;
}

:deep(.quick-add-dialog .el-dialog__header) {
  background: linear-gradient(135deg, var(--color-sage-light), var(--color-sage));
  padding: 20px 24px;
  margin: 0;
}

:deep(.quick-add-dialog .el-dialog__title) {
  color: white;
  font-weight: 600;
  font-size: 18px;
}

:deep(.quick-add-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

:deep(.quick-add-dialog .el-dialog__body) {
  padding: 32px 24px;
}

:deep(.quick-add-dialog .el-form-item__label) {
  font-weight: 500;
  color: var(--color-ink);
}

/* Responsive */
@media (max-width: 1200px) {
  .hero-section { grid-template-columns: 1fr; padding: 120px 60px 80px; text-align: center; }
  .hero-visual { order: -1; }
  .hero-cta { justify-content: center; }
  .scroll-indicator { display: none; }
  .features-grid { grid-template-columns: repeat(2, 1fr); }
  .report-layout { grid-template-columns: 1fr; }
  .notice-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
  .hero-section { padding: 100px 24px 60px; }
  .features-section { padding: 60px 24px; }
  .features-grid { grid-template-columns: 1fr; }
  .report-section { padding: 60px 24px; }
  .charts-section { padding: 60px 24px; }
  .charts-grid { grid-template-columns: 1fr; }
  .notice-section { padding: 60px 24px; }
  .notice-grid { grid-template-columns: 1fr; }
  .globe-container { width: 280px; height: 280px; }
  .globe-core { width: 100px; height: 100px; }
  .globe-core svg { width: 40px; height: 40px; }
  .ring-1 { width: 180px; height: 180px; }
  .ring-2 { width: 220px; height: 130px; }
  .ring-3 { width: 260px; height: 150px; }
  .orbit-1 { width: 200px; height: 200px; }
  .orbit-2 { width: 240px; height: 240px; }
  .orbit-3 { width: 280px; height: 280px; }
  .hero-stats { flex-wrap: wrap; justify-content: center; }
  .stat-card { padding: 16px 20px; }
  .stat-value { font-size: 28px; }
}
</style>
