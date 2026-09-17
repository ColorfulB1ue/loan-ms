<template>
  <div class="table-container" :class="{ 'is-loading': loading }">
    <el-table
      :data="data"
      :border="border"
      :stripe="stripe"
      :height="height"
      :max-height="maxHeight"
      :size="size"
      v-loading="loading"
      element-loading-text="加载中..."
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
    >
      <slot></slot>
    </el-table>
    <Pagination
      v-if="showPagination"
      v-model:page="currentPage"
      v-model:limit="currentLimit"
      :total="total"
      @change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import Pagination from './Pagination.vue'

const props = defineProps({
  // 表格数据
  data: {
    type: Array,
    default: () => []
  },
  // 是否加载中
  loading: {
    type: Boolean,
    default: false
  },
  // 是否显示边框
  border: {
    type: Boolean,
    default: true
  },
  // 是否斑马纹
  stripe: {
    type: Boolean,
    default: true
  },
  // 表格高度
  height: {
    type: [String, Number],
    default: undefined
  },
  // 最大高度
  maxHeight: {
    type: [String, Number],
    default: undefined
  },
  // 表格大小
  size: {
    type: String,
    default: 'default'
  },
  // 是否显示分页
  showPagination: {
    type: Boolean,
    default: true
  },
  // 当前页
  page: {
    type: Number,
    default: 1
  },
  // 每页大小
  limit: {
    type: Number,
    default: 10
  },
  // 总记录数
  total: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['update:page', 'update:limit', 'selection-change', 'sort-change', 'page-change'])

const currentPage = computed({
  get: () => props.page,
  set: (val) => emit('update:page', val)
})

const currentLimit = computed({
  get: () => props.limit,
  set: (val) => emit('update:limit', val)
})

const handleSelectionChange = (selection) => {
  emit('selection-change', selection)
}

const handleSortChange = ({ column, prop, order }) => {
  emit('sort-change', { column, prop, order })
}

const handlePageChange = ({ page, limit }) => {
  emit('page-change', { page, limit })
}
</script>

<style scoped>
.table-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.table-container.is-loading {
  min-height: 200px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f8f9fa !important;
  color: #495057;
  font-weight: 600;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: #f8f9fa;
}
</style>