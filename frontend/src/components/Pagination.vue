<template>
  <div class="pagination-wrap">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="pageSizes"
      :total="total"
      :layout="layout"
      :background="background"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
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
  },
  // 每页大小选项
  pageSizes: {
    type: Array,
    default: () => [10, 20, 50, 100]
  },
  // 布局
  layout: {
    type: String,
    default: 'total, sizes, prev, pager, next, jumper'
  },
  // 是否显示背景
  background: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:page', 'update:limit', 'change'])

const currentPage = computed({
  get: () => props.page,
  set: (val) => emit('update:page', val)
})

const pageSize = computed({
  get: () => props.limit,
  set: (val) => emit('update:limit', val)
})

const handleSizeChange = (val) => {
  emit('change', { page: currentPage.value, limit: val })
}

const handleCurrentChange = (val) => {
  emit('change', { page: val, limit: pageSize.value })
}
</script>

<style scoped>
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0;
}
</style>