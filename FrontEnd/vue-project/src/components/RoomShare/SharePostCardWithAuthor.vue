<template>
  <article class="card" @click="$emit('card-click', card)">
    <div class="card-image-container">
      <div
        class="card-image"
        :style="{
          backgroundImage: `url(${card.imageUrls[0] || './basic/basic1.jpg'})`,
        }"
      ></div>
    </div>
    <div class="card-content">
      <h3 class="card-title">{{ card.title }}</h3>
      <p class="card-description">{{ card.address }}</p>
      <div class="card-tag">
        <span v-for="(tag, index) in visibleTags" :key="index" class="tag-label">#{{ tag }}</span>
        <span
          v-if="hiddenTags.length > 0"
          class="more-tags"
          @mouseover="showTooltip = true"
          @mouseleave="showTooltip = false"
        >
          +{{ hiddenTags.length }}개
          <div v-if="showTooltip" class="tooltip">
            <span v-for="(tag, index) in hiddenTags" :key="index" class="tag-label"
              >#{{ tag }}</span
            >
          </div> </span
        >⠀
      </div>
      <div class="card-author">
        <div class="author-avatar-container">
          <div
            class="author-avatar"
            :style="{
              backgroundImage: `url(${card.hostImageUrls || './basic/basic2.jpg'})`,
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
      const maxTagLength = 10 // 보이는 태그의 최대 총 문자열 길이
      const maxVisibleTags = 3 // 최대 표시할 태그 수
      const tags = []
      let currentLength = 0

      for (let tag of this.card.tags) {
        if (tags.length >= maxVisibleTags) break // 최대 3개까지만 추가
        if (currentLength + tag.length > maxTagLength) break // 최대 문자열 길이 초과 시 중단
        tags.push(tag)
        currentLength += tag.length
      }

      return tags
    },
    hiddenTags() {
      const visible = this.visibleTags // visibleTags에서 이미 계산된 태그 가져오기
      return this.card.tags.slice(visible.length) // 나머지 태그 반환
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
  width: 300px;
  height: 360px;
  gap: 22px;
  padding: 16px;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  cursor: pointer;
  background-color: #fff;
  flex: 1 1 calc(33% - 16px);
  max-width: 300px; /* 카드의 최대 너비 제한 */
  margin: auto;
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

.card-title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block; /* 블록 요소로 설정 */
  font-size: 14px;
  margin-top: 0px;
  color: #555;
}

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
  font-size: 18px;
}

.card-tags {
  display: flex;
  align-items: center;
  gap: 8px; /* 태그 간의 가로 간격 */
  row-gap: 4px; /* 태그 줄 간의 세로 간격 */
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
  font-size: 12px;
}

.more-tags {
  cursor: pointer;
  color: #2054d2;
  position: relative;
  font-size: 12px;
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
</style>
