<template>
  <el-tag :type="tagType" :effect="effect" :size="size" :round="round">
    {{ label }}
  </el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  // 状态值
  status: {
    type: [Number, String],
    required: true
  },
  // 状态映射配置
  map: {
    type: Object,
    required: true
  },
  // 标签效果
  effect: {
    type: String,
    default: 'light'
  },
  // 标签大小
  size: {
    type: String,
    default: 'default'
  },
  // 是否圆角
  round: {
    type: Boolean,
    default: false
  }
})

const statusConfig = computed(() => {
  return props.map[props.status] || { label: '未知', type: 'info' }
})

const tagType = computed(() => statusConfig.value.type)
const label = computed(() => statusConfig.value.label)
</script>