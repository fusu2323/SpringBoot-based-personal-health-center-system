<template>
  <div class="health-container">
    <el-card class="health-card animate__animated animate__fadeInUp">
        <template #header>
            <div class="card-header">
                <div class="header-left">
                    <el-icon><TrendCharts /></el-icon>
                    <span class="header-title">健康数据管理</span>
                </div>
            </div>
        </template>

        <el-tabs v-model="activeName" @tab-click="loadData" class="custom-tabs">
          <!-- 身体指标 -->
          <el-tab-pane label="身体指标记录" name="indicator">
            <template #label>
                <span class="custom-tab-label"><el-icon><Odometer /></el-icon> 身体指标</span>
            </template>
            <el-table :data="indicatorList" stripe class="custom-table">
                <el-table-column prop="username" label="用户名" width="120">
                    <template #default="scope">
                        <el-tag size="small" effect="plain">{{ scope.row.username }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="nickname" label="昵称" width="120" />
                <el-table-column prop="recordDate" label="记录日期" width="120" sortable />
                <el-table-column prop="weight" label="体重(kg)" align="center" />
                <el-table-column prop="bloodPressure" label="血压" align="center" />
                <el-table-column prop="bloodSugar" label="血糖" align="center" />
                <el-table-column prop="heartRate" label="心率" align="center" />
                <el-table-column prop="temperature" label="体温" align="center" />
                <el-table-column label="操作" width="100" fixed="right">
                    <template #default="scope">
                        <el-button link type="danger" size="small" @click="handleDelete('indicator', scope.row.id)" icon="Delete">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
          </el-tab-pane>
    
          <!-- 运动记录 -->
          <el-tab-pane label="运动记录" name="sport">
            <template #label>
                <span class="custom-tab-label"><el-icon><Bicycle /></el-icon> 运动记录</span>
            </template>
            <el-table :data="sportList" stripe class="custom-table">
                <el-table-column prop="username" label="用户名" width="120">
                    <template #default="scope">
                        <el-tag size="small" effect="plain">{{ scope.row.username }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="nickname" label="昵称" width="120" />
                <el-table-column prop="recordDate" label="记录日期" width="120" sortable />
                <el-table-column prop="sportType" label="运动类型">
                     <template #default="scope">
                        <el-tag size="small" type="success">{{ scope.row.sportType }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="duration" label="时长(分钟)" align="center" />
                <el-table-column prop="calorie" label="消耗卡路里" align="center" />
                <el-table-column label="操作" width="100" fixed="right">
                    <template #default="scope">
                        <el-button link type="danger" size="small" @click="handleDelete('sport', scope.row.id)" icon="Delete">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
          </el-tab-pane>
    
          <!-- 饮食记录 -->
          <el-tab-pane label="饮食记录" name="diet">
            <template #label>
                <span class="custom-tab-label"><el-icon><Food /></el-icon> 饮食记录</span>
            </template>
            <el-table :data="dietList" stripe class="custom-table">
                <el-table-column prop="username" label="用户名" width="120">
                    <template #default="scope">
                        <el-tag size="small" effect="plain">{{ scope.row.username }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="nickname" label="昵称" width="120" />
                <el-table-column prop="recordDate" label="记录日期" width="120" sortable />
                <el-table-column prop="mealType" label="餐别">
                    <template #default="scope">
                        <el-tag size="small" type="warning">{{ scope.row.mealType }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="foodName" label="食物名称" />
                <el-table-column prop="calorie" label="卡路里" align="center" />
                <el-table-column label="操作" width="100" fixed="right">
                    <template #default="scope">
                        <el-button link type="danger" size="small" @click="handleDelete('diet', scope.row.id)" icon="Delete">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
          </el-tab-pane>
    
          <!-- 睡眠记录 -->
          <el-tab-pane label="睡眠记录" name="sleep">
             <template #label>
                <span class="custom-tab-label"><el-icon><Moon /></el-icon> 睡眠记录</span>
            </template>
            <el-table :data="sleepList" stripe class="custom-table">
                <el-table-column prop="username" label="用户名" width="120">
                    <template #default="scope">
                        <el-tag size="small" effect="plain">{{ scope.row.username }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="nickname" label="昵称" width="120" />
                <el-table-column prop="recordDate" label="记录日期" width="120" sortable />
                <el-table-column prop="sleepTime" label="入睡时间" width="180" />
                <el-table-column prop="wakeTime" label="起床时间" width="180" />
                <el-table-column prop="duration" label="时长(小时)" align="center" />
                <el-table-column prop="quality" label="质量" align="center">
                    <template #default="scope">
                        <el-tag size="small" :type="getSleepQualityType(scope.row.quality)">{{ scope.row.quality }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="100" fixed="right">
                    <template #default="scope">
                        <el-button link type="danger" size="small" @click="handleDelete('sleep', scope.row.id)" icon="Delete">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { TrendCharts, Odometer, Bicycle, Food, Moon, Delete } from '@element-plus/icons-vue'
import 'animate.css'

const activeName = ref('indicator')
const indicatorList = ref([])
const sportList = ref([])
const dietList = ref([])
const sleepList = ref([])

const loadData = () => {
    if (activeName.value === 'indicator') {
        request.get('/record/admin/indicator/all').then(res => indicatorList.value = res.data)
    } else if (activeName.value === 'sport') {
        request.get('/record/admin/sport/all').then(res => sportList.value = res.data)
    } else if (activeName.value === 'diet') {
        request.get('/record/admin/diet/all').then(res => dietList.value = res.data)
    } else if (activeName.value === 'sleep') {
        request.get('/record/admin/sleep/all').then(res => sleepList.value = res.data)
    }
}

onMounted(() => {
    loadData()
})

const handleDelete = (type, id) => {
     ElMessageBox.confirm('确认删除该记录吗?', '删除警告', { 
         confirmButtonText: '确定',
         cancelButtonText: '取消',
         type: 'warning' 
    }).then(() => {
        request.delete(`/record/${type}/${id}`).then(res => {
            if (res.code === '200') {
                ElMessage.success('删除成功')
                loadData()
            } else {
                ElMessage.error(res.msg)
            }
        })
    })
}

const getSleepQualityType = (quality) => {
    if (quality === '优' || quality === 'Good') return 'success'
    if (quality === '良' || quality === 'Normal') return 'primary'
    if (quality === '中' || quality === 'Bad') return 'warning'
    return 'danger'
}
</script>

<style scoped>
.health-container {
    padding: 10px;
}
.health-card {
    border: none;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}
.card-header {
    display: flex;
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
.custom-tab-label {
    display: flex;
    align-items: center;
    gap: 5px;
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
