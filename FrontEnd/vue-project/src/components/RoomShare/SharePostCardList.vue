<template>
  <div class="card-container">
    <SharePostCardWithAuthor
      v-for="(card, index) in cards"
      :key="index"
      :card="card"
      @card-click="handleCardClick"
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
  methods: {
    handleCardClick(card) {
      this.$emit('card-click', card) // 부모 컴포넌트로 이벤트 전달
    },
  },
}
</script>

<style scoped>
.card-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 한 줄에 최대 3개의 열 */
  gap: 16px; /* 카드 간의 간격 (열과 행 모두 적용) */
  row-gap: 36px; /* 카드 행 간격 */
  width: 100%; /* 카드 컨테이너가 화면 너비에 꽉 차도록 설정 */
  justify-content: center; /* 카드들이 중앙 정렬되도록 설정 */
  align-items: start; /* 카드들의 세로 정렬 */
  padding: 0 16px; /* 페이지 좌우 여백 */
}

@media (max-width: 1024px) {
  .card-container {
    grid-template-columns: repeat(2, 1fr); /* 한 줄에 최대 2개 */
    max-width: 800px; /* 컨테이너의 최대 너비 */
    gap: 12px; /* 카드 간격 조정 */
    row-gap: 20px; /* 행 간격 조정 */
  }
}

@media (max-width: 768px) {
  .card-container {
    grid-template-columns: 1fr; /* 한 줄에 1개 */
    max-width: 400px; /* 컨테이너의 최대 너비 */
    gap: 8px; /* 카드 간격 조정 */
    row-gap: 16px; /* 행 간격 조정 */
  }
}
</style>
