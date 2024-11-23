<template>
  <div class="room-share-page">
    <div class="page-header">
      <h2 class="main-title">{{ pageHeaderTitle }}</h2>
      <div v-if="showComponents">
        <div class="button-group">
          <button class="action-button" @click="goToMyPosts">내 글 보기</button>
          <button class="action-button create-button" @click="createNewPost">작성하기</button>
        </div>
        <form class="search-form" @submit.prevent="handleSearch">
          <input
            type="text"
            id="propertySearch"
            class="search-input"
            placeholder="Search for properties..."
            aria-label="Search for properties"
          />
          <button type="submit" class="search-button">Search</button>
        </form>
      </div>
    </div>

    <main class="main-content">
      <div class="post-list">
        <CardRow :cards="cardData" @card-click="handleCardClick" />
        <div v-if="isLoading" class="loading-spinner">Loading...</div>
        <div v-if="!hasMoreData" class="no-more-data">더 이상 글이 없습니다.</div>
      </div>

      <PropertyDetailModal
        v-if="selectedCard"
        :show="showModal"
        @close="closeModal"
        v-bind="selectedCard"
      />
    </main>
  </div>
</template>

<script>
import CardRow from '@/components/RoomShare/SharePostCardList.vue'
import PropertyDetailModal from '@/components/RoomShare/SharePostDetail.vue'
import axios from 'axios'

export default {
  components: {
    CardRow,
    PropertyDetailModal,
  },
  data() {
    return {
      cardData: [], // Holds the fetched card data
      showModal: false,
      selectedCard: null,
      cursorId: null, // For pagination: Tracks the last loaded item's ID
      pageSize: 9, // Number of items per page
      isLoading: false, // Tracks loading state
      hasMoreData: true, // Determines if more data can be loaded
    }
  },
  mounted() {
    this.fetchData() // Fetch initial data when the component is mounted
  },
  computed: {
    currentRouteName() {
      return this.$route.name // 현재 라우트 이름
    },
    pageHeaderTitle() {
      // 라우트 이름에 따라 헤더 제목 설정
      if (this.currentRouteName === 'share-user' && this.$route.params.id) {
        return `${this.$route.params.id} 님의 글보기`
      }
      return '모든 글 보기'
    },
    showComponents() {
      // 특정 라우트에서만 컴포넌트 표시
      return this.currentRouteName === 'share'
    },
  },

  methods: {
    async fetchData() {
      if (this.isLoading || !this.hasMoreData) return

      this.isLoading = true

      const params = {
        pageSize: this.pageSize,
      }
      if (this.cursorId) {
        params.cursorId = this.cursorId
      }

      try {
        const response = await axios.get('http://localhost:8080/aladin/boards', { params })

        if (response.data.success && response.data.data.length > 0) {
          const fetchedData = response.data.data.map((item) => ({
            id: item.roomBoardVo.id,
            title: item.roomBoardVo.title,
            description: item.roomBoardVo.detail,
            address: item.roomBoardVo.address,
            price: item.roomBoardVo.price,
            houseSize: item.roomBoardVo.houseSize,
            pricePer: item.roomBoardVo.pricePer,
            rentFrom: item.roomBoardVo.rentStart,
            rentTo: item.roomBoardVo.rentUntil,
            tags: JSON.parse(item.roomBoardVo.hashtags),
            views: item.roomBoardVo.views,
            hostId: item.roomBoardVo.membersUsername,
            createdAt: item.roomBoardVo.createdAt,
            imageUrls: [item.thumbnailUrl],
            hostNickname: item.nickname,
            hostImageUrls: item.profileImagePath,
          }))

          this.cardData.push(...fetchedData)

          // Update cursorId to the last loaded item's ID
          this.cursorId = fetchedData[fetchedData.length - 1].id
        } else {
          this.hasMoreData = false // No more data to load
        }
      } catch (error) {
        console.error('Error fetching data:', error)
      } finally {
        this.isLoading = false
      }
    },
    loadMoreData() {
      if (!this.isLoading && this.hasMoreData) {
        this.fetchData()
      }
    },
    handleScroll() {
      const { scrollTop, scrollHeight, clientHeight } = document.documentElement

      if (scrollTop + clientHeight >= scrollHeight - 50) {
        this.loadMoreData()
      }
    },
    handleCardClick(card) {
      this.selectedCard = card
      this.showModal = true

      // 현재 스크롤 위치 저장
      this.scrollY = window.scrollY

      // 스크롤 고정 및 위치 고정
      document.body.style.position = 'fixed'
      document.body.style.top = `-${this.scrollY}px`

      console.log(this.selectedCard)
    },
    closeModal() {
      this.showModal = false
      this.selectedCard = null

      // 스크롤 복원
      document.body.style.position = ''
      document.body.style.top = ''
      window.scrollTo(0, this.scrollY) // 원래 위치로 복원
    },
    goToMyPosts() {
      // 내 글 보기 기능 로직
      this.$router.push({ path: 'share/user/1' })
    },
    createNewPost() {
      // 글 작성 기능 로직
      this.$router.push({ name: 'share-write' })
    },
  },
  created() {
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll)
  },
}
</script>

<style scoped>
.room-share-page {
  padding: 20px;
  background-color: #f9f9f9; /* 페이지 배경을 약간 밝게 변경 */
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.page-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.main-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.button-group {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.action-button {
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  background-color: #007bff;
  color: #fff;
  transition:
    background-color 0.3s,
    transform 0.2s;
}

.action-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.create-button {
  background-color: #28a745;
}

.create-button:hover {
  background-color: #218838;
}

.search-form {
  display: flex;
  align-items: center;
  margin-top: 10px;
  width: 100%;
  max-width: 600px;
}

.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px 0 0 8px;
  font-size: 14px;
}

.search-button {
  padding: 10px 20px;
  border: none;
  border-radius: 0 8px 8px 0;
  background-color: #007bff;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0056b3;
}

.post-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.post-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.loading-spinner {
  text-align: center;
  margin: 20px 0;
  font-size: 16px;
  color: #888;
}

.no-more-data {
  text-align: center;
  margin: 20px 0;
  font-size: 16px;
  color: #888;
}
</style>
