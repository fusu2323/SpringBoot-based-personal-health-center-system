<template>
  <el-card>
    <el-tabs v-model="activeName" @tab-click="loadData">
      <el-tab-pane label="帖子管理" name="post">
        <el-table :data="postList" stripe border>
            <el-table-column prop="userNickname" label="发布人" width="120" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="createTime" label="发布时间" width="180" sortable />
            <el-table-column prop="viewCount" label="浏览量" width="100" />
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button link type="primary" @click="viewPost(scope.row)">查看</el-button>
                    <el-button link type="danger" @click="handleDeletePost(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="评论管理" name="comment">
        <el-table :data="commentList" stripe border>
            <el-table-column prop="userNickname" label="评论人" width="120" />
            <el-table-column prop="postTitle" label="所属帖子" width="200" show-overflow-tooltip />
            <el-table-column prop="content" label="评论内容" show-overflow-tooltip />
            <el-table-column prop="createTime" label="评论时间" width="180" sortable />
            <el-table-column label="操作" width="100">
                <template #default="scope">
                    <el-button link type="danger" @click="handleDeleteComment(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 帖子详情弹窗 -->
    <el-dialog v-model="dialogVisible" :title="currentPost.title" width="800px">
        <div v-html="currentPost.content" style="min-height: 200px;"></div>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeName = ref('post')
const postList = ref([])
const commentList = ref([])
const dialogVisible = ref(false)
const currentPost = ref({})

const loadData = () => {
    if (activeName.value === 'post') {
        request.get('/forum/post/all').then(res => postList.value = res.data)
    } else {
        request.get('/forum/comment/all').then(res => commentList.value = res.data)
    }
}

onMounted(() => {
    loadData()
})

const viewPost = (post) => {
    currentPost.value = post
    dialogVisible.value = true
}

const handleDeletePost = (id) => {
    ElMessageBox.confirm('确认删除该帖子及其所有评论吗?', '警告', { type: 'warning' }).then(() => {
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
