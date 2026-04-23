<script setup>
import { RouterView } from 'vue-router'
import Sidebar from './components/Sidebar.vue'
</script>

<template>
  <div class="app-layout">
    <Sidebar />
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding: 40px;
  margin-left: 260px; /* Leave space for fixed sidebar */
  transition: all 0.3s ease;
}

@media (max-width: 768px) {
  .app-layout {
    flex-direction: column;
  }
  .main-content {
    margin-left: 0;
    margin-top: 60px; /* Space for mobile top nav */
    padding: 20px;
  }
}
</style>
