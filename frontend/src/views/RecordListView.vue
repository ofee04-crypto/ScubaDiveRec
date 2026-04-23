<script setup>
import { ref, onMounted } from 'vue'

const records = ref([])
const isLoading = ref(true)

onMounted(async () => {
  try {
    const res = await fetch('/api/records')
    if (res.ok) {
      records.value = await res.json()
    }
  } catch (err) {
    console.error(err)
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <div class="records-view fade-in">
    <header class="page-header">
      <h1>潛水紀錄總覽</h1>
      <p>Your previous underwater adventures.</p>
    </header>

    <div v-if="isLoading" class="loading">載入中...</div>
    
    <div v-else-if="records.length === 0" class="empty-state glass-panel">
      尚無潛水紀錄，趕快去新增一筆吧！
    </div>

    <div v-else class="record-grid">
      <div v-for="r in records" :key="r.recId" class="record-card glass-panel">
        <div class="card-header">
          <span class="badge">{{ r.divingType }}</span>
          <h3>{{ r.location || '未記錄地點' }}</h3>
        </div>
        <div class="card-body">
          <div class="stat-row">
            <span>Diver ID:</span>
            <strong>#{{ r.diverId }}</strong>
          </div>
          <div class="stat-row">
            <span>深度:</span>
            <strong>{{ r.depth ? r.depth + ' m' : '--' }}</strong>
          </div>
          <div class="stat-row">
            <span>能見度:</span>
            <strong>{{ r.visibility ? r.visibility + ' m' : '--' }}</strong>
          </div>
          <div class="comment-box" v-if="r.comment">
            "{{ r.comment }}"
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-header {
  margin-bottom: 30px;
}
.page-header h1 {
  font-size: 2.5rem;
  color: var(--text-main);
}
.page-header p {
  color: var(--text-muted);
}

.record-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.record-card {
  padding: 20px;
  transition: transform 0.3s ease;
}

.record-card:hover {
  transform: translateY(-5px);
  border-color: rgba(0, 229, 255, 0.4);
}

.card-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
  margin-bottom: 20px;
  border-bottom: 1px solid var(--border-glass);
  padding-bottom: 15px;
}

.badge {
  background: rgba(0, 229, 255, 0.2);
  color: var(--accent-cyan);
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
}

.card-header h3 {
  font-size: 1.4rem;
  margin: 0;
}

.stat-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  color: var(--text-muted);
}
.stat-row strong {
  color: var(--text-main);
}

.comment-box {
  margin-top: 15px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  font-style: italic;
  font-size: 0.9rem;
  color: #B0C4DE;
}

.fade-in {
  animation: fadeIn 0.5s ease forwards;
}
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.loading, .empty-state {
  padding: 40px;
  text-align: center;
  color: var(--text-muted);
}
</style>
