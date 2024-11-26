<template>
  <main>
    <div class="land-content" id="fullpage">
      <section class="land-section section">
        <Background />
        <div class="title-container">
          <div class="title-text">서울 청년 내집 마련 요술램프</div>
          <!-- 타이틀 이미지 -->
          <div class="title-image"></div>
        </div>

        <section class="search-section">
          <SearchAutocomplete />
        </section>
      </section>
      <section class="akinator-section section">
        <Akinator></Akinator>
      </section>
    </div>
  </main>
</template>

<script>
import { onMounted, onUnmounted } from 'vue'
import SearchAutocomplete from '@/components/Util/SearchAutocomplete.vue'
import Akinator from '@/components/Akinator/GamePlay.vue'
import Background from '@/components/Akinator/Background.vue'
import FullPage from 'fullpage.js'
import 'fullpage.js/dist/fullpage.css'

export default {
  name: 'AladinFromSeoul',
  components: {
    Akinator,
    SearchAutocomplete,
    Background,
  },
  setup() {
    let fullpageInstance

    onMounted(() => {
      fullpageInstance = new FullPage('#fullpage', {
        autoScrolling: true,
        scrollHorizontally: true,
      })
    })

    onUnmounted(() => {
      if (fullpageInstance) {
        fullpageInstance.destroy('all')
      }
    })
  },
  mounted() {
    this.updateNavHeight()
  },
  methods: {
    handleSearch() {
      // Implement search functionality
    },
    handleSubscribe() {
      // Implement subscription functionality
    },
    updateNavHeight() {
      const navBar = document.querySelector('.top-bar') // Replace with your nav bar selector
      if (navBar) {
        const navHeight = navBar.offsetHeight
        document.documentElement.style.setProperty('--nav-height', `${navHeight}px`)
      }
    },
  },
}
</script>

<style scoped>
/* 전체 섹션 */
.land-section {
  height: calc(100vh - var(--nav-height)); /* 상단 네비게이션 높이를 제외한 전체 높이 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  position: relative;
  padding: 20px;
}

/* 제목 컨테이너 */
.title-container {
  text-align: center;
  margin-bottom: 30px;
}

.title-text {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.title-image {
  width: 500px;
  height: 300px;
  background-image: url('@/assets/title.svg');
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}

/* 검색 섹션 */
.search-section {
  width: 100%; /* 검색 섹션의 너비를 전체로 설정 */
  max-width: 600px; /* 검색 섹션 최대 너비 제한 */
  position: relative; /* 검색 결과 리스트의 위치 기준 */
  padding: 20px;
  margin-top: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
