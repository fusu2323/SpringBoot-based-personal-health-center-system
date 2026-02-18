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
        
        <!-- 管理员模式：面包屑 -->
        <div class="breadcrumb" v-if="user.role === 'ADMIN'">
           <el-breadcrumb separator="/">
                <el-breadcrumb-item>个人健康管理系统</el-breadcrumb-item>
                <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!-- 普通用户模式：顶部导航栏 (升级美化版) -->
        <div class="top-nav" v-else>
            <div class="nav-logo animate__animated animate__fadeInLeft">
                <el-icon class="logo-icon"><Monitor /></el-icon> 
                <span class="logo-text">个人健康</span>
            </div>
            <el-menu
                :default-active="$route.path"
                router
                mode="horizontal"
                class="el-menu-horizontal animate__animated animate__fadeInDown"
                :ellipsis="false"
            >
                <el-menu-item index="/layout/forum">社区广场</el-menu-item>
                <el-menu-item index="/layout/smart-plan">智能规划</el-menu-item>
                
                <el-sub-menu index="1">
                    <template #title>健康记录</template>
                    <el-menu-item index="/layout/health-record" @click="navigateToRecord('indicator')">身体指标</el-menu-item>
                    <el-menu-item index="/layout/health-record" @click="navigateToRecord('sport')">运动记录</el-menu-item>
                    <el-menu-item index="/layout/health-record" @click="navigateToRecord('diet')">饮食记录</el-menu-item>
                    <el-menu-item index="/layout/health-record" @click="navigateToRecord('sleep')">睡眠记录</el-menu-item>
                </el-sub-menu>
                
                <el-sub-menu index="2">
                    <template #title>个人中心</template>
                    <el-menu-item index="/layout/home">我的看板</el-menu-item>
                    <el-menu-item index="/layout/my-content">我的内容</el-menu-item>
                    <el-menu-item index="/layout/user-info">个人信息</el-menu-item>
                </el-sub-menu>
            </el-menu>
        </div>

        <div class="user-info">
          <el-avatar :size="36" :src="user.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" class="user-avatar" />
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              {{ user.nickname || user.username }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu class="user-dropdown">
                <el-dropdown-item @click="logout">
                    <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="content">
        <router-view v-slot="{ Component }">
            <transition name="fade-transform" mode="out-in">
                <component :is="Component" />
            </transition>
        </router-view>
      </div>
      
      <!-- AI 顾问 -->
      <AiAdvisor />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import AiAdvisor from '../components/AiAdvisor.vue'
import 'animate.css' // 引入动画库

const router = useRouter()
const route = useRoute()

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

const navigateToRecord = (tabName) => {
    router.push({ path: '/layout/health-record', query: { tab: tabName } })
}
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
}
.aside {
  width: 240px;
  background-color: #001529;
  color: white;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0,0,0,0.15);
  z-index: 10;
}
.logo {
  height: 64px;
  line-height: 64px;
  padding-left: 24px;
  font-size: 20px;
  font-weight: 600;
  background-color: #002140;
  display: flex;
  align-items: center;
  gap: 12px;
  letter-spacing: 1px;
}
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa; /* 更柔和的背景色 */
  position: relative;
}
.header {
  height: 64px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30px;
  background: rgba(255, 255, 255, 0.95); /* 半透明白色背景 */
  backdrop-filter: blur(10px); /* 毛玻璃效果 */
  box-shadow: 0 1px 4px rgba(0,21,41,0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}
.content {
  flex: 1;
  padding: 24px;
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
  color: #333; /* 文字颜色改为深色 */
  margin-left: 12px;
  font-weight: 500;
  transition: color 0.3s;
}
.el-dropdown-link:hover {
    color: #409EFF;
}
.user-info {
    display: flex;
    align-items: center;
}
.user-avatar {
    border: 2px solid #fff;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    transition: transform 0.3s;
}
.user-avatar:hover {
    transform: rotate(360deg);
}

/* 顶部导航样式 */
.top-nav {
    display: flex;
    align-items: center;
    flex: 1;
}
.nav-logo {
    font-size: 22px;
    font-weight: bold;
    margin-right: 40px;
    display: flex;
    align-items: center;
    gap: 10px;
    color: #409EFF; /* Logo 品牌色 */
    cursor: pointer;
}
.logo-icon {
    font-size: 26px;
}
.el-menu-horizontal {
    border-bottom: none !important;
    background-color: transparent !important;
    flex: 1;
}
/* 覆盖 Element Plus Menu 样式 */
:deep(.el-menu--horizontal > .el-menu-item) {
    border-bottom: 2px solid transparent !important;
    color: #555 !important;
    font-size: 15px;
    font-weight: 500;
    padding: 0 20px !important;
    transition: all 0.3s;
}
:deep(.el-menu--horizontal > .el-menu-item:hover) {
    color: #409EFF !important;
    background-color: rgba(64, 158, 255, 0.05) !important;
}
:deep(.el-menu--horizontal > .el-menu-item.is-active) {
    color: #409EFF !important;
    border-bottom: 2px solid #409EFF !important;
    background-color: transparent !important;
}
:deep(.el-sub-menu__title) {
    color: #555 !important;
    font-size: 15px;
    font-weight: 500;
}
:deep(.el-sub-menu__title:hover) {
    color: #409EFF !important;
    background-color: rgba(64, 158, 255, 0.05) !important;
}

/* 管理员面包屑样式适配 */
:deep(.el-breadcrumb__inner) {
    color: #666 !important;
}
:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
    color: #333 !important;
    font-weight: 600;
}

/* 路由切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.4s ease;
}
.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>
