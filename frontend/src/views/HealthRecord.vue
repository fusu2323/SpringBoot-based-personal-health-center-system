<template>
  <el-card>
    <el-tabs v-model="activeName">
      <!-- 身体指标 -->
      <el-tab-pane label="身体指标" name="indicator">
        <div style="margin-bottom: 10px;">
             <el-button type="primary" @click="handleAdd('indicator')">新增记录</el-button>
        </div>
        <el-table :data="indicatorList" stripe>
            <el-table-column prop="recordDate" label="日期" width="120" sortable />
            <el-table-column prop="weight" label="体重(kg)" />
            <el-table-column prop="bloodPressure" label="血压" />
            <el-table-column prop="bloodSugar" label="血糖" />
            <el-table-column prop="heartRate" label="心率" />
            <el-table-column prop="temperature" label="体温" />
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button link type="primary" @click="handleEdit('indicator', scope.row)">编辑</el-button>
                    <el-button link type="danger" @click="handleDelete('indicator', scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 运动记录 -->
      <el-tab-pane label="运动记录" name="sport">
         <div style="margin-bottom: 10px;">
             <el-button type="primary" @click="handleAdd('sport')">新增记录</el-button>
        </div>
        <el-table :data="sportList" stripe>
            <el-table-column prop="recordDate" label="日期" width="120" sortable />
            <el-table-column prop="sportType" label="运动类型" />
            <el-table-column prop="duration" label="时长(分钟)" />
            <el-table-column prop="calorie" label="消耗卡路里" />
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button link type="primary" @click="handleEdit('sport', scope.row)">编辑</el-button>
                    <el-button link type="danger" @click="handleDelete('sport', scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 饮食记录 -->
      <el-tab-pane label="饮食记录" name="diet">
         <div style="margin-bottom: 10px;">
             <el-button type="primary" @click="handleAdd('diet')">新增记录</el-button>
        </div>
        <el-table :data="dietList" stripe>
            <el-table-column prop="recordDate" label="日期" width="120" sortable />
            <el-table-column prop="mealType" label="餐别" />
            <el-table-column prop="foodName" label="食物名称" />
            <el-table-column prop="calorie" label="卡路里" />
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button link type="primary" @click="handleEdit('diet', scope.row)">编辑</el-button>
                    <el-button link type="danger" @click="handleDelete('diet', scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 睡眠记录 -->
      <el-tab-pane label="睡眠记录" name="sleep">
         <div style="margin-bottom: 10px;">
             <el-button type="primary" @click="handleAdd('sleep')">新增记录</el-button>
        </div>
        <el-table :data="sleepList" stripe>
            <el-table-column prop="recordDate" label="日期" width="120" sortable />
            <el-table-column prop="sleepTime" label="入睡时间" width="180" />
            <el-table-column prop="wakeTime" label="起床时间" width="180" />
            <el-table-column prop="duration" label="时长(小时)" />
            <el-table-column prop="quality" label="质量" />
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button link type="primary" @click="handleEdit('sleep', scope.row)">编辑</el-button>
                    <el-button link type="danger" @click="handleDelete('sleep', scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 通用 Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
        <el-form :model="form" label-width="100px">
            <el-form-item label="日期">
                 <el-date-picker v-model="form.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>

            <!-- Indicator Fields -->
            <template v-if="activeName === 'indicator'">
                <el-form-item label="体重(kg)">
                    <el-input-number v-model="form.weight" :precision="1" :step="0.1" />
                </el-form-item>
                 <el-form-item label="血压">
                    <div style="display: flex; gap: 8px; align-items: center;">
                        <el-input-number v-model="form.systolic" :min="60" :max="200" :step="1" placeholder="收缩压" style="width: 120px;" />
                        <span style="color: var(--color-text-muted);">/</span>
                        <el-input-number v-model="form.diastolic" :min="40" :max="130" :step="1" placeholder="舒张压" style="width: 120px;" />
                        <span style="color: var(--color-text-muted);">mmHg</span>
                    </div>
                </el-form-item>
                 <el-form-item label="血糖">
                    <el-input-number v-model="form.bloodSugar" :precision="1" :step="0.1" />
                </el-form-item>
                 <el-form-item label="心率">
                    <el-input-number v-model="form.heartRate" :step="1" />
                </el-form-item>
                 <el-form-item label="体温">
                    <el-input-number v-model="form.temperature" :precision="1" :step="0.1" />
                </el-form-item>
            </template>

            <!-- Sport Fields -->
            <template v-if="activeName === 'sport'">
                <el-form-item label="运动类型">
                    <el-input v-model="form.sportType" />
                </el-form-item>
                <el-form-item label="时长(分钟)">
                    <el-input-number v-model="form.duration" />
                </el-form-item>
                <el-form-item label="消耗(kcal)">
                    <el-input-number v-model="form.calorie" />
                </el-form-item>
            </template>

             <!-- Diet Fields -->
            <template v-if="activeName === 'diet'">
                <el-form-item label="餐别">
                    <el-select v-model="form.mealType">
                        <el-option label="早餐" value="早餐" />
                        <el-option label="午餐" value="午餐" />
                        <el-option label="晚餐" value="晚餐" />
                        <el-option label="加餐" value="加餐" />
                    </el-select>
                </el-form-item>
                <el-form-item label="食物名称">
                    <el-input v-model="form.foodName" />
                </el-form-item>
                <el-form-item label="卡路里">
                    <el-input-number v-model="form.calorie" />
                </el-form-item>
            </template>

             <!-- Sleep Fields -->
            <template v-if="activeName === 'sleep'">
                <el-form-item label="入睡时间">
                    <el-date-picker v-model="form.sleepTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
                </el-form-item>
                 <el-form-item label="起床时间">
                    <el-date-picker v-model="form.wakeTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
                </el-form-item>
                <el-form-item label="时长(小时)">
                    <el-input-number v-model="form.duration" :precision="1" :step="0.5" />
                </el-form-item>
                 <el-form-item label="睡眠质量">
                    <el-select v-model="form.quality">
                        <el-option label="优" value="优" />
                        <el-option label="良" value="良" />
                        <el-option label="中" value="中" />
                        <el-option label="差" value="差" />
                    </el-select>
                </el-form-item>
            </template>

        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="save">确定</el-button>
            </span>
        </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const activeName = ref('indicator')
const indicatorList = ref([])
const sportList = ref([])
const dietList = ref([])
const sleepList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive({})

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}

const loadData = () => {
    if (activeName.value === 'indicator') {
        request.get(`/record/indicator/list/${user.id}`).then(res => indicatorList.value = res.data)
    } else if (activeName.value === 'sport') {
        request.get(`/record/sport/list/${user.id}`).then(res => sportList.value = res.data)
    } else if (activeName.value === 'diet') {
        request.get(`/record/diet/list/${user.id}`).then(res => dietList.value = res.data)
    } else if (activeName.value === 'sleep') {
        request.get(`/record/sleep/list/${user.id}`).then(res => sleepList.value = res.data)
    }
}

watch(activeName, () => {
    loadData()
})

onMounted(() => {
    // 从路由 query 获取 tab
    if (route.query.tab) {
        activeName.value = route.query.tab
    }
    loadData()
})

// 监听路由变化 (防止同一个页面跳转不刷新)
watch(() => route.query.tab, (newTab) => {
    if (newTab) {
        activeName.value = newTab
    }
})

const handleAdd = (type) => {
    Object.keys(form).forEach(key => delete form[key])
    form.userId = user.id
    form.recordDate = new Date().toISOString().split('T')[0]
    form.systolic = null
    form.diastolic = null
    form.mood = 3
    form.stress = 3
    form.hydration = 0
    dialogTitle.value = '新增记录'
    dialogVisible.value = true
}

const handleEdit = (type, row) => {
    Object.assign(form, row)
    dialogTitle.value = '编辑记录'
    dialogVisible.value = true
}

const handleDelete = (type, id) => {
     ElMessageBox.confirm('确认删除该记录吗?', '提示', { type: 'warning' }).then(() => {
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

const save = () => {
    const type = activeName.value
    if (type === 'indicator' && form.systolic && form.diastolic) {
        form.bloodPressure = form.systolic + '/' + form.diastolic
    }
    request.post(`/record/${type}/save`, form).then(res => {
        if (res.code === '200') {
            ElMessage.success('保存成功')
            dialogVisible.value = false
            loadData()
        } else {
            ElMessage.error(res.msg)
        }
    })
}
</script>
