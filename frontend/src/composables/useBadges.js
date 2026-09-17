import { ref } from 'vue'
import request from '@/utils/request'

/**
 * 管理端徽章 composable
 */
export function useBadges() {
  const badges = ref({
    kyc: 0,
    loan: 0,
    credit: 0,
    unfreeze: 0,
    overdue: 0
  })
  const loading = ref(false)

  const fetchBadges = async () => {
    loading.value = true
    try {
      const res = await request.get('/admin/stat/badges')
      if (res.code === 200) {
        badges.value = res.data
      }
    } catch (e) {
      console.error('获取徽章数据失败:', e)
    } finally {
      loading.value = false
    }
  }

  return {
    badges,
    loading,
    fetchBadges
  }
}