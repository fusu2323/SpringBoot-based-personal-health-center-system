<template>
  <div class="ai-advisor-container">
    <!-- 悬浮入口按钮 -->
    <div class="advisor-entry" @click="openPanel">
      <div class="entry-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
        </svg>
      </div>
      <div class="entry-text">
        <span class="entry-title">AI 健康顾问</span>
        <span class="entry-desc">专属健康助手</span>
      </div>
      <div class="entry-arrow">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M9 18l6-6-6-6"/>
        </svg>
      </div>
    </div>

    <!-- 全屏面板 -->
    <transition name="panel-slide">
      <div class="advisor-panel" v-if="visible">
        <div class="panel-overlay" @click="closePanel"></div>
        <div class="panel-content">
          <!-- 面板头部 -->
          <div class="panel-header">
            <div class="header-info">
              <div class="header-avatar">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M12 2a7 7 0 0 1 7 7c0 2.38-1.19 4.47-3 5.74V17a2 2 0 0 1-2 2h-4a2 2 0 0 1-2-2v-2.26C6.19 13.47 5 11.38 5 9a7 7 0 0 1 7-7z"/>
                  <path d="M9 21h6"/>
                  <path d="M12 17v4"/>
                </svg>
              </div>
              <div class="header-text">
                <h3>AI 健康顾问</h3>
                <p>
                  <span class="status-dot"></span>
                  在线 · 随时为您服务
                </p>
              </div>
            </div>
            <button class="close-btn" @click="closePanel">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M18 6L6 18M6 6l12 12"/>
              </svg>
            </button>
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

          <!-- 聊天区域 -->
          <div class="chat-area" ref="chatAreaRef">
            <div class="messages-container">
              <!-- 欢迎消息 -->
              <div class="welcome-message">
                <div class="welcome-icon">
                  <svg viewBox="0 0 48 48" fill="none">
                    <circle cx="24" cy="24" r="24" fill="var(--color-primary-light)"/>
                    <path d="M24 14a7 7 0 0 1 7 7c0 2.38-1.19 4.47-3 5.74V30a2 2 0 0 1-2 2h-4a2 2 0 0 1-2-2v-3.26C18.19 25.47 17 23.38 17 21a7 7 0 0 1 7-7z" fill="var(--color-sage)"/>
                    <path d="M19 32h10M24 29v3" stroke="var(--color-white)" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                </div>
                <div class="welcome-text">
                  <h4>您好！我是您的专属健康顾问</h4>
                  <p>根据您的身体指标和运动记录为您提供建议</p>
                </div>
              </div>

              <!-- 消息列表 -->
              <div
                v-for="(msg, index) in messages"
                :key="index"
                :class="['message-item', msg.role === 'user' ? 'user-message' : 'ai-message']"
              >
                <div class="message-avatar" v-if="msg.role === 'ai'">
                  <svg viewBox="0 0 32 32" fill="none">
                    <circle cx="16" cy="16" r="16" fill="var(--color-sage-light)"/>
                    <path d="M16 8a5 5 0 0 1 5 5c0 1.7-.85 3.2-2.14 4.1V20a1.5 1.5 0 0 1-1.5 1.5h-3a1.5 1.5 0 0 1-1.5-1.5v-2.9C11.85 16.2 11 14.7 11 13a5 5 0 0 1 5-5z" fill="var(--color-sage)"/>
                    <path d="M13 22h6M16 20v2" stroke="var(--color-white)" stroke-width="1.5" stroke-linecap="round"/>
                  </svg>
                </div>
                <div class="message-bubble">
                  <div v-if="msg.role === 'user'" class="bubble-content user-bubble">
                    {{ msg.content }}
                  </div>
                  <div v-else class="bubble-content ai-bubble">
                    <div v-html="renderMarkdown(msg.content)" class="markdown-content"></div>
                  </div>
                  <span class="message-time">{{ msg.time }}</span>
                </div>
                <div class="message-avatar user-avatar" v-if="msg.role === 'user'">
                  <svg viewBox="0 0 32 32" fill="none">
                    <circle cx="16" cy="16" r="16" fill="var(--color-sage)"/>
                    <circle cx="16" cy="12" r="5" fill="var(--color-white)"/>
                    <path d="M8 26c0-4.4 3.6-8 8-8s8 3.6 8 8" fill="var(--color-white)"/>
                  </svg>
                </div>
              </div>

              <!-- 思考中状态 -->
              <div class="thinking-indicator" v-if="isThinking">
                <div class="thinking-avatar">
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
          </div>

          <!-- 输入区域 -->
          <div class="input-area">
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
                  <path d="M22 2L11 13M22 2l-7 20-4-9-9-4 20-7z"/>
                </svg>
              </button>
            </div>
            <p class="input-hint">按 Enter 发送，Shift + Enter 换行</p>
          </div>
        </div>
      </div>
    </transition>
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

const visible = ref(false)
const inputMessage = ref('')
const messages = ref([])
const isThinking = ref(false)
const chatAreaRef = ref(null)

const quickActions = [
  { icon: '🌙', text: '睡眠建议' },
  { icon: '🏃', text: '运动计划' },
  { icon: '🥗', text: '饮食推荐' },
  { icon: '📊', text: '健康报告' }
]

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}

const openPanel = () => {
  visible.value = true
  document.body.style.overflow = 'hidden'
}

defineExpose({ openPanel })

const closePanel = () => {
  visible.value = false
  document.body.style.overflow = ''
}

const scrollToBottom = () => {
  nextTick(() => {
    if (chatAreaRef.value) {
      chatAreaRef.value.scrollTop = chatAreaRef.value.scrollHeight
    }
  })
}

const renderMarkdown = (text) => {
  if (!text) return ''
  const processedText = text.replace(/\\n/g, '\n')
  return md.render(processedText)
}

const getTimeString = () => {
  const now = new Date()
  return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`
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

  eventSource.onerror = (err) => {
    console.error('SSE Error:', err)
    eventSource.close()
    isThinking.value = false
    if (!messages.value[aiMsgIndex].content) {
      messages.value[aiMsgIndex].content = '抱歉，连接 AI 服务失败，请稍后重试。'
    }
  }
}
</script>

<style scoped>
/* CSS Variables */
.advisor-container {
  --sage: #8B9A7D;
  --sage-light: #A8B89A;
  --sage-dark: #6B7A5D;
  --cream: #F5F1E8;
  --bg: #FDFBF7;
  --white: #FFFFFF;
  --ink: #3D3D3D;
  --text: #5A5A5A;
  --text-muted: #94A3B8;
  --radius: 20px;
  --radius-sm: 12px;
  --shadow: 0 8px 32px rgba(61, 61, 61, 0.12);
  --shadow-lg: 0 20px 60px rgba(61, 61, 61, 0.15);
}

/* 悬浮入口 */
.advisor-entry {
  position: fixed;
  bottom: 30px;
  right: 30px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px 12px 12px;
  background: linear-gradient(135deg, var(--sage) 0%, var(--sage-dark) 100%);
  border-radius: 50px;
  color: white;
  cursor: pointer;
  box-shadow: var(--shadow);
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  z-index: 1000;
}

.advisor-entry:hover {
  transform: translateY(-3px) scale(1.02);
  box-shadow: var(--shadow-lg);
}

.entry-icon {
  width: 44px;
  height: 44px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.entry-icon svg {
  width: 22px;
  height: 22px;
}

.entry-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.entry-title {
  font-size: 15px;
  font-weight: 600;
}

.entry-desc {
  font-size: 11px;
  opacity: 0.8;
}

.entry-arrow {
  opacity: 0.6;
}

.entry-arrow svg {
  width: 18px;
  height: 18px;
}

/* 全屏面板 */
.advisor-panel {
  position: fixed;
  inset: 0;
  z-index: 9999;
  display: flex;
  justify-content: flex-end;
}

.panel-overlay {
  position: absolute;
  inset: 0;
  background: rgba(61, 61, 61, 0.4);
  backdrop-filter: blur(4px);
}

.panel-content {
  position: relative;
  width: 100%;
  max-width: 480px;
  height: 100%;
  background: var(--bg);
  display: flex;
  flex-direction: column;
  box-shadow: -10px 0 40px rgba(0, 0, 0, 0.1);
}

/* 面板头部 */
.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  background: var(--white);
  border-bottom: 1px solid var(--cream);
}

.header-info {
  display: flex;
  align-items: center;
  gap: 14px;
}

.header-avatar {
  width: 48px;
  height: 48px;
  background: var(--cream);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-avatar svg {
  width: 28px;
  height: 28px;
  color: var(--sage);
}

.header-text h3 {
  margin: 0;
  font-size: 17px;
  font-weight: 600;
  color: var(--ink);
}

.header-text p {
  margin: 4px 0 0;
  font-size: 12px;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  gap: 6px;
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

.close-btn {
  width: 40px;
  height: 40px;
  border: none;
  background: var(--cream);
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.close-btn:hover {
  background: var(--sage-light);
}

.close-btn:hover svg {
  color: white;
}

.close-btn svg {
  width: 18px;
  height: 18px;
  color: var(--text);
  transition: color 0.2s;
}

/* 快捷入口 */
.quick-actions {
  display: flex;
  gap: 8px;
  padding: 16px 24px;
  background: var(--white);
  border-bottom: 1px solid var(--cream);
  overflow-x: auto;
}

.quick-actions::-webkit-scrollbar {
  display: none;
}

.quick-btn {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  border: 1px solid var(--cream);
  background: var(--white);
  border-radius: 50px;
  font-size: 13px;
  color: var(--text);
  cursor: pointer;
  transition: all 0.2s;
}

.quick-btn:hover {
  border-color: var(--sage);
  background: var(--sage);
  color: white;
}

.quick-icon {
  font-size: 14px;
}

/* 聊天区域 */
.chat-area {
  flex: 1;
  overflow-y: auto;
  padding: 20px 24px;
  background: var(--bg);
}

.messages-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 欢迎消息 */
.welcome-message {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 20px;
  background: var(--white);
  border-radius: var(--radius);
  box-shadow: 0 2px 12px rgba(139, 154, 125, 0.08);
}

.welcome-icon svg {
  width: 48px;
  height: 48px;
}

.welcome-text h4 {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
  color: var(--ink);
}

.welcome-text p {
  margin: 6px 0 0;
  font-size: 13px;
  color: var(--text-muted);
  line-height: 1.5;
}

/* 消息项 */
.message-item {
  display: flex;
  gap: 10px;
  max-width: 85%;
}

.user-message {
  flex-direction: row-reverse;
  margin-left: auto;
}

.message-avatar {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
}

.message-avatar svg {
  width: 100%;
  height: 100%;
}

.message-bubble {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-message .message-bubble {
  align-items: flex-end;
}

.bubble-content {
  padding: 12px 16px;
  border-radius: var(--radius);
  font-size: 14px;
  line-height: 1.6;
}

.user-bubble {
  background: linear-gradient(135deg, var(--sage) 0%, var(--sage-dark) 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.ai-bubble {
  background: var(--white);
  color: var(--ink);
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(139, 154, 125, 0.1);
}

.message-time {
  font-size: 10px;
  color: var(--text-muted);
}

/* Markdown 内容 */
.markdown-content {
  font-size: 14px;
}

.markdown-content :deep(p) {
  margin: 0 0 8px;
}

.markdown-content :deep(p:last-child) {
  margin-bottom: 0;
}

.markdown-content :deep(ul),
.markdown-content :deep(ol) {
  margin: 8px 0;
  padding-left: 20px;
}

.markdown-content :deep(li) {
  margin: 4px 0;
}

.markdown-content :deep(strong) {
  color: var(--sage);
  font-weight: 600;
}

.markdown-content :deep(h1),
.markdown-content :deep(h2),
.markdown-content :deep(h3) {
  font-size: 15px;
  margin: 12px 0 8px;
  color: var(--ink);
}

/* 思考中 */
.thinking-indicator {
  display: flex;
  align-items: center;
  gap: 10px;
}

.thinking-avatar {
  width: 32px;
  height: 32px;
}

.thinking-avatar svg {
  width: 100%;
  height: 100%;
}

.thinking-dots {
  display: flex;
  gap: 4px;
  padding: 12px 16px;
  background: var(--white);
  border-radius: var(--radius);
  box-shadow: 0 2px 8px rgba(139, 154, 125, 0.1);
}

.thinking-dots span {
  width: 8px;
  height: 8px;
  background: var(--sage-light);
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

/* 输入区域 */
.input-area {
  padding: 16px 24px 20px;
  background: var(--white);
  border-top: 1px solid var(--cream);
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  background: var(--bg);
  border: 2px solid var(--cream);
  border-radius: var(--radius);
  padding: 4px;
  transition: border-color 0.2s;
}

.input-wrapper:focus-within {
  border-color: var(--sage-light);
}

.input-wrapper textarea {
  flex: 1;
  border: none;
  background: transparent;
  padding: 12px 16px;
  font-size: 14px;
  font-family: inherit;
  color: var(--ink);
  resize: none;
  outline: none;
  max-height: 120px;
  line-height: 1.5;
}

.input-wrapper textarea::placeholder {
  color: var(--text-muted);
}

.send-btn {
  width: 44px;
  height: 44px;
  border: none;
  background: linear-gradient(135deg, var(--sage) 0%, var(--sage-dark) 100%);
  border-radius: var(--radius-sm);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  flex-shrink: 0;
}

.send-btn:hover:not(:disabled) {
  transform: scale(1.05);
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
  color: var(--text-muted);
  text-align: center;
}

/* 过渡动画 */
.panel-slide-enter-active .panel-overlay,
.panel-slide-leave-active .panel-overlay {
  transition: opacity 0.3s ease;
}

.panel-slide-enter-active .panel-content,
.panel-slide-leave-active .panel-content {
  transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.panel-slide-enter-from .panel-overlay,
.panel-slide-leave-to .panel-overlay {
  opacity: 0;
}

.panel-slide-enter-from .panel-content,
.panel-slide-leave-to .panel-content {
  transform: translateX(100%);
}

/* 响应式 */
@media (max-width: 480px) {
  .advisor-entry {
    bottom: 20px;
    right: 20px;
    padding: 10px 16px 10px 10px;
  }

  .entry-text {
    display: none;
  }

  .panel-content {
    max-width: 100%;
  }
}
</style>
