<template>
  <div class="layout-container">
    <!-- 侧边栏 (仅管理员) -->
    <div class="aside" v-if="user.role === 'ADMIN'">
      <div class="logo">
        <el-icon><Monitor /></el-icon> 健康管理系统
      </div>
      <el-menu
        :default-active="$route.path"
        router
        class="el-menu-vertical"
        background-color="#001529"
        text-color="rgba(255, 255, 255, 0.65)"
        active-text-color="#1890ff"
      >
          <el-menu-item index="/layout/admin-home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-menu-item index="/layout/user-manage">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
           <el-menu-item index="/layout/admin-manage">
            <el-icon><Avatar /></el-icon>
            <span>管理员管理</span>
          </el-menu-item>
          <el-menu-item index="/layout/admin-health">
            <el-icon><TrendCharts /></el-icon>
            <span>健康数据管理</span>
          </el-menu-item>
           <el-menu-item index="/layout/admin-forum">
            <el-icon><ChatDotRound /></el-icon>
            <span>论坛管理</span>
          </el-menu-item>
      </el-menu>
    </div>

    <!-- 主体区域 -->
    <div class="main">
      <!-- 头部 -->
      <div class="header">
        <!-- 普通用户模式：顶部导航栏 -->
        <div class="top-nav" v-if="user.role !== 'ADMIN'">
            <div class="nav-logo">
                <span class="logo-text">健康之约</span>
            </div>
            <el-menu
                :default-active="$route.path"
                router
                mode="horizontal"
                class="el-menu-horizontal"
                :ellipsis="false"
            >
                <el-menu-item index="/layout/home">健康仪表盘</el-menu-item>
                <el-menu-item index="/layout/forum">社区广场</el-menu-item>
                <el-menu-item index="/layout/smart-plan">智能规划</el-menu-item>
                <el-menu-item index="/layout/ai-advisor">
                  <span class="ai-nav-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                      <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                    </svg>
                  </span>
                  AI 顾问
                </el-menu-item>
            </el-menu>
        </div>

        <!-- 管理员模式：面包屑 -->
        <div class="breadcrumb" v-else>
           <el-breadcrumb separator="/">
                <el-breadcrumb-item>个人健康管理系统</el-breadcrumb-item>
                <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div class="user-info" ref="profileRef">
          <div class="avatar-wrapper" @click="toggleProfile" ref="avatarRef">
            <el-avatar :size="36" :src="user.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" class="user-avatar" :class="{ active: showProfile }" />
            <span class="user-name">{{ user.nickname || user.username }}</span>
            <el-icon class="arrow-icon" :class="{ rotated: showProfile }"><arrow-down /></el-icon>
          </div>

          <!-- Profile Popup -->
          <transition name="profile-popup">
            <div class="profile-popup" v-if="showProfile">
              <div class="profile-header">
                <el-avatar :size="64" :src="user.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" class="profile-avatar" />
                <div class="profile-info">
                  <h4>{{ user.nickname || user.username }}</h4>
                  <p>{{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}</p>
                </div>
              </div>
              <div class="profile-divider"></div>
              <nav class="profile-nav">
                <div class="profile-nav-item" @click="goTo('/layout/home'); closeProfile()">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
                  <span>我的看板</span>
                </div>
                <div class="profile-nav-item" @click="goTo('/layout/my-content'); closeProfile()">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
                  <span>我的内容</span>
                </div>
                <div class="profile-nav-item" @click="goTo('/layout/user-info'); closeProfile()">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                  <span>个人信息</span>
                </div>
              </nav>
              <div class="profile-divider"></div>
              <div class="profile-logout" @click="logout">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                <span>退出登录</span>
              </div>
            </div>
          </transition>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="content">
        <router-view v-slot="{ Component }">
            <transition name="fade-slide" mode="out-in">
                <component :is="Component" />
            </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import AiAdvisor from '../components/AiAdvisor.vue'
// AI Advisor is now a standalone page at /layout/ai-advisor
import anime from 'animejs'

const router = useRouter()
const route = useRoute()
const showProfile = ref(false)
const avatarRef = ref(null)
const profileRef = ref(null)

const goTo = (path) => {
  router.push(path)
}

const closeProfile = () => {
  showProfile.value = false
}

// Toggle profile with stop propagation
const toggleProfile = (e) => {
  e.stopPropagation()
  showProfile.value = !showProfile.value
}

// Handle click outside to close
const handleDocumentClick = (e) => {
  if (showProfile.value && profileRef.value && !profileRef.value.contains(e.target)) {
    const avatar = avatarRef.value
    if (avatar && !avatar.contains(e.target)) {
      showProfile.value = false
    }
  }
}

onMounted(() => {
  document.addEventListener('click', handleDocumentClick)
})

onUnmounted(() => {
  document.removeEventListener('click', handleDocumentClick)
})

const user = computed(() => {
    const str = localStorage.getItem("user")
    if (!str) return {}
    const obj = JSON.parse(str)
    const u = obj.user || {}
    u.role = obj.role
    return u
})

const currentRouteName = computed(() => {
    const map = {
        'Home': '系统首页',
        'SmartPlan': '智能规划',
        'AdminHome': '系统首页',
        'Forum': '健康论坛',
        'HealthRecord': '健康记录',
        'AdminHealth': '健康数据管理',
        'AdminForum': '论坛管理',
        'MyContent': '信息管理',
        'UserInfo': '个人信息',
        'UserManage': '用户管理',
        'AdminManage': '管理员管理'
    }
    return map[route.name] || route.name
})

const logout = () => {
  localStorage.removeItem("user")
  router.push("/login")
}

// anime.js: animate menu items on load
function animateMenuItems() {
  anime({
    targets: '.el-menu-item',
    opacity: [0, 1],
    translateY: [20, 0],
    delay: anime.stagger(50, { start: 300 }),
    duration: 400,
    easing: 'easeOutQuad'
  })
}

// anime.js: morphing logo animation on hover
function animateLogo() {
  const logo = document.querySelector('.nav-logo')
  if (!logo) return

  logo.addEventListener('mouseenter', () => {
    anime({
      targets: logo,
      scale: [1, 1.05],
      duration: 300,
      easing: 'easeOutQuad'
    })
  })

  logo.addEventListener('mouseleave', () => {
    anime({
      targets: logo,
      scale: [1.05, 1],
      duration: 300,
      easing: 'easeOutQuad'
    })
  })
}

// anime.js: avatar hover animation
function animateAvatar() {
  const wrapper = document.querySelector('.avatar-wrapper')
  if (!wrapper) return

  wrapper.addEventListener('mouseenter', () => {
    anime({
      targets: '.user-avatar',
      scale: [1, 1.1],
      duration: 300,
      easing: 'easeOutQuad'
    })
  })

  wrapper.addEventListener('mouseleave', () => {
    if (!showProfile.value) {
      anime({
        targets: '.user-avatar',
        scale: [1.1, 1],
        duration: 300,
        easing: 'easeOutQuad'
      })
    }
  })
}

function initAnimations() {
  animateMenuItems()
  animateLogo()
  animateAvatar()
}

onMounted(async () => {
  await nextTick()
  initAnimations()
})
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
}

.aside {
  width: 240px;
  background-color: var(--color-ink);
  color: white;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0,0,0,0.1);
  z-index: 10;
}

.logo {
  height: 72px;
  line-height: 72px;
  padding-left: 24px;
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 500;
  font-style: italic;
  background-color: rgba(0,0,0,0.2);
  display: flex;
  align-items: center;
  gap: 12px;
  letter-spacing: 1px;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: var(--color-bg);
  position: relative;
}

.header {
  height: 72px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 32px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 20px rgba(139, 154, 125, 0.08);
  position: relative;
  z-index: 100;
}

.top-nav,
.nav-logo,
.user-info {
  position: relative;
  z-index: 1;
}

.content {
  flex: 1;
  padding: 0;
  overflow-y: auto;
  overflow-x: hidden;
}

.el-menu-vertical {
  border-right: none;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: var(--color-ink);
  margin-left: 16px;
  font-weight: 500;
  transition: color 0.3s;
}

.el-dropdown-link:hover {
    color: var(--color-primary);
}

.user-info {
    display: flex;
    align-items: center;
}

.user-avatar {
    border: 2px solid var(--color-cream);
    box-shadow: 0 2px 12px rgba(139, 154, 125, 0.15);
    transition: transform 0.3s;
}

.user-avatar:hover {
    transform: scale(1.05);
}

/* Avatar Wrapper & Profile Popup */
.avatar-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 100px;
  transition: all 0.3s ease;
  position: relative;
}

.avatar-wrapper:hover {
  background: var(--color-cream);
}

.user-avatar {
  border: 2px solid var(--color-cream);
  box-shadow: 0 2px 12px rgba(139, 154, 125, 0.15);
  transition: all 0.3s ease;
}

.user-avatar.active {
  border-color: var(--color-sage);
}

.user-name {
  font-weight: 500;
  color: var(--color-ink);
  font-size: 14px;
}

.arrow-icon {
  color: var(--color-text);
  transition: transform 0.3s ease;
}

.arrow-icon.rotated {
  transform: rotate(180deg);
}

.profile-popup {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  width: 280px;
  background: var(--color-white);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15), 0 8px 24px rgba(0, 0, 0, 0.08);
  padding: 24px;
  z-index: 1000;
  transform-origin: top right;
}

.profile-popup::before {
  content: '';
  position: absolute;
  top: -8px;
  right: 24px;
  width: 16px;
  height: 16px;
  background: var(--color-white);
  transform: rotate(45deg);
  box-shadow: -2px -2px 4px rgba(0, 0, 0, 0.02);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding-bottom: 16px;
}

.profile-avatar {
  border: 3px solid var(--color-sage-light);
  box-shadow: 0 4px 16px rgba(139, 154, 125, 0.25);
}

.profile-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--color-ink);
}

.profile-info p {
  margin: 0;
  font-size: 12px;
  color: var(--color-text);
}

.profile-divider {
  height: 1px;
  background: var(--color-cream);
  margin: 16px 0;
}

.profile-nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.profile-nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: var(--color-text);
}

.profile-nav-item:hover {
  background: var(--color-primary-light);
  color: var(--color-sage-dark);
  transform: translateX(4px);
}

.profile-nav-item svg {
  width: 20px;
  height: 20px;
  stroke: currentColor;
}

.profile-nav-item span {
  font-size: 14px;
  font-weight: 500;
}

.profile-logout {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: var(--color-terracotta);
}

.profile-logout:hover {
  background: rgba(196, 125, 94, 0.1);
  transform: translateX(4px);
}

.profile-logout svg {
  width: 20px;
  height: 20px;
  stroke: currentColor;
}

.profile-logout span {
  font-size: 14px;
  font-weight: 500;
}

/* Profile Popup Animation */
.profile-popup-enter-active {
  animation: popup-in 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.profile-popup-leave-active {
  animation: popup-out 0.2s ease-out forwards;
}

@keyframes popup-in {
  0% {
    opacity: 0;
    transform: scale(0.8) translateY(-10px);
  }
  100% {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

@keyframes popup-out {
  0% {
    opacity: 1;
    transform: scale(1);
  }
  100% {
    opacity: 0;
    transform: scale(0.9) translateY(-10px);
  }
}

/* 顶部导航样式 */
.top-nav {
    display: flex;
    align-items: center;
    flex: 1;
}

.nav-logo {
    font-family: var(--font-display);
    font-size: 24px;
    font-weight: 500;
    font-style: italic;
    margin-right: 48px;
    color: var(--color-sage-dark);
    cursor: pointer;
}

.el-menu-horizontal {
    border-bottom: none !important;
    background-color: transparent !important;
    flex: 1;
}

/* 覆盖 Element Plus Menu 样式 */
:deep(.el-menu--horizontal > .el-menu-item) {
    border-bottom: 2px solid transparent !important;
    color: var(--color-text) !important;
    font-size: 15px;
    font-weight: 400;
    padding: 0 24px !important;
    transition: all 0.3s;
    border-radius: 100px;
    margin: 0 4px;
}

:deep(.el-menu--horizontal > .el-menu-item:hover) {
    color: var(--color-sage-dark) !important;
    background-color: var(--color-primary-light) !important;
}

:deep(.el-menu--horizontal > .el-menu-item.is-active) {
    color: var(--color-white) !important;
    background-color: var(--color-sage) !important;
    border-bottom: 2px solid var(--color-sage) !important;
}

.ai-nav-icon {
    display: inline-flex;
    align-items: center;
    margin-right: 4px;
}

.ai-nav-icon svg {
    width: 18px;
    height: 18px;
}

:deep(.el-sub-menu__title) {
    color: var(--color-text) !important;
    font-size: 15px;
    font-weight: 400;
    padding: 0 24px !important;
    border-radius: 100px;
    margin: 0 4px;
}

:deep(.el-sub-menu__title:hover) {
    color: var(--color-sage-dark) !important;
    background-color: var(--color-primary-light) !important;
}

:deep(.el-sub-menu .el-menu) {
    border-radius: var(--card-radius) !important;
    box-shadow: var(--card-shadow-hover) !important;
    border: none !important;
    overflow: hidden;
}

:deep(.el-sub-menu .el-menu-item) {
    border-radius: 0 !important;
    margin: 0 !important;
}

:deep(.el-sub-menu .el-menu-item:hover) {
    background-color: var(--color-primary-light) !important;
    color: var(--color-sage-dark) !important;
}

/* 管理员面包屑样式适配 */
:deep(.el-breadcrumb__inner) {
    color: var(--color-text) !important;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
    color: var(--color-ink) !important;
    font-weight: 500;
}

/* 路由切换动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.2s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Menu item initial opacity (before anime.js animation) */
.el-menu-item,
.el-sub-menu__title {
  opacity: 0;
}

/* Responsive navigation */
@media (max-width: 768px) {
  .header {
    padding: 0 16px;
  }

  .nav-logo {
    font-size: 20px;
    margin-right: 16px;
  }

  .el-menu-horizontal {
    display: none;
  }
}

@media (max-width: 480px) {
  .header {
    padding: 0 12px;
  }

  .nav-logo .logo-text {
    display: none;
  }

  .user-info {
    margin-left: 8px;
  }
}
</style>
