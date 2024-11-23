<template>
  <div class="profile-page">
    <section class="user-info-section">
      <div class="avatar">
        <img :src="profileImagePath || defaultAvatar" alt="User Avatar" class="avatar-image" />
      </div>
      <div class="user-details">
        <h2 class="user-name">{{ nickname || 'default nickname' }}</h2>
        <span class="user-type">{{ grade || 'default grade' }}</span>
        <p class="welcome-message">{{ bio || 'default bio' }}</p>
      </div>
      <div class="user-actions">
        <button class="btn-secondary">Log Out</button>
        <button class="btn-primary" @click="goToEditPage">Edit Profile</button>
      </div>
      <img
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
        alt=""
        class="background-image"
      />
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
            <CardComponent :card="fakeCardData"></CardComponent>
            <CardComponent :card="fakeCardData"></CardComponent>
            <CardComponent :card="fakeCardData"></CardComponent>
          </div>
        </section>
      </div>
    </section>

    <section class="user-interests-section">
      <!-- 왼쪽: 카드 -->
      <div class="interests-image-container">
        <section class="popular-properties">
          <div class="property-grid">
            <CardComponent :card="fakeCardData"></CardComponent>
            <CardComponent :card="fakeCardData"></CardComponent>
            <CardComponent :card="fakeCardData"></CardComponent>
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
import { computed } from 'vue'

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

    const handleLogout = () => {
      userStore.logout()
      alert('Logged out successfully!')
      window.location.href = '/login'
    }

    return { nickname, bio, profileImagePath, grade, handleLogout }
  },
  methods: {
    goToEditPage() {
      this.$router.push({ path: '/mypage/edit' })
    },
  },
  data() {
    return {
      defaultAvatar:
        'https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg',
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
  background-color: #ddd;
  margin-bottom: 16px;
}
.avatar-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 20px;
}

.user-details h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.user-details .user-type {
  font-size: 14px;
  color: #555;
  margin: 8px 0;
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

.btn-primary {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #ddd;
  color: #333;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-secondary:hover {
  background-color: #bbb;
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

.MyPageDetailComponent {
  display: flex; /* Flexbox 활성화 */
  flex-direction: column; /* 텍스트를 세로로 쌓기 */
  justify-content: center; /* 세로축 중앙 정렬 */
  align-items: center; /* 가로축 중앙 정렬 */
  height: 100%; /* 부모의 높이를 꽉 채움 */
  text-align: center; /* 텍스트 가운데 정렬 */
  padding: 16px; /* 여백 추가 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 테두리 그림자 */
  border-radius: 8px; /* 둥근 테두리 */
  background-color: #f9f9f9; /* 배경색 (선택 사항) */
}

.posts-title-container,
.posts-image-container,
.interests-image-container {
  display: flex; /* 내부 내용을 중앙 정렬 */
  flex-direction: column; /* 내부 콘텐츠를 세로 정렬 */
  justify-content: center; /* 세로축 중앙 정렬 */
  align-items: center; /* 가로축 중앙 정렬 */
  background-color: #f9f9f9; /* 배경색 추가 (선택 사항) */
  padding: 16px; /* 내부 여백 */
  border: 1px solid #ddd; /* 테두리 추가 */
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
