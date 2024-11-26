<template>
  <article class="card" @click="$emit('card-click', card)">
    <div class="card-image-container">
      <div
        class="card-image"
        :style="{
          backgroundImage: `url(${card.image1 || './basic/basic1.jpg'})`,
        }"
      ></div>
    </div>
    <div class="card-content">
      <h3 class="card-title">{{ card.title }}</h3>
      <div class="card-description-author">
        <p class="card-description">{{ card.address }}</p>
        <div
          class="author-avatar"
          :style="{
            backgroundImage: `url(${card.image2 || './basic/basic2.jpg'})`,
          }"
        ></div>
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
  width: 200px; /* 카드의 고정 너비 */
  height: 300px; /* 카드의 고정 높이 */
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 상하 균형 맞추기 */
  padding: 16px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  cursor: pointer;
  background-color: #fff;
  overflow: hidden; /* 내용이 넘치지 않도록 설정 */
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-image-container {
  width: 100%;
  height: 50%; /* 카드 높이의 절반을 이미지에 할당 */
  overflow: hidden;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지 비율을 유지하면서 카드 크기에 맞게 조정 */
}

.card-content {
  padding: 8px 0 0 0; /* 상단 여백 최소화 */
  text-align: center; /* 텍스트 가운데 정렬 */
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-description {
  font-size: 14px;
  color: #555;
  white-space: normal; /* 줄바꿈 허용 */
  overflow: visible; /* 텍스트가 잘리지 않도록 설정 */
  text-overflow: unset; /* 말줄임표 제거 */
}

.card-description-author {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 4px; /* 간격 조정 */
}

.author-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #ccc;
  background-size: cover;
  background-position: center;
}

.card-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); /* 고정 너비에 따라 카드 배치 */
  gap: 16px;
  justify-content: center; /* 카드들을 가운데 정렬 */
  padding: 16px;
}

.card-title,
.author-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block; /* 블록 요소로 설정 */
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
</style>
