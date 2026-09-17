<template>
  <div class="login-container">
    <div class="glass-box">
      <div class="header">
        <h1>贷款申请与审批系统</h1>
        <p>统一的贷款申请、审批与额度管理平台</p>
      </div>

      <div class="toggle-mode">
        <span :class="{ active: isLogin }" @click="isLogin = true">登录</span>
        <span :class="{ active: !isLogin }" @click="isLogin = false">注册账户</span>
      </div>

      <div class="form-body">
        <el-input 
          v-model="form.username" 
          placeholder="请输入账号名称" 
          :prefix-icon="User"
          size="large">
        </el-input>
        <el-input 
          v-model="form.password" 
          type="password" 
          placeholder="请输入密码" 
          :prefix-icon="Lock"
          size="large"
          show-password>
        </el-input>
        <el-input 
          v-if="!isLogin"
          v-model="form.confirmPassword" 
          type="password" 
          placeholder="请再次确认密码" 
          :prefix-icon="Lock"
          size="large"
          show-password>
        </el-input>

        <el-button 
          color="#2563eb" 
          class="submit-btn" 
          :loading="loading"
          @click="handleSubmit" 
          round>
          {{ isLogin ? '登录' : '创建账户' }}
        </el-button>
      </div>

      <div class="version">
        © 2026 Young-Bank all rights reserved.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { jwtDecode } from 'jwt-decode'

const router = useRouter()
const userStore = useUserStore()
const isLogin = ref(true)
const loading = ref(false)

const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

const handleSubmit = async () => {
    if (!form.value.username || !form.value.password) {
        ElMessage.warning('账号与密码均不可为空')
        return
    }
    if (!isLogin.value && form.value.password !== form.value.confirmPassword) {
        ElMessage.error('两次输入的密码不一致，请重新检查！')
        return
    }
    
    loading.value = true
    try {
        if (isLogin.value) {
            // 登录
            const res = await authApi.login(form.value)
            const token = res.data
            const payload = jwtDecode(token)
            if (!payload) {
                ElMessage.error('登录凭证解析失败')
                return
            }
            userStore.setAuth(token, payload.role, form.value.username)
            ElMessage.success('登录成功！欢迎回来~')
            if (payload.role === 1) {
                router.push('/admin/dashboard')
            } else {
                router.push('/client/dashboard')
            }
        } else {
            // 注册成功后尝试自动登录
            await authApi.register(form.value)
            ElMessage.success('注册成功！正在自动登录...')
            try {
                const loginRes = await authApi.login(form.value)
                const token = loginRes.data
                const payload = jwtDecode(token)
                if (!payload) {
                    ElMessage.warning('注册成功，请手动登录')
                    isLogin.value = true
                    return
                }
                userStore.setAuth(token, payload.role, form.value.username)
                if (payload.role === 1) {
                    router.push('/admin/dashboard')
                } else {
                    router.push('/client/dashboard')
                }
            } catch (e) {
                ElMessage.warning('注册成功，请手动登录')
                isLogin.value = true
            }
        }
    } catch (e) {
        // 请求内部已由拦截器进行 Message 提示
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, var(--color-gray-900, #0c1220) 0%, var(--color-primary-800, #1e3a5f) 50%, var(--color-gray-900, #0d1b2a) 100%);
  position: relative;
  overflow: hidden;
}

/* 炫光背景 */
.login-container::before {
  content: '';
  position: absolute;
  top: -20%;
  left: -10%;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.25) 0%, rgba(0,0,0,0) 70%);
  border-radius: 50%;
  filter: blur(40px);
  animation: floatLight 10s ease-in-out infinite alternate;
}

@keyframes floatLight {
  100% { transform: translate(50%, 50%); }
}

.glass-box {
  width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: var(--radius-2xl, 24px);
  box-shadow: var(--shadow-xl, 0 25px 50px -12px rgba(0, 0, 0, 0.5));
  z-index: 1;
  text-align: center;
}

.header h1 {
  font-size: var(--font-size-3xl, 28px);
  color: var(--text-inverse, #fff);
  margin-bottom: 5px;
  font-weight: var(--font-weight-bold, 800);
  letter-spacing: 1px;
}
.header h1 span {
  color: var(--color-primary-400, #60a5fa);
  font-weight: var(--font-weight-normal, 400);
}
.header p {
  color: var(--color-gray-300, #cbd5e1);
  font-size: var(--font-size-sm, 14px);
  margin-bottom: 30px;
}

.toggle-mode {
  display: flex;
  background: rgba(0,0,0,0.3);
  border-radius: var(--radius-xl, 12px);
  padding: 5px;
  margin-bottom: 30px;
}
.toggle-mode span {
  flex: 1;
  padding: 10px 0;
  color: var(--color-gray-300, #cbd5e1);
  cursor: pointer;
  font-size: var(--font-size-sm, 14px);
  font-weight: var(--font-weight-bold, bold);
  border-radius: var(--radius-lg, 8px);
  transition: all var(--transition-slow, 0.3s);
}
.toggle-mode span.active {
  background: var(--primary-color, #2563eb);
  color: var(--text-inverse, #fff);
  box-shadow: 0 4px 15px rgba(37, 99, 235, 0.4);
}

.form-body .el-input {
  margin-bottom: 20px;
}
:deep(.el-input__wrapper) {
  background: rgba(0,0,0,0.2) !important;
  box-shadow: 0 0 0 1px rgba(255,255,255,0.1) inset !important;
  border-radius: var(--radius-xl, 12px) !important;
  outline: none !important;
  transition: all var(--transition-normal, 0.2s);
}
:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px rgba(255,255,255,0.2) inset !important;
}
:deep(.el-input__wrapper.is-focus),
:deep(.el-input__wrapper:focus) {
  outline: none !important;
  box-shadow: 0 0 0 1px var(--el-color-primary, #2563eb) inset, 0 0 0 3px rgba(37, 99, 235, 0.25) !important;
  border-radius: var(--radius-xl, 12px) !important;
}
:deep(.el-input__inner) {
  color: var(--text-inverse, #fff) !important;
}
:deep(.el-input__inner::placeholder) {
  color: rgba(255,255,255,0.4);
}
/* 登录页输入框聚焦时的 outline（Element Plus 默认绿色框） */
:deep(.el-input__wrapper):focus-visible {
  outline: none !important;
}

.submit-btn {
  width: 100%;
  margin-top: 10px;
  height: 48px;
  font-size: var(--font-size-base, 16px);
  font-weight: var(--font-weight-bold, bold);
  letter-spacing: 2px;
}

.version {
  margin-top: 30px;
  font-size: var(--font-size-xs, 12px);
  color: rgba(255,255,255,0.3);
}
</style>
