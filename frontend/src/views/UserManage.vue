<template>
  <div class="manage-container">
    <el-card class="manage-card animate__animated animate__fadeInUp">
        <template #header>
            <div class="card-header">
                <div class="header-left">
                    <el-icon><User /></el-icon>
                    <span class="header-title">用户管理</span>
                </div>
                <div class="header-right">
                    <el-input v-model="searchKeyword" placeholder="搜索用户名/昵称..." class="search-input" clearable @clear="load" @keyup.enter="load">
                        <template #prefix>
                            <el-icon><Search /></el-icon>
                        </template>
                    </el-input>
                    <el-button type="primary" @click="handleAdd" icon="Plus">新增用户</el-button>
                </div>
            </div>
        </template>

        <el-table :data="filterTableData" stripe style="width: 100%" class="custom-table">
            <el-table-column prop="id" label="ID" width="80" align="center" />
            <el-table-column prop="username" label="用户名" width="150">
                <template #default="scope">
                    <el-tag size="small" effect="plain">{{ scope.row.username }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="nickname" label="昵称" width="150" />
            <el-table-column prop="gender" label="性别" width="80" align="center">
                <template #default="scope">
                    <el-tag :type="scope.row.gender === '女' ? 'danger' : ''" size="small" effect="light">
                        {{ scope.row.gender || '未知' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="80" align="center" />
            <el-table-column prop="phone" label="手机号" width="150" />
            <el-table-column prop="createTime" label="注册时间" width="180">
                <template #default="scope">
                    <span style="font-size: 12px; color: #909399;">{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="180" fixed="right">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="handleEdit(scope.row)" icon="Edit">编辑</el-button>
                    <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)" icon="Delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        
        <!-- 分页 (模拟) -->
        <div class="pagination-wrapper">
             <el-pagination background layout="prev, pager, next" :total="tableData.length" :page-size="10" />
        </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="500px" custom-class="custom-dialog">
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
        <el-form-item label="性别">
           <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="1" :max="120" />
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
import { ref, reactive, onMounted, computed } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import 'animate.css'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({})
const searchKeyword = ref('')

const load = () => {
  request.get('/user/all').then(res => {
    if (res.code === '200') {
      tableData.value = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 前端过滤模拟搜索
const filterTableData = computed(() => {
    if (!searchKeyword.value) return tableData.value
    return tableData.value.filter(item => 
        (item.username && item.username.includes(searchKeyword.value)) || 
        (item.nickname && item.nickname.includes(searchKeyword.value))
    )
})

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
  ElMessageBox.confirm('确认删除该用户吗?', '删除警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/user/' + id).then(res => {
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
    request.put('/user/update', form).then(res => {
      if (res.code === '200') {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    request.post('/user/add', form).then(res => {
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
.search-input {
    width: 200px;
}
.custom-table {
    margin-top: 10px;
}
.pagination-wrapper {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}
:deep(.el-table th) {
    background-color: #f5f7fa !important;
    color: #606266;
    font-weight: 600;
}
</style>
