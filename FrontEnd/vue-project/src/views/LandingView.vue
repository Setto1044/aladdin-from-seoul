<template>
  <main>
    <div class="land-content">
      <section class="land-section">
        어쩌구 저쩌구 소개
        <section class="search-section">
          <SearchAutocomplete />
        </section>
        <section class="subscribe-section"></section>
      </section>
      <section class="akinator-section">
        <Akinator></Akinator>
      </section>
    </div>
  </main>
</template>

<script>
import SearchAutocomplete from '@/components/Util/SearchAutocomplete.vue'
import Akinator from '@/components/Akinator/GamePlay.vue'

export default {
  name: 'AladinFromSeoul',
  components: {
    Akinator,
    SearchAutocomplete,
  },
  mounted() {
    this.updateNavHeight()
  },
  methods: {
    handleSearch() {
      // Implement search functionality
    },
    handleSubscribe() {
      // Implement subscription functionality
    },
    updateNavHeight() {
      const navBar = document.querySelector('.top-bar') // Replace with your nav bar selector
      if (navBar) {
        const navHeight = navBar.offsetHeight
        document.documentElement.style.setProperty('--nav-height', `${navHeight}px`)
      }
    },
  },
}
</script>

<style scoped>
.land-section {
  height: calc(100vh - var(--nav-height)); /* Subtract nav bar height */
}
</style>
