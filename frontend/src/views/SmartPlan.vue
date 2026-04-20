<template>
  <div class="plan-container" ref="planContainer">
    <!-- Decorative blob -->
    <div class="blob blob-1"></div>

    <!-- Compact Header -->
    <div class="plan-header">
      <h1 class="plan-title">智能健康计划</h1>
      <p class="plan-subtitle">AI 量身定制，专属您的7天健康方案</p>
    </div>

    <!-- 可折叠输入区域 -->
    <div class="generator-section" v-if="!currentPlan">
      <!-- 折叠触发器 -->
      <div class="generator-collapsible" :class="{ collapsed: isGeneratorCollapsed }">
        <div class="collapsible-header" @click="toggleGenerator">
          <div class="header-left">
            <div class="header-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
              </svg>
            </div>
            <div class="header-text">
              <span class="header-title">生成新计划</span>
              <span class="header-hint">{{ isGeneratorCollapsed ? '点击展开输入目标' : '点击收起' }}</span>
            </div>
          </div>
          <div class="collapse-indicator" :class="{ rotated: isGeneratorCollapsed }">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="18 15 12 9 6 15"/>
            </svg>
          </div>
        </div>

        <!-- 可折叠内容 -->
        <div class="collapsible-content" ref="collapsibleContent">
          <div class="generator-card">
            <div class="card-body">
              <p class="card-desc">AI 将根据您的身体指标和目标，为您量身定制7天饮食与运动计划</p>

              <div class="input-group">
                <textarea
                  v-model="goal"
                  class="goal-input"
                  placeholder="描述您的健康目标... 例如：我想在30天内减重5斤，主要想减肚子，希望改善睡眠质量..."
                  :disabled="loading"
                  rows="3"
                ></textarea>
              </div>

              <button class="btn btn-primary btn-generate" @click="generatePlan" :disabled="loading">
                <span v-if="!loading" class="btn-content">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
                  </svg>
                  立即生成
                </span>
                <span v-else class="btn-loading">
                  <svg class="spin" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M21 12a9 9 0 11-6.219-8.56"/>
                  </svg>
                  生成中...
                </span>
              </button>

              <!-- 进度展示 -->
              <div v-if="loading" class="progress-section">
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: percentage + '%' }"></div>
                </div>
                <div class="progress-info">
                  <span class="progress-text">{{ progressText }}</span>
                  <span class="progress-percent">{{ percentage }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 历史计划 - 始终显示 -->
      <div class="history-section" v-if="historyPlans.length > 0 && !loading">
        <div class="section-header">
          <h3 class="section-title">历史计划</h3>
          <span class="section-count">{{ historyPlans.length }} 个计划</span>
        </div>

        <div class="history-grid">
          <div
            v-for="(plan, index) in historyPlans"
            :key="index"
            class="history-card"
            :style="{ animationDelay: index * 0.08 + 's' }"
            @click="viewPlan(plan)"
          >
            <div class="history-card-header">
              <span class="history-date">{{ plan.createTime }}</span>
              <span class="history-arrow">→</span>
            </div>
            <div class="history-goal">{{ plan.goal }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 计划展示 -->
    <div class="result-section" v-else>
      <div class="result-header">
        <button class="btn btn-ghost back-btn" @click="currentPlan = null; isGeneratorCollapsed = true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="m15 18-6-6 6-6"/>
          </svg>
          返回
        </button>
        <div class="result-title">
          <h2>健康目标</h2>
          <p>{{ currentPlan.goal }}</p>
        </div>
      </div>

      <!-- AI 建议 - 紧凑显示 -->
      <div class="advice-card compact">
        <div class="advice-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
          </svg>
        </div>
        <div class="advice-content">
          <h4>AI 健康建议</h4>
          <p>{{ parsedContent.advice }}</p>
        </div>
      </div>

      <!-- Tab 切换 -->
      <div class="plan-tabs">
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'diet' }"
          @click="activeTab = 'diet'"
        >
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M18 8h1a4 4 0 0 1 0 8h-1"/>
            <path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"/>
            <line x1="6" y1="1" x2="6" y2="4"/>
            <line x1="10" y1="1" x2="10" y2="4"/>
            <line x1="14" y1="1" x2="14" y2="4"/>
          </svg>
          7天饮食
        </button>
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'sport' }"
          @click="activeTab = 'sport'"
        >
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <polyline points="12 6 12 12 16 14"/>
          </svg>
          7天运动
        </button>
      </div>

      <!-- 饮食计划 - 紧凑卡片 -->
      <div v-if="activeTab === 'diet'" class="plan-cards compact-cards">
        <div
          v-for="day in parsedContent.dietPlan"
          :key="day.day"
          class="day-card compact"
        >
          <div class="day-header compact">
            <span class="day-number">第 {{ day.day }} 天</span>
            <span class="day-badge">{{ getDayLabel(day.day) }}</span>
          </div>
          <div class="meal-list compact">
            <div class="meal-row">
              <span class="meal-tag breakfast">早</span>
              <span class="meal-text">{{ day.breakfast }}</span>
            </div>
            <div class="meal-row">
              <span class="meal-tag lunch">午</span>
              <span class="meal-text">{{ day.lunch }}</span>
            </div>
            <div class="meal-row">
              <span class="meal-tag dinner">晚</span>
              <span class="meal-text">{{ day.dinner }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 运动计划 - 紧凑时间线 -->
      <div v-if="activeTab === 'sport'" class="sport-grid">
        <div
          v-for="day in parsedContent.sportPlan"
          :key="day.day"
          class="sport-card compact"
        >
          <div class="sport-day-badge">第 {{ day.day }} 天</div>
          <div class="sport-info">
            <div class="sport-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
              </svg>
            </div>
            <div class="sport-details">
              <p class="sport-text">{{ day.content }}</p>
              <span class="sport-duration">{{ day.duration }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'
import anime from 'animejs'

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}
const goal = ref('')
const loading = ref(false)
const historyPlans = ref([])
const currentPlan = ref(null)
const activeTab = ref('diet')
const isGeneratorCollapsed = ref(false)
const collapsibleContent = ref(null)
const planContainer = ref(null)

// 进度条相关
const percentage = ref(0)
const progressText = ref('AI 正在分析您的健康数据...')
let timer = null

const parsedContent = computed(() => {
    if (!currentPlan.value || !currentPlan.value.content) return {}
    try {
        return JSON.parse(currentPlan.value.content)
    } catch (e) {
        console.error('JSON Parse Error', e)
        return { dietPlan: [], sportPlan: [], advice: '解析数据出错' }
    }
})

const getDayLabel = (day) => {
    const labels = ['启程', '适应', '进阶', '挑战', '突破', '巩固', '完成']
    return labels[day - 1] || '第' + day + '天'
}

onMounted(() => {
    loadHistory()
    animateEntrance()
    // 默认折叠
    if (collapsibleContent.value) {
        collapsibleContent.value.style.maxHeight = '0px'
        collapsibleContent.value.style.opacity = '0'
    }
})

onUnmounted(() => {
    if (timer) clearInterval(timer)
})

const toggleGenerator = () => {
    if (loading.value) return

    const content = collapsibleContent.value
    if (!content) return

    if (isGeneratorCollapsed.value) {
        // 展开动画
        content.style.maxHeight = content.scrollHeight + 'px'
        content.style.opacity = '1'
        anime({
            targets: content,
            opacity: [0, 1],
            translateY: [-20, 0],
            duration: 400,
            easing: 'easeOutQuart'
        })
    } else {
        // 折叠动画
        content.style.maxHeight = '0px'
        content.style.opacity = '0'
        anime({
            targets: content,
            opacity: 0,
            translateY: -10,
            duration: 300,
            easing: 'easeInQuart'
        })
    }
    isGeneratorCollapsed.value = !isGeneratorCollapsed.value
}

const animateEntrance = () => {
    anime.timeline({ easing: 'easeOutQuart' })
        .add({
            targets: '.plan-header',
            opacity: [0, 1],
            translateY: [20, 0],
            duration: 600
        })
        .add({
            targets: '.collapsible-header',
            opacity: [0, 1],
            translateY: [20, 0],
            scale: [0.95, 1],
            duration: 600
        }, '-=300')
        .add({
            targets: '.history-card',
            opacity: [0, 1],
            translateY: [20, 0],
            delay: anime.stagger(60),
            duration: 500,
            easing: 'easeOutQuart'
        }, '-=200')
}

const loadHistory = () => {
    request.get(`/plan/list/${user.id}`).then(res => {
        if (res.code === '200') {
            historyPlans.value = res.data || []
        }
    })
}

const startFakeProgress = () => {
    percentage.value = 0
    progressText.value = 'AI 正在分析您的健康数据...'

    timer = setInterval(() => {
        if (percentage.value < 30) {
            percentage.value += 5
        } else if (percentage.value < 60) {
            progressText.value = 'AI 正在制定饮食方案...'
            percentage.value += 2
        } else if (percentage.value < 80) {
            progressText.value = 'AI 正在规划运动课程...'
            percentage.value += 1
        } else if (percentage.value < 95) {
            progressText.value = '正在整理最终报告...'
            percentage.value += 0.5
        }
    }, 500)
}

const generatePlan = () => {
    if (!goal.value.trim()) {
        ElMessage.warning('请输入您的目标')
        return
    }

    loading.value = true
    startFakeProgress()

    request.post('/plan/generate', {
        userId: user.id,
        goal: goal.value
    }).then(res => {
        if (res.code === '200') {
            percentage.value = 100
            progressText.value = '生成完成！'
            ElMessage.success('计划生成成功！')

            const newPlan = {
                goal: goal.value,
                content: res.data,
                createTime: '刚刚生成'
            }
            setTimeout(() => {
                loading.value = false
                currentPlan.value = newPlan
                loadHistory()
                if (timer) clearInterval(timer)
                animateResultEntrance()
            }, 500)
        } else {
            loading.value = false
            ElMessage.error(res.msg || '生成失败，请重试')
            if (timer) clearInterval(timer)
        }
    }).catch(err => {
        loading.value = false
        if (timer) clearInterval(timer)
        ElMessage.error('请求超时或出错，请稍后重试')
    })
}

const animateResultEntrance = () => {
    anime.timeline({ easing: 'easeOutQuart' })
        .add({
            targets: '.result-header',
            opacity: [0, 1],
            translateY: [20, 0],
            duration: 500
        })
        .add({
            targets: '.advice-card',
            opacity: [0, 1],
            translateY: [20, 0],
            scale: [0.95, 1],
            duration: 500
        }, '-=300')
        .add({
            targets: '.plan-tabs',
            opacity: [0, 1],
            translateY: [15, 0],
            duration: 400
        }, '-=200')
        .add({
            targets: '.day-card, .sport-card',
            opacity: [0, 1],
            translateY: [20, 0],
            scale: [0.95, 1],
            delay: anime.stagger(50),
            duration: 500,
            easing: 'easeOutQuart'
        }, '-=200')
}

const viewPlan = (plan) => {
    currentPlan.value = plan
    activeTab.value = 'diet'
    setTimeout(() => animateResultEntrance(), 100)
}
</script>

<style scoped>
.plan-container {
    padding: 0 40px 60px;
    max-width: 1400px;
    margin: 0 auto;
    position: relative;
}

/* Decorative Blob */
.blob {
    position: fixed;
    border-radius: 50%;
    pointer-events: none;
    z-index: 0;
}

.blob-1 {
    top: -100px;
    right: -150px;
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, rgba(196, 125, 94, 0.12) 0%, transparent 70%);
}

/* Compact Header */
.plan-header {
    padding: 32px 0 28px;
    text-align: center;
    position: relative;
    z-index: 1;
}

.plan-title {
    font-family: var(--font-display);
    font-size: 32px;
    font-weight: 500;
    color: var(--color-ink);
    margin-bottom: 8px;
}

.plan-subtitle {
    font-size: 14px;
    color: var(--color-text);
    margin: 0;
}

/* Generator Collapsible */
.generator-section {
    position: relative;
    z-index: 1;
}

.generator-collapsible {
    margin-bottom: 32px;
}

.collapsible-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 24px;
    background: var(--color-white);
    border-radius: 16px;
    box-shadow: var(--card-shadow);
    cursor: pointer;
    transition: all 0.3s ease;
}

.collapsible-header:hover {
    box-shadow: var(--card-shadow-hover);
    transform: translateY(-2px);
}

.collapsed .collapsible-header {
    background: linear-gradient(135deg, var(--color-sage-light), var(--color-sage));
}

.collapsed .collapsible-header .header-icon {
    background: rgba(255,255,255,0.2);
}

.collapsed .collapsible-header .header-icon svg {
    stroke: white;
}

.collapsed .collapsible-header .header-title,
.collapsed .collapsible-header .header-hint {
    color: white;
}

.header-left {
    display: flex;
    align-items: center;
    gap: 16px;
}

.header-icon {
    width: 44px;
    height: 44px;
    background: var(--color-cream);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.header-icon svg {
    width: 22px;
    height: 22px;
    stroke: var(--color-sage);
    transition: all 0.3s ease;
}

.header-text {
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.header-title {
    font-family: var(--font-display);
    font-size: 18px;
    font-weight: 600;
    color: var(--color-ink);
}

.header-hint {
    font-size: 12px;
    color: var(--color-text-muted);
}

.collapse-indicator {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.collapse-indicator svg {
    width: 20px;
    height: 20px;
    stroke: var(--color-text-muted);
}

.collapse-indicator.rotated {
    transform: rotate(180deg);
}

.collapsed .collapse-indicator svg {
    stroke: white;
}

/* Collapsible Content */
.collapsible-content {
    max-height: 500px;
    overflow: hidden;
    transition: max-height 0.4s cubic-bezier(0.4, 0, 0.2, 1),
                opacity 0.3s ease;
    margin-top: 16px;
}

/* Generator Card */
.generator-card {
    background: var(--color-white);
    border-radius: 20px;
    padding: 32px;
    box-shadow: var(--card-shadow);
    animation: none;
}

.card-body {
    text-align: center;
}

.card-desc {
    font-size: 14px;
    color: var(--color-text);
    margin-bottom: 24px;
    line-height: 1.6;
}

.input-group {
    margin-bottom: 20px;
}

.goal-input {
    width: 100%;
    padding: 16px 20px;
    border: 2px solid var(--color-sand);
    border-radius: 16px;
    font-size: 14px;
    font-family: var(--font-body);
    line-height: 1.6;
    resize: none;
    transition: all 0.3s ease;
    background: var(--color-cream);
}

.goal-input:focus {
    outline: none;
    border-color: var(--color-sage);
    background: var(--color-white);
    box-shadow: 0 0 0 4px rgba(139, 154, 125, 0.15);
}

.goal-input::placeholder {
    color: var(--color-text-muted);
}

/* Button */
.btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 12px 28px;
    border-radius: 100px;
    font-size: 14px;
    font-family: var(--font-body);
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    border: none;
}

.btn svg {
    width: 18px;
    height: 18px;
}

.btn-primary {
    background: linear-gradient(135deg, var(--color-sage), var(--color-sage-dark));
    color: white;
}

.btn-primary:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(139, 154, 125, 0.4);
}

.btn-primary:disabled {
    opacity: 0.7;
    cursor: not-allowed;
}

.btn-ghost {
    background: transparent;
    color: var(--color-text);
    border: 1px solid var(--color-sand);
}

.btn-ghost:hover {
    background: var(--color-cream);
    border-color: var(--color-sage);
}

.btn-content, .btn-loading {
    display: flex;
    align-items: center;
    gap: 8px;
}

.spin {
    animation: spin 1s linear infinite;
}

@keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
}

/* Progress */
.progress-section {
    margin-top: 20px;
    text-align: left;
}

.progress-bar {
    height: 6px;
    background: var(--color-cream);
    border-radius: 100px;
    overflow: hidden;
    margin-bottom: 8px;
}

.progress-fill {
    height: 100%;
    background: linear-gradient(90deg, var(--color-sage), var(--color-terracotta));
    border-radius: 100px;
    transition: width 0.3s ease;
}

.progress-info {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
}

.progress-text {
    color: var(--color-text);
}

.progress-percent {
    color: var(--color-sage);
    font-weight: 600;
}

/* History Section */
.history-section {
    margin-top: 8px;
}

.section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
}

.section-title {
    font-family: var(--font-display);
    font-size: 20px;
    font-weight: 500;
    color: var(--color-ink);
    margin: 0;
}

.section-count {
    font-size: 12px;
    color: var(--color-text-muted);
    background: var(--color-cream);
    padding: 4px 12px;
    border-radius: 100px;
}

/* History Grid */
.history-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 12px;
}

.history-card {
    background: var(--color-white);
    border-radius: 14px;
    padding: 16px 20px;
    box-shadow: var(--card-shadow);
    cursor: pointer;
    transition: all 0.3s ease;
    animation: fadeInUp 0.5s ease-out both;
}

.history-card:hover {
    transform: translateY(-4px) scale(1.02);
    box-shadow: var(--card-shadow-hover);
}

.history-card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.history-date {
    font-size: 11px;
    color: var(--color-text-muted);
}

.history-arrow {
    font-size: 14px;
    color: var(--color-sage);
    transition: transform 0.3s ease;
}

.history-card:hover .history-arrow {
    transform: translateX(4px);
}

.history-goal {
    font-size: 14px;
    color: var(--color-ink);
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

/* Result Section */
.result-section {
    position: relative;
    z-index: 1;
}

.result-header {
    display: flex;
    align-items: flex-start;
    gap: 20px;
    margin-bottom: 20px;
}

.back-btn {
    padding: 8px 16px;
    flex-shrink: 0;
}

.back-btn svg {
    width: 16px;
    height: 16px;
}

.result-title h2 {
    font-family: var(--font-display);
    font-size: 14px;
    font-weight: 500;
    color: var(--color-text-muted);
    margin-bottom: 2px;
}

.result-title p {
    font-family: var(--font-display);
    font-size: 24px;
    font-weight: 500;
    color: var(--color-ink);
    margin: 0;
}

/* Advice Card */
.advice-card {
    display: flex;
    gap: 16px;
    background: linear-gradient(135deg, rgba(139, 154, 125, 0.1), rgba(196, 125, 94, 0.1));
    border-radius: 16px;
    padding: 20px 24px;
    margin-bottom: 20px;
    border: 1px solid var(--color-sand);
}

.advice-card.compact {
    padding: 16px 20px;
}

.advice-icon {
    width: 40px;
    height: 40px;
    background: linear-gradient(135deg, var(--color-terracotta), var(--color-sand));
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.advice-icon svg {
    width: 20px;
    height: 20px;
    stroke: white;
}

.advice-content h4 {
    font-family: var(--font-display);
    font-size: 14px;
    font-weight: 500;
    color: var(--color-ink);
    margin-bottom: 4px;
}

.advice-content p {
    font-size: 13px;
    color: var(--color-text);
    line-height: 1.6;
    margin: 0;
}

/* Tabs */
.plan-tabs {
    display: flex;
    gap: 10px;
    margin-bottom: 16px;
}

.tab-btn {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 20px;
    background: var(--color-white);
    border: 2px solid var(--color-sand);
    border-radius: 100px;
    font-size: 13px;
    font-family: var(--font-body);
    font-weight: 500;
    color: var(--color-text);
    cursor: pointer;
    transition: all 0.3s ease;
}

.tab-btn svg {
    width: 16px;
    height: 16px;
}

.tab-btn:hover {
    border-color: var(--color-sage);
    color: var(--color-sage);
}

.tab-btn.active {
    background: var(--color-sage);
    border-color: var(--color-sage);
    color: white;
}

/* Compact Day Cards */
.plan-cards.compact-cards {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 12px;
}

.day-card.compact {
    background: var(--color-white);
    border-radius: 14px;
    overflow: hidden;
    box-shadow: var(--card-shadow);
    transition: all 0.3s ease;
}

.day-card.compact:hover {
    transform: translateY(-3px);
    box-shadow: var(--card-shadow-hover);
}

.day-header.compact {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: linear-gradient(135deg, var(--color-sage-light), var(--color-sage));
}

.day-number {
    font-family: var(--font-display);
    font-size: 15px;
    font-weight: 600;
    color: white;
}

.day-badge {
    font-size: 11px;
    padding: 3px 10px;
    background: rgba(255,255,255,0.2);
    border-radius: 100px;
    color: white;
}

.meal-list.compact {
    padding: 12px 16px;
}

.meal-row {
    display: flex;
    gap: 10px;
    padding: 6px 0;
    border-bottom: 1px solid var(--color-cream);
}

.meal-row:last-child {
    border-bottom: none;
    padding-bottom: 0;
}

.meal-row:first-child {
    padding-top: 0;
}

.meal-tag {
    width: 22px;
    height: 22px;
    border-radius: 6px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 11px;
    font-weight: 600;
    flex-shrink: 0;
}

.meal-tag.breakfast {
    background: rgba(251, 191, 36, 0.15);
    color: #fbbf24;
}

.meal-tag.lunch {
    background: rgba(139, 154, 125, 0.15);
    color: var(--color-sage);
}

.meal-tag.dinner {
    background: rgba(196, 125, 94, 0.15);
    color: var(--color-terracotta);
}

.meal-text {
    font-size: 12px;
    color: var(--color-ink);
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

/* Sport Grid */
.sport-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 12px;
}

.sport-card.compact {
    background: var(--color-white);
    border-radius: 14px;
    overflow: hidden;
    box-shadow: var(--card-shadow);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
}

.sport-card.compact:hover {
    transform: translateY(-3px);
    box-shadow: var(--card-shadow-hover);
}

.sport-day-badge {
    font-family: var(--font-display);
    font-size: 13px;
    font-weight: 600;
    color: white;
    background: linear-gradient(135deg, var(--color-terracotta), #D4A574);
    padding: 10px 16px;
    text-align: center;
}

.sport-info {
    display: flex;
    gap: 12px;
    padding: 14px;
    flex: 1;
}

.sport-icon {
    width: 32px;
    height: 32px;
    background: var(--color-cream);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.sport-icon svg {
    width: 16px;
    height: 16px;
    stroke: var(--color-terracotta);
}

.sport-details {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.sport-text {
    font-size: 12px;
    color: var(--color-ink);
    line-height: 1.5;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.sport-duration {
    font-size: 11px;
    color: var(--color-text-muted);
}

/* Animations */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* Responsive */
@media (max-width: 768px) {
    .plan-container {
        padding: 0 20px 40px;
    }

    .plan-header {
        padding: 24px 0 20px;
    }

    .plan-title {
        font-size: 26px;
    }

    .plan-cards.compact-cards {
        grid-template-columns: 1fr;
    }

    .sport-grid {
        grid-template-columns: 1fr;
    }

    .history-grid {
        grid-template-columns: 1fr;
    }

    .result-header {
        flex-direction: column;
        gap: 12px;
    }

    .plan-tabs {
        flex-direction: column;
    }

    .tab-btn {
        width: 100%;
        justify-content: center;
    }
}
</style>
