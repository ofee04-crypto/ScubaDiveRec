<script setup>
import { ref, onMounted } from 'vue'

const divers = ref([])
const formData = ref({
  account: '',
  password: '',
  expYear: '初階',
  age: ''
})
const message = ref('')
const isLoading = ref(false)

const loadDivers = async () => {
  try {
    const res = await fetch('/api/divers')
    if (res.ok) {
      divers.value = await res.json()
    }
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  loadDivers()
})

const submitDiver = async () => {
  isLoading.value = true
  message.value = ''
  try {
    const res = await fetch('/api/divers', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData.value)
    })
    if (res.status === 201) {
      message.value = '✅ 潛水員新增成功！'
      formData.value = { account: '', password: '', expYear: '初階', age: '' }
      loadDivers() // 重新載入列表
    } else {
      message.value = '❌ 新增失敗'
    }
  } catch (err) {
    message.value = '❌ 網路錯誤'
  } finally {
    isLoading.value = false
  }
}

const deleteDiver = async (id) => {
  if(!confirm('確定要刪除這名潛水員嗎？')) return
  try {
    await fetch(`/api/divers/${id}`, { method: 'DELETE' })
    loadDivers()
  } catch (err) {
    console.error(err)
  }
}
</script>

<template>
  <div class="diver-manager fade-in">
    <header class="page-header">
      <h1>潛水員管理</h1>
      <p>Manage divers and their profiles.</p>
    </header>

    <div class="content-split">
      <!-- 新增表單 -->
      <div class="form-section glass-panel">
        <h2>➕ 新增潛水員</h2>
        <form @submit.prevent="submitDiver" class="diver-form">
          <div class="form-group">
            <label>帳號 (Account)</label>
            <input type="text" v-model="formData.account" required>
          </div>
          <div class="form-group">
            <label>密碼 (Password)</label>
            <input type="password" v-model="formData.password" required>
          </div>
          <div class="form-group">
            <label>潛水經驗 </label>
            <select v-model="formData.expYear">
              <option value="初階" style="color:gray">初階</option>
              <option value="進階" style="color:green">進階</option>
              <option value="教練" style="color:goldenrod">教練</option>
            </select>
          </div>
          <div class="form-group">
            <label>潛齡</label>
            <input type="number" v-model="formData.age">
          </div>
          
          <button type="submit" class="btn-primary" :disabled="isLoading">
            {{ isLoading ? '處理中...' : '建立潛水員' }}
          </button>
          <p v-if="message" class="msg">{{ message }}</p>
        </form>
      </div>

      <!-- 列表 -->
      <div class="list-section">
        <h2>📋 潛水夥伴名單</h2>
        <div class="diver-list">
          <div v-for="d in divers" :key="d.diverId" class="diver-card glass-panel">
            <div class="diver-info">
              <h3>{{ d.account }}</h3>
              <span class="exp-badge">{{ d.expYear }}</span>
              <p class="age-text">潛齡: {{ d.age || 未透漏 }}</p>
            </div>
            <button @click="deleteDiver(d.diverId)" class="btn-danger">刪除</button>
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
}

.content-split {
  display: flex;
  gap: 30px;
  align-items: flex-start;
}

.form-section {
  flex: 1;
  padding: 30px;
}

.form-section h2 {
  margin-bottom: 20px;
  color: var(--accent-cyan);
}

.diver-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.list-section {
  flex: 1.5;
}

.list-section h2 {
  margin-bottom: 20px;
}

.diver-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.diver-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
}

.diver-info h3 {
  margin: 0 0 5px 0;
  font-size: 1.2rem;
}

.exp-badge {
  display: inline-block;
  background: rgba(255, 255, 255, 0.1);
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  margin-right: 10px;
}

.age-text {
  display: inline-block;
  font-size: 0.85rem;
  color: var(--text-muted);
}

.msg {
  margin-top: 10px;
  color: var(--accent-cyan);
}

.fade-in {
  animation: fadeIn 0.5s ease forwards;
}
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@media (max-width: 900px) {
  .content-split {
    flex-direction: column;
  }
}
</style>
