import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 用户状态 composable
 */
export function useUser() {
  const userStore = useUserStore()

  const token = computed(() => userStore.token)
  const role = computed(() => userStore.role)
  const username = computed(() => userStore.username)
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => role.value === 1)
  const isCustomer = computed(() => role.value === 0)

  const logout = () => {
    userStore.logout()
    window.location.href = '/login'
  }

  return {
    token,
    role,
    username,
    isLoggedIn,
    isAdmin,
    isCustomer,
    logout
  }
}