<template>
  <div class="profile-page">
    <section class="user-info-section">
      <div class="avatar">
        <img :src="profileImagePath || defaultAvatar" alt="User Avatar" class="avatar-image" />
      </div>
      <div class="user-details">
        <h2 class="user-name">{{ nickname || 'default nickname' }}</h2>
        <span class="user-type">{{ grade || 'default grade' }}</span>
        <p class="welcome-message">{{ bio || `(🖐🏻'-' )` }}</p>
      </div>
      <div class="user-actions">
        <button class="edit-button" @click="goToEditPage">프로필 수정</button>
        <button class="edit-button" @click="goToPWEditPage">비밀번호 변경</button>
      </div>
    </section>

    <section class="user-posts-section">
      <!-- 왼쪽: 글 정보 -->
      <div class="posts-title-container">
        <MyPageDetailComponent
          title="내가 올린 글"
          description="내가 올린 글을 봅니다"
          url="/listings/2"
        ></MyPageDetailComponent>
      </div>

      <!-- 오른쪽: 카드 -->
      <div class="posts-image-container">
        <section class="popular-properties">
          <div class="property-grid">
            <CardComponent
              v-for="board in boardStats"
              :key="board.roomBoardVo.id"
              :card="{
                id: board.roomBoardVo.id,
                title: board.roomBoardVo.title,
                address: board.roomBoardVo.address,
                image1: board.thumbnailUrl ? board.thumbnailUrl : null,
                image2: board.profileImagePath ? board.profileImagePath : null,
              }"
            />
          </div>
        </section>
      </div>
    </section>

    <section class="user-interests-section">
      <!-- 왼쪽: 카드 -->
      <div class="interests-image-container">
        <section class="popular-properties">
          <div class="property-grid">
            <CardComponent
              v-for="bookmark in bookmarks"
              :key="bookmark.roomBoardVo.id"
              :card="{
                id: bookmark.roomBoardVo.id,
                title: bookmark.roomBoardVo.title,
                address: bookmark.roomBoardVo.address,
                image1: null,
                image2: null,
              }"
            />
          </div>
        </section>
      </div>

      <!-- 오른쪽: 관심 목록 -->
      <div class="posts-title-container">
        <MyPageDetailComponent
          title="관심목록"
          description="내가 찜한 관심목록을 봅니다"
          url="/favorites"
        ></MyPageDetailComponent>
      </div>
    </section>
  </div>
</template>

<script>
import HouseCardComponent from '@/components/RoomShare/HouseCardComponent.vue'
import HouseUserCardComponent from '@/components/RoomShare/HouseUserCardComponent.vue'
import MyPageDetailComponent from '@/components/User/MyPageDetailSection.vue'
import CardComponent from '@/components/RoomShare/PostPreviewCard.vue'
import useUserStore from '@/stores/user-store'
import { computed, ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  components: {
    HouseCardComponent,
    HouseUserCardComponent,
    MyPageDetailComponent,
    CardComponent,
  },
  setup() {
    const userStore = useUserStore()
    const nickname = computed(() => userStore.memberInfo.nickname)
    const bio = computed(() => userStore.memberInfo.bio)
    const profileImagePath = computed(() => userStore.memberInfo.profileImagePath)
    const grade = computed(() => userStore.memberInfo.grade)
    const username = computed(() => userStore.memberInfo.username)

    const handleLogout = () => {
      userStore.logout()
      alert('Logged out successfully!')
      window.location.href = '/login'
    }

    // Reactive state for API responses
    const boardStats = ref([])
    const bookmarks = ref([])
    const isLoading = ref(true)

    // Fetch boards data
    const fetchBoards = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/aladin/boards/my/${username.value}`)
        if (response.data.success) {
          boardStats.value = response.data.data
        }
      } catch (error) {
        console.error('Failed to fetch board stats:', error)
      } finally {
        isLoading.value = false
      }
    }

    // Fetch bookmark data
    const fetchBookmarks = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/bookmark/board/${username.value}?pageSize=3`,
        )
        if (response.data.success) {
          bookmarks.value = response.data.data
        }
        console.log('fetchBookmarks', response.data)
      } catch (error) {
        console.error('Failed to fetch bookmarks:', error)
      }
    }

    // Fetch both data sources concurrently
    const fetchData = async () => {
      isLoading.value = true
      await Promise.all([fetchBoards(), fetchBookmarks()])
      isLoading.value = false
    }

    // Fetch data on component mount
    onMounted(() => {
      fetchData()
    })

    return {
      nickname,
      bio,
      profileImagePath,
      grade,
      handleLogout,
      boardStats,
      bookmarks,
      isLoading,
    }
  },
  methods: {
    goToEditPage() {
      this.$router.push({ path: '/mypage/edit' })
    },
    goToPWEditPage() {
      const un = useUserStore().memberInfo.username
      this.$router.push({ name: 'chpw', params: { username: un } })
    },
  },
  data() {
    return {
      defaultAvatar: './basic/basic1.jpg',
      fakeCardData: {
        id: 4,
        title: 'Card 4',
        description: 'Luxury villa with garden.',
        detail: 'Beautiful villa perfect for family living.',
        address: '321 Oak Rd, Suburb',
        price: 5000,
        houseSize: 150.0,
        pricePer: 'month',
        latitude: '37.7949',
        longitude: '-122.4594',
        rentFrom: '2024-05-01',
        rentTo: '2024-12-31',
        createdAt: '2024-11-10T10:00:00Z',
        tags: ['#Garden', '#Pool'],
        views: 300,
        hostId: 126,
        imageUrls: ['https://example.com/image5.jpg', 'https://example.com/image6.jpg'],
        comments: ['Amazing garden!', 'Spacious and luxurious.'],
      },
    }
  },
}
</script>

<style scoped>
.profile-page {
  display: flex;
  flex-direction: column;
  gap: 32px; /* 섹션 간격 */
  padding: 16px;
}

/* section {
  position: relative;
  padding: 24px;
  background-color: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
} */

.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.1;
  z-index: -1; /* 배경 뒤로 */
  border-radius: 12px;
}

.user-info-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 16px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 16px;
}
.avatar-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  object-fit: cover;
  margin-bottom: 20px;
}

.user-details h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.user-details {
  font-size: 14px;
  color: #555;
  margin: 8px 0;
}

.user-type {
  font-size: 14px;
  color: #555;
  margin: 8px 0;
  background-color: #ddd;
  padding: 2px;
  border-radius: 3%;
}

.welcome-message {
  font-size: 16px;
  color: #666;
  margin: 0;
}

.user-actions {
  display: flex;
  gap: 16px;
  margin-top: 16px;
}

.edit-button {
  margin-left: auto; /* 버튼을 오른쪽 끝으로 밀기 */
  padding: 6px 12px; /* 여유로운 내부 여백 */
  border: 1px solid transparent; /* 기본적으로 테두리 투명 */
  background-color: transparent; /* 배경 투명 */
  color: #2054d2; /* 버튼 텍스트 색상 */
  border-radius: 4px; /* 둥근 모서리 */
  cursor: pointer; /* 클릭 가능 표시 */
  font-size: 14px; /* 적당한 폰트 크기 */
  transition: all 0.3s ease; /* 부드러운 전환 효과 */
}

.edit-button:hover {
  background-color: #f0f8ff; /* 살짝 밝은 배경 */
  color: #0056b3; /* 텍스트 색상 어둡게 */
}

.edit-button:active {
  background-color: #e0f0ff; /* 누를 때 더 밝은 배경 */
  border-color: #2054d2; /* 테두리 색상 어둡게 */
  color: #003d7a; /* 텍스트 색상 더 어둡게 */
}

.property-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 한 줄에 2개 */
  gap: 16px;
}

.popular-properties {
  padding: 16px 0;
}

.user-posts-section,
.user-interests-section {
  display: flex; /* Flexbox로 가로 정렬 */
  justify-content: center; /* 전체 영역을 중앙에 정렬 */
  align-items: stretch; /* 자식 요소의 높이를 동일하게 */
  gap: 0; /* 두 영역 사이의 간격 제거 */
}

.interests-image-container,
.posts-image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
}

.posts-title-container {
  display: flex; /* 내부 내용을 중앙 정렬 */
  flex-direction: column; /* 내부 콘텐츠를 세로 정렬 */
  justify-content: center; /* 세로축 중앙 정렬 */
  align-items: center; /* 가로축 중앙 정렬 */
  padding: 16px; /* 내부 여백 */
  border-radius: 8px; /* 둥근 테두리 */
}

.interests-image-container {
  display: flex; /* 내부 내용을 중앙 정렬 */
  flex-direction: column; /* 내부 콘텐츠를 세로 정렬 */
  justify-content: center; /* 세로축 중앙 정렬 */
  align-items: center; /* 가로축 중앙 정렬 */
  padding: 16px; /* 내부 여백 */
  border-radius: 8px; /* 둥근 테두리 */
}

.posts-image-container {
  display: flex; /* 내부 내용을 중앙 정렬 */
  flex-direction: column; /* 내부 콘텐츠를 세로 정렬 */
  justify-content: center; /* 세로축 중앙 정렬 */
  align-items: center; /* 가로축 중앙 정렬 */
  padding: 16px; /* 내부 여백 */
  border-radius: 8px; /* 둥근 테두리 */
}

.property-grid {
  display: flex; /* Flexbox 사용 */
  justify-content: center; /* 카드가 중앙에 위치 */
  flex-wrap: wrap; /* 여러 줄 배치 가능 */
  gap: 8px; /* 카드 간격 */
}

.CardComponent {
  margin: 0; /* 카드 간 여백 제거 */
}

.MyPageDetailComponent {
  flex: 1; /* 남은 공간 균등 분배 */
}
</style>
