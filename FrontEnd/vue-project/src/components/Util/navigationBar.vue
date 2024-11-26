<template>
  <header :class="['top-bar', { hidden: isHidden }]">
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
      <button v-else class="nav-link" @click="redirectToLogin">시작하기 / 로그인</button>
    </nav>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
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

// 상태 관리: 네비게이션 바 가시성
const isHidden = ref(false)
const throttleTimeout = ref(null) // 스크롤 이벤트를 조절하기 위한 타이머
let lastScrollY = 0

// 스크롤 핸들러
const handleScroll = () => {
  // Throttle 처리 (성능 최적화)
  if (throttleTimeout.value) return
  throttleTimeout.value = setTimeout(() => {
    const currentScrollY = window.scrollY

    // 스크롤 아래로: 숨기기
    if (currentScrollY > lastScrollY && currentScrollY > 50) {
      isHidden.value = true
    }
    // 스크롤 위로: 다시 보이기
    else if (currentScrollY < lastScrollY) {
      isHidden.value = false
    }
    lastScrollY = currentScrollY
    throttleTimeout.value = null
  }, 50) // 50ms 간격으로만 실행
}

// Vue lifecycle: 컴포넌트 마운트 시 스크롤 이벤트 추가
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})
// Vue lifecycle: 컴포넌트 언마운트 시 스크롤 이벤트 제거
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.top-bar {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #ffffff;
  padding: 10px 20px;
  height: 60px;
  border-bottom: 1px solid #f0f0f059;
  position: sticky;
  top: 0;
  z-index: 1000;
  transition:
    transform 0.4s ease,
    opacity 0.4s ease; /* 부드러운 전환 효과 */
}

.top-bar.hidden {
  transform: translateY(-100%);
  opacity: 0;
  pointer-events: none;
}

.top-bar .logo-container {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo {
  height: 40px;
  margin-right: 8px;
}

.main-navigation {
  display: flex;
  align-items: center;
}

.nav-link {
  text-decoration: none;
  font-size: 0.9rem; /* 적당한 크기 조정 */
  font-weight: 500;
  color: #444444;
  padding: 8px 16px;
  transition: all 0.3s ease-in-out;
  cursor: pointer;
  border: 1px solid transparent; /* 기본적으로 테두리 투명 */
  background-color: transparent; /* 배경 투명 */
}

.nav-link:hover {
  background-color: #b399d4; /* 포인터 올렸을 때 색상 변경 */
  color: #ffffff;
  transform: translateY(-2px); /* 약간 위로 올라가는 효과 */
}

.nav-link:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 클릭 시 그림자 조정 */
}

@media (max-width: 768px) {
  .top-bar {
    padding: 8px 16px; /* 작은 화면에서 간격 조정 */
  }

  .nav-link {
    font-size: 0.8rem; /* 작은 화면에서 폰트 크기 줄임 */
    padding: 6px 12px; /* 간격 축소 */
  }
}
</style>
