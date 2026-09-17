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
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.05) 0%, rgba(59, 130, 246, 0.05) 100%);
  border-radius: var(--radius-xl, 12px);
  padding: var(--space-6, 24px);
  margin-bottom: var(--space-6, 24px);
  border: 1px solid rgba(37, 99, 235, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: var(--font-size-xl, 20px);
  font-weight: var(--font-weight-semibold, 600);
  color: var(--text-primary);
  margin: 0;
}

.page-subtitle {
  font-size: var(--font-size-sm, 14px);
  color: var(--text-secondary);
  margin: var(--space-1, 4px) 0 0 0;
}

.header-extra {
  margin-top: var(--space-4, 16px);
  padding-top: var(--space-4, 16px);
  border-top: 1px solid var(--border-subtle);
}

.header-right {
  display: flex;
  gap: var(--space-3, 12px);
  align-items: center;
}
</style>