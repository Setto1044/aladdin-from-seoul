<template>
  <article class="card" @click="goToHouseDetail">
    <div class="card-image-container">
      <div class="card-image"></div>
    </div>
    <div class="card-content">
      <h3 class="card-title">{{ title }}</h3>
      <p class="card-description">{{ description }}</p>
      <div class="card-tag">
        <template v-for="(tag, index) in visibleTags" :key="index">
          <span class="tag-label">{{ tag }}</span>
        </template>
        <span
          v-if="hiddenTags.length > 0"
          class="more-tags"
          @mouseover="showTooltip = true"
          @mouseleave="showTooltip = false"
        >
          +{{ hiddenTags.length }}개
          <div v-if="showTooltip" class="tooltip">
            <span v-for="(tag, index) in hiddenTags" :key="`hidden-${index}`" class="tag-label">{{
              tag
            }}</span>
          </div>
        </span>
      </div>
      <div class="card-author">
        <div class="author-avatar"></div>
        <span class="author-name">NatureEnthusiast</span>
      </div>
    </div>
  </article>
</template>

<script>
export default {
  props: {
    title: String,
    description: String,
    tags: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      showTooltip: false,
    }
  },
  computed: {
    visibleTags() {
      const maxVisibleTags = 3 // 보이는 해시태그의 최대 개수
      return this.tags.slice(0, maxVisibleTags)
    },
    hiddenTags() {
      const maxVisibleTags = 3 // 보이는 해시태그의 최대 개수
      return this.tags.length > maxVisibleTags ? this.tags.slice(maxVisibleTags) : []
    },
  },
  methods: {
    goToHouseDetail() {
      this.$router.push({ name: 'house', params: { id: '2' } })
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
  object-fit: cover;
}

.card-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  justify-content: center;
  padding: 0 16px; /* 페이지 좌우 여백 */
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

.author-avatar {
  border-radius: 50%;
  background-color: #bbb;
  width: 32px;
  height: 32px;
  object-fit: cover;
}
</style>
