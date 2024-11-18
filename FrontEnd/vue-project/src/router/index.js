import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { showSearchBar: false, showFooterBar:true },
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/InitView.vue'),
      meta: { showSearchBar: false, showFooterBar:false },
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: () => import('@/views/MyProfilePage.vue'),
      meta: { showSearchBar: false, showFooterBar:false },
    },
    {
      path: '/map',
      name: 'map',
      component: () => import('@/views/MapView.vue'),
      meta: { showSearchBar: true, showFooterBar:false },
    },
    {
      path: '/archive',
      name: 'archive',
      component: () => import('@/views/ArchiveBoardView.vue'),
      meta: { showSearchBar: true, showFooterBar:true },
    }

  ],
})

export default router
