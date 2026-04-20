<template>
  <div class="ai-advisor-page">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="bg-circle bg-circle-1"></div>
      <div class="bg-circle bg-circle-2"></div>
      <div class="bg-circle bg-circle-3"></div>
    </div>

    <div class="page-container">
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <div class="header-avatar">
            <svg viewBox="0 0 48 48" fill="none">
              <circle cx="24" cy="24" r="24" fill="var(--color-primary-light)"/>
              <path d="M24 12a7 7 0 0 1 7 7c0 2.38-1.19 4.47-3 5.74V28a2 2 0 0 1-2 2h-4a2 2 0 0 1-2-2v-3.26C18.19 23.47 17 21.38 17 19a7 7 0 0 1 7-7z" fill="var(--color-sage)"/>
              <path d="M19 32h10M24 29v3" stroke="var(--color-white)" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="header-text">
            <h1>AI 健康顾问</h1>
            <p>您的专属健康助手，根据您的身体指标和运动记录提供个性化建议</p>
          </div>
        </div>
        <div class="header-status">
          <span class="status-badge">
            <span class="status-dot"></span>
            AI 在线
          </span>
        </div>
      </div>

      <!-- 快捷入口 -->
      <div class="quick-actions">
        <button
          v-for="action in quickActions"
          :key="action.text"
          class="quick-btn"
          @click="sendQuickMessage(action.text)"
        >
          <span class="quick-icon">{{ action.icon }}</span>
          <span>{{ action.text }}</span>
        </button>
      </div>

      <!-- 主内容区 -->
      <div class="main-content">
        <!-- 左侧：推荐话题 -->
        <div class="sidebar">
          <div class="sidebar-section">
            <h3 class="sidebar-title">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M12 2a7 7 0 0 1 7 7c0 2.38-1.19 4.47-3 5.74V17a2 2 0 0 1-2 2h-4a2 2 0 0 1-2-2v-2.26C6.19 13.47 5 11.38 5 9a7 7 0 0 1 7-7z"/>
                <path d="M9 21h6"/>
                <path d="M12 17v4"/>
              </svg>
              推荐话题
            </h3>
            <ul class="topic-list">
              <li @click="sendQuickMessage('如何改善睡眠质量？')">
                <span class="topic-icon">🌙</span>
                <span>睡眠改善建议</span>
              </li>
              <li @click="sendQuickMessage('适合久坐族的运动有哪些？')">
                <span class="topic-icon">🏃</span>
                <span>办公室运动</span>
              </li>
              <li @click="sendQuickMessage('如何通过饮食调理身体？')">
                <span class="topic-icon">🥗</span>
                <span>健康饮食指南</span>
              </li>
              <li @click="sendQuickMessage('压力太大怎么缓解？')">
                <span class="topic-icon">🧘</span>
                <span>压力管理</span>
              </li>
              <li @click="sendQuickMessage('如何提高免疫力？')">
                <span class="topic-icon">💪</span>
                <span>增强免疫力</span>
              </li>
            </ul>
          </div>

          <div class="sidebar-section">
            <h3 class="sidebar-title">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                <line x1="16" y1="2" x2="16" y2="6"/>
                <line x1="8" y1="2" x2="8" y2="6"/>
                <line x1="3" y1="10" x2="21" y2="10"/>
              </svg>
              健康小工具
            </h3>
            <ul class="tool-list">
              <li @click="sendQuickMessage('帮我计算BMI')">
                <span class="tool-icon">📊</span>
                <span>BMI 计算器</span>
              </li>
              <li @click="sendQuickMessage('今天应该吃什么？')">
                <span class="tool-icon">🍽️</span>
                <span>每日食谱</span>
              </li>
              <li @click="sendQuickMessage('给我制定一个运动计划')">
                <span class="tool-icon">📅</span>
                <span>运动计划</span>
              </li>
            </ul>
          </div>
        </div>

        <!-- 右侧：聊天区域 -->
        <div class="chat-section">
          <div class="chat-container">
            <!-- 消息列表 -->
            <div class="chat-messages" ref="chatMessagesRef">
              <!-- 欢迎消息 -->
              <div class="welcome-card" v-if="messages.length === 0">
                <div class="welcome-icon">
                  <svg viewBox="0 0 64 64" fill="none">
                    <circle cx="32" cy="32" r="32" fill="var(--color-primary-light)"/>
                    <path d="M32 16a9 9 0 0 1 9 9c0 3.06-1.53 5.75-3.85 7.38V37a2.5 2.5 0 0 1-2.5 2.5h-5.3a2.5 2.5 0 0 1-2.5-2.5v-4.62C22.38 34.75 20 31.06 20 25a9 9 0 0 1 9-9z" fill="var(--color-sage)"/>
                    <path d="M25 41h14M32 37v4" stroke="var(--color-white)" stroke-width="2.5" stroke-linecap="round"/>
                  </svg>
                </div>
                <h3>您好！我是您的专属健康顾问</h3>
                <p>我可以帮您分析健康数据、制定运动计划、提供饮食建议。请选择左侧话题或直接输入您的问题。</p>
              </div>

              <!-- 消息 -->
              <div
                v-for="(msg, index) in messages"
                :key="index"
                :class="['message-item', msg.role === 'user' ? 'user-message' : 'ai-message']"
              >
                <div class="message-avatar ai-avatar" v-if="msg.role === 'ai'">
                  <svg viewBox="0 0 32 32" fill="none">
                    <circle cx="16" cy="16" r="16" fill="var(--color-sage-light)"/>
                    <path d="M16 8a5 5 0 0 1 5 5c0 1.7-.85 3.2-2.14 4.1V20a1.5 1.5 0 0 1-1.5 1.5h-3a1.5 1.5 0 0 1-1.5-1.5v-2.9C11.85 16.2 11 14.7 11 13a5 5 0 0 1 5-5z" fill="var(--color-sage)"/>
                    <path d="M13 22h6M16 20v2" stroke="var(--color-white)" stroke-width="1.5" stroke-linecap="round"/>
                  </svg>
                </div>
                <div class="message-content">
                  <div class="message-bubble" :class="msg.role === 'user' ? 'user-bubble' : 'ai-bubble'">
                    <div v-if="msg.role === 'user'" class="bubble-text">{{ msg.content }}</div>
                    <div v-else v-html="renderMarkdown(msg.content)" class="bubble-text markdown-body"></div>
                  </div>
                  <span class="message-time">{{ msg.time }}</span>
                </div>
                <div class="message-avatar user-avatar" v-if="msg.role === 'user'">
                  <svg viewBox="0 0 32 32" fill="none">
                    <circle cx="16" cy="16" r="16" fill="var(--color-sage)"/>
                    <circle cx="16" cy="12" r="4" fill="var(--color-white)"/>
                    <path d="M8 26c0-4.4 3.6-8 8-8s8 3.6 8 8" fill="var(--color-white)"/>
                  </svg>
                </div>
              </div>

              <!-- 思考中 -->
              <div class="thinking-indicator" v-if="isThinking">
                <div class="message-avatar ai-avatar">
                  <svg viewBox="0 0 32 32" fill="none">
                    <circle cx="16" cy="16" r="16" fill="var(--color-sage-light)"/>
                    <path d="M16 8a5 5 0 0 1 5 5c0 1.7-.85 3.2-2.14 4.1V20a1.5 1.5 0 0 1-1.5 1.5h-3a1.5 1.5 0 0 1-1.5-1.5v-2.9C11.85 16.2 11 14.7 11 13a5 5 0 0 1 5-5z" fill="var(--color-sage)"/>
                  </svg>
                </div>
                <div class="thinking-dots">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>

            <!-- 输入区 -->
            <div class="chat-input">
              <div class="input-wrapper">
                <textarea
                  v-model="inputMessage"
                  placeholder="输入您的问题..."
                  @keydown.enter.exact.prevent="sendMessage"
                  :disabled="isThinking"
                  rows="1"
                ></textarea>
                <button
                  class="send-btn"
                  @click="sendMessage"
                  :disabled="isThinking || !inputMessage.trim()"
                >
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="22" y1="2" x2="11" y2="13"/>
                    <polygon points="22 2 15 22 11 13 2 9 22 2"/>
                  </svg>
                </button>
              </div>
              <p class="input-hint">按 Enter 发送，Shift + Enter 换行</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import MarkdownIt from 'markdown-it'

const md = new MarkdownIt({
  html: true,
  breaks: true,
  linkify: true
})

const inputMessage = ref('')
const messages = ref([])
const isThinking = ref(false)
const chatMessagesRef = ref(null)

const quickActions = [
  { icon: '🌙', text: '睡眠建议' },
  { icon: '🏃', text: '运动计划' },
  { icon: '🥗', text: '饮食推荐' },
  { icon: '📊', text: '健康报告' }
]

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}

const renderMarkdown = (text) => {
  if (!text) return ''
  return md.render(text.replace(/\\n/g, '\n'))
}

const getTimeString = () => {
  const now = new Date()
  return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`
}

const scrollToBottom = () => {
  nextTick(() => {
    if (chatMessagesRef.value) {
      chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
    }
  })
}

const sendQuickMessage = (text) => {
  if (isThinking.value) return
  inputMessage.value = text
  sendMessage()
}

const sendMessage = async () => {
  const text = inputMessage.value.trim()
  if (!text || isThinking.value) return

  messages.value.push({
    role: 'user',
    content: text,
    time: getTimeString()
  })
  inputMessage.value = ''
  scrollToBottom()

  isThinking.value = true
  const aiMsgIndex = messages.value.length
  messages.value.push({ role: 'ai', content: '', time: getTimeString() })

  const baseUrl = 'http://localhost:9090'
  const url = `${baseUrl}/ai/chat/stream?userId=${user.id}&message=${encodeURIComponent(text)}`

  const eventSource = new EventSource(url)

  eventSource.onmessage = (event) => {
    const data = event.data
    if (data === '[DONE]') {
      eventSource.close()
      isThinking.value = false
      return
    }
    messages.value[aiMsgIndex].content += data
    scrollToBottom()
  }

  eventSource.onerror = () => {
    eventSource.close()
    isThinking.value = false
    if (!messages.value[aiMsgIndex]?.content) {
      messages.value[aiMsgIndex].content = '抱歉，连接 AI 服务失败，请稍后重试。'
    }
  }
}
</script>

<style scoped>
.ai-advisor-page {
  min-height: calc(100vh - 72px);
  background: var(--color-bg);
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--color-sage-light) 0%, transparent 70%);
  opacity: 0.1;
}

.bg-circle-1 {
  width: 600px;
  height: 600px;
  top: -200px;
  right: -100px;
}

.bg-circle-2 {
  width: 400px;
  height: 400px;
  bottom: -100px;
  left: -100px;
}

.bg-circle-3 {
  width: 300px;
  height: 300px;
  top: 50%;
  left: 30%;
}

/* 页面容器 */
.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px;
  position: relative;
  z-index: 1;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
}

.header-content {
  display: flex;
  gap: 20px;
  align-items: center;
}

.header-avatar svg {
  width: 64px;
  height: 64px;
}

.header-text h1 {
  margin: 0 0 8px;
  font-size: 28px;
  font-weight: 700;
  color: var(--color-ink);
}

.header-text p {
  margin: 0;
  font-size: 15px;
  color: var(--color-text-secondary);
  max-width: 400px;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: var(--color-white);
  border-radius: 50px;
  font-size: 13px;
  font-weight: 500;
  color: var(--color-sage);
  box-shadow: 0 2px 12px rgba(139, 154, 125, 0.15);
}

.status-dot {
  width: 8px;
  height: 8px;
  background: #67C23A;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* 快捷入口 */
.quick-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
  flex-wrap: wrap;
}

.quick-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: var(--color-white);
  border: 2px solid var(--color-cream);
  border-radius: 50px;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text);
  cursor: pointer;
  transition: all 0.25s ease;
}

.quick-btn:hover {
  border-color: var(--color-sage);
  background: var(--color-sage);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(139, 154, 125, 0.3);
}

.quick-icon {
  font-size: 16px;
}

/* 主内容区 */
.main-content {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 32px;
}

/* 侧边栏 */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.sidebar-section {
  background: var(--color-white);
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 2px 16px rgba(139, 154, 125, 0.08);
}

.sidebar-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 16px;
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
}

.sidebar-title svg {
  width: 18px;
  height: 18px;
  color: var(--color-sage);
}

.topic-list,
.tool-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.topic-list li,
.tool-list li {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  color: var(--color-text);
}

.topic-list li:hover,
.tool-list li:hover {
  background: var(--color-primary-light);
  color: var(--color-sage-dark);
  transform: translateX(4px);
}

.topic-icon,
.tool-icon {
  font-size: 16px;
}

/* 聊天区域 */
.chat-section {
  display: flex;
  flex-direction: column;
}

.chat-container {
  flex: 1;
  background: var(--color-white);
  border-radius: 20px;
  box-shadow: 0 2px 16px rgba(139, 154, 125, 0.08);
  display: flex;
  flex-direction: column;
  height: calc(100vh - 300px);
  min-height: 500px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 欢迎卡片 */
.welcome-card {
  text-align: center;
  padding: 40px 20px;
  max-width: 400px;
  margin: auto;
}

.welcome-icon svg {
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
}

.welcome-card h3 {
  margin: 0 0 12px;
  font-size: 20px;
  font-weight: 600;
  color: var(--color-ink);
}

.welcome-card p {
  margin: 0;
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.6;
}

/* 消息 */
.message-item {
  display: flex;
  gap: 12px;
  max-width: 80%;
}

.user-message {
  flex-direction: row-reverse;
  margin-left: auto;
}

.message-avatar {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
}

.message-avatar svg {
  width: 100%;
  height: 100%;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-message .message-content {
  align-items: flex-end;
}

.message-bubble {
  padding: 14px 18px;
  border-radius: 18px;
  font-size: 14px;
  line-height: 1.6;
}

.user-bubble {
  background: linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.ai-bubble {
  background: var(--color-bg);
  color: var(--color-ink);
  border-bottom-left-radius: 4px;
}

.message-time {
  font-size: 11px;
  color: var(--color-text-muted);
  padding: 0 4px;
}

/* Markdown */
.markdown-body {
  font-size: 14px;
}

.markdown-body :deep(p) {
  margin: 0 0 8px;
}

.markdown-body :deep(p:last-child) {
  margin-bottom: 0;
}

.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  margin: 8px 0;
  padding-left: 20px;
}

.markdown-body :deep(strong) {
  color: var(--color-sage);
  font-weight: 600;
}

/* 思考中 */
.thinking-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
}

.thinking-dots {
  display: flex;
  gap: 4px;
  padding: 14px 18px;
  background: var(--color-bg);
  border-radius: 18px;
}

.thinking-dots span {
  width: 8px;
  height: 8px;
  background: var(--color-sage-light);
  border-radius: 50%;
  animation: thinking 1.4s ease-in-out infinite;
}

.thinking-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.thinking-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes thinking {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-6px); }
}

/* 输入区 */
.chat-input {
  padding: 20px 24px;
  border-top: 1px solid var(--color-cream);
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  background: var(--color-bg);
  border: 2px solid var(--color-cream);
  border-radius: 16px;
  padding: 4px;
  transition: border-color 0.2s;
}

.input-wrapper:focus-within {
  border-color: var(--color-sage-light);
}

.input-wrapper textarea {
  flex: 1;
  border: none;
  background: transparent;
  padding: 12px 16px;
  font-size: 14px;
  font-family: inherit;
  color: var(--color-ink);
  resize: none;
  outline: none;
  max-height: 120px;
  line-height: 1.5;
}

.input-wrapper textarea::placeholder {
  color: var(--color-text-muted);
}

.send-btn {
  width: 48px;
  height: 48px;
  border: none;
  background: linear-gradient(135deg, var(--color-sage) 0%, var(--color-sage-dark) 100%);
  border-radius: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  flex-shrink: 0;
}

.send-btn:hover:not(:disabled) {
  transform: scale(1.05);
  box-shadow: 0 4px 16px rgba(139, 154, 125, 0.3);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.send-btn svg {
  width: 20px;
  height: 20px;
  color: white;
}

.input-hint {
  margin: 8px 0 0;
  font-size: 11px;
  color: var(--color-text-muted);
  text-align: center;
}

/* 响应式 */
@media (max-width: 900px) {
  .main-content {
    grid-template-columns: 1fr;
  }

  .sidebar {
    order: 2;
    flex-direction: row;
    overflow-x: auto;
  }

  .sidebar-section {
    flex-shrink: 0;
    min-width: 250px;
  }

  .chat-section {
    order: 1;
  }

  .chat-container {
    height: calc(100vh - 350px);
    min-height: 400px;
  }

  .page-header {
    flex-direction: column;
    gap: 16px;
  }
}

@media (max-width: 600px) {
  .page-container {
    padding: 20px;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
  }

  .quick-actions {
    overflow-x: auto;
    flex-wrap: nowrap;
    padding-bottom: 8px;
  }

  .quick-btn {
    flex-shrink: 0;
  }
}
</style>
