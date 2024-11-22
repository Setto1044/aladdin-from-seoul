<template>
  <header class="top-bar">
    <!-- Logo and Site Title -->
    <a href="/" class="logo-container">
      <img src="@/assets/path-to-logo.svg" alt="Site Logo" class="logo" />
    </a>

    <nav class="main-navigation">
      <button class="nav-link" @click="navigateTo('/map')">지도</button>
      <button class="nav-link" @click="navigateTo('/share')">방 나누기 게시판</button>
      <button class="nav-link" @click="navigateTo('/favorites')">내 관심목록</button>
      <!-- 로그인 상태에 따라 다른 버튼 활성화 -->
      <button v-if="isLoggedIn" class="nav-link" @click="navigateTo('/mypage')">마이페이지</button>
      <button v-if="isLoggedIn" class="nav-link" @click="handleLogout">로그아웃</button>
      <button v-else class="nav-link" @click="redirectToLogin">로그인</button>
    </nav>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import useUserStore from '@/stores/user-store'

const router = useRouter()
const userStore = useUserStore()

// Pinia 상태를 반응형으로 사용
const isLoggedIn = computed(() => userStore.isLoggedIn)

// 라우트 이동 함수
const navigateTo = (path) => {
  router.push(path)
}

// 로그아웃 처리
const handleLogout = () => {
  userStore.logout()
  alert('로그아웃 되었습니다.')
  router.push('/login') // 로그아웃 후 로그인 페이지로 이동
}

// 로그인 페이지로 이동
const redirectToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.top-bar {
  width: 100%; /* 스크롤바로 인해 네비게이션 바가 조정되지 않도록 뷰포트 기준 너비 설정 */
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #ffffff; /* 배경색 */
  padding: 0 20px;
  height: 40px; /* 고정 높이 */
  position: sticky;
  top: 0;
  z-index: 1000;
}

.logo-container {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo {
  height: 40px; /* 로고 크기 */
  margin-right: 10px;
}

.site-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333333;
}

.main-navigation {
  display: flex;
  align-items: center;
  gap: 20px; /* 네비게이션 링크 간 간격 */
}

.nav-link {
  text-decoration: none;
  font-size: 1rem;
  font-weight: 600;
  color: #333333;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  background-color: #ffffff;
  transition: all 0.3s ease; /* 모든 변화에 부드러운 전환 효과 */
  cursor: pointer;
}

.nav-link:hover {
  background-color: #007bff; /* hover 시 강조 색상 */
  color: #ffffff; /* hover 시 텍스트 색상 변경 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* hover 시 그림자 강화 */
  transform: translateY(-2px); /* 살짝 올라가는 효과 */
}

.nav-link:active {
  transform: translateY(0); /* 클릭 시 원래 위치로 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 클릭 시 그림자 축소 */
}
</style>
