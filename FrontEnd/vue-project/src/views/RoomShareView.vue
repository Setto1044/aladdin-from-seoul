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
          <CardRow :cards="cardData" />
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
import CardRow from '@/components/SharePostCardList.vue'

export default {
  components: {
    CardRow,
  },
  data() {
    return {
      cardData: [
        {
          title: 'Card 1',
          description: 'Description 1',
          tags: ['#Photography', '#Nature', '#Wildlife', '#Travel', '#Adventure'],
        },
        { title: 'Card 2', description: 'Description 2', tags: ['#Tag3', '#Tag4'] },
        {
          title: 'Card 3',
          description: 'Description 3',
          tags: ['#Photography', '#Nature', '#Wildlife', '#Travel', '#Adventure'],
        },
        { title: 'Card 4', description: 'Description 4', tags: ['#Tag7', '#Tag8'] },
        { title: 'Card 5', description: 'Description 5', tags: ['#Tag9', '#Tag10'] },
        { title: 'Card 6', description: 'Description 6', tags: ['#Tag11', '#Tag12'] },
        {
          title: 'Card 8',
          description:
            'DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription',
          tags: ['#Photography', '#Nature', '#Wildlife', '#Travel', '#Adventure', '#안녕'],
        },
        // 카드 데이터를 필요에 맞게 추가
      ],
    }
  },
}
</script>

<style scoped></style>
