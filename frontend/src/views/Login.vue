<template>
  <div class="login-container">
    <div class="background-animation">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
    </div>
    
    <div class="login-box animate__animated animate__fadeInUp">
      <div class="title">
          <el-icon class="logo-icon"><Monitor /></el-icon>
          健康管理系统
      </div>
      <p class="subtitle">Personal Health Management</p>
      
      <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入账号" prefix-icon="User" class="custom-input" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password class="custom-input" />
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="form.role" class="custom-radio">
            <el-radio label="USER">普通用户</el-radio>
            <el-radio label="ADMIN">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="login" :loading="loading">
              {{ loading ? '登 录 中...' : '登 录' }}
          </el-button>
        </el-form-item>
        <div class="form-footer">
          <router-link to="/register" class="link">没有账号？去注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Monitor, User, Lock } from '@element-plus/icons-vue'
import 'animate.css'

const router = useRouter()
const form = reactive({
  username: '',
  password: '',
  role: 'USER'
})
const formRef = ref(null)
const loading = ref(false)

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      request.post('/login', form).then(res => {
        loading.value = false
        if (res.code === '200') {
          ElMessage.success('登录成功')
          const userData = res.data
          userData.role = form.role
          localStorage.setItem('user', JSON.stringify(userData))
          
          if (form.role === 'ADMIN') {
             router.push('/layout/admin-home')
          } else {
             router.push('/layout/forum')
          }
        } else {
          ElMessage.error(res.msg)
        }
      }).catch(() => {
          loading.value = false
          ElMessage.error('网络错误，请稍后重试')
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  background-color: #f0f2f5;
}

/* 动态背景动画 */
.background-animation {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 0;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.shape {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.6;
    animation: float 10s infinite ease-in-out;
}
.shape-1 {
    width: 300px;
    height: 300px;
    background: #409EFF;
    top: -50px;
    left: -50px;
    animation-delay: 0s;
}
.shape-2 {
    width: 400px;
    height: 400px;
    background: #67C23A;
    bottom: -100px;
    right: -100px;
    animation-delay: 2s;
}
.shape-3 {
    width: 200px;
    height: 200px;
    background: #E6A23C;
    top: 40%;
    left: 40%;
    animation-delay: 4s;
}

@keyframes float {
    0%, 100% { transform: translateY(0) scale(1); }
    50% { transform: translateY(-20px) scale(1.1); }
}

.login-box {
  width: 400px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px); /* 毛玻璃 */
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  z-index: 1;
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.title {
  text-align: center;
  font-size: 26px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
.logo-icon {
    color: #409EFF;
}
.subtitle {
    text-align: center;
    color: #909399;
    font-size: 14px;
    margin-bottom: 30px;
    letter-spacing: 1px;
}

.login-form {
    margin-top: 20px;
}

.custom-input :deep(.el-input__wrapper) {
    background-color: rgba(245, 247, 250, 0.5);
    border-radius: 8px;
    padding: 10px 15px;
    box-shadow: none;
    border: 1px solid #dcdfe6;
    transition: all 0.3s;
}
.custom-input :deep(.el-input__wrapper:hover),
.custom-input :deep(.el-input__wrapper.is-focus) {
    border-color: #409EFF;
    background-color: white;
    box-shadow: 0 0 0 1px #409EFF inset;
}

.custom-radio {
    display: flex;
    justify-content: center;
    width: 100%;
}

.login-btn {
    width: 100%;
    padding: 22px 0;
    font-size: 16px;
    border-radius: 8px;
    background: linear-gradient(90deg, #409EFF 0%, #3a8ee6 100%);
    border: none;
    transition: transform 0.2s;
}
.login-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
}

.form-footer {
    text-align: center;
    margin-top: 15px;
}
.link {
    color: #909399;
    text-decoration: none;
    font-size: 14px;
    transition: color 0.3s;
}
.link:hover {
    color: #409EFF;
}
</style>
