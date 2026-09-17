<template>
  <div class="page-container glass-panel">
    <div class="header-banner">
      <div class="header-content">
        <h2>消息中心</h2>
        <p>您的还款提醒与平台通知都在这里</p>
      </div>
      <el-button size="default" class="glass-action-btn" @click="markAll" :disabled="messages.length === 0" round>
        <el-icon style="margin-right: 6px;"><CircleCheck /></el-icon>
        全部标为已读
      </el-button>
    </div>

    <div v-loading="loading">
      <!-- 空态 -->
      <div v-if="!loading && messages.length === 0" class="empty-state">
        <div class="empty-icon-wrap">
          <el-icon class="empty-icon-el"><Bell /></el-icon>
        </div>
        <div class="empty-title">收件箱目前无新消息</div>
        <div class="empty-desc">
          您目前没有任何通知<br/>保持良好的还款记录，系统将在有新动态时第一时间提醒您
        </div>
        <div class="empty-badge">
          <el-icon style="vertical-align: middle; margin-right: 4px;"><CircleCheck /></el-icon>信用良好
        </div>
      </div>

      <!-- 消息列表 -->
      <div v-for="msg in messages" :key="msg.id" class="msg-card" :class="{ unread: msg.isRead === 0 }" @click="readMsg(msg)">
        <div class="msg-header">
          <span class="msg-title">
            <span v-if="msg.isRead === 0" class="msg-dot"></span>
            {{ msg.title }}
          </span>
          <el-tag v-if="msg.isRead === 0" class="unread-tag" size="small">未读</el-tag>
          <el-tag v-else type="info" size="small">已读</el-tag>
        </div>
        <div class="msg-body">{{ msg.content }}</div>
        <div class="msg-time">{{ formatTime(msg.createTime) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Bell, CircleCheck } from '@element-plus/icons-vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
import { formatTime } from '../../utils/format'

const messages = ref([])
const loading = ref(false)

const loadMessages = async () => {
  loading.value = true
  try {
    const res = await request.get('/message/list')
    messages.value = res.data || []
  } finally {
    loading.value = false
  }
}

const readMsg = async (msg) => {
  if (msg.isRead === 1) return
  try {
    await request.put(`/message/read/${msg.id}`)
    msg.isRead = 1
    window.dispatchEvent(new CustomEvent('unread-changed'))
  } catch (e) {
    console.error(e)
  }
}

const markAll = async () => {
  try {
    await request.put('/message/read-all')
    messages.value.forEach(m => (m.isRead = 1))
    ElMessage.success('全部已读')
    window.dispatchEvent(new CustomEvent('unread-changed'))
  } catch (e) {
    console.error(e)
    ElMessage.error('标记已读失败')
  }
}

onMounted(() => loadMessages())
</script>

<style scoped>
.page-container { padding: var(--space-6, 30px); }
.header-banner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: var(--space-6, 30px);
  border-bottom: 1px solid var(--border-default);
  padding-bottom: var(--space-5, 20px);
}
.header-banner h2 { 
  font-size: var(--font-size-2xl, 24px); 
  color: var(--text-primary); 
  margin-bottom: var(--space-1, 5px); 
}
.header-banner p { color: var(--text-secondary); margin: 0; }

.glass-action-btn {
  background: var(--bg-secondary) !important;
  border: 1px solid var(--border-default) !important;
  color: var(--text-primary) !important;
  font-weight: var(--font-weight-semibold, 600);
  transition: all var(--transition-normal, 0.2s);
}
.glass-action-btn:hover:not(:disabled) {
  background: var(--primary-light) !important;
  border-color: var(--primary-color) !important;
  color: var(--primary-color) !important;
  transform: translateY(-1px);
}
.glass-action-btn:disabled {
  background: rgba(128, 128, 128, 0.03) !important;
  border: 1px solid var(--border-subtle) !important; /* 绑定自适应边框色，彻底解决浅色主题下看不清的问题 */
  color: var(--text-secondary) !important; /* 自适应辅助字色 */
  cursor: not-allowed;
}

/* 空态区块 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
  border: 1px dashed var(--border-default);
  border-radius: var(--radius-2xl, 20px);
  background: var(--bg-secondary);
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(12px); }
  to   { opacity: 1; transform: translateY(0); }
}

.empty-icon-wrap {
  width: 110px;
  height: 110px;
  background: rgba(37, 99, 235, 0.1);
  border: 1px solid rgba(37, 99, 235, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 28px;
  box-shadow: 0 0 50px rgba(37, 99, 235, 0.15);
}

.empty-icon-el {
  font-size: 52px;
  color: var(--primary-color);
}

.empty-title {
  font-size: var(--font-size-xl, 20px);
  font-weight: var(--font-weight-bold, 700);
  color: var(--text-primary);
  margin-bottom: 12px;
  letter-spacing: 0.5px;
}

.empty-desc {
  font-size: var(--font-size-sm, 14px);
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed, 1.75);
  max-width: 320px;
  margin-bottom: 24px;
}

.empty-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 12px;
  color: var(--color-success);
  background: var(--color-success-light);
  border: none;
  border-radius: var(--radius-full, 20px);
  padding: 4px 12px;
  letter-spacing: 0.5px;
  line-height: 1;
  vertical-align: middle;
}

.empty-badge :deep(.el-icon) {
  font-size: 12px;
  vertical-align: middle;
  margin: 0;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 消息卡片 */
.msg-card {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-default) !important;
  border-radius: var(--radius-xl, 12px);
  padding: 18px 20px;
  margin-bottom: 14px;
  cursor: pointer;
  transition: all var(--transition-normal, 0.2s);
}
.msg-card:hover { 
  background: var(--bg-hover) !important; 
  border-color: var(--primary-color) !important; 
  box-shadow: var(--shadow-md); 
}
.msg-card.unread { border-left: 4px solid var(--color-danger) !important; }

.msg-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.msg-title { font-size: var(--font-size-base, 16px); font-weight: var(--font-weight-bold, 700); color: var(--text-primary) !important; }
.msg-body { font-size: var(--font-size-sm, 14px); color: var(--text-secondary) !important; line-height: var(--line-height-normal, 1.5); margin-bottom: 8px; }
.msg-time { font-size: var(--font-size-xs, 12px); color: var(--text-muted) !important; text-align: right; }

:deep(.unread-tag) {
  background-color: var(--tag-danger-bg, var(--color-danger-light)) !important;
  color: var(--tag-danger-text, var(--color-danger)) !important;
  border: none !important;
  font-weight: var(--font-weight-bold, 700);
}

.msg-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: var(--color-danger);
  box-shadow: 0 0 6px rgba(245, 108, 108, 0.8);
  margin-right: 8px;
  vertical-align: middle;
}

/* 移动端响应式 */
@media (max-width: 768px) {
  .page-container { padding: var(--space-4, 16px); }
  .header-banner { flex-direction: column; align-items: flex-start; }
  .glass-action-btn { width: 100%; }
  .msg-card { padding: 14px 16px; }
  .msg-title { font-size: var(--font-size-sm, 14px); }
  .msg-body { font-size: var(--font-size-xs, 12px); }
}
</style>
