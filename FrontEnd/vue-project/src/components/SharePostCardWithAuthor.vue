<template>
  <article class="card" @click="$emit('card-click', card)">
    <div class="card-image-container">
      <div
        class="card-image"
        :style="{
          backgroundImage: `url(${card.imageUrls[0] || 'https://via.placeholder.com/150'})`,
        }"
      ></div>
    </div>
    <div class="card-content">
      <h3 class="card-title">{{ card.title }}</h3>
      <p class="card-description">{{ card.address }}</p>
      <div class="card-tag">
        <span v-for="(tag, index) in visibleTags" :key="index" class="tag-label">{{ tag }}</span>
        <span
          v-if="hiddenTags.length > 0"
          class="more-tags"
          @mouseover="showTooltip = true"
          @mouseleave="showTooltip = false"
        >
          +{{ hiddenTags.length }}개
          <div v-if="showTooltip" class="tooltip">
            <span v-for="(tag, index) in hiddenTags" :key="index" class="tag-label">{{ tag }}</span>
          </div>
        </span>
      </div>
      <div class="card-author">
        <div class="author-avatar-container">
          <div
            class="author-avatar"
            :style="{
              backgroundImage: `url(${card.hostImageUrls || 'https://via.placeholder.com/150'})`,
            }"
          ></div>
        </div>
        <span class="author-name">{{ card.hostNickname }}</span>
        <span class="created-time">{{ formattedCreatedAt }}</span>
        <!-- Show the Edit button only if the user is the author -->
        <button v-if="isAuthor" class="edit-button" @click.stop="goToEditPage">수정</button>
      </div>
    </div>
  </article>
</template>

<script>
import useUserStore from '@/stores/user-store'

export default {
  props: {
    card: Object,
  },
  setup() {
    const userStore = useUserStore() // Access Pinia store

    return { userStore }
  },
  data() {
    return {
      showTooltip: false,
    }
  },
  computed: {
    visibleTags() {
      const maxVisibleTags = 3 // 보이는 해시태그의 최대 개수
      return this.card.tags.slice(0, maxVisibleTags)
    },
    hiddenTags() {
      const maxVisibleTags = 3 // 보이는 해시태그의 최대 개수
      return this.card.tags.length > maxVisibleTags ? this.card.tags.slice(maxVisibleTags) : []
    },
    formattedCreatedAt() {
      // 현재 시간 (UTC)
      const now = new Date()
      // 작성 시간 (UTC)
      const createdAt = new Date(this.card.createdAt)

      // 작성 시간과 현재 시간의 차이를 분 단위로 계산
      const diffInMinutes = Math.floor((now - createdAt) / 60000)
      console.log(createdAt, diffInMinutes, now)

      if (diffInMinutes < 1) {
        return '방금 전 작성'
      } else if (diffInMinutes < 60) {
        return `${diffInMinutes}분 전 작성`
      } else if (diffInMinutes < 1440) {
        const diffInHours = Math.floor(diffInMinutes / 60)
        return `${diffInHours}시간 전 작성`
      } else {
        const diffInDays = Math.floor(diffInMinutes / 1440)
        if (diffInDays <= 30) {
          return `${diffInDays}일 전 작성`
        } else {
          // 30일이 넘으면 "YYYY-MM-DD" 형식으로 반환
          const year = createdAt.getUTCFullYear()
          const month = String(createdAt.getUTCMonth() + 1).padStart(2, '0') // 월은 0부터 시작하므로 +1
          const day = String(createdAt.getUTCDate()).padStart(2, '0')
          return `${year}-${month}-${day}`
        }
      }
    },
    isAuthor() {
      // Compare the current user's ID with the card's hostId or membersUsername
      return this.card.hostId === this.userStore.memberInfo.userid
    },
  },
  methods: {
    handleCardClick() {
      this.$emit('card-click', this.$props) // 클릭된 카드의 데이터를 부모로 전달
    },
    goToEditPage() {
      // card.id를 기반으로 이동
      this.$router.push(`/share/edit/${this.card.id}`)
    },
  },
}
</script>

<style scoped>
.card {
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  cursor: pointer;
  background-color: #fff;
  flex: 1 1 calc(33% - 16px);
  max-width: 300px; /* 카드의 최대 너비 제한 */
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border-radius: 8px;
  height: 150px;
}

.card-image {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.card-container {
  display: grid;
  grid-template-columns: repeat(
    auto-fit,
    minmax(300px, 1fr)
  ); /* 카드의 최소 크기를 설정하고 자동으로 맞춤 */
  gap: 16px; /* 카드들 사이의 간격 */
  width: 100%; /* 컨테이너가 전체 너비를 차지하도록 설정 */
  justify-content: space-between; /* 카드들 사이의 간격을 균등하게 */
  padding: 0 16px; /* 좌우 여백 */
}

.card-title,
.author-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block; /* 블록 요소로 설정 */
}

.card-description {
  display: -webkit-box; /* 여러 줄로 자르기 위해 설정 */
  -webkit-line-clamp: 2; /* 최대 2줄까지만 표시 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-tags {
  display: flex;
  align-items: center;
  gap: 8px; /* 태그 간의 가로 간격 */
  row-gap: 4px; /* 태그 줄 간의 세로 간격 */
  font-size: 12px;
  margin-bottom: 8px;
  flex-wrap: wrap; /* 여러 줄로 나열 */
  position: relative;
}

.tag-label {
  background-color: #f1f1f1;
  padding: 4px 8px;
  border-radius: 12px;
  color: #555;
  border: 1px solid #ccc;
  margin: 4px; /* 태그 간의 여백 */
}

.more-tags {
  cursor: pointer;
  color: #007bff;
  position: relative;
}

.tooltip {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  background-color: #fff;
  border: 1px solid #ddd;
  padding: 8px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
  white-space: nowrap;
}

.more-tags:hover .tooltip {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.card-author {
  display: flex;
  align-items: center;
  gap: 8px; /* 아이템 간의 간격 */
  font-size: 14px;
  color: #333;
  margin-top: 12px; /* 다른 요소와의 간격 */
}

.author-name {
  font-size: 14px;
  font-weight: bold;
  white-space: nowrap; /* 한 줄로 표시 */
  overflow: hidden; /* 넘치는 부분 숨기기 */
  text-overflow: ellipsis; /* 넘치는 부분에 ... 표시 */
  display: block; /* 블록 요소로 설정 */
}

.author-avatar-container {
  border-radius: 50%; /* 둥글게 */
  width: 25px; /* 크기 조정 */
  height: 25px; /* 크기 조정 */
  overflow: hidden; /* 컨테이너를 넘어가는 이미지 숨김 */
  display: flex; /* 중앙 정렬 */
  justify-content: center; /* 가로 중앙 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  background-color: #bbb; /* 기본 배경색 */
}

.author-avatar {
  width: 100%;
  height: 100%;
  background-size: cover; /* 이미지가 컨테이너를 채움 */
  background-position: center; /* 중앙 정렬 */
}

.created-time {
  margin-right: 8px;
  color: #999;
}
.edit-button {
  padding: 4px 8px;
  border: none;
  background-color: #007bff;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
}
.edit-button:hover {
  background-color: #0056b3;
}
</style>
