<template>
  <el-tooltip :content="isDarkMode ? '切换到亮色主题' : '切换到暗色主题'" placement="right">
    <div class="theme-toggle-btn" @click="toggleTheme">
      <el-icon :size="18">
        <Sunny v-if="isDarkMode" />
        <Moon v-else />
      </el-icon>
    </div>
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
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-secondary, #475569);
  transition: all 0.2s;
  background: transparent;
  border: none;
}

.theme-toggle-btn:hover {
  background: var(--bg-hover, #f1f5f9);
  color: var(--primary-color, #2563eb);
}
</style>