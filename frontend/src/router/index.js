import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/hero'
  },
  {
    path: '/hero',
    name: 'Hero',
    component: () => import('../views/HeroPage.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/layout',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: 'smart-plan',
        name: 'SmartPlan',
        component: () => import('../views/SmartPlan.vue')
      },
      {
        path: 'user-manage',
        name: 'UserManage',
        component: () => import('../views/UserManage.vue')
      },
      {
        path: 'admin-manage',
        name: 'AdminManage',
        component: () => import('../views/AdminManage.vue')
      },
      {
        path: 'health-record',
        name: 'HealthRecord',
        component: () => import('../views/HealthRecord.vue')
      },
      {
        path: 'analytics',
        name: 'Analytics',
        component: () => import('../views/Analytics.vue')
      },
      {
        path: 'forum',
        name: 'Forum',
        component: () => import('../views/Forum.vue')
      },
      {
        path: 'user-info',
        name: 'UserInfo',
        component: () => import('../views/UserInfo.vue')
      },
      {
        path: 'admin-home',
        name: 'AdminHome',
        component: () => import('../views/AdminHome.vue')
      },
      {
        path: 'admin-health',
        name: 'AdminHealth',
        component: () => import('../views/AdminHealth.vue')
      },
      {
        path: 'admin-forum',
        name: 'AdminForum',
        component: () => import('../views/AdminForumManage.vue')
      },
      {
        path: 'my-content',
        name: 'MyContent',
        component: () => import('../views/MyContent.vue')
      },
      {
        path: 'ai-advisor',
        name: 'AiAdvisor',
        component: () => import('../views/AiAdvisorPage.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
