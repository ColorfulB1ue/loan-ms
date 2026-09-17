import { ref, watch, onMounted } from 'vue'

/**
 * 主题切换 composable
 * 支持亮色/暗色主题
 */
export function useTheme() {
  // 从localStorage读取主题，默认为light
  const theme = ref(localStorage.getItem('theme') || 'light')

  // 切换主题
  const toggleTheme = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light'
  }

  // 设置主题
  const setTheme = (newTheme) => {
    theme.value = newTheme
  }

  // 应用主题到DOM
  const applyTheme = (newTheme) => {
    const html = document.documentElement
    html.setAttribute('data-theme', newTheme)
    localStorage.setItem('theme', newTheme)
    
    // 更新Element Plus暗色模式
    if (newTheme === 'dark') {
      html.classList.add('dark')
    } else {
      html.classList.remove('dark')
    }
  }

  // 监听主题变化
  watch(theme, (newTheme) => {
    applyTheme(newTheme)
  })

  // 初始化时应用主题
  onMounted(() => {
    applyTheme(theme.value)
  })

  return {
    theme,
    toggleTheme,
    setTheme,
    isDark: () => theme.value === 'dark'
  }
}