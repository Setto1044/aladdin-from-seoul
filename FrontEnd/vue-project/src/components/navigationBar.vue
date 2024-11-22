<template>
  <header class="top-bar">
    <a href="/" class="logolink nav-link">
      <div class="logo"></div>
      <h1 class="site-title">알라딘 프롬 서울</h1>
    </a>

    <nav class="main-navigation">
      <form v-if="$route.meta.showSearchBar" class="header-search-form nav-link">
        <input
          type="text"
          id="search-input"
          class="header-search-input"
          placeholder="Search in site"
          aria-label="Search in site"
        />
        <button type="submit" class="header-search-button" aria-label="Submit search">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/ff75430b99a5dc1ef509cf04ee77ca85091d2f5a3a9e255ba3ad5b4f13bcf5f9?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
            class="header-search-icon"
            alt="Search"
          />
        </button>
      </form>

      <button class="nav-button" @click="navigateTo('/map')">지도</button>
      <button class="nav-button" @click="navigateTo('/share')">방 나누기 게시판</button>
      <button class="nav-button" @click="navigateTo('/favorites')">내 관심목록</button>
      <!-- 로그인 상태에 따라 다른 버튼 활성화 -->
      <button v-if="isLoggedIn" class="nav-button" @click="navigateTo('/mypage')">
        마이페이지
      </button>
      <button v-if="isLoggedIn" class="nav-button" @click="handleLogout">로그아웃</button>
      <button v-else class="nav-button" @click="redirectToLogin">로그인</button>
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

<style scoped></style>
