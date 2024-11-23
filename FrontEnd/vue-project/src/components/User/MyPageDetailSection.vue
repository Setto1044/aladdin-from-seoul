<template>
  <div class="interests-content">
    <h2 class="section-title">{{ title }}</h2>
    <p class="section-description">{{ description }}</p>
    <ul class="interests-list">
      <li class="interest-item" @click="goToPage">
        <span class="interest-icon">❤️</span>
        <h3 class="interest-title">더보기</h3>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  setup() {
    return {}
  },
  props: ['title', 'description', 'url'],
  methods: {
    goToPage() {
      this.$router.push({ path: this.url })
    },
  },
}
</script>

<style scoped>
.interests-content {
  display: flex; /* Flexbox 활성화 */
  flex-direction: column; /* 세로 정렬 */
  align-items: center; /* 가로축 중앙 정렬 */
  justify-content: center; /* 세로축 중앙 정렬 */
  text-align: center; /* 텍스트 가로 정렬 */
  padding: 20px; /* 내부 여백 */
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.section-description {
  font-size: 16px;
  color: #555;
  margin-bottom: 16px;
}

.interests-list {
  list-style: none; /* 기본 리스트 스타일 제거 */
  padding: 0;
  margin: 0;
  display: flex; /* Flexbox로 리스트 아이템 정렬 */
  justify-content: center; /* 아이템을 중앙 정렬 */
  gap: 16px; /* 아이템 간 간격 */
}

.interest-item {
  display: flex; /* Flexbox로 내부 아이템 정렬 */
  flex-direction: column; /* 아이콘과 제목을 세로 정렬 */
  align-items: center; /* 가로축 중앙 정렬 */
  justify-content: center; /* 세로축 중앙 정렬 */
  text-align: center; /* 텍스트 정렬 */
  padding: 16px; /* 내부 여백 */
  border: 1px solid #ddd; /* 외곽선 */
  border-radius: 8px; /* 둥근 테두리 */
  background-color: #f9f9f9; /* 배경색 */
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

.interest-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 호버 시 그림자 효과 */
}

.interest-icon {
  font-size: 24px;
  color: red;
  margin-bottom: 8px; /* 아이콘과 제목 사이 간격 */
}

.interest-title {
  font-size: 18px;
  color: #333;
}
</style>
