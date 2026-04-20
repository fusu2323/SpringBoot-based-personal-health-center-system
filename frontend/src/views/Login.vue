<template>
  <div class="login-container" ref="loginContainer" :class="{ 'is-admin': form.role === 'ADMIN' }">
    <!-- Canvas Background -->
    <canvas ref="bgCanvas" class="bg-canvas"></canvas>

    <!-- Gradient Overlay -->
    <div class="bg-gradient-overlay"></div>

    <!-- Login Card -->
    <div class="login-card" ref="loginCard">
      <!-- Card Glow Effect -->
      <div class="card-glow"></div>

      <!-- Header Section -->
      <div class="card-header">
        <div class="logo-container">
          <div class="logo-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2z"/>
              <path d="M12 6v6l4 2"/>
              <circle cx="12" cy="12" r="3"/>
            </svg>
          </div>
          <div class="logo-pulse"></div>
        </div>
        <h1 class="title">健康管理系统</h1>
        <p class="subtitle">Personal Health Management</p>
      </div>

      <!-- Role Toggle -->
      <div class="role-toggle-container">
        <div class="role-toggle" ref="roleToggle">
          <div class="toggle-slider" ref="toggleSlider"></div>
          <button
            class="toggle-option"
            :class="{ active: form.role === 'USER' }"
            @click="switchRole('USER')"
          >
            <svg class="role-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            <span>普通用户</span>
          </button>
          <button
            class="toggle-option"
            :class="{ active: form.role === 'ADMIN' }"
            @click="switchRole('ADMIN')"
          >
            <svg class="role-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5z"/>
              <path d="M2 17l10 5 10-5"/>
              <path d="M2 12l10 5 10-5"/>
            </svg>
            <span>管理员</span>
          </button>
        </div>
        <div class="toggle-decoration">
          <div class="deco-line"></div>
          <div class="deco-dot"></div>
          <div class="deco-line"></div>
        </div>
      </div>

      <!-- Form Section -->
      <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
        <!-- Username Field -->
        <div class="form-field" ref="usernameField">
          <label class="field-label">
            <span class="label-text">账号</span>
            <span class="label-indicator"></span>
          </label>
          <div class="input-wrapper" :class="{ focused: usernameFocused, error: usernameError }">
            <div class="input-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
            </div>
            <el-input
              v-model="form.username"
              placeholder="请输入账号"
              @focus="usernameFocused = true"
              @blur="usernameFocused = false; validateUsername"
              class="custom-input"
            />
            <div class="input-decoration"></div>
          </div>
        </div>

        <!-- Password Field -->
        <div class="form-field" ref="passwordField">
          <label class="field-label">
            <span class="label-text">密码</span>
            <span class="label-indicator"></span>
          </label>
          <div class="input-wrapper" :class="{ focused: passwordFocused, error: passwordError }">
            <div class="input-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
            </div>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
              @focus="passwordFocused = true"
              @blur="passwordFocused = false; validatePassword"
              class="custom-input"
            />
            <div class="input-decoration"></div>
          </div>
        </div>

        <!-- Submit Button -->
        <div class="submit-section" ref="submitSection">
          <button
            class="login-btn"
            @click="login"
            :disabled="loading"
            ref="loginBtn"
          >
            <span class="btn-content" :class="{ loading }">
              <span class="btn-text" v-if="!loading">登 录</span>
              <span class="btn-loading" v-else>
                <span class="dot"></span>
                <span class="dot"></span>
                <span class="dot"></span>
              </span>
            </span>
            <span class="btn-shine"></span>
            <span class="btn-glow"></span>
          </button>
        </div>
      </el-form>

      <!-- Footer -->
      <div class="card-footer">
        <div class="footer-line">
          <span class="line"></span>
          <span class="line-text">还没有账号？</span>
          <span class="line"></span>
        </div>
        <router-link to="/register" class="register-link">
          <span class="link-text">立即注册</span>
          <svg class="link-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </router-link>
      </div>

      <!-- Decorative Elements -->
      <div class="corner-decoration top-left"></div>
      <div class="corner-decoration top-right"></div>
      <div class="corner-decoration bottom-left"></div>
      <div class="corner-decoration bottom-right"></div>
    </div>

    <!-- Bottom Brand -->
    <div class="brand-section" ref="brandSection">
      <span class="brand-text">Health Management System</span>
      <span class="brand-divider">|</span>
      <span class="brand-version">v2.0</span>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import anime from 'animejs'

const router = useRouter()
const loginContainer = ref(null)
const loginCard = ref(null)
const roleToggle = ref(null)
const toggleSlider = ref(null)
const usernameField = ref(null)
const passwordField = ref(null)
const submitSection = ref(null)
const loginBtn = ref(null)
const brandSection = ref(null)
const bgCanvas = ref(null)
let animationId = null
let ctx = null
let particles = []
let isAdmin = false

// Canvas animation
class Particle {
  constructor(canvas) {
    this.canvas = canvas
    this.x = Math.random() * canvas.width
    this.y = Math.random() * canvas.height
    this.vx = (Math.random() - 0.5) * 0.5
    this.vy = (Math.random() - 0.5) * 0.5
    this.radius = Math.random() * 2 + 1
    this.alpha = Math.random() * 0.5 + 0.2
  }

  update() {
    this.x += this.vx
    this.y += this.vy

    if (this.x < 0 || this.x > this.canvas.width) this.vx *= -1
    if (this.y < 0 || this.y > this.canvas.height) this.vy *= -1
  }

  draw(context, color) {
    context.beginPath()
    context.arc(this.x, this.y, this.radius, 0, Math.PI * 2)
    context.fillStyle = color.replace('1)', `${this.alpha})`)
    context.fill()
  }
}

class ConnectionLine {
  constructor(p1, p2) {
    this.p1 = p1
    this.p2 = p2
    this.alpha = 0
    this.maxAlpha = 0.15
    this.threshold = 150
  }

  update() {
    const dx = this.p1.x - this.p2.x
    const dy = this.p1.y - this.p2.y
    const dist = Math.sqrt(dx * dx + dy * dy)

    if (dist < this.threshold) {
      this.alpha = this.maxAlpha * (1 - dist / this.threshold)
    } else {
      this.alpha = 0
    }
  }

  draw(context, color) {
    if (this.alpha <= 0) return

    context.beginPath()
    context.moveTo(this.p1.x, this.p1.y)
    context.lineTo(this.p2.x, this.p2.y)
    context.strokeStyle = color.replace('1)', `${this.alpha})`)
    context.lineWidth = 1
    context.stroke()
  }
}

function initCanvas() {
  const canvas = bgCanvas.value
  if (!canvas) return

  ctx = canvas.getContext('2d')
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)

  // Create particles
  const particleCount = Math.min(80, Math.floor(window.innerWidth / 15))
  particles = []

  for (let i = 0; i < particleCount; i++) {
    particles.push(new Particle(canvas))
  }

  animateCanvas()
}

function resizeCanvas() {
  const canvas = bgCanvas.value
  if (!canvas) return

  canvas.width = window.innerWidth
  canvas.height = window.innerHeight
}

function getColor() {
  return isAdmin ? 'rgba(196, 125, 94, 1)' : 'rgba(139, 154, 125, 1)'
}

function animateCanvas() {
  if (!ctx || !bgCanvas.value) return

  const canvas = bgCanvas.value
  ctx.clearRect(0, 0, canvas.width, canvas.height)

  const color = getColor()

  // Draw connections first
  for (let i = 0; i < particles.length; i++) {
    for (let j = i + 1; j < particles.length; j++) {
      const line = new ConnectionLine(particles[i], particles[j])
      line.update()
      line.draw(ctx, color)
    }
  }

  // Draw and update particles
  particles.forEach(p => {
    p.update()
    p.draw(ctx, color)
  })

  animationId = requestAnimationFrame(animateCanvas)
}

function updateCanvasColor() {
  // Color will be picked up in next animation frame
}

const form = reactive({
  username: '',
  password: '',
  role: 'USER'
})

const formRef = ref(null)
const loading = ref(false)
const usernameFocused = ref(false)
const passwordFocused = ref(false)
const usernameError = ref(false)
const passwordError = ref(false)

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// Role switching animation
function switchRole(role) {
  if (form.role === role) return

  isAdmin = role === 'ADMIN'
  const slider = toggleSlider.value

  // Animate slider
  anime({
    targets: slider,
    translateX: isAdmin ? '100%' : '0%',
    duration: 400,
    easing: 'easeOutQuad'
  })

  // Role-specific animations - Admin has terracotta/slate theme, User has sage theme
  if (isAdmin) {
    // Toggle slider turns terracotta for admin
    anime({
      targets: slider,
      background: ['linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%)', 'linear-gradient(135deg, var(--color-terracotta) 0%, #A85C3D 100%)', 'linear-gradient(135deg, var(--color-terracotta) 0%, #A85C3D 100%)'],
      duration: 600,
      easing: 'easeOutQuad'
    })

    // Card glow turns warm for admin
    anime({
      targets: '.card-glow',
      background: 'radial-gradient(circle at 50% 0%, rgba(196, 125, 94, 0.4) 0%, transparent 50%)',
      duration: 400,
      easing: 'easeOutQuad'
    })

    // Logo icon changes to terracotta
    anime({
      targets: '.logo-icon',
      background: ['rgba(139, 154, 125, 0.15)', 'rgba(196, 125, 94, 0.2)', 'rgba(196, 125, 94, 0.2)'],
      borderColor: ['var(--color-sage)', 'var(--color-terracotta)', 'var(--color-terracotta)'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.logo-icon svg',
      stroke: ['#8B9A7D', '#C47D5E', '#C47D5E'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    // Button gradient changes
    anime({
      targets: '.login-btn',
      background: ['linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%)', 'linear-gradient(135deg, var(--color-terracotta) 0%, #A85C3D 100%)'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    // Input border turns terracotta
    anime({
      targets: '.input-wrapper',
      borderColor: ['var(--color-sand)', 'var(--color-terracotta-light)', 'var(--color-terracotta-light)'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    // Decorative elements change
    anime({
      targets: '.blob-1',
      background: ['radial-gradient(circle, rgba(139, 154, 125, 0.25) 0%, transparent 70%)', 'radial-gradient(circle, rgba(196, 125, 94, 0.3) 0%, transparent 70%)'],
      duration: 600,
      easing: 'easeOutQuad'
    })

    // Corner decorations
    anime({
      targets: '.corner-decoration',
      borderColor: ['rgba(139, 154, 125, 0.2)', 'rgba(196, 125, 94, 0.3)', 'rgba(196, 125, 94, 0.3)'],
      duration: 400,
      easing: 'easeOutQuad'
    })
  } else {
    // Revert to sage theme for user
    anime({
      targets: slider,
      background: ['linear-gradient(135deg, var(--color-terracotta) 0%, #A85C3D 100%)', 'linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%)'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.card-glow',
      background: 'radial-gradient(circle at 50% 0%, rgba(139, 154, 125, 0.3) 0%, transparent 50%)',
      duration: 400,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.logo-icon',
      background: ['rgba(196, 125, 94, 0.2)', 'rgba(139, 154, 125, 0.15)', 'rgba(139, 154, 125, 0.15)'],
      borderColor: ['var(--color-terracotta)', 'var(--color-sage)', 'var(--color-sage)'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.logo-icon svg',
      stroke: ['#C47D5E', '#8B9A7D', '#8B9A7D'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.login-btn',
      background: ['linear-gradient(135deg, var(--color-terracotta) 0%, #A85C3D 100%)', 'linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%)'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.input-wrapper',
      borderColor: ['var(--color-terracotta-light)', 'var(--color-sand)', 'var(--color-sand)'],
      duration: 400,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.blob-1',
      background: ['radial-gradient(circle, rgba(196, 125, 94, 0.3) 0%, transparent 70%)', 'radial-gradient(circle, rgba(139, 154, 125, 0.25) 0%, transparent 70%)'],
      duration: 600,
      easing: 'easeOutQuad'
    })

    anime({
      targets: '.corner-decoration',
      borderColor: ['rgba(196, 125, 94, 0.3)', 'rgba(139, 154, 125, 0.2)', 'rgba(139, 154, 125, 0.2)'],
      duration: 400,
      easing: 'easeOutQuad'
    })
  }

  form.role = role
}

function validateUsername() {
  if (!form.username) {
    usernameError.value = true
    anime({
      targets: '.form-field:nth-child(1) .input-wrapper',
      translateX: [-10, 10, -10, 10, 0],
      duration: 400,
      easing: 'easeOutQuad'
    })
  } else {
    usernameError.value = false
  }
}

function validatePassword() {
  if (!form.password) {
    passwordError.value = true
    anime({
      targets: '.form-field:nth-child(2) .input-wrapper',
      translateX: [-10, 10, -10, 10, 0],
      duration: 400,
      easing: 'easeOutQuad'
    })
  } else {
    passwordError.value = false
  }
}

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      loading.value = true

      // Button loading animation
      anime({
        targets: loginBtn.value,
        scale: [1, 0.95, 1],
        duration: 200,
        easing: 'easeOutQuad'
      })

      request.post('/login', form).then(res => {
        loading.value = false
        if (res.code === '200') {
          // Success animation
          anime({
            targets: '.login-card',
            translateY: [0, -20, 0],
            duration: 400,
            easing: 'easeOutQuad'
          })

          ElMessage.success('登录成功')

          const userData = res.data
          userData.role = form.role
          localStorage.setItem('user', JSON.stringify(userData))

          setTimeout(() => {
            if (form.role === 'ADMIN') {
              router.push('/layout/admin-home')
            } else {
              router.push('/layout/forum')
            }
          }, 600)
        } else {
          ElMessage.error(res.msg)
          // Error shake
          anime({
            targets: loginCard.value,
            translateX: [-15, 15, -15, 15, 0],
            duration: 500,
            easing: 'easeOutQuad'
          })
        }
      }).catch(() => {
        loading.value = false
        ElMessage.error('网络错误，请稍后重试')
        anime({
          targets: loginCard.value,
          translateX: [-15, 15, -15, 15, 0],
          duration: 500,
          easing: 'easeOutQuad'
        })
      })
    } else {
      // Shake all fields
      anime({
        targets: '.form-field',
        translateX: [-10, 10, -10, 10, 0],
        delay: anime.stagger(100),
        duration: 400,
        easing: 'easeOutQuad'
      })
    }
  })
}

onMounted(async () => {
  await nextTick()

  // Initial animations timeline
  const timeline = anime.timeline()

  // Background animation
  timeline.add({
    targets: '.bg-gradient-overlay',
    opacity: [0, 1],
    duration: 600,
    easing: 'easeOutQuad'
  })

  // Card entrance
  timeline.add({
    targets: loginCard.value,
    opacity: [0, 1],
    translateY: [60, 0],
    scale: [0.95, 1],
    duration: 800,
    easing: 'easeOutQuad'
  })
  .add({
    targets: '.card-glow',
    opacity: [0, 1],
    scale: [0.5, 1],
    duration: 600,
    easing: 'easeOutQuad'
  }, '-=400')
  .add({
    targets: '.card-header',
    opacity: [0, 1],
    translateY: [30, 0],
    duration: 600,
    easing: 'easeOutQuad'
  }, '-=500')

  // Logo animation
  timeline.add({
    targets: '.logo-icon',
    scale: [0, 1],
    rotate: [360, 0],
    duration: 800,
    easing: 'easeOutElastic(1, 0.5)'
  })
  .add({
    targets: '.logo-pulse',
    scale: [0, 1, 1.5, 0],
    opacity: [0, 1, 0.5, 0],
    duration: 1200,
    easing: 'easeOutQuad'
  }, '-=400')

  // Toggle animation
  timeline.add({
    targets: roleToggle.value,
    opacity: [0, 1],
    scaleX: [0, 1],
    duration: 500,
    easing: 'easeOutQuad'
  }, '-=400')

  // Set initial slider position
  if (toggleSlider.value) {
    anime.set(toggleSlider.value, { translateX: '0%' })
  }

  // Form fields stagger animation
  timeline.add({
    targets: '.form-field',
    opacity: [0, 1],
    translateY: [40, 0],
    delay: anime.stagger(150),
    duration: 600,
    easing: 'easeOutQuad'
  }, '-=200')

  // Submit button
  timeline.add({
    targets: submitSection.value,
    opacity: [0, 1],
    translateY: [20, 0],
    duration: 500,
    easing: 'easeOutQuad'
  }, '-=300')

  // Footer
  timeline.add({
    targets: '.card-footer',
    opacity: [0, 1],
    duration: 500,
    easing: 'easeOutQuad'
  }, '-=200')

  // Brand
  timeline.add({
    targets: brandSection.value,
    opacity: [0, 1],
    translateY: [20, 0],
    duration: 500,
    easing: 'easeOutQuad'
  }, '-=300')

  // Init canvas animation
  initCanvas()

  // Input focus animations
  const inputElements = document.querySelectorAll('.input-wrapper')
  inputElements.forEach((wrapper) => {
    const input = wrapper.querySelector('.el-input')
    if (input) {
      input.addEventListener('focus', () => {
        const isAdmin = form.role === 'ADMIN'
        anime({
          targets: wrapper,
          boxShadow: isAdmin
            ? ['0 0 0 2px rgba(196, 125, 94, 0.3)', '0 0 0 3px rgba(196, 125, 94, 0.5)']
            : ['0 0 0 2px rgba(139, 154, 125, 0.3)', '0 0 0 3px rgba(139, 154, 125, 0.5)'],
          duration: 200,
          easing: 'easeOutQuad'
        })
      })
      input.addEventListener('blur', () => {
        anime({
          targets: wrapper,
          boxShadow: '0 0 0 2px transparent',
          duration: 200,
          easing: 'easeOutQuad'
        })
      })
    }
  })
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  padding: 40px 20px;
}

/* Canvas Background */
.bg-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.bg-gradient-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(253, 251, 247, 0.9) 0%, rgba(245, 241, 232, 0.85) 50%, rgba(237, 232, 218, 0.9) 100%);
  z-index: 1;
  pointer-events: none;
  transition: background 0.6s ease;
}

.is-admin .bg-gradient-overlay {
  background: linear-gradient(135deg, rgba(248, 246, 249, 0.9) 0%, rgba(240, 236, 244, 0.85) 50%, rgba(232, 228, 237, 0.9) 100%);
}

/* Login Card */
.login-card {
  position: relative;
  width: 100%;
  max-width: 440px;
  padding: 48px 40px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 32px;
  box-shadow: 0 25px 50px rgba(139, 154, 125, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.8);
  z-index: 10;
  opacity: 0;
}

.card-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: radial-gradient(circle at 50% 0%, rgba(139, 154, 125, 0.3) 0%, transparent 50%);
  border-radius: 34px;
  opacity: 0;
  z-index: -1;
}

/* Corner Decorations */
.corner-decoration {
  position: absolute;
  width: 30px;
  height: 30px;
  border: 2px solid rgba(139, 154, 125, 0.2);
}

.corner-decoration.top-left {
  top: 16px;
  left: 16px;
  border-right: none;
  border-bottom: none;
  border-radius: 8px 0 0 0;
}

.corner-decoration.top-right {
  top: 16px;
  right: 16px;
  border-left: none;
  border-bottom: none;
  border-radius: 0 8px 0 0;
}

.corner-decoration.bottom-left {
  bottom: 16px;
  left: 16px;
  border-right: none;
  border-top: none;
  border-radius: 0 0 0 8px;
}

.corner-decoration.bottom-right {
  bottom: 16px;
  right: 16px;
  border-left: none;
  border-top: none;
  border-radius: 0 0 8px 0;
}

/* Card Header */
.card-header {
  text-align: center;
  margin-bottom: 36px;
}

.logo-container {
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
}

.logo-icon {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(139, 154, 125, 0.15) 0%, rgba(139, 154, 125, 0.05) 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid var(--color-sage);
  opacity: 0;
}

.logo-icon svg {
  width: 40px;
  height: 40px;
  stroke: var(--color-sage);
}

.logo-pulse {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  border: 2px solid var(--color-sage);
  border-radius: 50%;
  opacity: 0;
}

.title {
  font-family: var(--font-display);
  font-size: 28px;
  font-weight: 500;
  color: var(--color-ink);
  margin-bottom: 8px;
  letter-spacing: 2px;
}

.subtitle {
  font-family: var(--font-body);
  font-size: 13px;
  color: var(--color-text-muted);
  letter-spacing: 3px;
  text-transform: uppercase;
  margin: 0;
}

/* Role Toggle */
.role-toggle-container {
  margin-bottom: 32px;
}

.role-toggle {
  display: flex;
  position: relative;
  background: var(--color-cream);
  border-radius: 100px;
  padding: 4px;
  opacity: 0;
}

.toggle-slider {
  position: absolute;
  top: 4px;
  left: 4px;
  width: calc(50% - 4px);
  height: calc(100% - 8px);
  background: linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%);
  border-radius: 100px;
  box-shadow: 0 4px 15px rgba(139, 154, 125, 0.3);
  z-index: 0;
}

.toggle-option {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 16px;
  background: none;
  border: none;
  cursor: pointer;
  position: relative;
  z-index: 1;
  transition: color 0.3s ease;
  border-radius: 100px;
}

.toggle-option span {
  font-family: var(--font-body);
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-muted);
  transition: color 0.3s ease;
}

.toggle-option.active span {
  color: white;
}

.role-icon {
  width: 18px;
  height: 18px;
  stroke: var(--color-text-muted);
  transition: stroke 0.3s ease;
}

.toggle-option.active .role-icon {
  stroke: white;
}

.toggle-decoration {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 12px;
}

.deco-line {
  width: 40px;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--color-sand), transparent);
  transition: background 0.4s ease;
}

.is-admin .deco-line {
  background: linear-gradient(90deg, transparent, var(--color-terracotta-light), transparent);
}

.deco-dot {
  width: 6px;
  height: 6px;
  background: var(--color-sage);
  border-radius: 50%;
  opacity: 0.5;
  transition: background 0.4s ease;
}

.is-admin .deco-dot {
  background: var(--color-terracotta);
}

/* Form */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-field {
  opacity: 0;
}

.field-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.label-text {
  font-family: var(--font-body);
  font-size: 13px;
  font-weight: 500;
  color: var(--color-text);
  letter-spacing: 1px;
}

.label-indicator {
  width: 20px;
  height: 2px;
  background: var(--color-sage);
  border-radius: 1px;
  transition: background 0.4s ease;
}

.is-admin .label-indicator {
  background: var(--color-terracotta);
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  background: var(--color-white);
  border: 2px solid var(--color-sand);
  border-radius: 16px;
  padding: 4px 16px;
  transition: all 0.3s ease;
}

.input-wrapper.focused {
  border-color: var(--color-sage);
  background: var(--color-white);
}

.is-admin .input-wrapper.focused {
  border-color: var(--color-terracotta);
}

.input-wrapper.error {
  border-color: var(--color-terracotta);
}

.input-icon {
  width: 20px;
  height: 20px;
  margin-right: 12px;
  color: var(--color-text-muted);
  flex-shrink: 0;
}

.input-icon svg {
  width: 100%;
  height: 100%;
}

.input-wrapper :deep(.el-input) {
  flex: 1;
}

.input-wrapper :deep(.el-input__wrapper) {
  background: transparent !important;
  box-shadow: none !important;
  padding: 0 !important;
  border-radius: 0 !important;
}

.input-wrapper :deep(.el-input__inner) {
  font-family: var(--font-body);
  font-size: 15px;
  color: var(--color-ink);
}

.input-wrapper :deep(.el-input__inner::placeholder) {
  color: var(--color-text-muted);
}

.input-decoration {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--color-sage), var(--color-sage-light));
  transition: width 0.3s ease, background 0.4s ease;
  border-radius: 1px;
}

.input-wrapper.focused .input-decoration {
  width: calc(100% - 40px);
}

.is-admin .input-decoration {
  background: linear-gradient(90deg, var(--color-terracotta), var(--color-terracotta-light));
}

/* Submit Section */
.submit-section {
  margin-top: 8px;
  opacity: 0;
}

.login-btn {
  position: relative;
  width: 100%;
  padding: 18px 32px;
  background: linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%);
  border: none;
  border-radius: 100px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(139, 154, 125, 0.35);
}

.login-btn:active {
  transform: translateY(0);
}

.login-btn:disabled {
  cursor: not-allowed;
  opacity: 0.8;
}

.btn-content {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-text {
  font-family: var(--font-body);
  font-size: 16px;
  font-weight: 600;
  color: white;
  letter-spacing: 4px;
}

.btn-loading {
  display: flex;
  gap: 6px;
}

.btn-loading .dot {
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
  animation: dotPulse 1.4s ease-in-out infinite;
}

.btn-loading .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.btn-loading .dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes dotPulse {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.btn-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  z-index: 1;
  animation: btnShine 3s ease-in-out infinite;
}

@keyframes btnShine {
  0%, 100% { left: -100%; }
  50% { left: 100%; }
}

.btn-glow {
  position: absolute;
  bottom: -50%;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 100%;
  background: radial-gradient(ellipse, rgba(255,255,255,0.3) 0%, transparent 70%);
  z-index: 0;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.login-btn:hover .btn-glow {
  opacity: 1;
}

/* Footer */
.card-footer {
  margin-top: 28px;
  text-align: center;
  opacity: 0;
}

.footer-line {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 16px;
}

.footer-line .line {
  width: 50px;
  height: 1px;
  background: var(--color-sand);
}

.footer-line .line-text {
  font-size: 13px;
  color: var(--color-text-muted);
}

.register-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 24px;
  background: var(--color-cream);
  border-radius: 100px;
  text-decoration: none;
  transition: all 0.3s ease;
}

.register-link:hover {
  background: var(--color-sage);
}

.register-link:hover .link-text {
  color: white;
}

.register-link:hover .link-icon {
  stroke: white;
  transform: translateX(4px);
}

.link-text {
  font-family: var(--font-body);
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text);
  transition: color 0.3s ease;
}

.link-icon {
  width: 16px;
  height: 16px;
  stroke: var(--color-text);
  transition: all 0.3s ease;
}

/* Brand Section */
.brand-section {
  position: fixed;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 12px;
  opacity: 0;
  z-index: 1;
}

.brand-text {
  font-family: var(--font-body);
  font-size: 12px;
  color: var(--color-text-muted);
  letter-spacing: 1px;
}

.brand-divider {
  color: var(--color-sand);
}

.brand-version {
  font-family: var(--font-display);
  font-size: 12px;
  font-style: italic;
  color: var(--color-sage);
}

/* Responsive */
@media (max-width: 480px) {
  .login-card {
    padding: 36px 24px;
    border-radius: 24px;
  }

  .title {
    font-size: 24px;
  }

  .logo-container {
    width: 64px;
    height: 64px;
  }

  .logo-icon svg {
    width: 32px;
    height: 32px;
  }

  .shape {
    display: none;
  }

  .floating-shapes {
    display: none;
  }
}
</style>
