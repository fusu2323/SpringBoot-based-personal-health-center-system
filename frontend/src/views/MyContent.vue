<template>
  <el-card>
    <el-tabs v-model="activeName" @tab-click="loadData">
      <el-tab-pane label="我的帖子" name="post">
        <el-table :data="postList" stripe>
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="createTime" label="发布时间" width="180" />
            <el-table-column prop="viewCount" label="浏览量" width="100" />
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button link type="danger" @click="handleDeletePost(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="我的评论" name="comment">
        <el-table :data="commentList" stripe>
            <el-table-column prop="postTitle" label="所属帖子" width="200" show-overflow-tooltip />
            <el-table-column prop="content" label="评论内容" show-overflow-tooltip />
            <el-table-column prop="createTime" label="评论时间" width="180" />
            <el-table-column label="操作" width="100">
                <template #default="scope">
                    <el-button link type="danger" @click="handleDeleteComment(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 编辑帖子弹窗 -->
    <el-dialog v-model="dialogVisible" title="编辑帖子" width="800px" :close-on-click-modal="false">
        <el-form :model="form" label-width="80px">
            <el-form-item label="标题">
                <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
            <el-form-item label="内容">
                <div style="border: 1px solid #ccc; width: 100%;">
                    <div id="my-toolbar-container" style="border-bottom: 1px solid #ccc;"></div>
                    <div id="my-editor-container" style="height: 400px; overflow-y: hidden;"></div>
                </div>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="savePost">确定</el-button>
            </span>
        </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, shallowRef, onBeforeUnmount, onMounted, nextTick } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import '@wangeditor/editor/dist/css/style.css'
import { createEditor, createToolbar } from '@wangeditor/editor'

const activeName = ref('post')
const postList = ref([])
const commentList = ref([])
const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}
const dialogVisible = ref(false)
const form = reactive({
    id: null,
    title: '',
    content: ''
})

// 编辑器实例
const editorRef = shallowRef()

const initEditor = () => {
    // 销毁旧实例
    if (editorRef.value) {
        editorRef.value.destroy()
        editorRef.value = null
    }

    const editorConfig = { 
        placeholder: '请输入内容...',
        onChange(editor) {
          const html = editor.getHtml()
          form.content = html
        }
    }

    // 注意：ID 要唯一，或者保证每次渲染只存在一个
    const editor = createEditor({
      selector: '#my-editor-container',
      html: form.content || '',
      config: editorConfig,
      mode: 'default', 
    })

    const toolbarConfig = {}
    const toolbar = createToolbar({
      editor,
      selector: '#my-toolbar-container',
      config: toolbarConfig,
      mode: 'default', 
    })
    
    editorRef.value = editor
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
    const editor = editorRef.value
    if (editor == null) return
    editor.destroy()
})

const loadData = () => {
    if (activeName.value === 'post') {
        request.get('/forum/post/my/' + user.id).then(res => postList.value = res.data)
    } else {
        request.get('/forum/comment/my/' + user.id).then(res => commentList.value = res.data)
    }
}

onMounted(() => {
    loadData()
})

const handleEdit = (row) => {
    form.id = row.id
    form.title = row.title
    form.content = row.content
    dialogVisible.value = true
    
    // 弹窗打开后初始化编辑器
    nextTick(() => {
        initEditor()
    })
}

const savePost = () => {
    if (!form.title || !form.content) {
        ElMessage.warning('标题和内容不能为空')
        return
    }
    
    request.post('/forum/post/save', { ...form, userId: user.id }).then(res => {
        if (res.code === '200') {
            ElMessage.success('更新成功')
            dialogVisible.value = false
            loadData()
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const handleDeletePost = (id) => {
    ElMessageBox.confirm('确认删除该帖子吗?', '提示', { type: 'warning' }).then(() => {
        request.delete('/forum/post/' + id).then(res => {
            if (res.code === '200') {
                ElMessage.success('删除成功')
                loadData()
            }
        })
    })
}

const handleDeleteComment = (id) => {
    ElMessageBox.confirm('确认删除该评论吗?', '提示', { type: 'warning' }).then(() => {
        request.delete('/forum/comment/' + id).then(res => {
            if (res.code === '200') {
                ElMessage.success('删除成功')
                loadData()
            }
        })
    })
}
</script>
