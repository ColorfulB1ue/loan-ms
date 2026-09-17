<template>
  <el-container class="layout-container admin-layout">
    <el-header class="top-header">
      <div class="header-left">
        <el-icon class="mobile-menu-btn" @click="showMobileMenu = true" v-if="isMobile">
          <Fold />
        </el-icon>
        <div class="logo">贷款审批后台</div>
      </div>
      <div class="user-profile">
        <el-dropdown trigger="click" placement="bottom-end">
          <span class="username-btn">
            <el-icon style="margin-right:4px;"><UserFilled /></el-icon>
            <span class="username-text">{{ username }}</span>
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="openPasswordDialog">
                <el-icon><Key /></el-icon>
                修改密码
              </el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout" style="color:#f56c6c;">
                <el-icon><SwitchButton /></el-icon>
                安全退出
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="main-body">
      <!-- 桌面端侧边栏 -->
      <el-aside v-if="!isMobile" width="220px" class="side-nav">
        <el-menu 
          :default-active="activeMenu" 
          class="el-menu-vertical" 
          router 
          background-color="transparent"
        >
          <div class="menu-title">核心业务区</div>
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataLine /></el-icon>
            <span>数据看板</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/kyc">
            <el-icon><Check /></el-icon>
            <span>客户管理</span>
            <span v-if="badges.kyc > 0" class="menu-count">{{ badges.kyc > 99 ? '99+' : badges.kyc }}</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/loan">
            <el-icon><Coordinate /></el-icon>
            <span>审批中心</span>
            <span v-if="pendingCount > 0" class="menu-count warning">{{ pendingCount > 99 ? '99+' : pendingCount }}</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/finance">
            <el-icon><Coin /></el-icon>
            <span>财务中心</span>
            <span v-if="badges.overdue > 0" class="menu-count danger">{{ badges.overdue > 99 ? '99+' : badges.overdue }}</span>
          </el-menu-item>

          <div class="menu-title" style="margin-top:12px">产品运营</div>
          <el-menu-item index="/admin/products">
            <el-icon><Goods /></el-icon>
            <span>贷款产品</span>
          </el-menu-item>
        </el-menu>
        <div class="sidebar-footer">
          <ThemeToggle />
        </div>
      </el-aside>

      <!-- 移动端抽屉侧边栏 -->
      <el-drawer
        v-model="showMobileMenu"
        direction="ltr"
        size="260px"
        :show-close="false"
        class="mobile-drawer"
      >
        <div class="mobile-menu-header">
          <span>贷款审批后台</span>
          <el-icon @click="showMobileMenu = false"><Close /></el-icon>
        </div>
        <el-menu 
          :default-active="activeMenu" 
          class="el-menu-vertical" 
          router 
          @select="showMobileMenu = false"
        >
          <div class="menu-title">核心业务区</div>
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataLine /></el-icon>
            <span>数据看板</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/kyc">
            <el-icon><Check /></el-icon>
            <span>客户管理</span>
            <span v-if="badges.kyc > 0" class="menu-count">{{ badges.kyc > 99 ? '99+' : badges.kyc }}</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/loan">
            <el-icon><Coordinate /></el-icon>
            <span>审批中心</span>
            <span v-if="pendingCount > 0" class="menu-count warning">{{ pendingCount > 99 ? '99+' : pendingCount }}</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/finance">
            <el-icon><Coin /></el-icon>
            <span>财务中心</span>
            <span v-if="badges.overdue > 0" class="menu-count danger">{{ badges.overdue > 99 ? '99+' : badges.overdue }}</span>
          </el-menu-item>

          <div class="menu-title" style="margin-top:12px">产品运营</div>
          <el-menu-item index="/admin/products">
            <el-icon><Goods /></el-icon>
            <span>贷款产品</span>
          </el-menu-item>
        </el-menu>
      </el-drawer>

      <el-main class="content-area">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>

    <!-- 修改密码弹窗 -->
    <PasswordDialog ref="passwordDialogRef" />
  </el-container>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { 
  DataLine, Check, Coordinate, Coin, Goods, 
  UserFilled, ArrowDown, SwitchButton, Key, Fold, Close
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useBadges } from '@/composables/useBadges'
import { usePassword } from '@/composables/usePassword'
import { authApi } from '@/api'
import PasswordDialog from '@/components/PasswordDialog.vue'
import ThemeToggle from '@/components/ThemeToggle.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const showMobileMenu = ref(false)
const isMobile = ref(false)

// 使用 composables
const { badges, fetchBadges } = useBadges()
const { passwordDialogRef, openPasswordDialog } = usePassword()

// 待审批总数
const pendingCount = computed(() => {
  return (badges.value.loan || 0) + (badges.value.credit || 0) + (badges.value.unfreeze || 0)
})

// 用户名
const username = computed(() => {
  if (userStore.username) return userStore.username
  return '管理员'
})

// 检测是否为移动端
const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

// 轮询定时器
let timer = null

onMounted(() => {
  fetchBadges()
  timer = setInterval(fetchBadges, 30000)
  window.addEventListener('fetch-badges', fetchBadges)
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
  window.removeEventListener('fetch-badges', fetchBadges)
  window.removeEventListener('resize', checkMobile)
})

// 登出
const handleLogout = async () => {
  try {
    await authApi.logout()
  } catch (e) {
    // 忽略吊销失败
  }
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
@import './layout.css';

/* 管理后台使用统一的主题色，不再单独定义红色系 */

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.mobile-menu-btn {
  font-size: 20px;
  cursor: pointer;
  color: var(--text-secondary, #606266);
}

.mobile-menu-btn:hover {
  color: var(--primary-color);
}

/* 菜单计数徽章 */
.menu-count {
  margin-left: auto;
  flex-shrink: 0;
  min-width: 18px;
  height: 18px;
  line-height: 18px;
  padding: 0 5px;
  border-radius: var(--radius-full, 9px);
  background-color: #ef4444;
  color: #fff;
  font-size: 11px;
  font-weight: var(--font-weight-bold, 700);
  text-align: center;
  box-sizing: border-box;
}

.menu-count.warning {
  background-color: #ef4444;
}

.menu-count.danger {
  background-color: #ef4444;
}

.menu-title {
  padding: 12px 20px 8px;
  font-size: 12px;
  color: var(--text-muted, #909399);
  text-transform: uppercase;
  letter-spacing: 1px;
}

.mobile-drawer .mobile-menu-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-default, #e4e7ed);
  font-size: 16px;
  font-weight: 600;
}

.mobile-drawer .mobile-menu-header .el-icon {
  cursor: pointer;
  font-size: 18px;
}

/* 移动端样式 */
@media (max-width: 767px) {
  .username-text {
    display: none;
  }
  
  .top-header {
    padding: 0 12px;
  }
  
  .content-area {
    padding: 12px !important;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>