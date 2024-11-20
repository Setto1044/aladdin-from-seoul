<template>
  <div class="blog-posts-page">
    <section class="blog-hero-section">
      <div class="blog-hero-content">
        <h2 class="blog-hero-title">All Posts</h2>
        <p class="blog-hero-description">Explore all the posts in one place</p>
      </div>
      <img
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
        alt=""
        class="blog-hero-background"
      />
    </section>

    <main class="blog-main-content">
      <h2 class="blog-main-title">All Posts Board</h2>
      <div class="blog-post-list">
        <div class="blog-post-row">
          <CardComponent></CardComponent>
          <CardComponent></CardComponent>
        </div>
        <div class="blog-post-row">
          <CardComponent></CardComponent>
          <CardComponent></CardComponent>
        </div>
      </div>
      <img
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
        alt=""
        class="blog-content-background"
      />
    </main>
  </div>
</template>

<script>
import CardComponent from '@/components/SharePostCardWithAuthor.vue'
export default {
  components: {
    CardComponent,
  },
}
</script>

<style scoped></style>
