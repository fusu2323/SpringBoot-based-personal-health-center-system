<template>
  <el-card class="user-info-card">
    <template #header>
      <div class="card-header">
        <span>个人信息</span>
      </div>
    </template>
    
    <el-row :gutter="40">
        <el-col :span="12">
            <el-form :model="form" label-width="100px">
                <el-form-item label="用户名">
                    <el-input v-model="form.username" disabled />
                </el-form-item>
                <el-form-item label="昵称">
                    <el-input v-model="form.nickname" />
                </el-form-item>
                <el-form-item label="性别">
                     <el-radio-group v-model="form.gender">
                        <el-radio label="男">男</el-radio>
                        <el-radio label="女">女</el-radio>
                     </el-radio-group>
                </el-form-item>
                <el-form-item label="年龄">
                    <el-input-number v-model="form.age" />
                </el-form-item>
                <el-form-item label="身高(cm)">
                    <el-input-number v-model="form.height" :precision="1" />
                </el-form-item>
                 <el-form-item label="体重(kg)">
                    <el-input-number v-model="form.weight" :precision="1" />
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input v-model="form.phone" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="saveInfo">保存信息</el-button>
                </el-form-item>
            </el-form>
        </el-col>
        
        <el-col :span="12">
            <el-card shadow="never">
                <template #header>
                    <span>修改密码</span>
                </template>
                <el-form :model="passForm" label-width="100px">
                    <el-form-item label="新密码">
                        <el-input v-model="passForm.password" type="password" show-password />
                    </el-form-item>
                     <el-form-item>
                        <el-button type="danger" @click="updatePass">确认修改</el-button>
                    </el-form-item>
                </el-form>
            </el-card>
        </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const form = reactive({})
const passForm = reactive({
    password: ''
})

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}

const load = () => {
    request.get('/user/' + user.id).then(res => {
        if (res.code === '200') {
            Object.assign(form, res.data)
        }
    })
}

onMounted(() => {
    load()
})

const saveInfo = () => {
    request.put('/user/update', form).then(res => {
        if (res.code === '200') {
            ElMessage.success('保存成功')
            // 更新本地存储的昵称等信息（可选）
        } else {
             ElMessage.error(res.msg)
        }
    })
}

const updatePass = () => {
    if (!passForm.password) {
        ElMessage.warning('请输入新密码')
        return
    }
    const data = {
        id: user.id,
        password: passForm.password
    }
    request.put('/user/password', data).then(res => {
         if (res.code === '200') {
            ElMessage.success('修改成功，下次登录生效')
            passForm.password = ''
        } else {
             ElMessage.error(res.msg)
        }
    })
}
</script>

<style scoped>
.user-info-card {
    max-width: 1000px;
    margin: 20px auto;
}
</style>
