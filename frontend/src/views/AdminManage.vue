<template>
  <div class="manage-container">
    <el-card class="manage-card animate__animated animate__fadeInUp">
        <template #header>
            <div class="card-header">
                <div class="header-left">
                    <el-icon><Avatar /></el-icon>
                    <span class="header-title">管理员管理</span>
                </div>
                <div class="header-right">
                    <el-button type="primary" @click="handleAdd" icon="Plus">新增管理员</el-button>
                </div>
            </div>
        </template>

        <el-table :data="tableData" stripe style="width: 100%" class="custom-table">
            <el-table-column prop="id" label="ID" width="80" align="center" />
            <el-table-column prop="username" label="用户名" width="150">
                <template #default="scope">
                    <el-tag size="small" type="success" effect="plain">{{ scope.row.username }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="nickname" label="昵称" width="150" />
            <el-table-column prop="phone" label="手机号" />
            <el-table-column label="操作" width="180" fixed="right">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="handleEdit(scope.row)" icon="Edit">编辑</el-button>
                    <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)" icon="Delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑管理员' : '新增管理员'" width="500px" custom-class="custom-dialog">
      <el-form :model="form" label-width="80px" class="dialog-form">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="!!form.id" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
         <el-form-item label="密码" v-if="!form.id">
          <el-input v-model="form.password" type="password" show-password placeholder="默认123456" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Avatar, Plus, Edit, Delete } from '@element-plus/icons-vue'
import 'animate.css'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({})

const load = () => {
  request.get('/admin/all').then(res => {
    if (res.code === '200') {
      tableData.value = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
  load()
})

const handleAdd = () => {
  Object.keys(form).forEach(key => delete form[key])
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该管理员吗?', '删除警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/admin/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

const save = () => {
  if (form.id) {
    request.put('/admin/update', form).then(res => {
      if (res.code === '200') {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    request.post('/admin/add', form).then(res => {
      if (res.code === '200') {
        ElMessage.success('新增成功')
        dialogVisible.value = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}
</script>

<style scoped>
.manage-container {
    padding: 10px;
}
.manage-card {
    border: none;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.header-left {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
}
.header-right {
    display: flex;
    gap: 12px;
}
.custom-table {
    margin-top: 10px;
}
:deep(.el-table th) {
    background-color: #f5f7fa !important;
    color: #606266;
    font-weight: 600;
}
</style>
