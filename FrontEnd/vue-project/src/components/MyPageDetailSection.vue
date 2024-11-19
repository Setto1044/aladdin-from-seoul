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

<style scoped></style>
