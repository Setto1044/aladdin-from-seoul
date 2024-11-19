<template>
  <article class="post-card" @click="goToHouseDetail">
    <div class="post-image-container">
      <div class="post-image"></div>
    </div>
    <div class="post-content">
      <h3 class="post-title">Amazing Nature Photography</h3>
      <p class="post-subtitle">Discover breathtaking photos of nature from around the world</p>
      <div class="post-category">
        <span class="category-label">Photography</span>
      </div>
      <div class="post-author">
        <div class="author-avatar"></div>
        <span class="author-name">NatureEnthusiast</span>
      </div>
    </div>
  </article>
</template>

<script>
export default {
  props: ['tag', 'title', 'description', 'price'],
  methods: {
    goToHouseDetail() {
      this.$router.push({ name: 'house', params: { id: '2' } })
    },
  },
}
</script>
<style scoped>
.post-list {
  z-index: 1;
  display: flex;
  margin-top: 60px;
  width: 100%;
  flex-direction: column;
  justify-content: center;
  padding: 20px 0;
}

.post-row {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 40px;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.post-row:not(:first-child) {
  margin-top: 40px;
}

.post-card {
  border-radius: 6px;
  display: flex;
  min-width: 240px;
  align-items: flex-start;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
  flex: 1;
  margin: auto 0;
  padding: 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.post-image-container {
  display: flex;
  min-height: 100px;
  overflow: hidden;
  justify-content: flex-start;
  width: 100px;
}

.post-image {
  background-color: rgba(217, 217, 217, 0.5);
  min-height: 100px;
  width: 100px;
  height: 100px;
  flex: 1;
}

.post-content {
  display: flex;
  min-width: 240px;
  flex-direction: column;
  font-family: Roboto, sans-serif;
  color: #000;
  font-weight: 500;
  justify-content: flex-start;
  flex: 1;
}

.post-title {
  font-size: 20px;
  line-height: 1.4;
}

.post-subtitle {
  font-size: 16px;
  font-weight: 400;
  line-height: 24px;
  margin-top: 8px;
}

.post-category {
  display: flex;
  margin-top: 8px;
  width: 100%;
  align-items: center;
  gap: 6px;
  overflow: hidden;
  font-size: 12px;
  font-weight: 400;
  white-space: nowrap;
  line-height: 1;
  justify-content: flex-start;
  padding: 4px 0;
}

.category-label {
  border-radius: 2px;
  background-color: rgba(217, 217, 217, 0.5);
  padding: 2px 4px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.post-author {
  display: flex;
  margin-top: 8px;
  width: 100%;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  white-space: nowrap;
  line-height: 1;
  justify-content: flex-start;
  padding: 4px 0;
}

.author-avatar {
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.1);
  width: 20px;
  height: 20px;
}

.author-name {
  flex: 1;
}

.content-background {
  object-fit: contain;
  object-position: center;
  width: 100%;
  position: absolute;
  z-index: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 0;
}
</style>
