<template>
  <div class="forum-container">
    <div class="post-list" v-if="!showDetail">
      <!-- 搜索栏 -->
      <div class="search-bar animate__animated animate__fadeInDown">
          <div class="search-wrapper">
              <el-input v-model="searchKeyword" placeholder="🔍 搜索感兴趣的话题..." class="custom-input" @keyup.enter="loadPosts">
                  <template #append>
                      <el-button @click="loadPosts">查询</el-button>
                  </template>
              </el-input>
              <el-button type="primary" plain class="reset-btn" @click="resetSearch">重置</el-button>
          </div>
          <el-button type="primary" class="publish-btn" @click="handlePublish">
              <el-icon><EditPen /></el-icon> 发布新帖
          </el-button>
      </div>

      <!-- 帖子列表 (卡片式) -->
      <el-row :gutter="24" class="post-grid">
          <el-col :span="12" v-for="(post, index) in posts" :key="post.id" class="animate__animated animate__fadeInUp" :style="{ animationDelay: index * 0.1 + 's' }">
              <el-card shadow="never" :body-style="{ padding: '0px' }" class="post-card" @click="viewDetail(post)">
                  <div class="card-content">
                      <div class="card-img-wrapper">
                          <img :src="getCoverImg(post.id)" alt="cover" class="card-img" />
                          <div class="img-overlay">
                              <span class="view-tag"><el-icon><View /></el-icon> {{ post.viewCount }}</span>
                          </div>
                      </div>
                      <div class="card-info">
                          <div class="info-top">
                              <h3 class="card-title" :title="post.title">{{ post.title }}</h3>
                              <p class="card-desc">{{ getTextContent(post.content) }}</p>
                          </div>
                          <div class="card-meta">
                              <div class="author">
                                  <el-avatar :size="28" :src="post.userAvatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" />
                                  <span class="author-name">{{ post.userNickname }}</span>
                              </div>
                              <span class="publish-time">{{ formatDate(post.createTime) }}</span>
                          </div>
                      </div>
                  </div>
              </el-card>
          </el-col>
      </el-row>
      <el-empty v-if="posts.length === 0" description="暂无帖子，快来发布第一条吧！" />
    </div>

    <div class="post-detail animate__animated animate__fadeInRight" v-else>
      <div class="detail-nav">
          <el-button link @click="showDetail = false" class="back-btn">
              <el-icon><ArrowLeft /></el-icon> 返回列表
          </el-button>
      </div>
      
      <el-card class="detail-card" shadow="never">
        <template #header>
          <div class="detail-header">
            <h1 class="detail-title">{{ currentPost.title }}</h1>
            <div class="detail-meta">
               <div class="meta-left">
                   <el-avatar :size="32" :src="currentPost.userAvatar" />
                   <span class="meta-author">{{ currentPost.userNickname }}</span>
                   <span class="meta-time">{{ currentPost.createTime }}</span>
               </div>
               <div class="meta-right">
                   <el-tag size="small" effect="plain"><el-icon><View /></el-icon> {{ currentPost.viewCount }} 阅读</el-tag>
               </div>
            </div>
          </div>
        </template>
        <div class="detail-content w-e-text-container" v-html="currentPost.content"></div>
      </el-card>

      <div class="comment-section">
        <div class="section-title">
            <span>评论区</span>
            <span class="comment-count">({{ comments.length }})</span>
        </div>
        
        <div class="comment-input-wrapper">
            <el-input 
                v-model="commentContent" 
                type="textarea" 
                :rows="3" 
                placeholder="友善评论，理性交流..." 
                resize="none"
            />
            <div class="input-actions">
                <el-button type="primary" @click="submitComment">发表评论</el-button>
            </div>
        </div>
        
        <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <el-avatar :size="40" :src="comment.userAvatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" />
                <div class="comment-main">
                    <div class="comment-header">
                        <span class="comment-user">{{ comment.userNickname }}</span>
                        <span class="comment-time">{{ comment.createTime }}</span>
                    </div>
                    <div class="comment-content">{{ comment.content }}</div>
                </div>
            </div>
            <el-empty v-if="comments.length === 0" description="还没有人评论，抢个沙发吧~" :image-size="100" />
        </div>
      </div>
    </div>

    <!-- 发布帖子弹窗 -->
    <el-dialog v-model="publishDialogVisible" title="发布新帖" width="800px" custom-class="publish-dialog" top="5vh">
        <el-form :model="postForm" label-width="0">
            <el-form-item>
                <el-input v-model="postForm.title" placeholder="请输入标题（最多50字）" class="publish-title-input" maxlength="50" show-word-limit />
            </el-form-item>
            <el-form-item>
                <div style="border: 1px solid #dcdfe6; border-radius: 4px; width: 100%; overflow: hidden;">
                    <div id="toolbar-container" style="border-bottom: 1px solid #dcdfe6;"></div>
                    <div id="editor-container" style="height: 400px; overflow-y: hidden;"></div>
                </div>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="publishDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="savePost">发布</el-button>
            </span>
        </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, shallowRef, onBeforeUnmount, nextTick } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'
import '@wangeditor/editor/dist/css/style.css'
import { createEditor, createToolbar } from '@wangeditor/editor'
import { View, EditPen, ArrowLeft } from '@element-plus/icons-vue'
import 'animate.css'

const posts = ref([])
const showDetail = ref(false)
const currentPost = ref({})
const comments = ref([])
const commentContent = ref('')
const publishDialogVisible = ref(false)
const searchKeyword = ref('')
const postForm = reactive({
    title: '',
    content: ''
})

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}

// WangEditor 实例
const editorRef = shallowRef()

const initEditor = () => {
    if (editorRef.value) {
        editorRef.value.destroy()
        editorRef.value = null
    }

    const editorConfig = { 
        placeholder: '分享你的健康心得...',
        onChange(editor) {
          const html = editor.getHtml()
          postForm.content = html
        }
    }

    const editor = createEditor({
      selector: '#editor-container',
      html: postForm.content || '',
      config: editorConfig,
      mode: 'default', 
    })

    const toolbarConfig = {}
    const toolbar = createToolbar({
      editor,
      selector: '#toolbar-container',
      config: toolbarConfig,
      mode: 'default', 
    })
    
    editorRef.value = editor
}

onBeforeUnmount(() => {
    const editor = editorRef.value
    if (editor == null) return
    editor.destroy()
})

const loadPosts = () => {
    request.get('/forum/post/all', { params: { title: searchKeyword.value } }).then(res => {
        if (res.code === '200') {
            posts.value = res.data
        }
    })
}

const resetSearch = () => {
    searchKeyword.value = ''
    loadPosts()
}

onMounted(() => {
    loadPosts()
})

const handlePublish = () => {
    postForm.title = ''
    postForm.content = ''
    publishDialogVisible.value = true
    nextTick(() => {
        initEditor()
    })
}

const savePost = () => {
    if (!postForm.title || !postForm.content) {
        ElMessage.warning('标题和内容不能为空')
        return
    }
    const data = { ...postForm, userId: user.id }
    request.post('/forum/post/save', data).then(res => {
        if (res.code === '200') {
            ElMessage.success('发布成功')
            publishDialogVisible.value = false
            loadPosts()
        } else {
             ElMessage.error(res.msg)
        }
    })
}

const viewDetail = (post) => {
    request.get('/forum/post/' + post.id).then(res => {
        currentPost.value = res.data
        showDetail.value = true
        loadComments(post.id)
    })
}

const loadComments = (postId) => {
    request.get('/forum/comment/' + postId).then(res => {
        comments.value = res.data
    })
}

const submitComment = () => {
    if (!commentContent.value) return
    const data = {
        postId: currentPost.value.id,
        userId: user.id,
        content: commentContent.value
    }
    request.post('/forum/comment/add', data).then(res => {
        if (res.code === '200') {
            ElMessage.success('评论成功')
            commentContent.value = ''
            loadComments(currentPost.value.id)
        } else {
             ElMessage.error(res.msg)
        }
    })
}

const getTextContent = (html) => {
    if (!html) return ''
    return html.replace(/<[^>]+>/g, "").substring(0, 60) + '...'
}

const getCoverImg = (id) => {
    return `https://picsum.photos/seed/${id}/400/300`
}

const formatDate = (str) => {
    return str ? str.substring(0, 10) : ''
}
</script>

<style scoped>
.forum-container {
    padding: 10px 20px;
    max-width: 1200px;
    margin: 0 auto;
}

/* 搜索栏样式 */
.search-bar {
    background-color: white;
    padding: 20px 24px;
    margin-bottom: 24px;
    border-radius: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 4px 20px rgba(0,0,0,0.03);
}
.search-wrapper {
    display: flex;
    gap: 12px;
    align-items: center;
}
.custom-input {
    width: 320px;
}
.publish-btn {
    padding: 10px 24px;
    font-weight: 500;
    border-radius: 8px;
    transition: all 0.3s;
}
.publish-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(64,158,255,0.3);
}

/* 帖子卡片样式 */
.post-grid {
    margin-bottom: 20px;
}
.post-card {
    cursor: pointer;
    border: none;
    border-radius: 12px;
    overflow: hidden;
    background: white;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 2px 12px rgba(0,0,0,0.04);
    height: 160px; /* 固定高度 */
    margin-bottom: 24px;
}
.post-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 32px rgba(0,0,0,0.08);
}
.card-content {
    display: flex;
    height: 100%;
}
.card-img-wrapper {
    width: 220px;
    height: 100%;
    position: relative;
    overflow: hidden;
}
.card-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s;
}
.post-card:hover .card-img {
    transform: scale(1.05);
}
.img-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    padding: 8px;
    background: linear-gradient(to top, rgba(0,0,0,0.6), transparent);
    display: flex;
    justify-content: flex-end;
}
.view-tag {
    color: white;
    font-size: 12px;
    display: flex;
    align-items: center;
    gap: 4px;
}
.card-info {
    flex: 1;
    padding: 20px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}
.card-title {
    margin: 0 0 8px 0;
    font-size: 18px;
    font-weight: 600;
    color: #2c3e50;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
.card-desc {
    color: #666;
    font-size: 14px;
    line-height: 1.6;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
.card-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    border-top: 1px solid #f0f2f5;
}
.author {
    display: flex;
    align-items: center;
    gap: 8px;
}
.author-name {
    font-size: 13px;
    font-weight: 500;
    color: #606266;
}
.publish-time {
    font-size: 12px;
    color: #909399;
}

/* 详情页样式优化 */
.detail-nav {
    margin-bottom: 16px;
}
.back-btn {
    font-size: 15px;
    color: #606266;
}
.back-btn:hover {
    color: #409EFF;
}
.detail-card {
    border-radius: 12px;
    border: none;
    box-shadow: 0 4px 20px rgba(0,0,0,0.03);
}
.detail-header {
    padding-bottom: 10px;
}
.detail-title {
    font-size: 28px;
    color: #1a1a1a;
    margin-bottom: 20px;
    line-height: 1.4;
}
.detail-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.meta-left {
    display: flex;
    align-items: center;
    gap: 12px;
}
.meta-author {
    font-weight: 600;
    color: #333;
}
.meta-time {
    color: #999;
    font-size: 13px;
}
.detail-content {
    padding: 30px 0;
    font-size: 16px;
    color: #2c3e50;
    line-height: 1.8;
}

/* 评论区样式 */
.comment-section {
    margin-top: 30px;
    background: white;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.03);
}
.section-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 8px;
}
.comment-count {
    color: #909399;
    font-size: 14px;
}
.comment-input-wrapper {
    background: #f5f7fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
}
.input-actions {
    margin-top: 12px;
    text-align: right;
}
.comment-item {
    display: flex;
    gap: 16px;
    padding: 20px 0;
    border-bottom: 1px solid #f0f2f5;
}
.comment-item:last-child {
    border-bottom: none;
}
.comment-main {
    flex: 1;
}
.comment-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
}
.comment-user {
    font-weight: 600;
    font-size: 14px;
    color: #333;
}
.comment-time {
    font-size: 12px;
    color: #999;
}
.comment-content {
    font-size: 14px;
    color: #555;
    line-height: 1.6;
}

/* 发布弹窗样式 */
:deep(.publish-dialog .el-dialog__header) {
    margin-right: 0;
    border-bottom: 1px solid #f0f2f5;
    padding: 20px 24px;
}
:deep(.publish-dialog .el-dialog__footer) {
    border-top: 1px solid #f0f2f5;
    padding: 16px 24px;
}
.publish-title-input :deep(.el-input__inner) {
    font-size: 18px;
    font-weight: 500;
    height: 50px;
}
</style>
