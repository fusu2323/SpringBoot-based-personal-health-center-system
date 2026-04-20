<template>
  <div class="awwwards-hero">
    <!-- 背景层：6层Z轴深度 -->
    <div class="parallax-layers">
      <!-- 第1层：深空背景 -->
      <div class="layer layer-deep-space" data-speed="0.1"></div>

      <!-- 第2层：星云粒子 -->
      <div class="layer layer-nebula" data-speed="0.2">
        <div class="nebula-particle" v-for="i in 30" :key="i" :style="getParticleStyle(i)"></div>
      </div>

      <!-- 第3层：远景光晕 -->
      <div class="layer layer-distant-glow" data-speed="0.3">
        <div class="glow-orb orb-1"></div>
        <div class="glow-orb orb-2"></div>
      </div>

      <!-- 第4层：中景全息网格 -->
      <div class="layer layer-holographic-grid" data-speed="0.4">
        <div class="holo-grid"></div>
        <div class="holo-rings">
          <div class="holo-ring ring-1"></div>
          <div class="holo-ring ring-2"></div>
          <div class="holo-ring ring-3"></div>
        </div>
      </div>

      <!-- 第5层：全息人体模型 -->
      <div class="layer layer-human-model" data-speed="0.5">
        <div class="human-model-container" ref="modelContainer">
          <div class="human-model">
            <div class="model-skeleton">
              <div class="skeleton-head"></div>
              <div class="skeleton-torso"></div>
              <div class="skeleton-arm left"></div>
              <div class="skeleton-arm right"></div>
              <div class="skeleton-leg left"></div>
              <div class="skeleton-leg right"></div>
            </div>
            <div class="model-scan-line"></div>
            <div class="model-data-points">
              <div class="data-point" style="--x: 50%; --y: 20%; --delay: 0s;">❤️ 72</div>
              <div class="data-point" style="--x: 35%; --y: 35%; --delay: 0.2s;">🫁 98%</div>
              <div class="data-point" style="--x: 65%; --y: 40%; --delay: 0.4s;">💪 85</div>
              <div class="data-point" style="--x: 45%; --y: 60%; --delay: 0.6s;">🧠 92</div>
              <div class="data-point" style="--x: 55%; --y: 75%; --delay: 0.8s;">🦴 78</div>
            </div>
          </div>
          <div class="model-reflection"></div>
        </div>
      </div>

      <!-- 第6层：近景光效 -->
      <div class="layer layer-near-light" data-speed="0.6">
        <div class="light-streak streak-1"></div>
        <div class="light-streak streak-2"></div>
        <div class="light-streak streak-3"></div>
      </div>
    </div>

    <!-- 主内容层 -->
    <div class="hero-content" ref="heroContent">
      <!-- 顶部导航 -->
      <nav class="hero-nav">
        <div class="nav-logo">
          <span class="logo-icon">◈</span>
          <span class="logo-text">HEALTH<span class="logo-accent">·</span>AI</span>
        </div>
        <div class="nav-links">
          <a href="#" class="nav-link">智能诊断</a>
          <a href="#" class="nav-link">健康监测</a>
          <a href="#" class="nav-link">个性化方案</a>
          <a href="#" class="nav-link">关于我们</a>
        </div>
        <div class="nav-cta">
          <button class="btn-enter">开始探索</button>
        </div>
      </nav>

      <!-- 主标题区 -->
      <div class="hero-headline">
        <div class="headline-tag">
          <span class="tag-dot"></span>
          <span>AI驱动的智能健康管理</span>
        </div>
        <h1 class="headline-main">
          <span class="line line-1" ref="line1">预见</span>
          <span class="line line-2" ref="line2">健康</span>
          <span class="line line-3" ref="line3">未来</span>
        </h1>
        <p class="headline-sub" ref="headlineSub">
          整合可穿戴设备数据 · AI深度分析 · 个性化健康方案
        </p>
      </div>

      <!-- AI评分环 -->
      <div class="ai-score-container" ref="aiScore">
        <div class="ai-score-ring">
          <svg viewBox="0 0 200 200" class="score-svg">
            <circle cx="100" cy="100" r="85" class="score-bg" />
            <circle cx="100" cy="100" r="85" class="score-progress" ref="scoreProgress" />
          </svg>
          <div class="score-content">
            <div class="score-value">92</div>
            <div class="score-label">健康评分</div>
          </div>
        </div>
        <div class="score-details">
          <div class="score-item">
            <span class="item-label">心血管</span>
            <span class="item-value">优秀</span>
          </div>
          <div class="score-item">
            <span class="item-label">代谢</span>
            <span class="item-value">良好</span>
          </div>
          <div class="score-item">
            <span class="item-label">睡眠</span>
            <span class="item-value">优秀</span>
          </div>
        </div>
      </div>

      <!-- CTA按钮组 -->
      <div class="hero-cta" ref="heroCta">
        <button class="cta-primary">
          <span class="cta-text">立即体验</span>
          <span class="cta-arrow">→</span>
          <div class="cta-glow"></div>
        </button>
        <button class="cta-secondary">
          <span class="cta-icon">▶</span>
          <span>观看演示</span>
        </button>
      </div>

      <!-- 滚动提示 -->
      <div class="scroll-indicator" ref="scrollIndicator">
        <div class="scroll-line"></div>
        <span class="scroll-text">向下滚动</span>
      </div>
    </div>

    <!-- 数据流动画层 -->
    <div class="data-flow-layer">
      <div class="data-stream stream-1">
        <div class="stream-dot" v-for="i in 20" :key="i"></div>
      </div>
      <div class="data-stream stream-2">
        <div class="stream-dot" v-for="i in 15" :key="i"></div>
      </div>
      <div class="data-stream stream-3">
        <div class="stream-dot" v-for="i in 18" :key="i"></div>
      </div>
    </div>

    <!-- 鼠标跟随光效 -->
    <div class="mouse-follower" ref="mouseFollower">
      <div class="follower-ring ring-1"></div>
      <div class="follower-ring ring-2"></div>
      <div class="follower-core"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import anime from 'animejs'

const heroContent = ref(null)
const line1 = ref(null)
const line2 = ref(null)
const line3 = ref(null)
const headlineSub = ref(null)
const aiScore = ref(null)
const heroCta = ref(null)
const scrollIndicator = ref(null)
const modelContainer = ref(null)
const mouseFollower = ref(null)
const scoreProgress = ref(null)

let scrollY = 0
let mouseX = 0
let mouseY = 0
let animationFrameId = null

// 粒子样式
const getParticleStyle = (index) => {
  const size = Math.random() * 4 + 1
  return {
    left: `${Math.random() * 100}%`,
    top: `${Math.random() * 100}%`,
    width: `${size}px`,
    height: `${size}px`,
    animationDelay: `${Math.random() * 3}s`,
    animationDuration: `${Math.random() * 2 + 2}s`
  }
}

// 入场动画
const playIntroAnimation = () => {
  const tl = anime.timeline({ easing: 'easeOutExpo' })

  // 导航动画
  tl.add({
    targets: '.nav-logo',
    opacity: [0, 1],
    translateY: [-20, 0],
    duration: 800
  })
  .add({
    targets: '.nav-link',
    opacity: [0, 1],
    translateY: [-20, 0],
    duration: 600,
    delay: anime.stagger(100)
  }, '-=400')

  // 主标题动画
  tl.add({
    targets: line1.value,
    opacity: [0, 1],
    translateY: [100, 0],
    rotateX: [90, 0],
    duration: 1000,
    easing: 'easeOutQuart'
  })
  .add({
    targets: line2.value,
    opacity: [0, 1],
    translateY: [100, 0],
    rotateX: [90, 0],
    duration: 1000,
    easing: 'easeOutQuart'
  }, '-=700')
  .add({
    targets: line3.value,
    opacity: [0, 1],
    translateY: [100, 0],
    rotateX: [90, 0],
    duration: 1000,
    easing: 'easeOutQuart'
  }, '-=700')
  .add({
    targets: '.headline-tag',
    opacity: [0, 1],
    scale: [0.8, 1],
    duration: 600
  }, '-=500')
  .add({
    targets: headlineSub.value,
    opacity: [0, 1],
    translateY: [30, 0],
    duration: 800
  }, '-=400')

  // AI评分环动画
  tl.add({
    targets: aiScore.value,
    opacity: [0, 1],
    scale: [0.5, 1],
    rotate: [180, 0],
    duration: 1200,
    easing: 'easeOutElastic(1, 0.5)'
  }, '-=400')

  // CTA按钮动画
  tl.add({
    targets: heroCta.value,
    opacity: [0, 1],
    translateY: [50, 0],
    duration: 800
  }, '-=600')

  // 滚动提示动画
  tl.add({
    targets: scrollIndicator.value,
    opacity: [0, 1],
    duration: 600
  }, '-=400')

  // 环形进度条动画
  tl.add({
    targets: scoreProgress.value,
    strokeDashoffset: [anime.setDashoffset, 0],
    duration: 2000,
    easing: 'easeInOutCirc'
  }, '-=800')
}

// 鼠标跟随
const handleMouseMove = (e) => {
  mouseX = e.clientX
  mouseY = e.clientY

  if (mouseFollower.value) {
    mouseFollower.value.style.transform = `translate(${mouseX - 100}px, ${mouseY - 100}px)`
  }

  // 3D模型视角旋转
  if (modelContainer.value) {
    const rotateY = ((mouseX / window.innerWidth) - 0.5) * 30
    const rotateX = ((mouseY / window.innerHeight) - 0.5) * -20
    modelContainer.value.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`
  }
}

// 滚动处理
const handleScroll = () => {
  scrollY = window.scrollY
  const viewportHeight = window.innerHeight

  // Parallax效果
  const layers = document.querySelectorAll('.layer')
  layers.forEach(layer => {
    const speed = parseFloat(layer.dataset.speed) || 0.5
    const yPos = scrollY * speed
    layer.style.transform = `translateY(${yPos}px)`
  })

  // Hero内容parallax
  if (heroContent.value && scrollY < viewportHeight) {
    const opacity = 1 - (scrollY / viewportHeight) * 1.5
    const translateY = scrollY * 0.3
    heroContent.value.style.opacity = Math.max(0, opacity)
    heroContent.value.style.transform = `translateY(${translateY}px)`
  }

  // 模型缩放
  if (modelContainer.value) {
    const scale = 1 - (scrollY / viewportHeight) * 0.3
    modelContainer.value.style.transform = `perspective(1000px) scale(${Math.max(0.7, scale)})`
  }

  // AI评分环随滚动旋转
  if (aiScore.value) {
    const rotate = scrollY * 0.2
    aiScore.value.querySelector('.ai-score-ring').style.transform = `rotate(${rotate}deg)`
  }
}

// 动画循环
const animate = () => {
  // 数据流动画
  const streams = document.querySelectorAll('.data-stream')
  streams.forEach((stream, index) => {
    const offset = (Date.now() / 50) % 1000
    stream.style.transform = `translateY(${offset * (index % 3 === 0 ? 1 : -1)}px)`
  })

  // 扫描线动画
  const scanLines = document.querySelectorAll('.model-scan-line')
  scanLines.forEach(line => {
    line.style.top = `${(Date.now() / 20) % 100}%`
  })

  animationFrameId = requestAnimationFrame(animate)
}

onMounted(() => {
  // 入场动画
  setTimeout(playIntroAnimation, 300)

  // 启动动画循环
  animate()

  // 事件监听
  window.addEventListener('scroll', handleScroll, { passive: true })
  window.addEventListener('mousemove', handleMouseMove, { passive: true })

  // CTA按钮悬停效果
  const ctaPrimary = document.querySelector('.cta-primary')
  if (ctaPrimary) {
    ctaPrimary.addEventListener('mouseenter', () => {
      anime({
        targets: ctaPrimary,
        scale: 1.05,
        duration: 300,
        easing: 'easeOutElastic(1, 0.5)'
      })
    })
    ctaPrimary.addEventListener('mouseleave', () => {
      anime({
        targets: ctaPrimary,
        scale: 1,
        duration: 300,
        easing: 'easeOutQuad'
      })
    })
  }
})

onUnmounted(() => {
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId)
  }
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('mousemove', handleMouseMove)
})
</script>

<style scoped>
/* ========== 基础变量 ========== */
:root {
  --aurora-blue: #4A9EFF;
  --aurora-blue-dim: #2A5A8F;
  --deep-black: #0A0A0F;
  --carbon-black: #12121A;
  --panel-bg: rgba(20, 25, 40, 0.6);
  --glass-blur: blur(24px);
  --holo-cyan: #00F5FF;
  --holo-purple: #B44AFF;
  --text-primary: #FFFFFF;
  --text-secondary: rgba(255, 255, 255, 0.6);
}

/* ========== 主容器 ========== */
.awwwards-hero {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background: var(--deep-black);
  font-family: 'Inter', 'Noto Sans SC', sans-serif;
  cursor: none;
}

/* ========== Z轴分层背景 ========== */
.parallax-layers {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 120%;
  will-change: transform;
}

/* 第1层：深空 */
.layer-deep-space {
  background:
    radial-gradient(ellipse at 20% 30%, rgba(74, 158, 255, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 70%, rgba(180, 74, 255, 0.1) 0%, transparent 50%),
    linear-gradient(180deg, #0A0A0F 0%, #0D0D15 100%);
}

/* 第2层：星云粒子 */
.layer-nebula {
  background: transparent;
}

.nebula-particle {
  position: absolute;
  background: radial-gradient(circle, rgba(74, 158, 255, 0.8) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); opacity: 0.6; }
  50% { transform: translateY(-20px) scale(1.2); opacity: 1; }
}

/* 第3层：远景光晕 */
.layer-distant-glow {
  background: transparent;
}

.glow-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
}

.orb-1 {
  width: 600px;
  height: 600px;
  top: 10%;
  left: 10%;
  background: radial-gradient(circle, var(--aurora-blue) 0%, transparent 70%);
  animation: pulse 8s ease-in-out infinite;
}

.orb-2 {
  width: 400px;
  height: 400px;
  top: 50%;
  right: 10%;
  background: radial-gradient(circle, var(--holo-purple) 0%, transparent 70%);
  animation: pulse 6s ease-in-out infinite reverse;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.3; }
  50% { transform: scale(1.2); opacity: 0.5; }
}

/* 第4层：全息网格 */
.layer-holographic-grid {
  background: transparent;
}

.holo-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    linear-gradient(rgba(74, 158, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(74, 158, 255, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  transform: perspective(500px) rotateX(60deg);
  transform-origin: center top;
  opacity: 0.5;
}

.holo-rings {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.holo-ring {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid rgba(74, 158, 255, 0.2);
  border-radius: 50%;
  animation: ring-pulse 4s ease-in-out infinite;
}

.ring-1 { width: 300px; height: 300px; animation-delay: 0s; }
.ring-2 { width: 500px; height: 500px; animation-delay: 0.5s; }
.ring-3 { width: 700px; height: 700px; animation-delay: 1s; }

@keyframes ring-pulse {
  0% { opacity: 0.2; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 0.5; transform: translate(-50%, -50%) scale(1.05); }
  100% { opacity: 0.2; transform: translate(-50%, -50%) scale(1); }
}

/* 第5层：人体模型 */
.layer-human-model {
  background: transparent;
}

.human-model-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  transition: transform 0.1s ease-out;
}

.human-model {
  position: relative;
  width: 200px;
  height: 400px;
}

.model-skeleton {
  position: relative;
  width: 100%;
  height: 100%;
}

/* 头部 */
.skeleton-head {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 60px;
  background: linear-gradient(135deg, rgba(74, 158, 255, 0.3) 0%, rgba(74, 158, 255, 0.1) 100%);
  border: 1px solid rgba(74, 158, 255, 0.5);
  border-radius: 50% 50% 45% 45%;
  box-shadow:
    0 0 30px rgba(74, 158, 255, 0.3),
    inset 0 0 20px rgba(74, 158, 255, 0.1);
}

/* 躯干 */
.skeleton-torso {
  position: absolute;
  top: 70px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 140px;
  background: linear-gradient(180deg, rgba(74, 158, 255, 0.25) 0%, rgba(74, 158, 255, 0.1) 100%);
  border: 1px solid rgba(74, 158, 255, 0.4);
  border-radius: 40px 40px 30px 30px;
  box-shadow:
    0 0 40px rgba(74, 158, 255, 0.2),
    inset 0 0 30px rgba(74, 158, 255, 0.05);
}

/* 四肢 */
.skeleton-arm {
  position: absolute;
  top: 75px;
  width: 25px;
  height: 120px;
  background: linear-gradient(180deg, rgba(74, 158, 255, 0.2) 0%, rgba(74, 158, 255, 0.08) 100%);
  border: 1px solid rgba(74, 158, 255, 0.3);
  border-radius: 12px;
}

.skeleton-arm.left { left: 20px; transform: rotate(15deg); }
.skeleton-arm.right { right: 20px; transform: rotate(-15deg); }

.skeleton-leg {
  position: absolute;
  top: 200px;
  width: 30px;
  height: 150px;
  background: linear-gradient(180deg, rgba(74, 158, 255, 0.2) 0%, rgba(74, 158, 255, 0.08) 100%);
  border: 1px solid rgba(74, 158, 255, 0.3);
  border-radius: 15px;
}

.skeleton-leg.left { left: 50px; }
.skeleton-leg.right { right: 50px; }

/* 扫描线 */
.model-scan-line {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--holo-cyan), transparent);
  box-shadow: 0 0 20px var(--holo-cyan);
  animation: scan 3s ease-in-out infinite;
}

@keyframes scan {
  0% { top: 0; opacity: 0; }
  10% { opacity: 1; }
  90% { opacity: 1; }
  100% { top: 100%; opacity: 0; }
}

/* 数据点 */
.model-data-points {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.data-point {
  position: absolute;
  left: var(--x);
  top: var(--y);
  transform: translate(-50%, -50%);
  padding: 4px 10px;
  background: rgba(10, 10, 15, 0.8);
  border: 1px solid rgba(74, 158, 255, 0.5);
  border-radius: 20px;
  font-size: 12px;
  color: var(--text-primary);
  white-space: nowrap;
  backdrop-filter: blur(10px);
  animation: data-pulse 2s ease-in-out infinite;
  animation-delay: var(--delay);
}

@keyframes data-pulse {
  0%, 100% { opacity: 0.7; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 1; transform: translate(-50%, -50%) scale(1.05); }
}

/* 模型反射 */
.model-reflection {
  position: absolute;
  bottom: -80px;
  left: 0;
  width: 100%;
  height: 80px;
  background: linear-gradient(to bottom, rgba(74, 158, 255, 0.1), transparent);
  transform: scaleY(-1);
  opacity: 0.3;
  filter: blur(5px);
}

/* 第6层：近景光效 */
.layer-near-light {
  background: transparent;
}

.light-streak {
  position: absolute;
  width: 2px;
  height: 200px;
  background: linear-gradient(to bottom, transparent, var(--aurora-blue), transparent);
  opacity: 0.3;
  animation: streak 3s linear infinite;
}

.streak-1 { left: 20%; animation-delay: 0s; }
.streak-2 { left: 50%; animation-delay: 1s; }
.streak-3 { left: 80%; animation-delay: 2s; }

@keyframes streak {
  0% { transform: translateY(-200px); opacity: 0; }
  50% { opacity: 0.5; }
  100% { transform: translateY(100vh); opacity: 0; }
}

/* ========== 主内容层 ========== */
.hero-content {
  position: relative;
  z-index: 10;
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 0 60px;
  will-change: transform, opacity;
}

/* 导航 */
.hero-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30px 0;
  opacity: 0;
}

.nav-logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
  color: var(--aurora-blue);
  text-shadow: 0 0 20px var(--aurora-blue);
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 3px;
  color: var(--text-primary);
}

.logo-accent {
  color: var(--aurora-blue);
}

.nav-links {
  display: flex;
  gap: 40px;
}

.nav-link {
  font-size: 14px;
  color: var(--text-secondary);
  text-decoration: none;
  letter-spacing: 1px;
  transition: color 0.3s;
}

.nav-link:hover {
  color: var(--text-primary);
}

.nav-cta {
  opacity: 0;
}

.btn-enter {
  padding: 12px 28px;
  background: linear-gradient(135deg, var(--aurora-blue) 0%, var(--holo-purple) 100%);
  border: none;
  border-radius: 30px;
  font-size: 14px;
  font-weight: 600;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 0 30px rgba(74, 158, 255, 0.3);
}

.btn-enter:hover {
  transform: scale(1.05);
  box-shadow: 0 0 50px rgba(74, 158, 255, 0.5);
}

/* 主标题区 */
.hero-headline {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding-bottom: 100px;
}

.headline-tag {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 8px 20px;
  background: rgba(74, 158, 255, 0.1);
  border: 1px solid rgba(74, 158, 255, 0.3);
  border-radius: 30px;
  font-size: 13px;
  color: var(--aurora-blue);
  letter-spacing: 2px;
  margin-bottom: 30px;
  opacity: 0;
  backdrop-filter: var(--glass-blur);
}

.tag-dot {
  width: 8px;
  height: 8px;
  background: var(--holo-cyan);
  border-radius: 50%;
  animation: dot-pulse 1.5s ease-in-out infinite;
}

@keyframes dot-pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(0.8); }
}

.headline-main {
  display: flex;
  flex-direction: column;
  gap: 0;
  margin: 0;
}

.line {
  font-size: clamp(60px, 12vw, 140px);
  font-weight: 800;
  line-height: 0.95;
  letter-spacing: -2px;
  background: linear-gradient(135deg, #FFFFFF 0%, rgba(255, 255, 255, 0.8) 50%, var(--aurora-blue) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  opacity: 0;
  transform-origin: center;
}

.line-1 { transform: perspective(500px) rotateX(90deg); }
.line-2 { transform: perspective(500px) rotateX(90deg); }
.line-3 { transform: perspective(500px) rotateX(90deg); }

.headline-sub {
  margin-top: 30px;
  font-size: 16px;
  color: var(--text-secondary);
  letter-spacing: 4px;
  opacity: 0;
}

/* AI评分环 */
.ai-score-container {
  position: absolute;
  right: 10%;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  opacity: 0;
}

.ai-score-ring {
  position: relative;
  width: 200px;
  height: 200px;
  transition: transform 0.1s ease-out;
}

.score-svg {
  transform: rotate(-90deg);
}

.score-bg {
  fill: none;
  stroke: rgba(74, 158, 255, 0.1);
  stroke-width: 8;
}

.score-progress {
  fill: none;
  stroke: url(#score-gradient);
  stroke-width: 8;
  stroke-linecap: round;
  stroke-dasharray: 534;
  stroke-dashoffset: 534;
}

.score-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.score-value {
  font-size: 56px;
  font-weight: 700;
  color: var(--text-primary);
  text-shadow: 0 0 30px var(--aurora-blue);
}

.score-label {
  font-size: 12px;
  color: var(--text-secondary);
  letter-spacing: 2px;
  margin-top: 5px;
}

.score-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 15px 20px;
  background: var(--panel-bg);
  border: 1px solid rgba(74, 158, 255, 0.2);
  border-radius: 15px;
  backdrop-filter: var(--glass-blur);
}

.score-item {
  display: flex;
  justify-content: space-between;
  gap: 30px;
  font-size: 13px;
}

.item-label {
  color: var(--text-secondary);
}

.item-value {
  color: var(--holo-cyan);
  font-weight: 600;
}

/* CTA按钮组 */
.hero-cta {
  position: absolute;
  bottom: 150px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 20px;
  opacity: 0;
}

.cta-primary {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 18px 40px;
  background: linear-gradient(135deg, var(--aurora-blue) 0%, #2563EB 100%);
  border: none;
  border-radius: 50px;
  font-size: 16px;
  font-weight: 600;
  color: white;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow:
    0 0 40px rgba(74, 158, 255, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.cta-text {
  position: relative;
  z-index: 1;
}

.cta-arrow {
  position: relative;
  z-index: 1;
  transition: transform 0.3s;
}

.cta-glow {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  animation: cta-shine 3s ease-in-out infinite;
}

@keyframes cta-shine {
  0% { left: -100%; }
  50%, 100% { left: 100%; }
}

.cta-primary:hover .cta-arrow {
  transform: translateX(5px);
}

.cta-secondary {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 18px 30px;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 50px;
  font-size: 16px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.3s;
  backdrop-filter: blur(10px);
}

.cta-secondary:hover {
  border-color: var(--aurora-blue);
  color: var(--text-primary);
}

.cta-icon {
  font-size: 12px;
}

/* 滚动提示 */
.scroll-indicator {
  position: absolute;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  opacity: 0;
}

.scroll-line {
  width: 1px;
  height: 60px;
  background: linear-gradient(to bottom, var(--aurora-blue), transparent);
  animation: scroll-line 2s ease-in-out infinite;
}

@keyframes scroll-line {
  0% { transform: scaleY(0); transform-origin: top; }
  50% { transform: scaleY(1); transform-origin: top; }
  51% { transform-origin: bottom; }
  100% { transform: scaleY(0); transform-origin: bottom; }
}

.scroll-text {
  font-size: 11px;
  color: var(--text-secondary);
  letter-spacing: 3px;
  writing-mode: vertical-rl;
}

/* ========== 数据流动画层 ========== */
.data-flow-layer {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 5;
}

.data-stream {
  position: absolute;
  display: flex;
  gap: 20px;
}

.stream-1 { left: 5%; top: 0; flex-direction: column; }
.stream-2 { right: 5%; top: 0; flex-direction: column-reverse; }
.stream-3 { left: 50%; top: 0; }

.stream-dot {
  width: 4px;
  height: 4px;
  background: var(--aurora-blue);
  border-radius: 50%;
  opacity: 0.5;
  animation: stream-flow 1s linear infinite;
  animation-delay: calc(var(--i, 0) * 0.1s);
}

@keyframes stream-flow {
  0% { transform: translateY(0); opacity: 0; }
  50% { opacity: 0.8; }
  100% { transform: translateY(100vh); opacity: 0; }
}

/* ========== 鼠标跟随 ========== */
.mouse-follower {
  position: fixed;
  top: 0;
  left: 0;
  width: 200px;
  height: 200px;
  pointer-events: none;
  z-index: 100;
  opacity: 0.6;
}

.follower-ring {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid rgba(74, 158, 255, 0.3);
  border-radius: 50%;
  animation: follower-pulse 2s ease-in-out infinite;
}

.ring-1 { width: 80px; height: 80px; animation-delay: 0s; }
.ring-2 { width: 140px; height: 140px; animation-delay: 0.3s; }

.follower-core {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 20px;
  height: 20px;
  background: radial-gradient(circle, var(--aurora-blue) 0%, transparent 70%);
  border-radius: 50%;
}

@keyframes follower-pulse {
  0%, 100% { opacity: 0.3; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 0.6; transform: translate(-50%, -50%) scale(1.1); }
}

/* ========== 响应式 ========== */
@media (max-width: 1200px) {
  .ai-score-container {
    right: 5%;
  }
}

@media (max-width: 768px) {
  .awwwards-hero {
    cursor: auto;
  }

  .hero-content {
    padding: 0 30px;
  }

  .nav-links {
    display: none;
  }

  .ai-score-container {
    position: relative;
    right: auto;
    top: auto;
    transform: none;
    margin-top: 40px;
  }

  .hero-cta {
    bottom: 100px;
  }

  .mouse-follower {
    display: none;
  }
}
</style>
