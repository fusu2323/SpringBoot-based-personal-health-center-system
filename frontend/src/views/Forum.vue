<template>
  <div class="forum-container">
    <!-- Decorative blob -->
    <div class="blob blob-1"></div>

    <!-- Header -->
    <div class="forum-header">
      <div class="header-content">
        <h1 class="forum-title">健康社区</h1>
        <p class="forum-subtitle">分享心得，交流经验，一起迈向健康生活</p>
      </div>
    </div>

    <div class="post-list" v-if="!showDetail">
      <!-- 搜索栏 -->
      <div class="search-bar">
          <div class="search-wrapper">
              <div class="search-input-wrapper">
                  <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="11" cy="11" r="8"/>
                      <path d="m21 21-4.35-4.35"/>
                  </svg>
                  <input
                      v-model="searchKeyword"
                      placeholder="搜索感兴趣的话题..."
                      class="search-input"
                      @keyup.enter="loadPosts"
                  />
              </div>
              <button class="btn btn-ghost" @click="resetSearch">重置</button>
          </div>
          <button class="btn btn-primary" @click="handlePublish">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M12 20h9"/>
                  <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/>
              </svg>
              发布新帖
          </button>
      </div>

      <!-- 瀑布流帖子列表 -->
      <div class="masonry-grid" ref="masonryGrid">
          <div
              v-for="(post, index) in posts"
              :key="post.id"
              class="masonry-item"
              :style="{ animationDelay: index * 0.06 + 's' }"
              @click="viewDetail(post)"
          >
              <div class="post-card">
                  <div class="card-image">
                      <img :src="getCoverImg(post.id)" :alt="post.title" loading="lazy" />
                      <div class="image-overlay">
                          <span class="view-count">
                              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                                  <circle cx="12" cy="12" r="3"/>
                              </svg>
                              {{ post.viewCount }}
                          </span>
                      </div>
                  </div>
                  <div class="card-body">
                      <h3 class="card-title">{{ post.title }}</h3>
                      <p class="card-excerpt">{{ getTextContent(post.content) }}</p>
                      <div class="card-meta">
                          <div class="author">
                              <el-avatar :size="24" :src="post.userAvatar || defaultAvatar" />
                              <span class="author-name">{{ post.userNickname }}</span>
                          </div>
                          <span class="publish-time">{{ formatDate(post.createTime) }}</span>
                      </div>
                  </div>
              </div>
          </div>
      </div>

      <div v-if="posts.length === 0" class="empty-state">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"/>
          </svg>
          <p>暂无帖子，快来发布第一条吧！</p>
      </div>
    </div>

    <!-- 详情页 -->
    <div class="post-detail" v-else>
      <div class="detail-nav">
          <button class="btn btn-ghost back-btn" @click="showDetail = false">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="m15 18-6-6 6-6"/>
              </svg>
              返回列表
          </button>
      </div>

      <div class="detail-card">
        <div class="detail-header">
          <h1 class="detail-title">{{ currentPost.title }}</h1>
          <div class="detail-meta">
             <div class="meta-left">
                 <el-avatar :size="44" :src="currentPost.userAvatar" />
                 <div class="meta-info">
                     <span class="meta-author">{{ currentPost.userNickname }}</span>
                     <span class="meta-time">{{ currentPost.createTime }}</span>
                 </div>
             </div>
             <div class="meta-right">
                 <span class="view-badge">
                     <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                         <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                         <circle cx="12" cy="12" r="3"/>
                     </svg>
                     {{ currentPost.viewCount }} 阅读
                 </span>
             </div>
          </div>
        </div>
        <div class="detail-content" v-html="currentPost.content"></div>
      </div>

      <!-- 评论区 -->
      <div class="comment-section">
        <div class="section-header">
            <h2 class="section-title">评论区</h2>
            <span class="comment-count">({{ comments.length }})</span>
        </div>

        <div class="comment-input-wrapper">
            <el-input
                v-model="commentContent"
                type="textarea"
                :rows="3"
                placeholder="友善评论，理性交流..."
                resize="none"
                class="comment-input"
            />
            <div class="input-actions">
                <button class="btn btn-primary" @click="submitComment">发表评论</button>
            </div>
        </div>

        <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <el-avatar :size="40" :src="comment.userAvatar || defaultAvatar" />
                <div class="comment-main">
                    <div class="comment-header">
                        <span class="comment-user">{{ comment.userNickname }}</span>
                        <span class="comment-time">{{ comment.createTime }}</span>
                    </div>
                    <div class="comment-body">{{ comment.content }}</div>
                </div>
            </div>
            <el-empty v-if="comments.length === 0" description="还没有人评论，抢个沙发吧~" :image-size="80" />
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
                <div class="editor-wrapper">
                    <div id="toolbar-container"></div>
                    <div id="editor-container"></div>
                </div>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <button class="btn btn-ghost" @click="publishDialogVisible = false">取消</button>
                <button class="btn btn-primary" @click="savePost">发布</button>
            </div>
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

const defaultAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'
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
            posts.value = res.data || []
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
        comments.value = res.data || []
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
    return html.replace(/<[^>]+>/g, "").substring(0, 100) + '...'
}

const getCoverImg = (id) => {
    return `https://picsum.photos/seed/${id + 100}/600/400`
}

const formatDate = (str) => {
    return str ? str.substring(0, 10) : ''
}
</script>

<style scoped>
.forum-container {
    padding: 0 40px 60px;
    max-width: 1400px;
    margin: 0 auto;
    position: relative;
}

/* Decorative Blob */
.blob {
    position: fixed;
    top: -100px;
    right: -150px;
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, rgba(139, 154, 125, 0.12) 0%, transparent 70%);
    border-radius: 50%;
    pointer-events: none;
    z-index: 0;
}

/* Forum Header */
.forum-header {
    padding: 60px 0 48px;
    text-align: center;
    position: relative;
    z-index: 1;
}

.header-content {
    animation: fadeInDown 0.8s ease-out;
}

.forum-title {
    font-family: var(--font-display);
    font-size: 42px;
    font-weight: 500;
    color: var(--color-ink);
    margin-bottom: 12px;
}

.forum-subtitle {
    font-size: 16px;
    color: var(--color-text);
    margin: 0;
}

/* Search Bar */
.search-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 20px;
    margin-bottom: 40px;
    position: relative;
    z-index: 1;
    animation: fadeInDown 0.8s ease-out 0.1s both;
}

.search-wrapper {
    display: flex;
    gap: 12px;
    flex: 1;
}

.search-input-wrapper {
    flex: 1;
    max-width: 400px;
    position: relative;
}

.search-icon {
    position: absolute;
    left: 16px;
    top: 50%;
    transform: translateY(-50%);
    width: 20px;
    height: 20px;
    stroke: var(--color-text-muted);
}

.search-input {
    width: 100%;
    padding: 14px 16px 14px 48px;
    border: 1px solid var(--color-sand);
    border-radius: 100px;
    font-size: 15px;
    font-family: var(--font-body);
    background: var(--color-white);
    transition: all 0.3s ease;
}

.search-input:focus {
    outline: none;
    border-color: var(--color-sage);
    box-shadow: 0 0 0 3px var(--color-primary-light);
}

/* Buttons */
.btn {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 12px 24px;
    border-radius: 100px;
    font-size: 14px;
    font-family: var(--font-body);
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    border: none;
}

.btn svg {
    width: 18px;
    height: 18px;
}

.btn-primary {
    background: var(--color-sage);
    color: white;
}

.btn-primary:hover {
    background: var(--color-sage-dark);
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(139, 154, 125, 0.3);
}

.btn-ghost {
    background: transparent;
    color: var(--color-text);
    border: 1px solid var(--color-sand);
}

.btn-ghost:hover {
    background: var(--color-cream);
    border-color: var(--color-sage);
}

/* Masonry Grid - 瀑布流布局 */
.masonry-grid {
    column-count: 3;
    column-gap: 28px;
    position: relative;
    z-index: 1;
}

.masonry-item {
    break-inside: avoid;
    margin-bottom: 28px;
    animation: fadeInUp 0.6s ease-out both;
}

/* Post Card */
.post-card {
    background: var(--color-white);
    border-radius: var(--card-radius);
    overflow: hidden;
    box-shadow: var(--card-shadow);
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.post-card:hover {
    transform: translateY(-8px) scale(1.02);
    box-shadow: var(--card-shadow-hover);
}

.card-image {
    position: relative;
    overflow: hidden;
    background: var(--color-cream);
}

.card-image img {
    width: 100%;
    height: auto;
    display: block;
    transition: transform 0.5s ease;
}

.post-card:hover .card-image img {
    transform: scale(1.08);
}

.image-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 12px 16px;
    background: linear-gradient(to top, rgba(0,0,0,0.5), transparent);
    display: flex;
    justify-content: flex-end;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.post-card:hover .image-overlay {
    opacity: 1;
}

.view-count {
    display: flex;
    align-items: center;
    gap: 6px;
    color: white;
    font-size: 13px;
}

.view-count svg {
    width: 16px;
    height: 16px;
}

.card-body {
    padding: 20px;
}

.card-title {
    font-family: var(--font-display);
    font-size: 18px;
    font-weight: 500;
    color: var(--color-ink);
    margin-bottom: 10px;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.card-excerpt {
    font-size: 14px;
    color: var(--color-text);
    line-height: 1.7;
    margin-bottom: 16px;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.card-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 14px;
    border-top: 1px solid var(--color-cream);
}

.author {
    display: flex;
    align-items: center;
    gap: 8px;
}

.author-name {
    font-size: 13px;
    font-weight: 500;
    color: var(--color-text);
}

.publish-time {
    font-size: 12px;
    color: var(--color-text-muted);
}

/* Empty State */
.empty-state {
    text-align: center;
    padding: 80px 20px;
    color: var(--color-text-muted);
}

.empty-state svg {
    width: 80px;
    height: 80px;
    margin-bottom: 20px;
    opacity: 0.5;
}

.empty-state p {
    font-size: 16px;
    margin: 0;
}

/* Detail Page */
.post-detail {
    position: relative;
    z-index: 1;
}

.detail-nav {
    margin-bottom: 24px;
}

.back-btn {
    padding: 10px 20px;
}

.back-btn svg {
    width: 18px;
    height: 18px;
}

.detail-card {
    background: var(--color-white);
    border-radius: var(--card-radius-lg);
    padding: 48px;
    box-shadow: var(--card-shadow);
    margin-bottom: 32px;
}

.detail-header {
    margin-bottom: 32px;
    padding-bottom: 24px;
    border-bottom: 1px solid var(--color-cream);
}

.detail-title {
    font-family: var(--font-display);
    font-size: 36px;
    font-weight: 500;
    color: var(--color-ink);
    line-height: 1.3;
    margin-bottom: 24px;
}

.detail-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.meta-left {
    display: flex;
    align-items: center;
    gap: 14px;
}

.meta-info {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.meta-author {
    font-weight: 600;
    color: var(--color-ink);
    font-size: 15px;
}

.meta-time {
    font-size: 13px;
    color: var(--color-text-muted);
}

.view-badge {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
    background: var(--color-cream);
    border-radius: 100px;
    font-size: 13px;
    color: var(--color-text);
}

.view-badge svg {
    width: 16px;
    height: 16px;
}

.detail-content {
    font-size: 16px;
    line-height: 2;
    color: var(--color-text);
}

.detail-content :deep(p) {
    margin-bottom: 16px;
}

.detail-content :deep(strong) {
    color: var(--color-ink);
    font-weight: 600;
}

/* Comment Section */
.comment-section {
    background: var(--color-white);
    border-radius: var(--card-radius-lg);
    padding: 40px;
    box-shadow: var(--card-shadow);
}

.section-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 24px;
}

.section-title {
    font-family: var(--font-display);
    font-size: 24px;
    font-weight: 500;
    color: var(--color-ink);
    margin: 0;
}

.comment-count {
    color: var(--color-text-muted);
    font-size: 14px;
}

.comment-input-wrapper {
    background: var(--color-cream);
    padding: 24px;
    border-radius: var(--card-radius);
    margin-bottom: 32px;
}

.comment-input :deep(.el-textarea__inner) {
    background: var(--color-white);
    border: 1px solid var(--color-sand);
    border-radius: 12px;
    font-family: var(--font-body);
    resize: none;
}

.comment-input :deep(.el-textarea__inner:focus) {
    border-color: var(--color-sage);
    box-shadow: 0 0 0 3px var(--color-primary-light);
}

.input-actions {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
}

.comment-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.comment-item {
    display: flex;
    gap: 14px;
    padding-bottom: 20px;
    border-bottom: 1px solid var(--color-cream);
}

.comment-item:last-child {
    border-bottom: none;
    padding-bottom: 0;
}

.comment-main {
    flex: 1;
}

.comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 6px;
}

.comment-user {
    font-weight: 600;
    font-size: 14px;
    color: var(--color-ink);
}

.comment-time {
    font-size: 12px;
    color: var(--color-text-muted);
}

.comment-body {
    font-size: 14px;
    color: var(--color-text);
    line-height: 1.7;
}

/* Editor */
.editor-wrapper {
    border: 1px solid var(--color-sand);
    border-radius: var(--card-radius);
    overflow: hidden;
}

:deep(#toolbar-container) {
    border-bottom: 1px solid var(--color-sand);
    background: var(--color-cream);
}

:deep(#editor-container) {
    height: 350px;
    overflow-y: auto;
}

/* Dialog */
:deep(.publish-dialog) {
    border-radius: var(--card-radius-lg);
}

:deep(.publish-dialog .el-dialog__header) {
    padding: 24px 32px;
    border-bottom: 1px solid var(--color-cream);
}

:deep(.publish-dialog .el-dialog__body) {
    padding: 32px;
}

:deep(.publish-dialog .el-dialog__footer) {
    padding: 20px 32px;
    border-top: 1px solid var(--color-cream);
}

.publish-title-input :deep(.el-input__inner) {
    font-size: 18px;
    font-weight: 500;
    height: 50px;
    border: 1px solid var(--color-sand);
    border-radius: var(--card-radius);
}

.publish-title-input :deep(.el-input__inner:focus) {
    border-color: var(--color-sage);
}

/* Animations */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes fadeInDown {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* Responsive */
@media (max-width: 1200px) {
    .masonry-grid {
        column-count: 2;
    }
}

@media (max-width: 768px) {
    .forum-container {
        padding: 0 24px 40px;
    }

    .forum-header {
        padding: 40px 0 32px;
    }

    .forum-title {
        font-size: 32px;
    }

    .search-bar {
        flex-direction: column;
        align-items: stretch;
    }

    .search-wrapper {
        flex-direction: column;
    }

    .search-input-wrapper {
        max-width: none;
    }

    .masonry-grid {
        column-count: 1;
    }

    .detail-card {
        padding: 32px 24px;
    }

    .detail-title {
        font-size: 28px;
    }

    .comment-section {
        padding: 24px;
    }
}
</style>
