<template>
  <el-tooltip :content="isDarkMode ? '切换到亮色主题' : '切换到暗色主题'" placement="bottom">
    <el-button :icon="isDarkMode ? Sunny : Moon" circle @click="toggleTheme" class="theme-toggle-btn" />
  </el-tooltip>
</template>

<script setup>
import { computed } from 'vue'
import { Moon, Sunny } from '@element-plus/icons-vue'
import { useTheme } from '@/composables/useTheme'

const { theme, toggleTheme } = useTheme()

const isDarkMode = computed(() => theme.value === 'dark')
</script>

<style scoped>
.theme-toggle-btn {
  background: transparent;
  border: 1px solid var(--border-subtle, rgba(255,255,255,0.15));
  color: var(--text-primary, #fff);
}

.theme-toggle-btn:hover {
  background: var(--bg-hover, rgba(255,255,255,0.1));
  border-color: var(--border-subtle, rgba(255,255,255,0.3));
}
</style>