<script setup>
import { ref, onMounted, computed } from 'vue'

const records = ref([])
const divers = ref([])
const isLoading = ref(true)
const selectedRecord = ref(null)
const isEditing = ref(false)
const editForm = ref({})

// 分頁設定
const currentPage = ref(1)
const pageSize = 50

onMounted(async () => {
  fetchRecords()
  fetchDivers()
})

const fetchRecords = async () => {
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
}

const fetchDivers = async () => {
  try {
    const res = await fetch('/api/divers')
    if (res.ok) {
      divers.value = await res.json()
    }
  } catch (err) {
    console.error(err)
  }
}

// 計算當前頁面要顯示的資料
const paginatedRecords = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return records.value.slice(start, end)
})

const totalPages = computed(() => Math.ceil(records.value.length / pageSize))

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return 'N/A'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-TW', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const showDetails = (record) => {
  selectedRecord.value = record
  isEditing.value = false
}

const closeDetails = () => {
  selectedRecord.value = null
  isEditing.value = false
}

const startEdit = () => {
  editForm.value = JSON.parse(JSON.stringify(selectedRecord.value))
  isEditing.value = true
}

const cancelEdit = () => {
  isEditing.value = false
}

const saveEdit = async () => {
  try {
    const res = await fetch(`/api/records/${editForm.value.recId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(editForm.value)
    })
    if (res.ok) {
      alert('✅ 更新成功')
      fetchRecords()
      closeDetails()
    } else {
      alert('❌ 更新失敗')
    }
  } catch (err) {
    alert('❌ 網路錯誤')
  }
}

const deleteRecord = async (id) => {
  if (!confirm('🚨 確定要刪除這筆紀錄嗎？此動作無法復原。')) return
  
  try {
    const res = await fetch(`/api/records/${id}`, {
      method: 'DELETE'
    })
    if (res.ok) {
      fetchRecords()
      closeDetails()
    } else {
      alert('❌ 刪除失敗')
    }
  } catch (err) {
    alert('❌ 網路錯誤')
  }
}
</script>

<template>
  <div class="records-view fade-in">
    <header class="page-header">
      <h1>潛水紀錄總覽</h1>
      <p>顯示第 {{ currentPage }} 頁，共 {{ totalPages }} 頁</p>
    </header>

    <div v-if="isLoading" class="loading">載入中...</div>
    
    <div v-else-if="records.length === 0" class="empty-state glass-panel">
      尚無潛水紀錄，趕快去新增一筆吧！
    </div>

    <div v-else>
      <div class="record-grid">
        <div 
          v-for="r in paginatedRecords" 
          :key="r.recId" 
          class="record-card glass-panel"
          @click="showDetails(r)"
        >
          <div class="card-header">
            <span class="badge">{{ r.divingType }} : {{ r.location || '未知地' }}</span>
            <h3 class="date-small">{{ formatDate(r.startDatetime).split(' ')[0] }}</h3>
          </div>
          <div class="card-footer">
            
          </div>
          <div class="card-body">
            <div class="stat-mini">
              <span class="label">潛水員: {{ r.account || '?' }}</span>
            </div>
            
            <div class="stat-mini">
              <span class="label">天氣: {{ r.weather || '?' }}</span>
            </div>

            <div class="stat-mini">
              <span class="label">深度: {{ r.maxDeepthMeter || '?'}} m</span>
            </div>

            <div class="stat-mini">
              <span class="label">生物: {{ r.creatureFound || '-'}}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 分頁控制按鈕 -->
      <div v-if="totalPages > 1" class="pagination-container">
        <button 
          class="btn-ghost" 
          :disabled="currentPage === 1" 
          @click="changePage(currentPage - 1)"
        >
          上一頁
        </button>
        <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
        <button 
          class="btn-ghost" 
          :disabled="currentPage === totalPages" 
          @click="changePage(currentPage + 1)"
        >
          下一頁
        </button>
      </div>
    </div>

    <!-- 詳細資訊彈窗 (保持不變) -->
    <Transition name="fade">
      <div v-if="selectedRecord" class="modal-overlay" @click.self="closeDetails">
        <div class="modal-content glass-panel fade-up">
          <div class="modal-header">
            <h2 v-if="!isEditing">潛水詳情 - {{ selectedRecord.location }}</h2>
            <h2 v-else>編輯紀錄</h2>
            <button class="close-btn" @click="closeDetails">&times;</button>
          </div>
          
          <div class="modal-body">
            <!-- 詳情模式 -->
            <template v-if="!isEditing">
              <div class="detail-grid">
                <div class="detail-item">
                  <label>潛水員</label>
                  <p>{{ selectedRecord.account }}</p>
                </div>
                <div class="detail-item">
                  <label>天氣</label>
                  <p>{{ selectedRecord.weather || '未記錄' }}</p>
                </div>
                <div class="detail-item">
                  <label>潛水類型</label>
                  <p>{{ selectedRecord.divingType }}</p>
                </div>
                <div class="detail-item">
                  <label>氣體類型</label>
                  <p>{{ selectedRecord.gasType }}</p>
                </div>
                <div class="detail-item">
                  <label>入水時間</label>
                  <p>{{ formatDate(selectedRecord.startDatetime) }}</p>
                </div>
                <div class="detail-item">
                  <label>出水時間</label>
                  <p>{{ formatDate(selectedRecord.endDatetime) }}</p>
                </div>
                <div class="detail-item">
                  <label>最大深度</label>
                  <p>{{ selectedRecord.maxDeepthMeter }} m</p>
                </div>
                <div class="detail-item">
                  <label>能見度</label>
                  <p>{{ selectedRecord.visibilityMeter }} m</p>
                </div>
                <div class="detail-item">
                  <label>起始 / 結束氣壓</label>
                  <p>{{ selectedRecord.startGasBar }} / {{ selectedRecord.endGasBar }} Bar</p>
                </div>
              </div>
              <div class="detail-item full-width">
                <label>遇見的生物</label>
                <p>{{ selectedRecord.creatureFound || '無' }}</p>
              </div>
              <div class="detail-item full-width">
                <label>潛水心得</label>
                <p class="desc">{{ selectedRecord.experienceDescribe || '無備註' }}</p>
              </div>

              <div class="modal-actions">
                <button class="btn-primary" @click="startEdit">編輯紀錄</button>
                <button class="btn-danger" @click="deleteRecord(selectedRecord.recId)">刪除紀錄</button>
              </div>
            </template>

            <!-- 編輯模式 (保持不變) -->
            <template v-else>
              <div class="edit-form">
                <div class="form-row">
                  <div class="form-group">
                    <label>潛水員</label>
                    <select v-model="editForm.diverId">
                      <option v-for="d in divers" :key="d.diverId" :value="d.diverId">
                        {{ d.account }}
                      </option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label>潛水類型</label>
                    <select v-model="editForm.divingType">
                      <option value="岸潛">岸潛</option>
                      <option value="船潛">船潛</option>
                      <option value="放流">放流</option>
                      <option value="自潛">自潛</option>
                    </select>
                  </div>
                </div>
                <div class="form-row">
                  <div class="form-group">
                    <label>潛點位置</label>
                    <input type="text" v-model="editForm.location">
                  </div>
                  <div class="form-group">
                    <label>天氣</label>
                    <input type="text" v-model="editForm.weather">
                  </div>
                </div>
                <div class="form-row">
                  <div class="form-group">
                    <label>入水時間</label>
                    <input type="datetime-local" v-model="editForm.startDatetime">
                  </div>
                  <div class="form-group">
                    <label>出水時間</label>
                    <input type="datetime-local" v-model="editForm.endDatetime">
                  </div>
                </div>
                <div class="form-row">
                  <div class="form-group">
                    <label>最大深度</label>
                    <input type="number" v-model="editForm.maxDeepthMeter" step="0.1">
                  </div>
                  <div class="form-group">
                    <label>能見度</label>
                    <input type="number" v-model="editForm.visibilityMeter">
                  </div>
                </div>
                <div class="form-group">
                  <label>潛水心得</label>
                  <textarea v-model="editForm.experienceDescribe"></textarea>
                </div>
                <div class="modal-actions">
                  <button class="btn-primary" @click="saveEdit">儲存變更</button>
                  <button class="btn-ghost" @click="cancelEdit">取消</button>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<style scoped>
.page-header {
  margin-bottom: 30px;
}

.record-grid {
  display: grid;
  /* 最多 5 欄，卡片寬度在 150px 到 1fr 之間切換 */
  grid-template-columns: repeat(auto-fill, minmax(max(150px, (100% - 9 * 15px) / 5), 1fr));
  gap: 15px;
}

.record-card {
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-height: 180px;
}

.location-title {
  font-size: 1.1rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin: 5px 0;
}

.stat-mini {
  display: flex;
  flex-direction: column;
  font-size: 1rem;
}

.stat-mini .label {
  color: var(--text-muted);
  font-size: 1rem;
}

.date-small {
  font-size: 0.85rem;
  color: white;
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 40px;
  padding: 20px;
}

.page-info {
  font-weight: 500;
  color: var(--accent-cyan);
}

.modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(4px);
  z-index: 1000;
  display: flex; justify-content: center; align-items: center;
  padding: 20px;
}

.modal-content {
  width: 100%; max-width: 650px;
  max-height: 90vh; overflow-y: auto;
  padding: 30px;
}

.detail-grid {
  display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;
}

.full-width { grid-column: span 2; }

.modal-actions {
  display: flex; gap: 15px; margin-top: 30px;
  border-top: 1px solid var(--border-glass); padding-top: 20px;
}

.btn-danger {
  background: rgba(255, 59, 48, 0.1);
  color: #FF3B30; border: 1px solid #FF3B30;
  padding: 10px 24px; border-radius: 8px; cursor: pointer;
}

.btn-danger:hover {
  background: #FF3B30; color: #fff;
}
</style>
