<template>
  <div class="page-header">
    <div class="header-content">
      <div class="header-left">
        <h2 class="page-title">{{ title }}</h2>
        <p v-if="subtitle" class="page-subtitle">{{ subtitle }}</p>
      </div>
      <div class="header-right">
        <slot name="actions"></slot>
      </div>
    </div>
    <div v-if="$slots.default" class="header-extra">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true
  },
  subtitle: {
    type: String,
    default: ''
  }
})
</script>

<style scoped>
.page-header {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.05) 0%, rgba(139, 92, 246, 0.05) 100%);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  border: 1px solid rgba(99, 102, 241, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary, #1a1a2e);
  margin: 0;
}

.page-subtitle {
  font-size: 14px;
  color: var(--text-secondary, #666);
  margin: 4px 0 0 0;
}

.header-extra {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.header-right {
  display: flex;
  gap: 12px;
  align-items: center;
}
</style>