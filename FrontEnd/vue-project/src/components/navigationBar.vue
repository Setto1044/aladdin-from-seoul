<template>
  <header class="top-bar">
    <a href="/" class="logolink nav-link">
      <div class="logo"></div>
      <h1 class="site-title">알라딘 프롬 서울</h1>
    </a>

    <nav class="main-navigation">
      <form v-if="$route.meta.showSearchBar" class="header-search-form nav-link">
        <label for="search-input" class="visually-hidden">Search in site</label>
        <input
          type="text"
          id="search-input"
          class="header-search-input"
          placeholder="Search in site"
          aria-label="Search in site"
        />
        <button type="submit" class="header-search-button" aria-label="Submit search">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/ff75430b99a5dc1ef509cf04ee77ca85091d2f5a3a9e255ba3ad5b4f13bcf5f9?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
            class="header-search-icon"
            alt="Search"
          />
        </button>
      </form>

      <a href="/map" class="nav-link">지도</a>
      <a href="/listings" class="nav-link">매물 게시판</a>
      <a href="/favorites" class="nav-link">내 관심목록</a>
      <a href="/mypage" class="nav-link">마이페이지</a>
    </nav>
  </header>
</template>

<script setup></script>

<style scoped></style>
