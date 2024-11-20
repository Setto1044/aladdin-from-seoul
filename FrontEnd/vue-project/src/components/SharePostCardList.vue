<template>
  <div class="card-container">
    <SharePostCardWithAuthor
      v-for="(card, index) in cards"
      :key="index"
      :title="card.title"
      :description="card.description"
      :tags="card.tags"
    />
  </div>
</template>

<script>
import SharePostCardWithAuthor from './SharePostCardWithAuthor.vue'

export default {
  components: {
    SharePostCardWithAuthor,
  },
  props: {
    cards: {
      type: Array,
      required: true,
    },
  },
}
</script>

<style scoped>
.card-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 한 줄에 최대 3개의 열 */
  gap: 16px; /* 카드 간의 간격 */
  max-width: 1200px; /* 카드 컨테이너의 최대 너비 */
  margin: 0 auto; /* 좌우 여백을 균등하게 */
  padding: 0 16px; /* 페이지 좌우 여백 */
}

@media (max-width: 1024px) {
  .card-container {
    grid-template-columns: repeat(2, 1fr); /* 한 줄에 최대 2개 */
    max-width: 800px; /* 컨테이너의 최대 너비 */
  }
}

@media (max-width: 768px) {
  .card-container {
    grid-template-columns: 1fr; /* 한 줄에 1개 */
    max-width: 400px; /* 컨테이너의 최대 너비 */
  }
}
</style>
