<template>
  <div class="plan-container">
    <el-card class="box-card">
      <template #header>
        <div class="clearfix">
          <span><el-icon><MagicStick /></el-icon> 智能健康计划生成器</span>
        </div>
      </template>
      
      <!-- 输入区域 -->
      <div class="input-section" v-if="!currentPlan">
        <el-alert
          title="AI 将根据您的身体指标和目标，为您量身定制 7 天饮食与运动计划"
          type="info"
          show-icon
          :closable="false"
          style="margin-bottom: 20px;"
        />
        <el-form label-width="100px">
            <el-form-item label="您的目标">
                <el-input 
                    v-model="goal" 
                    type="textarea" 
                    :rows="3"
                    placeholder="例如：我想在30天内减重5斤，主要想减肚子..." 
                    :disabled="loading"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="generatePlan" :loading="loading" round>
                    {{ loading ? '正在规划中...' : '立即生成计划' }}
                </el-button>
            </el-form-item>
        </el-form>

        <!-- 进度条展示 -->
        <div v-if="loading" class="progress-section">
             <div class="progress-title">
                <el-icon class="is-loading"><Loading /></el-icon> 
                {{ progressText }}
             </div>
             <el-progress 
                :percentage="percentage" 
                :status="percentage === 100 ? 'success' : ''"
                :stroke-width="15" 
                striped 
                striped-flow 
             />
             <div class="progress-tip">AI 正在分析您的健康数据并制定详细计划，请耐心等待...</div>
        </div>
        
        <!-- 历史计划列表 -->
        <div class="history-section" v-if="historyPlans.length > 0 && !loading">
            <div class="divider">历史计划</div>
            <el-timeline>
                <el-timeline-item
                  v-for="(activity, index) in historyPlans"
                  :key="index"
                  :timestamp="activity.createTime"
                  placement="top"
                >
                  <el-card shadow="hover" @click="viewPlan(activity)" class="history-card">
                    <h4>{{ activity.goal }}</h4>
                    <p>点击查看详情</p>
                  </el-card>
                </el-timeline-item>
            </el-timeline>
        </div>
      </div>

      <!-- 计划展示区域 -->
      <div class="result-section" v-else>
        <div class="plan-header">
            <h3>目标：{{ currentPlan.goal }}</h3>
            <el-button @click="currentPlan = null">返回重新生成</el-button>
        </div>

        <el-alert :title="parsedContent.advice" type="success" :closable="false" style="margin-bottom: 20px;" />

        <el-tabs type="border-card">
            <el-tab-pane label="📅 7天饮食安排">
                <el-table :data="parsedContent.dietPlan" style="width: 100%" border stripe>
                    <el-table-column prop="day" label="天数" width="80" align="center">
                        <template #default="scope">第 {{ scope.row.day }} 天</template>
                    </el-table-column>
                    <el-table-column prop="breakfast" label="早餐" />
                    <el-table-column prop="lunch" label="午餐" />
                    <el-table-column prop="dinner" label="晚餐" />
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="🏃‍♂️ 7天运动安排">
                <el-table :data="parsedContent.sportPlan" style="width: 100%" border stripe>
                    <el-table-column prop="day" label="天数" width="80" align="center">
                         <template #default="scope">第 {{ scope.row.day }} 天</template>
                    </el-table-column>
                    <el-table-column prop="content" label="运动内容" />
                    <el-table-column prop="duration" label="建议时长" width="150" />
                </el-table>
            </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'
import { MagicStick, Loading } from '@element-plus/icons-vue'

const user = JSON.parse(localStorage.getItem('user') || '{}').user || {}
const goal = ref('')
const loading = ref(false)
const historyPlans = ref([])
const currentPlan = ref(null)

// 进度条相关
const percentage = ref(0)
const progressText = ref('AI 正在思考中...')
let timer = null

const parsedContent = computed(() => {
    if (!currentPlan.value || !currentPlan.value.content) return {}
    try {
        return JSON.parse(currentPlan.value.content)
    } catch (e) {
        console.error('JSON Parse Error', e)
        return { dietPlan: [], sportPlan: [], advice: '解析数据出错' }
    }
})

onMounted(() => {
    loadHistory()
})

onUnmounted(() => {
    if (timer) clearInterval(timer)
})

const loadHistory = () => {
    request.get(`/plan/list/${user.id}`).then(res => {
        if (res.code === '200') {
            historyPlans.value = res.data
        }
    })
}

// 模拟进度条
const startFakeProgress = () => {
    percentage.value = 0
    progressText.value = 'AI 正在分析您的身体指标...'
    
    timer = setInterval(() => {
        if (percentage.value < 30) {
            percentage.value += 5
        } else if (percentage.value < 60) {
            progressText.value = 'AI 正在制定饮食方案...'
            percentage.value += 2
        } else if (percentage.value < 80) {
            progressText.value = 'AI 正在规划运动课程...'
            percentage.value += 1
        } else if (percentage.value < 95) {
            progressText.value = '正在整理最终报告...'
            percentage.value += 0.5
        }
    }, 500)
}

const generatePlan = () => {
    if (!goal.value.trim()) {
        ElMessage.warning('请输入您的目标')
        return
    }
    
    loading.value = true
    startFakeProgress()
    
    request.post('/plan/generate', {
        userId: user.id,
        goal: goal.value
    }).then(res => {
        if (res.code === '200') {
            percentage.value = 100
            progressText.value = '生成完成！'
            ElMessage.success('计划生成成功！')
            
            const newPlan = {
                goal: goal.value,
                content: res.data,
                createTime: '刚刚'
            }
            // 稍微延迟一下展示结果，让用户看到100%
            setTimeout(() => {
                loading.value = false
                currentPlan.value = newPlan
                loadHistory()
                if (timer) clearInterval(timer)
            }, 500)
        } else {
            loading.value = false
            ElMessage.error(res.msg || '生成失败，请重试')
            if (timer) clearInterval(timer)
        }
    }).catch(err => {
        loading.value = false
        if (timer) clearInterval(timer)
        ElMessage.error('请求超时或出错，请稍后重试')
    })
}

const viewPlan = (plan) => {
    currentPlan.value = plan
}
</script>

<style scoped>
.plan-container {
    padding: 20px;
}
.input-section {
    max-width: 800px;
    margin: 0 auto;
}
.history-section {
    margin-top: 40px;
}
.divider {
    font-size: 16px;
    font-weight: bold;
    color: #909399;
    margin-bottom: 20px;
    border-bottom: 1px solid #EBEEF5;
    padding-bottom: 10px;
}
.history-card {
    cursor: pointer;
}
.history-card:hover {
    border-color: #409EFF;
}
.plan-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}
.progress-section {
    margin-top: 30px;
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 8px;
    text-align: center;
}
.progress-title {
    margin-bottom: 15px;
    font-weight: bold;
    color: #409EFF;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
}
.progress-tip {
    margin-top: 10px;
    font-size: 12px;
    color: #909399;
}
</style>
