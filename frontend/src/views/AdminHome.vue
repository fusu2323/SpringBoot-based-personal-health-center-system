<template>
  <div class="admin-home-container">
    <!-- 欢迎卡片 -->
    <el-card class="welcome-card animate__animated animate__fadeInDown">
        <div class="welcome-content">
            <div class="welcome-left">
                <el-avatar :size="64" :src="user.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" class="avatar" />
                <div class="welcome-text">
                    <h2>早安，{{ user.nickname }}，祝你今天工作愉快！</h2>
                    <p>超级管理员 | 个人健康管理系统</p>
                </div>
            </div>
            <div class="welcome-right">
               <div class="stat-item">
                   <div class="stat-title">系统公告</div>
                   <div class="stat-value">{{ tableData.length }}</div>
               </div>
            </div>
        </div>
    </el-card>

    <!-- 公告管理卡片 -->
    <el-card class="notice-card animate__animated animate__fadeInUp">
        <template #header>
            <div class="card-header">
                <div class="header-title">
                    <el-icon><Bell /></el-icon>
                    <span>系统公告管理</span>
                </div>
                <el-button type="primary" @click="handleAdd" icon="Plus">发布新公告</el-button>
            </div>
        </template>
        
        <el-table :data="tableData" stripe style="width: 100%" class="custom-table">
            <el-table-column prop="createTime" label="发布时间" width="200">
                <template #default="scope">
                    <el-icon><Timer /></el-icon> {{ scope.row.createTime }}
                </template>
            </el-table-column>
            <el-table-column prop="title" label="公告标题" width="250">
                <template #default="scope">
                    <span style="font-weight: 600; color: #333;">{{ scope.row.title }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="content" label="公告内容" show-overflow-tooltip />
            <el-table-column label="操作" width="120" fixed="right">
                <template #default="scope">
                    <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)" icon="Delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-card>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" title="发布新公告" width="500px" custom-class="custom-dialog">
        <el-form :model="form" label-width="80px" class="dialog-form">
            <el-form-item label="标题">
                <el-input v-model="form.title" placeholder="请输入公告标题" />
            </el-form-item>
            <el-form-item label="内容">
                <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入公告内容" resize="none" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="save">立即发布</el-button>
            </span>
        </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell, Plus, Delete, Timer } from '@element-plus/icons-vue'
import 'animate.css'

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}
const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({
    title: '',
    content: ''
})

const load = () => {
    request.get('/home/notice/all').then(res => {
        if (res.code === '200') {
            tableData.value = res.data
        }
    })
}

onMounted(() => {
    load()
})

const handleAdd = () => {
    form.title = ''
    form.content = ''
    dialogVisible.value = true
}

const handleDelete = (id) => {
    ElMessageBox.confirm('确认删除该公告吗?', '删除警告', { 
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning' 
    }).then(() => {
        request.delete('/home/notice/' + id).then(res => {
            if (res.code === '200') {
                ElMessage.success('删除成功')
                load()
            }
        })
    })
}

const save = () => {
    if (!form.title || !form.content) {
        ElMessage.warning('请填写完整信息')
        return
    }
    const data = { ...form, adminId: user.id }
    request.post('/home/notice/add', data).then(res => {
        if (res.code === '200') {
            ElMessage.success('发布成功')
            dialogVisible.value = false
            load()
        } else {
            ElMessage.error(res.msg)
        }
    })
}
</script>

<style scoped>
.admin-home-container {
    padding: 10px;
}

/* 欢迎卡片样式 */
.welcome-card {
    margin-bottom: 24px;
    border: none;
    background: linear-gradient(135deg, #fff 0%, #f0f7ff 100%);
}
.welcome-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.welcome-left {
    display: flex;
    align-items: center;
    gap: 20px;
}
.avatar {
    border: 3px solid #fff;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.welcome-text h2 {
    margin: 0 0 8px 0;
    font-size: 20px;
    color: #303133;
}
.welcome-text p {
    margin: 0;
    color: #909399;
    font-size: 14px;
}
.welcome-right {
    display: flex;
    gap: 40px;
    padding-right: 20px;
}
.stat-item {
    text-align: center;
}
.stat-title {
    color: #909399;
    font-size: 14px;
    margin-bottom: 5px;
}
.stat-value {
    font-size: 24px;
    font-weight: bold;
    color: #409EFF;
}

/* 公告卡片样式 */
.notice-card {
    border: none;
}
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.header-title {
    font-size: 18px;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
    color: #303133;
}

/* 表格样式优化 */
.custom-table {
    margin-top: 10px;
}
:deep(.el-table th) {
    background-color: #f5f7fa !important;
    color: #606266;
    font-weight: 600;
}
:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
    background-color: #fafafa;
}
</style>
