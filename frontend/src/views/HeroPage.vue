<template>
  <div class="hero-container" ref="heroContainer">
    <!-- Hero Section with Parallax -->
    <section class="hero-section" ref="heroSection">
      <div class="hero-bg">
        <div class="bg-shape shape-1" data-speed="0.3"></div>
        <div class="bg-shape shape-2" data-speed="0.5"></div>
        <div class="bg-shape shape-3" data-speed="0.7"></div>
        <div class="bg-grid" data-speed="0.2"></div>
      </div>

      <div class="hero-content" data-speed="0.4">
        <div class="hero-badge" data-speed="0.6">
          <span class="badge-dot"></span>
          智能健康管理新时代
        </div>

        <h1 class="hero-title">
          <span class="title-line" data-speed="0.8">用数据记录</span>
          <span class="title-line accent" data-speed="0.9">用科学守护</span>
          <span class="title-line" data-speed="1.0">每一天的健康</span>
        </h1>

        <p class="hero-subtitle" data-speed="0.5">
          智能健康管理系统，为您提供全方位的健康监测、<br/>
          数据分析与个性化建议，让健康管理变得简单高效。
        </p>

        <div class="hero-actions" data-speed="0.6">
          <button class="btn-primary" @click="enterApp">
            <span>开启健康之旅</span>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </button>
          <button class="btn-secondary" @click="scrollToFeatures">
            <span>了解更多</span>
          </button>
        </div>
      </div>

      <!-- Stats Section - Sticky -->
      <div class="stats-wrapper">
        <div class="stats-sticky">
          <div class="stats-grid" ref="statsGrid">
            <div class="stat-item" v-for="(stat, index) in stats" :key="index">
              <div class="stat-bar-wrapper">
                <div class="stat-bar-fill" :class="`stat-${index}`" :style="{ '--target-width': stat.targetWidth }"></div>
              </div>
              <div class="stat-number">
                <span class="stat-value" :data-target="stat.target">{{ stat.display }}</span>
                <span class="stat-suffix">{{ stat.suffix }}</span>
              </div>
              <div class="stat-label">{{ stat.label }}</div>
              <div class="stat-dots">
                <span v-for="i in 3" :key="i" class="stat-dot" :class="{ active: i <= stat.dots }"></span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section - Sticky -->
    <section class="features-section" ref="featuresSection">
      <div class="features-sticky">
        <div class="section-header">
          <span class="section-label">核心功能</span>
          <h2 class="section-title">全方位守护您的健康</h2>
        </div>

        <div class="features-grid" ref="featuresGrid">
          <div
            class="feature-card"
            v-for="(feature, index) in features"
            :key="index"
            :style="{ '--delay': index * 0.1 + 's' }"
          >
            <div class="feature-icon" :style="{ background: feature.bgColor }">
              <svg viewBox="0 0 24 24" fill="none" :stroke="feature.color" stroke-width="1.5" v-html="feature.icon"></svg>
            </div>
            <div class="feature-number">{{ String(index + 1).padStart(2, '0') }}</div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-desc">{{ feature.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- About Section -->
    <section class="about-section" ref="aboutSection">
      <div class="about-content">
        <div class="about-text">
          <span class="section-label">关于系统</span>
          <h2 class="section-title">科学健康管理</h2>
          <p class="about-desc">
            我们相信，健康是人生最宝贵的财富。通过智能数据分析和个性化方案，
            帮助您更好地了解自己的身体状态，建立健康的生活方式。
          </p>

          <div class="highlights">
            <div class="highlight-item">
              <div class="highlight-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
                </svg>
              </div>
              <div class="highlight-content">
                <h4>实时监测</h4>
                <p>多维度健康数据追踪</p>
              </div>
            </div>
            <div class="highlight-item">
              <div class="highlight-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                </svg>
              </div>
              <div class="highlight-content">
                <h4>AI 智能分析</h4>
                <p>专业健康建议</p>
              </div>
            </div>
            <div class="highlight-item">
              <div class="highlight-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M12 2L2 7l10 5 10-5-10-5z"/>
                  <path d="M2 17l10 5 10-5"/>
                  <path d="M2 12l10 5 10-5"/>
                </svg>
              </div>
              <div class="highlight-content">
                <h4>个性化方案</h4>
                <p>量身定制健康计划</p>
              </div>
            </div>
          </div>
        </div>

        <div class="about-visual">
          <div class="visual-container">
            <div class="visual-ring ring-1"></div>
            <div class="visual-ring ring-2"></div>
            <div class="visual-ring ring-3"></div>
            <div class="visual-core">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section" ref="ctaSection">
      <div class="cta-bg-element"></div>
      <div class="cta-content">
        <h2 class="cta-title">准备好开始您的健康之旅了吗？</h2>
        <p class="cta-desc">加入我们，探索科学健康管理的无限可能</p>
        <button class="btn-primary large" @click="enterApp">
          <span>立即开始</span>
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
      </div>
    </section>

    <!-- Footer -->
    <footer class="hero-footer">
      <div class="footer-logo">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
        </svg>
        <span>健康生活</span>
      </div>
      <p class="footer-copy">© 2026 健康生活管理系统. 守护您的每一次心跳</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import anime from 'animejs'

const router = useRouter()
const heroContainer = ref(null)
const heroSection = ref(null)
const featuresSection = ref(null)
const aboutSection = ref(null)
const ctaSection = ref(null)
const statsGrid = ref(null)
const featuresGrid = ref(null)

let ticking = false
let scrollY = 0

const stats = reactive([
  { display: 0, target: 7, suffix: '天', label: '持续记录天数', targetWidth: '100%', dots: 3 },
  { display: 0, target: 186, suffix: '+', label: '健康指标追踪', targetWidth: '100%', dots: 2 },
  { display: 0, target: 98, suffix: '%', label: '用户满意度', targetWidth: '100%', dots: 3 },
  { display: 0, target: 24, suffix: '/7', label: '全天候守护', targetWidth: '100%', dots: 1 }
])

const features = [
  { title: '身体指标监测', desc: '记录血压、血糖、心率等核心指标，实时追踪健康状态变化', color: '#C47D5E', bgColor: 'rgba(196, 125, 94, 0.1)', icon: '<path d="M22 12h-4l-3 9L9 3l-3 9H2"/>' },
  { title: '运动数据管理', desc: '多种运动类型支持，精确记录运动时长与卡路里消耗', color: '#8B9A7D', bgColor: 'rgba(139, 154, 125, 0.1)', icon: '<circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/>' },
  { title: '饮食营养分析', desc: '科学分析每日摄入，计算营养配比，制定专属饮食方案', color: '#D4A574', bgColor: 'rgba(212, 165, 116, 0.1)', icon: '<path d="M18 8h1a4 4 0 0 1 0 8h-1"/><path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"/>' },
  { title: '睡眠质量追踪', desc: '深度分析睡眠时长与质量，提供科学的睡眠改善建议', color: '#7B68A6', bgColor: 'rgba(123, 104, 166, 0.1)', icon: '<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>' },
  { title: 'AI 健康报告', desc: '智能分析健康数据，生成个性化健康报告与改善方案', color: '#5BA3A3', bgColor: 'rgba(91, 163, 163, 0.1)', icon: '<polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>' },
  { title: '智能健康计划', desc: 'AI 量身定制运动与饮食计划，7天见证改变', color: '#C47D5E', bgColor: 'rgba(196, 125, 94, 0.1)', icon: '<path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/>' }
]

// Parallax effect - the key to that "heavy" feeling
const updateParallax = () => {
  scrollY = window.scrollY
  const viewportHeight = window.innerHeight

  // Hero parallax elements
  const heroContent = document.querySelector('.hero-content')
  const heroBg = document.querySelector('.hero-bg')

  if (heroContent && scrollY < viewportHeight) {
    // Slower movement for content - creates "weight" feeling
    heroContent.style.transform = `translateY(${scrollY * 0.3}px)`
    heroContent.style.opacity = 1 - (scrollY / viewportHeight) * 0.8
  }

  if (heroBg && scrollY < viewportHeight * 2) {
    // Background moves even slower
    const shapes = heroBg.querySelectorAll('.bg-shape')
    shapes.forEach(shape => {
      const speed = parseFloat(shape.dataset.speed) || 0.3
      shape.style.transform = `translateY(${scrollY * speed}px)`
    })

    const grid = heroBg.querySelector('.bg-grid')
    if (grid) {
      grid.style.transform = `translateY(${scrollY * 0.15}px)`
    }
  }

  // Stats animation when entering viewport
  if (statsGrid.value) {
    const statsRect = statsGrid.value.getBoundingClientRect()
    const statsProgress = Math.max(0, Math.min(1, (viewportHeight - statsRect.top) / viewportHeight))

    if (statsProgress > 0.3 && statsProgress < 1) {
      statsGrid.value.querySelectorAll('.stat-bar-fill').forEach(bar => {
        bar.style.width = bar.style.getPropertyValue('--target-width')
      })

      stats.forEach((stat, index) => {
        if (stat.display === 0) {
          animateStatValue(stat, index)
        }
      })
    }
  }

  // Features animation
  if (featuresGrid.value) {
    const featuresRect = featuresGrid.value.getBoundingClientRect()
    if (featuresRect.top < viewportHeight * 0.85) {
      featuresGrid.value.querySelectorAll('.feature-card').forEach((card, index) => {
        setTimeout(() => {
          card.style.opacity = '1'
          card.style.transform = 'translateY(0) scale(1)'
        }, index * 100)
      })
    }
  }
}

const animateStatValue = (stat, index) => {
  const duration = 2000
  const start = performance.now()
  const easeOutCubic = t => 1 - Math.pow(1 - t, 3)

  const step = (currentTime) => {
    const elapsed = currentTime - start
    const progress = Math.min(elapsed / duration, 1)
    const eased = easeOutCubic(progress)
    stat.display = Math.round(stat.target * eased)

    if (progress < 1) {
      requestAnimationFrame(step)
    } else {
      stat.display = stat.target
    }
  }

  requestAnimationFrame(step)
}

const scrollToFeatures = () => {
  featuresSection.value?.scrollIntoView({ behavior: 'smooth' })
}

const enterApp = () => {
  anime({
    targets: 'body',
    opacity: [1, 0],
    duration: 600,
    easing: 'easeInQuad',
    complete: () => {
      router.push('/login')
    }
  })
}

// Initial animations
const animateEntrance = () => {
  anime.timeline({ easing: 'easeOutQuart' })
    .add({
      targets: '.hero-badge',
      opacity: [0, 1],
      translateY: [30, 0],
      duration: 800
    })
    .add({
      targets: '.title-line',
      opacity: [0, 1],
      translateY: [80, 0],
      rotateX: [-30, 0],
      delay: anime.stagger(120),
      duration: 1000
    }, '-=400')
    .add({
      targets: '.hero-subtitle',
      opacity: [0, 1],
      translateY: [30, 0],
      duration: 800
    }, '-=600')
    .add({
      targets: '.hero-actions',
      opacity: [0, 1],
      translateY: [30, 0],
      scale: [0.9, 1],
      duration: 800
    }, '-=400')
    .add({
      targets: '.stat-item',
      opacity: [0, 1],
      translateY: [60, 0],
      scale: [0.85, 1],
      delay: anime.stagger(120, { start: 1000 }),
      duration: 900,
      easing: 'easeOutBack'
    })
}

const handleScroll = () => {
  if (!ticking) {
    requestAnimationFrame(() => {
      updateParallax()
      ticking = false
    })
    ticking = true
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true })
  updateParallax()
  setTimeout(animateEntrance, 200)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.hero-container {
  position: relative;
  background: var(--color-cream, #F5F1E8);
  overflow-x: hidden;
}

/* Hero Section */
.hero-section {
  position: relative;
  min-height: 200vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding-top: 100vh;
}

.hero-bg {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 0;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  will-change: transform;
}

.shape-1 {
  top: -200px;
  right: -150px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(139, 154, 125, 0.15) 0%, transparent 70%);
}

.shape-2 {
  top: 30%;
  left: -200px;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(196, 125, 94, 0.1) 0%, transparent 70%);
}

.shape-3 {
  bottom: 20%;
  right: -100px;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(212, 165, 116, 0.1) 0%, transparent 70%);
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(139, 154, 125, 0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(139, 154, 125, 0.04) 1px, transparent 1px);
  background-size: 80px 80px;
  will-change: transform;
}

/* Hero Content - Fixed initially */
.hero-content {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  max-width: 900px;
  width: 90%;
  z-index: 1;
  will-change: transform, opacity;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(139, 154, 125, 0.2);
  border-radius: 100px;
  font-size: 13px;
  color: var(--color-sage-dark, #6B7A5D);
  margin-bottom: 32px;
  opacity: 0;
  will-change: transform, opacity;
}

.badge-dot {
  width: 8px;
  height: 8px;
  background: var(--color-sage, #8B9A7D);
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.3); opacity: 0.7; }
}

.hero-title {
  display: flex;
  flex-direction: column;
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: clamp(36px, 7vw, 72px);
  font-weight: 500;
  line-height: 1.15;
  color: var(--color-ink, #3D3D3D);
  margin-bottom: 28px;
  perspective: 1000px;
}

.title-line {
  display: block;
  opacity: 0;
  will-change: transform, opacity;
}

.title-line.accent {
  background: linear-gradient(135deg, var(--color-sage, #8B9A7D), var(--color-terracotta, #C47D5E));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 17px;
  line-height: 1.8;
  color: var(--color-text, #5A5A5A);
  margin-bottom: 48px;
  opacity: 0;
  will-change: transform, opacity;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  opacity: 0;
  will-change: transform, opacity;
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 14px 32px;
  background: linear-gradient(135deg, var(--color-sage, #8B9A7D), var(--color-sage-dark, #6B7A5D));
  color: white;
  border: none;
  border-radius: 100px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  will-change: transform;
}

.btn-primary svg {
  width: 18px;
  height: 18px;
  transition: transform 0.3s ease;
}

.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(139, 154, 125, 0.4);
}

.btn-primary:hover svg {
  transform: translateX(4px);
}

.btn-primary.large {
  padding: 18px 40px;
  font-size: 16px;
}

.btn-secondary {
  display: inline-flex;
  align-items: center;
  padding: 14px 32px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  color: var(--color-text, #5A5A5A);
  border: 1px solid var(--color-sand, #D4C4A8);
  border-radius: 100px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  background: white;
  border-color: var(--color-sage, #8B9A7D);
  color: var(--color-sage, #8B9A7D);
}

/* Stats Wrapper - creates sticky effect */
.stats-wrapper {
  position: relative;
  width: 100%;
  max-width: 1100px;
  margin-top: 80vh;
  margin-bottom: 20vh;
}

.stats-sticky {
  position: sticky;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-radius: 28px;
  padding: 32px;
  box-shadow: 0 20px 60px rgba(139, 154, 125, 0.1);
}

.stat-item {
  text-align: center;
  opacity: 0;
  transform: translateY(40px) scale(0.9);
  will-change: transform, opacity;
}

.stat-bar-wrapper {
  height: 3px;
  background: var(--color-cream, #F5F1E8);
  border-radius: 100px;
  margin-bottom: 20px;
  overflow: hidden;
}

.stat-bar-fill {
  height: 100%;
  width: 0;
  border-radius: 100px;
  transition: width 1.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-0 { background: linear-gradient(90deg, var(--color-sage, #8B9A7D), var(--color-sage-light, #A8B89A)); }
.stat-1 { background: linear-gradient(90deg, var(--color-terracotta, #C47D5E), #D4A574); }
.stat-2 { background: linear-gradient(90deg, var(--color-sage, #8B9A7D), var(--color-terracotta, #C47D5E)); }
.stat-3 { background: linear-gradient(90deg, #7B68A6, #A594C0); }

.stat-number {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
  margin-bottom: 8px;
}

.stat-value {
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: 48px;
  font-weight: 600;
  color: var(--color-ink, #3D3D3D);
  line-height: 1;
}

.stat-suffix {
  font-size: 20px;
  color: var(--color-sage, #8B9A7D);
  font-weight: 500;
}

.stat-label {
  font-size: 13px;
  color: var(--color-text, #5A5A5A);
  margin-bottom: 12px;
}

.stat-dots {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.stat-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--color-cream, #F5F1E8);
  transition: background 0.3s ease;
}

.stat-dot.active {
  background: var(--color-sage, #8B9A7D);
}

/* Features Section */
.features-section {
  position: relative;
  min-height: 150vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 15vh 60px;
}

.features-sticky {
  position: sticky;
  top: 10vh;
  width: 100%;
  max-width: 1200px;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-label {
  display: block;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 3px;
  color: var(--color-terracotta, #C47D5E);
  margin-bottom: 16px;
  font-weight: 500;
}

.section-title {
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: clamp(28px, 4vw, 42px);
  font-weight: 500;
  color: var(--color-ink, #3D3D3D);
  margin: 0;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 28px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  padding: 36px 28px;
  position: relative;
  overflow: hidden;
  opacity: 0;
  transform: translateY(60px) scale(0.9);
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  transition-delay: var(--delay, 0s);
  will-change: transform, opacity;
  cursor: pointer;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--color-sage, #8B9A7D), var(--color-terracotta, #C47D5E));
  transform: scaleX(0);
  transform-origin: left;
  transition: transform 0.4s ease;
}

.feature-card:hover {
  transform: translateY(-8px) scale(1) !important;
  box-shadow: 0 25px 70px rgba(139, 154, 125, 0.15);
}

.feature-card:hover::before {
  transform: scaleX(1);
}

.feature-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  transition: transform 0.3s ease;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1) rotate(5deg);
}

.feature-icon svg {
  width: 26px;
  height: 26px;
}

.feature-number {
  position: absolute;
  top: 20px;
  right: 20px;
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: 56px;
  font-weight: 500;
  color: var(--color-cream, #F5F1E8);
  line-height: 1;
  z-index: 0;
}

.feature-title {
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: 20px;
  font-weight: 500;
  color: var(--color-ink, #3D3D3D);
  margin-bottom: 10px;
  position: relative;
  z-index: 1;
}

.feature-desc {
  font-size: 13px;
  color: var(--color-text, #5A5A5A);
  line-height: 1.7;
  margin: 0;
  position: relative;
  z-index: 1;
}

/* About Section */
.about-section {
  position: relative;
  padding: 120px 60px;
  background: white;
}

.about-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  max-width: 1200px;
  margin: 0 auto;
  align-items: center;
}

.about-text .section-label {
  text-align: left;
}

.about-text .section-title {
  text-align: left;
  margin-bottom: 24px;
}

.about-desc {
  font-size: 16px;
  color: var(--color-text, #5A5A5A);
  line-height: 1.8;
  margin-bottom: 40px;
}

.highlights {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.highlight-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.highlight-icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  background: var(--color-cream, #F5F1E8);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.highlight-icon svg {
  width: 24px;
  height: 24px;
  stroke: var(--color-sage, #8B9A7D);
}

.highlight-content h4 {
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: 17px;
  font-weight: 500;
  color: var(--color-ink, #3D3D3D);
  margin: 0 0 4px;
}

.highlight-content p {
  font-size: 13px;
  color: var(--color-text, #5A5A5A);
  margin: 0;
}

.about-visual {
  display: flex;
  justify-content: center;
  align-items: center;
}

.visual-container {
  position: relative;
  width: 320px;
  height: 320px;
}

.visual-ring {
  position: absolute;
  border: 1px solid rgba(139, 154, 125, 0.25);
  border-radius: 50%;
}

.ring-1 {
  width: 100%;
  height: 100%;
  animation: rotate 25s linear infinite;
}

.ring-2 {
  width: 80%;
  height: 80%;
  top: 10%;
  left: 10%;
  border-style: dashed;
  animation: rotate 20s linear infinite reverse;
}

.ring-3 {
  width: 60%;
  height: 60%;
  top: 20%;
  left: 20%;
  animation: rotate 30s linear infinite;
}

.visual-core {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, var(--color-sage-light, #A8B89A), var(--color-sage, #8B9A7D));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 20px 60px rgba(139, 154, 125, 0.3);
}

.visual-core svg {
  width: 50px;
  height: 50px;
  stroke: white;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* CTA Section */
.cta-section {
  position: relative;
  padding: 120px 60px;
  text-align: center;
  overflow: hidden;
}

.cta-bg-element {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 700px;
  height: 700px;
  background: radial-gradient(circle, rgba(139, 154, 125, 0.1) 0%, transparent 60%);
  pointer-events: none;
}

.cta-content {
  position: relative;
  z-index: 1;
  max-width: 600px;
  margin: 0 auto;
}

.cta-title {
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: clamp(26px, 4vw, 38px);
  font-weight: 500;
  color: var(--color-ink, #3D3D3D);
  margin-bottom: 16px;
}

.cta-desc {
  font-size: 16px;
  color: var(--color-text, #5A5A5A);
  margin-bottom: 40px;
}

/* Footer */
.hero-footer {
  padding: 40px 60px;
  background: var(--color-ink, #3D3D3D);
}

.footer-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: white;
  font-family: var(--font-display, 'Cormorant Garamond', serif);
  font-size: 18px;
  margin-bottom: 12px;
}

.footer-logo svg {
  width: 22px;
  height: 22px;
  stroke: var(--color-sage-light, #A8B89A);
}

.footer-copy {
  text-align: center;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
  margin: 0;
}

/* Responsive */
@media (max-width: 1024px) {
  .stats-grid { grid-template-columns: repeat(2, 1fr); }
  .features-grid { grid-template-columns: repeat(2, 1fr); }
  .about-content { grid-template-columns: 1fr; gap: 60px; }
}

@media (max-width: 768px) {
  .hero-section { padding-top: 80vh; }
  .stats-wrapper { margin-top: 60vh; }
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
    padding: 24px;
  }
  .stat-value { font-size: 36px; }
  .features-section { padding: 10vh 24px; }
  .features-grid { grid-template-columns: 1fr; }
  .about-section { padding: 80px 24px; }
  .about-visual { display: none; }
  .cta-section { padding: 80px 24px; }
  .hero-footer { padding: 32px 24px; }
}
</style>
