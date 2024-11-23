import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/LandingView.vue'
import useUserStore from '@/stores/user-store'

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
      path: '/mypage',
      name: 'mypage',
      component: () => import('@/views/User/MyPageView.vue'),
      meta: { showSearchBar: false, showFooterBar: false },
    },
    {
      path: '/mypage/edit',
      name: 'mypageEdit',
      component: () => import('@/views/User/EditProfilePage.vue'),
      meta: { showSearchBar: false, showFooterBar: false, auth: true },
    },
    {
      path: '/map',
      name: 'map',
      component: () => import('@/views/Map/Apt/MapView.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/share',
      name: 'share',
      component: () => import('@/views/RoomShare/RoomShareView.vue'),
      meta: { showSearchBar: true, showFooterBar: true },
    },
    {
      path: '/share/user/:id',
      name: 'share-user',
      component: () => import('@/views/RoomShare/RoomShareView.vue'),
      meta: { showSearchBar: true, showFooterBar: true },
    },
    {
      path: '/share/write',
      name: 'share-write',
      component: () => import('@/views/RoomShare/RoomShareWriteView.vue'),
      meta: { showSearchBar: true, showFooterBar: false, auth: true },
    },
    {
      path: '/share/edit/:id',
      name: 'share-edit',
      component: () => import('@/views/RoomShare/RoomShareEditView.vue'),
      meta: { showSearchBar: true, showFooterBar: false, auth: true },
      props: true,
    },
    {
      path: '/favorites',
      name: 'favorites',
      component: () => import('@/views/Map/Bookmark/MapBookmarkView.vue'),
      meta: { showSearchBar: true, showFooterBar: false, auth: true },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/User/LoginPage.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: () => import('@/views/User/SignupPage.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: () => import('@/views/User/ForgotPasswordPage.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
    {
      path: '/RealEstateDetail',
      name: 'RealEstateDetail',
      component: () => import('@/components/Map/RealEstateDetail.vue'),
      meta: { showSearchBar: true, showFooterBar: false },
    },
  ],
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore() // Call the store function here
  const userId = userStore.getUserId // Access the user ID safely
  if (to.meta.auth && !userId) {
    alert('로그인이 필요한 페이지입니다!')
    next('/login')
  } else {
    next()
  }
})

export default router
