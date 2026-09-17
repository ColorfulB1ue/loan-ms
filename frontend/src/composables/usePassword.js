import { ref } from 'vue'

/**
 * 修改密码 composable
 */
export function usePassword() {
  const passwordDialogRef = ref(null)

  const openPasswordDialog = () => {
    if (passwordDialogRef.value) {
      passwordDialogRef.value.open()
    }
  }

  return {
    passwordDialogRef,
    openPasswordDialog
  }
}