<script setup>
import { ref, onMounted } from 'vue'

const divers = ref([])
const formData = ref({
  diverId: '',
  divingType: '',
  startDatetime: '',
  endDatetime: '',
  weather: '',
  location: '',
  gasType: '',
  startGasBar: '0.0',
  endGasBar: '0.0',
  maxDeepthMeter: '0.0',
  visibilityMeter: '0.0',
  creatureFound: '',
  experienceDescribe: ''
})
const message = ref('')
const isLoading = ref(false)

// 載入潛水員選單
onMounted(async () => {
  try {
    const res = await fetch('/api/divers')
    if (res.ok) {
      divers.value = await res.json()
    }
  } catch (err) {
    console.error('Failed to load divers', err)
  }
})

const submitRecord = async () => {
  isLoading.value = true
  message.value = ''
  try {
    const res = await fetch('/api/records', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData.value)
    })
    if (res.status === 201) {
      message.value = '✅ 紀錄新增成功！'
      // 重置表單 (保留 diverId 方便連續填寫)
      formData.value.location = ''
      formData.value.startDatetime = ''
      formData.value.endDatetime = ''
      formData.value.startGasBar = ''
      formData.value.endGasBar = ''
      formData.value.maxDeepthMeter = ''
      formData.value.visibilityMeter = ''
      formData.value.creatureFound = ''
      formData.value.experienceDescribe = ''
    } else {
      message.value = '❌ 新增失敗，請重試'
    }
  } catch (err) {
    message.value = '❌ 網路連線錯誤'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="home-view fade-in">
    <header class="page-header">
      <h1>新增潛水紀錄</h1>
      <p>Log your amazing underwater journey.</p>
    </header>

    <div class="form-container glass-panel">
      <form @submit.prevent="submitRecord" class="dive-form">
        
        <!-- Row 1: Diver & Type -->
        <div class="form-row">
          <div class="form-group">
            <label>*潛水員</label>
            <select v-model="formData.diverId" required>
              <option value="" disabled>請選擇</option>
              <option v-for="d in divers" :key="d.diverId" :value="d.diverId">
               {{ d.account }} <!--  (ID: {{ d.diverId }}) -->
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>*潛水類型</label>
            <select v-model="formData.divingType" required>
              <option value="" disabled>請選擇</option>
              <option value="岸潛">岸潛 (Shore Dive)</option>
              <option value="船潛">船潛 (Boat Dive)</option>
              <option value="放流">放流 (Drift Dive)</option>
              <option value="自潛">自潛 (Free Dive)</option>
            </select>
          </div>
        </div>

        <!-- Row 2: DateTime -->
        <div class="form-row">
          <div class="form-group">
            <label>入水時間 (Start)</label>
            <input type="datetime-local" v-model="formData.startDatetime">
          </div>
          <div class="form-group">
            <label>出水時間 (End)</label>
            <input type="datetime-local" v-model="formData.endDatetime">
          </div>
        </div>

        <!-- Row 3: Location & Weather -->
        <div class="form-row">
          <div class="form-group">
            <label>*潛點位置</label>
            <input type="text" v-model="formData.location" placeholder="例如: 綠島 石朗" required>
          </div>
          <div class="form-group">
            <label>天氣情況</label>
            <select v-model="formData.weather">
              <option value="" disabled>請選擇</option>
              <option value="晴天">晴天 (Sunny)</option>
              <option value="多雲">多雲 (Cloudy)</option>
              <option value="陰天">陰天 (Overcast)</option>
              <option value="雨天">雨天 (Rainy)</option>
            </select>
          </div>
        </div>

        <!-- Row 4: Gas & Pressure -->
        <div class="form-row">
          <div class="form-group">
            <label>*氣體類型</label>
            <select v-model="formData.gasType" required>
              <option value="" disabled>請選擇</option>
              <option value="空氣 (Air)">空氣 (Air)</option>
              <option value="高氧 (Nitrox)">高氧 (Nitrox)</option>
            </select>
          </div>
          <div class="form-group">
            <label>起始氣壓 (Bar)</label>
            <input type="number" v-model="formData.startGasBar" placeholder="例如: 200">
          </div>
          <div class="form-group">
            <label>結束氣壓 (Bar)</label>
            <input type="number" v-model="formData.endGasBar" placeholder="例如: 50">
          </div>
        </div>

        <!-- Row 5: Depth & Visibility -->
        <div class="form-row">
          <div class="form-group">
            <label>最大深度 (米)</label>
            <input type="number" v-model="formData.maxDeepthMeter" step="0.1" placeholder="例如: 18.5">
          </div>
          <div class="form-group">
            <label>能見度 (米)</label>
            <input type="number" v-model="formData.visibilityMeter" placeholder="例如: 15">
          </div>
        </div>

        <!-- Row 6: Creatures -->
        <div class="form-group">
          <label>遇見的海洋生物</label>
          <input type="text" v-model="formData.creatureFound" placeholder="例如: 海龜, 小丑魚, 海蛞蝓">
        </div>

        <!-- Row 7: Comment -->
        <div class="form-group">
          <label>潛水心得與註記</label>
          <textarea v-model="formData.experienceDescribe" placeholder="看到了..."></textarea>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary" :disabled="isLoading">
            {{ isLoading ? '儲存中...' : '送出紀錄' }}
          </button>
          <span v-if="message" class="toast-msg">{{ message }}</span>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.page-header {
  margin-bottom: 30px;
}
.page-header h1 {
  font-size: 2.5rem;
  background: linear-gradient(90deg, #FFFFFF, var(--accent-cyan));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.form-container {
  max-width: 800px;
  padding: 40px;
}

.dive-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: flex;
  gap: 20px;
}
.form-row > .form-group {
  flex: 1;
}

textarea {
  width: 100%;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--border-glass);
  border-radius: 8px;
  color: white;
  outline: none;
  resize: vertical;
  min-height: 100px;
  transition: border-color 0.3s ease;
}
textarea:focus {
  border-color: var(--accent-cyan);
}

.form-actions {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-top: 10px;
}

.toast-msg {
  color: var(--accent-cyan);
  font-weight: 500;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.fade-in {
  animation: fadeIn 0.5s ease forwards;
}

@media (max-width: 600px) {
  .form-row {
    flex-direction: column;
  }
}
</style>
