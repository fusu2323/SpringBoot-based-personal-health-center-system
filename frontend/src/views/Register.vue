<template>
  <div class="register-container">
    <div class="background-animation">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
    </div>

    <div class="register-box animate__animated animate__fadeInUp">
      <div class="title">
          <el-icon class="logo-icon"><UserFilled /></el-icon>
          欢迎注册
      </div>
      <p class="subtitle">Join Us Today</p>

      <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入账号" prefix-icon="User" class="custom-input" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password class="custom-input" />
        </el-form-item>
        <el-form-item prop="confirmPass">
          <el-input v-model="form.confirmPass" type="password" placeholder="请确认密码" prefix-icon="Lock" show-password class="custom-input" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="register-btn" @click="register" :loading="loading">
              {{ loading ? '注 册 中...' : '立 即 注 册' }}
          </el-button>
        </el-form-item>
        <div class="form-footer">
          <router-link to="/login" class="link">已有账号？去登录</router-link>
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
import { UserFilled, User, Lock } from '@element-plus/icons-vue'
import 'animate.css'

const router = useRouter()
const form = reactive({
  username: '',
  password: '',
  confirmPass: ''
})
const formRef = ref(null)
const loading = ref(false)

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPass: [{ validator: validatePass, trigger: 'blur' }]
}

const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      request.post('/register', form).then(res => {
        loading.value = false
        if (res.code === '200') {
          ElMessage.success('注册成功')
          router.push('/login')
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
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  background-color: #f0f2f5;
}

/* 复用登录页的动画背景样式 */
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
    background: #67C23A; /* 绿色系 */
    top: -50px;
    right: -50px;
    animation-delay: 0s;
}
.shape-2 {
    width: 400px;
    height: 400px;
    background: #409EFF;
    bottom: -100px;
    left: -100px;
    animation-delay: 2s;
}
.shape-3 {
    width: 200px;
    height: 200px;
    background: #F56C6C;
    top: 30%;
    left: 40%;
    animation-delay: 4s;
}

@keyframes float {
    0%, 100% { transform: translateY(0) scale(1); }
    50% { transform: translateY(-20px) scale(1.1); }
}

.register-box {
  width: 400px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
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
    color: #67C23A;
}
.subtitle {
    text-align: center;
    color: #909399;
    font-size: 14px;
    margin-bottom: 30px;
    letter-spacing: 1px;
}

.register-form {
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
    border-color: #67C23A;
    background-color: white;
    box-shadow: 0 0 0 1px #67C23A inset;
}

.register-btn {
    width: 100%;
    padding: 22px 0;
    font-size: 16px;
    border-radius: 8px;
    background: linear-gradient(90deg, #67C23A 0%, #529b2e 100%);
    border: none;
    transition: transform 0.2s;
}
.register-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(103, 194, 58, 0.4);
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
    color: #67C23A;
}
</style>
