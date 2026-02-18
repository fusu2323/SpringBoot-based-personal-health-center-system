<template>
  <div class="ai-advisor-container">
    <!-- 悬浮按钮 -->
    <div class="float-btn" @click="toggleChat" v-if="!visible">
        <el-icon :size="30" color="white"><ChatDotSquare /></el-icon>
        <span class="btn-text">AI 顾问</span>
    </div>

    <!-- 聊天窗口 -->
    <transition name="el-fade-in-linear">
        <div class="chat-window" v-if="visible">
            <div class="chat-header">
                <div class="header-title">
                    <el-icon><Service /></el-icon> 
                    <span style="margin-left: 5px;">AI 健康顾问</span>
                </div>
                <el-icon class="close-btn" @click="toggleChat"><Close /></el-icon>
            </div>
            
            <div class="chat-body" ref="chatBodyRef">
                <div v-for="(msg, index) in messages" :key="index" :class="['message-item', msg.role === 'user' ? 'user-msg' : 'ai-msg']">
                    <div class="avatar">
                        <el-avatar :size="30" :icon="msg.role === 'user' ? 'UserFilled' : 'Service'" :style="{ backgroundColor: msg.role === 'user' ? '#409EFF' : '#67C23A' }" />
                    </div>
                    <div class="content">
                        <div v-if="msg.role === 'user'">{{ msg.content }}</div>
                        <!-- AI 回复支持 Markdown -->
                        <div v-else v-html="renderMarkdown(msg.content)" class="markdown-body"></div>
                    </div>
                </div>
                <div v-if="isThinking" class="thinking">AI 正在思考中...</div>
            </div>

            <div class="chat-footer">
                <el-input 
                    v-model="inputMessage" 
                    placeholder="输入您的问题..." 
                    @keyup.enter="sendMessage"
                    :disabled="isThinking"
                >
                    <template #append>
                        <el-button @click="sendMessage" :disabled="isThinking || !inputMessage.trim()">
                            <el-icon><Position /></el-icon>
                        </el-button>
                    </template>
                </el-input>
            </div>
        </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { UserFilled, Service, Close, ChatDotSquare, Position } from '@element-plus/icons-vue'
import MarkdownIt from 'markdown-it'

const md = new MarkdownIt({
    html: true,
    breaks: true, // 关键：将 \n 转换为 <br>
    linkify: true
})
const visible = ref(false)
const inputMessage = ref('')
const messages = ref([
    { role: 'ai', content: '您好！我是您的专属健康顾问。我会根据您的身体指标和运动记录为您提供建议。请问有什么可以帮您？' }
])
const isThinking = ref(false)
const chatBodyRef = ref(null)

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}

const toggleChat = () => {
    visible.value = !visible.value
}

const scrollToBottom = () => {
    nextTick(() => {
        if (chatBodyRef.value) {
            chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
        }
    })
}

const renderMarkdown = (text) => {
    // 预处理：SSE 传输时换行符可能被转义为 \\n，需要替换回 \n
    // 如果后端直接传的是 \n (真实换行)，SSE event.data 可能被截断或处理
    // 但通常 event.data 是字符串。
    // 关键点：AI 返回的流式数据是片段，拼接后整体渲染 Markdown
    // 额外处理：如果 SSE 传回的是字面量 "\\n"，则替换为 "\n"
    if (!text) return ''
    const processedText = text.replace(/\\n/g, '\n')
    return md.render(processedText)
}

const sendMessage = async () => {
    const text = inputMessage.value.trim()
    if (!text || isThinking.value) return

    // 1. 添加用户消息
    messages.value.push({ role: 'user', content: text })
    inputMessage.value = ''
    scrollToBottom()

    // 2. 准备 AI 消息占位
    isThinking.value = true
    const aiMsgIndex = messages.value.length
    messages.value.push({ role: 'ai', content: '' }) // 先放空字符串

    // 3. 发起 SSE 请求
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
        
        // SSE 规范中，换行符可能被作为事件分隔。
        // Spring SSE 发送的是 data: content\n\n
        // 如果内容本身包含换行，Spring 默认可能会处理。
        // 这里直接拼接 data
        
        // 修正：有时候 data 里的换行符会被转义，或者 SSE 自动去掉了换行
        // 如果后端发送时没有特殊处理换行，浏览器收到的 data 可能是一行
        // 我们尝试将 JSON 字符串中的换行符还原 (如果后端发的是 JSON)
        // 但这里后端发的是 raw string。
        // 简单处理：直接拼接，依赖 renderMarkdown 中的 replace
        
        // 另一种常见情况：SSE 将 \n 解析为数据结束，所以后端发送时通常会将 \n 替换为特殊标记或转义
        // 但 Spring SseEmitter.send() 会自动处理 data: 前缀。
        // 如果后端直接发 \n，浏览器 EventSource 可能会把它当做消息结束。
        // 让我们观察一下实际效果。通常 Spring AI 返回的片段不含换行，换行是单独的片段。
        
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
.ai-advisor-container {
    position: fixed;
    bottom: 30px;
    right: 30px;
    z-index: 9999;
}

.float-btn {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background-color: #409EFF;
    box-shadow: 0 4px 10px rgba(0,0,0,0.3);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: transform 0.3s;
}

.float-btn:hover {
    transform: scale(1.1);
}

.btn-text {
    color: white;
    font-size: 10px;
    margin-top: 2px;
}

.chat-window {
    width: 380px;
    height: 550px;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.2);
    display: flex;
    flex-direction: column;
    overflow: hidden;
    border: 1px solid #ebeef5;
}

.chat-header {
    height: 50px;
    background-color: #409EFF;
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 15px;
    font-size: 16px;
    font-weight: bold;
}

.close-btn {
    cursor: pointer;
}

.chat-body {
    flex: 1;
    padding: 15px;
    overflow-y: auto;
    background-color: #f5f7fa;
}

.message-item {
    display: flex;
    margin-bottom: 15px;
}

.user-msg {
    flex-direction: row-reverse;
}

.avatar {
    margin: 0 10px;
}

.content {
    max-width: 70%;
    background-color: white;
    padding: 10px 15px;
    border-radius: 10px;
    font-size: 14px;
    line-height: 1.5;
    word-break: break-all;
    box-shadow: 0 2px 5px rgba(0,0,0,0.05);
}

.user-msg .content {
    background-color: #ecf5ff; /* 浅蓝 */
    color: #333;
    border-top-right-radius: 0;
}

.ai-msg .content {
    background-color: white;
    border-top-left-radius: 0;
}

.thinking {
    font-size: 12px;
    color: #999;
    text-align: center;
    margin-bottom: 10px;
}

.chat-footer {
    padding: 10px;
    border-top: 1px solid #ebeef5;
    background-color: white;
}

/* Markdown 样式增强 */
:deep(.markdown-body) {
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica,Arial,sans-serif;
}
:deep(.markdown-body p) {
    margin-bottom: 8px;
}
:deep(.markdown-body ul), :deep(.markdown-body ol) {
    padding-left: 20px;
    margin-bottom: 8px;
}
:deep(.markdown-body li) {
    margin-bottom: 4px;
}
:deep(.markdown-body strong) {
    font-weight: 600;
    color: #409EFF; /* 强调色 */
}
:deep(.markdown-body h1), :deep(.markdown-body h2), :deep(.markdown-body h3) {
    margin-top: 12px;
    margin-bottom: 8px;
    font-weight: 600;
    font-size: 1.1em;
}
</style>
