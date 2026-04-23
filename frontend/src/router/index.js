import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import RecordListView from '../views/RecordListView.vue'
import DiverManagerView from '../views/DiverManagerView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView // 新增潛水紀錄 (主畫面)
    },
    {
      path: '/records',
      name: 'records',
      component: RecordListView // 潛水紀錄總覽
    },
    {
      path: '/divers',
      name: 'divers',
      component: DiverManagerView // 潛水員管理 (CRUD)
    }
  ]
})

export default router
