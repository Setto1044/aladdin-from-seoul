<template>
    <div class="blog-posts-page">
      
  
      <section class="hero-section">
        <div class="hero-content">
          <h2 class="hero-title">All Posts</h2>
          <p class="hero-description">Explore all the posts in one place</p>
        </div>
        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e" alt="" class="hero-background" />
      </section>
  
      <main class="main-content">
        <h2 class="main-title">All Posts Board</h2>
        <div class="post-list">
          <div class="post-row">
            <article class="post-card">
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
            <article class="post-card">
              <div class="post-image-container">
                <div class="post-image"></div>
              </div>
              <div class="post-content">
                <h3 class="post-title">Tech Innovations of the Year</h3>
                <p class="post-subtitle">Stay updated on the latest technological advancements and innovations</p>
                <div class="post-category">
                  <span class="category-label">Technology</span>
                </div>
                <div class="post-author">
                  <div class="author-avatar"></div>
                  <span class="author-name">TechInsider</span>
                </div>
              </div>
            </article>
          </div>
          <div class="post-row">
            <article class="post-card">
              <div class="post-image-container">
                <div class="post-image"></div>
              </div>
              <div class="post-content">
                <h3 class="post-title">Travel Destinations Bucket List</h3>
                <p class="post-subtitle">Plan your next adventure with these must-visit travel destinations</p>
                <div class="post-category">
                  <span class="category-label">Travel</span>
                </div>
                <div class="post-author">
                  <div class="author-avatar"></div>
                  <span class="author-name">TravelBug</span>
                </div>
              </div>
            </article>
            <article class="post-card">
              <div class="post-image-container">
                <div class="post-image"></div>
              </div>
              <div class="post-content">
                <h3 class="post-title">Healthy Lifestyle Tips</h3>
                <p class="post-subtitle">Get valuable insights on maintaining a healthy lifestyle and well-being</p>
                <div class="post-category">
                  <span class="category-label">Health</span>
                </div>
                <div class="post-author">
                  <div class="author-avatar"></div>
                  <span class="author-name">WellnessGuru</span>
                </div>
              </div>
            </article>
          </div>
        </div>
        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e" alt="" class="content-background" />
      </main>
  
    </div>
  </template>
  
  <style scoped>
  .blog-posts-page {
    background-color: #fff;
    position: relative;
    display: flex;
    padding-top: 80px;
    flex-direction: column;
    justify-content: flex-start;
  }
  
  .top-bar {
    background-color: #fff;
    box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
    position: absolute;
    z-index: 1;
    display: flex;
    width: 100%;
    height: 80px;
    align-items: center;
    gap: 20px;
    overflow: hidden;
    font-family: Roboto, sans-serif;
    color: #000;
    justify-content: center;
    flex-wrap: wrap;
    left: 0;
    right: 0;
    top: 0;
    padding: 20px;
  }
  
  .logo {
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.1);
    width: 40px;
    height: 40px;
    margin: auto 0;
  }
  
  .app-title {
    font-size: 28px;
    font-weight: 500;
    line-height: 1;
    flex: 1;
    margin: auto 0;
  }
  
  .main-navigation {
    background-color: #fff;
    display: flex;
    min-width: 240px;
    align-items: center;
    gap: 40px;
    font-size: 16px;
    justify-content: center;
    margin: auto 0;
  }
  
  .nav-link {
    text-decoration: none;
    color: inherit;
  }
  
  .search-field {
    border-radius: 6px;
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.5);
    line-height: 1;
    justify-content: flex-end;
    width: 200px;
    margin: auto 0;
    padding: 8px;
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
  
  .search-input {
    flex: 1;
    border: none;
    outline: none;
    background: transparent;
  }
  
  .search-icon {
    width: 20px;
    height: 20px;
    object-fit: contain;
  }
  
  .hero-section {
    background-color: rgba(0, 0, 0, 0.6);
    position: relative;
    z-index: 0;
    display: flex;
    width: 100%;
    align-items: flex-start;
    gap: 40px 60px;
    overflow: hidden;
    font-family: Roboto, sans-serif;
    color: #fff;
    text-align: center;
    justify-content: center;
    padding: 60px 170px;
  }
  
  .hero-content {
    z-index: 1;
    display: flex;
    min-width: 240px;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    flex: 1;
  }
  
  .hero-title {
    font-size: 40px;
    font-weight: 700;
    line-height: 1.2;
  }
  
  .hero-description {
    font-size: 16px;
    font-weight: 400;
    margin-top: 24px;
  }
  
  .hero-background {
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
  
  .main-content {
    position: relative;
    z-index: 0;
    display: flex;
    width: 100%;
    flex-direction: column;
    overflow: hidden;
    justify-content: center;
    padding: 60px 170px;
  }
  
  .main-title {
    width: 100%;
    color: #000;
    text-align: center;
    font: 700 40px/1.2 Roboto, sans-serif;
  }
  
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
  
  .site-footer {
    z-index: 1;
    display: flex;
    width: 100%;
    align-items: center;
    gap: 40px 60px;
    overflow: hidden;
    color: #000;
    text-align: center;
    justify-content: center;
    padding: 60px;
    font: 400 20px/28px Roboto, sans-serif;
  }
  
  .footer-nav {
    display: flex;
    min-width: 240px;
    min-height: 100px;
    gap: 40px 60px;
    justify-content: center;
    margin: auto 0;
  }
  
  .footer-link {
    text-decoration: none;
    color: inherit;
  }
  
  .visually-hidden {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border: 0;
  }
  
  @media (max-width: 991px) {
    .top-bar,
    .hero-section,
    .main-content,
    .site-footer {
      max-width: 100%;
      padding: 20px;
    }
  
    .hero-section {
      padding: 40px 20px;
    }
  
    .post-list {
      margin-top: 40px;
    }
  
    .post-category,
    .post-author {
      white-space: normal;
    }
  }
  </style>