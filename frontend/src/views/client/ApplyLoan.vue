<template>
  <div class="page-container glass-panel">
    <div class="header-banner" style="display: flex; justify-content: space-between; align-items: center;">
      <div>
        <h2>申请贷款</h2>
        <p>您的全部贷款申请与审批记录</p>
      </div>
      <el-button type="primary" size="large" @click="openDialog" color="#2563eb"
        style="color:#fff; display:inline-flex; align-items:center; gap:6px;">
        <el-icon><Plus /></el-icon>
        新增贷款申请
      </el-button>
    </div>

    <el-table :data="pagedList" style="width: 100%" class="custom-table" v-loading="loading">
      <el-table-column label="申请时间" min-width="200">
        <template #default="scope">
          {{ formatTime(scope.row.applyTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="贷款金额(元)" min-width="150" :formatter="(row, column, cellValue) => formatMoney(cellValue)" />
      <el-table-column prop="termMonths" label="期限(月)" min-width="100" />
      <el-table-column prop="purpose" label="申请说明" />
    <el-table-column prop="productName" label="贷款产品">
        <template #default="scope">
          {{ scope.row.productName || '无关联产品' }}
        </template>
      </el-table-column>
      <el-table-column label="审批状态" min-width="120">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'warning' : (scope.row.status === 1 ? 'success' : 'danger')" effect="dark">
            {{ scope.row.status === 0 ? '待审批' : (scope.row.status === 1 ? '已放款' : '已驳回') }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-wrap">
      <el-pagination v-model:current-page="currentPage" :page-size="10" :total="list.length" layout="total, prev, pager, next" background />
    </div>

    <!-- 申请弹窗 -->
    <el-dialog v-model="dialogVisible" title="填写贷款申请表" width="600px" custom-class="dark-dialog" @closed="resetForm">
      
      <!-- 第一步：选择产品 -->
      <div v-if="!selectedProduct" class="product-selection">
        <div class="step-title">请选择贷款产品</div>
        <div v-if="products.length === 0" class="no-product">当前暂无上架贷款产品，请联系客服。</div>
        <div class="product-grid">
          <div 
            v-for="p in products" :key="p.id" 
            class="product-card" 
            @click="selectProduct(p)"
          >
            <div class="p-header">
              <span class="p-name">{{ p.name }}</span>
              <el-tag :type="p.type === 1 ? 'warning' : (p.type === 2 ? 'success' : (p.type === 3 ? 'danger' : ''))" size="small">{{ PRODUCT_TYPE_MAP[p.type]?.label || '未知' }}</el-tag>
            </div>
            <div class="p-rate">年化 {{ (p.annualRate * 100).toFixed(2) }}%</div>
            <div class="p-desc">{{ p.description }}</div>
            <div class="p-limits">额度：{{ p.minAmount }} - {{ p.maxAmount }} 元</div>
            <div class="p-limits">期限：{{ p.minTerm }} - {{ p.maxTerm }} 月</div>
          </div>
        </div>
      </div>

      <!-- 第二步：填写表单 -->
      <div v-else>
        <div class="selected-product-bar">
          <div>已选产品：<strong>{{ selectedProduct.name }}</strong> (年化 {{ (selectedProduct.annualRate * 100).toFixed(2) }}%)</div>
          <el-button type="primary" link @click="selectedProduct = null">重新选择</el-button>
        </div>
        <el-form :model="form" label-position="top">
          <el-form-item label="贷款金额 (元)">
            <el-input-number v-model="form.amount" :min="selectedProduct.minAmount" :max="selectedProduct.maxAmount" :step="1000" size="large" style="width: 100%"></el-input-number>
            <div class="field-tip">限额：{{ selectedProduct.minAmount }} ~ {{ selectedProduct.maxAmount }}</div>
          </el-form-item>
          <el-form-item label="分期数 (月)">
            <el-input-number v-model="form.termMonths" :min="selectedProduct.minTerm" :max="selectedProduct.maxTerm" size="large" style="width: 100%"></el-input-number>
            <div class="field-tip">期限：{{ selectedProduct.minTerm }} ~ {{ selectedProduct.maxTerm }} 个月</div>
          </el-form-item>
          <el-form-item label="资金用途声明">
            <el-input v-model="form.purpose" type="textarea" :rows="10" resize="none" placeholder="例如：日常消费、工程周转" size="large"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button v-if="selectedProduct" type="primary" color="#2563eb" @click="submit" :loading="submitting" style="color:#fff;">
            确认提交
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { formatTime, formatMoney } from '../../utils/format'
import { PRODUCT_TYPE_MAP } from '../../constants'

const list = ref([])
const products = ref([])
const selectedProduct = ref(null)
const loading = ref(false)
const currentPage = ref(1)
const pagedList = computed(() => {
  const start = (currentPage.value - 1) * 10
  return list.value.slice(start, start + 10)
})
const dialogVisible = ref(false)
const submitting = ref(false)

const form = ref({
  amount: 0,
  termMonths: 0,
  purpose: ''
})

const loadData = async () => {
    loading.value = true
    try {
        const res = await request.get('/loan/my')
        list.value = res.data?.list || res.data || []
    } catch (e) {
        console.error('加载数据失败:', e)
    } finally {
        loading.value = false
    }
}

const loadProducts = async () => {
    const res = await request.get('/product/active')
    products.value = res.data || []
}

const openDialog = () => {
    loadProducts()
    resetForm()
    dialogVisible.value = true
}

const resetForm = () => {
    selectedProduct.value = null
    form.value = { amount: 0, termMonths: 0, purpose: '' }
}

const selectProduct = (p) => {
    selectedProduct.value = p
    form.value.amount = p.minAmount
    form.value.termMonths = p.minTerm
}

const submit = async () => {
  if (!form.value.purpose) { ElMessage.error('请填写资金用途'); return }
  submitting.value = true
  try {
    await request.post('/loan/apply', {
        productId: selectedProduct.value.id,
        amount: form.value.amount,
        termMonths: form.value.termMonths,
        purpose: form.value.purpose
    })
    ElMessage.success('贷款申请已提交，等待审批')
    dialogVisible.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

onMounted(() => loadData())
</script>

<style scoped>
.page-container { padding: var(--space-6, 30px); }
.header-banner { 
  margin-bottom: var(--space-6, 30px); 
  border-bottom: 1px solid var(--border-default); 
  padding-bottom: var(--space-5, 20px);
}
.header-banner h2 { 
  font-size: var(--font-size-2xl, 24px); 
  color: var(--text-primary); 
  margin-bottom: var(--space-1, 5px); 
}
.header-banner p { color: var(--text-secondary); }

:deep(.el-table) { background: transparent !important; }
:deep(.el-table th.el-table__cell) { 
  background-color: var(--table-header-bg, var(--bg-secondary)) !important; 
  color: var(--table-header-text, var(--text-secondary)); 
  font-weight: var(--font-weight-semibold, 600);
}
:deep(.el-table td.el-table__cell) { 
  border-bottom: 1px solid var(--table-border, var(--border-default)); 
  color: var(--table-text, var(--text-primary)); 
}
:deep(.el-table--enable-row-hover .el-table__body tr:hover>td.el-table__cell) { 
  background-color: var(--table-row-hover-bg, var(--bg-hover)) !important; 
}

.rate-preview {
  margin: 15px 0 20px;
  padding: 15px;
  background: rgba(37, 99, 235, 0.08);
  border-left: 4px solid var(--primary-color);
  color: var(--text-primary);
  border-radius: var(--radius-md, 4px);
}
.rate-preview strong { color: var(--text-primary); }

.step-title { 
  font-size: var(--font-size-base, 16px); 
  font-weight: var(--font-weight-bold, 700); 
  margin-bottom: var(--space-5, 20px); 
  color: var(--text-primary); 
}
.no-product { color: var(--text-muted); text-align: center; padding: var(--space-6, 30px); }
.product-grid { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-4, 15px); }
.product-card {
  border: 1px solid var(--border-default); 
  border-radius: var(--radius-lg, 8px); 
  padding: var(--space-4, 16px);
  cursor: pointer; 
  transition: all var(--transition-normal, 0.2s); 
  background: var(--bg-card);
}
.product-card:hover { 
  border-color: var(--primary-color); 
  box-shadow: var(--shadow-md); 
  transform: translateY(-2px); 
}
.p-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.p-name { font-weight: var(--font-weight-bold, 700); font-size: var(--font-size-base, 16px); color: var(--text-primary); }
.p-rate { color: var(--color-danger); font-size: var(--font-size-lg, 18px); font-weight: var(--font-weight-bold, 700); margin-bottom: 6px; }
.p-desc { color: var(--text-secondary); font-size: var(--font-size-xs, 12px); margin-bottom: 10px; min-height: 36px; line-height: 1.4; }
.p-limits { color: var(--text-secondary); font-size: var(--font-size-xs, 12px); }

.selected-product-bar {
  display: flex; 
  justify-content: space-between; 
  align-items: center;
  background: rgba(37, 99, 235, 0.08); 
  border-left: 4px solid var(--primary-color);
  padding: 12px 16px; 
  border-radius: var(--radius-md, 4px); 
  margin-bottom: var(--space-5, 20px);
  color: var(--text-primary); 
  font-size: var(--font-size-sm, 14px);
}
.field-tip { font-size: var(--font-size-xs, 12px); color: var(--text-muted); margin-top: 4px; }

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: var(--space-4, 16px);
}
</style>
