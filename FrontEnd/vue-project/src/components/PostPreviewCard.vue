<template>
  <article class="card" @click="$emit('card-click', card)">
    <div class="card-image-container">
      <div class="card-image"></div>
    </div>
    <div class="card-content">
      <h3 class="card-title">{{ card.title }}</h3>
      <div class="card-description-author">
        <p class="card-description">{{ card.description }}</p>
        <div class="author-avatar"></div>
      </div>
    </div>
  </article>
</template>

<script>
export default {
  props: {
    card: Object,
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
  max-width: 180px; /* 카드의 최대 너비 제한 */
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
  object-fit: cover;
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

.card-description-author {
  display: flex; /* Flexbox를 사용하여 가로 정렬 */
  align-items: center; /* 세로축 중앙 정렬 */
  justify-content: space-between; /* 왼쪽과 오른쪽에 요소를 배치 */
}

.card-description {
  margin: 0;
  font-size: 14px;
  color: #555;
  flex: 1; /* 남은 공간을 차지하여 description이 더 넓게 표시 */
}

.author-avatar {
  width: 30px;
  height: 30px;
  background-color: #ccc;
  border-radius: 50%; /* 둥근 형태로 변경 */
  flex-shrink: 0; /* 크기 축소 방지 */
  margin-left: 10px; /* description과의 간격 조정 */
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
