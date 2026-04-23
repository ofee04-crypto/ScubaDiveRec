<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <aside class="sidebar glass-panel">
    <div class="logo-area">
      <h2>🌊 ScubaFun</h2>
      <p>Log Your Dive</p>
    </div>
    
    <nav class="nav-links">
      <RouterLink to="/" class="nav-item">
        <span class="icon">📝</span>
        <span class="text">新增潛水紀錄</span>
      </RouterLink>
      <RouterLink to="/records" class="nav-item">
        <span class="icon">🗂️</span>
        <span class="text">紀錄總覽</span>
      </RouterLink>
      <div class="divider"></div>
      <RouterLink to="/divers" class="nav-item">
        <span class="icon">🤿</span>
        <span class="text">潛水員管理 (CRUD)</span>
      </RouterLink>
    </nav>
  </aside>
</template>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 260px;
  height: 100vh;
  border-radius: 0;
  border-left: none;
  border-top: none;
  border-bottom: none;
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  z-index: 100;
}

.logo-area {
  margin-bottom: 40px;
  text-align: center;
}

.logo-area h2 {
  color: var(--accent-cyan);
  font-size: 1.8rem;
  letter-spacing: 1px;
}

.logo-area p {
  color: var(--text-muted);
  font-size: 0.9rem;
  margin-top: 5px;
}

.nav-links {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-radius: 8px;
  text-decoration: none;
  color: var(--text-muted);
  transition: all 0.3s ease;
}

.nav-item .icon {
  margin-right: 12px;
  font-size: 1.2rem;
}

.nav-item:hover {
  background: rgba(0, 229, 255, 0.1);
  color: var(--text-main);
  transform: translateX(5px);
}

.router-link-active {
  background: rgba(0, 229, 255, 0.15);
  color: var(--accent-cyan);
  font-weight: 600;
  border-left: 3px solid var(--accent-cyan);
}

.divider {
  height: 1px;
  background: var(--border-glass);
  margin: 15px 0;
}

@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: 60px;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    border-right: none;
    border-bottom: 1px solid var(--border-glass);
  }
  
  .logo-area {
    margin-bottom: 0;
  }
  
  .logo-area h2 {
    font-size: 1.2rem;
  }
  
  .logo-area p {
    display: none;
  }
  
  .nav-links {
    flex-direction: row;
  }
  
  .nav-item .text {
    display: none; /* Hide text on mobile, just icons */
  }
  
  .nav-item {
    padding: 8px;
  }
  
  .nav-item:hover {
    transform: none;
  }
  
  .divider {
    width: 1px;
    height: 20px;
    margin: 0 10px;
  }
}
</style>
