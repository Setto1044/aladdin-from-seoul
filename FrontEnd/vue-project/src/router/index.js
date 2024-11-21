import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { showSearchBar: false, showFooterBar: true },
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/InitView.vue'),
      meta: { showSearchBar: false, showFooterBar: false },
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: () => import('@/views/MyPage.vue'),
      meta: { showSearchBar: false, showFooterBar: false },
    },
    {
      path: '/mypage/edit',
      name: 'mypageEdit',
      component: () => import('@/views/EditProfilePage.vue'),
      meta: { showSearchBar: false, showFooterBar: false },
    },
    {
      path: '/map',
      name: 'map',
      component: () => import('@/views/MapView.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/share',
      name: 'share',
      component: () => import('@/views/RoomShareView.vue'),
      meta: { showSearchBar: true, showFooterBar: true },
    },
    {
      path: '/share/user/:id',
      name: 'share-user',
      component: () => import('@/views/RoomShareView.vue'),
      meta: { showSearchBar: true, showFooterBar: true },
    },
    {
      path: '/share/write',
      name: 'share-write',
      component: () => import('@/views/RoomShareWriteView.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/share/edit/:id',
      name: 'share-edit',
      component: () => import('@/views/RoomShareEditView.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
      props: true,
    },
    {
      path: '/favorites',
      name: 'favorites',
      component: () => import('@/views/MapView.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginPage.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: () => import('@/views/SignupPage.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: () => import('@/views/ForgotPasswordPage.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/RealEstateDetail',
      name: 'RealEstateDetail',
      component: () => import('@/components/RealEstateDetail.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
  ],
})

export default router
