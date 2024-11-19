<template>
    <article class="community-post">
        <header class="post-header">
            <div class="user-avatar"></div>
            <div class="user-info">
            <h3 class="user-name">Homeowner123</h3>
            <p class="post-meta">Posted 2 hours ago - New York</p>
            </div>
            <button class="post-options" aria-label="Post options">•••</button>
        </header>
        <div class="post-image-container">
            <img src="" alt="Dream Home" class="post-image">
            <h4 class="post-image-title">Dream Home</h4>
            <div class="post-image-pagination">
            <span class="pagination-dot"></span>
            </div>
        </div>
        <div class="post-content">
            <p class="post-text">Just moved into my new beautiful house! 😍 #newbeginnings</p>
            <span class="post-tag">Inspiration</span>
        </div>
    </article>
</template>


<script>
export default {
    props: ['tag', 'title', 'description', 'price'],
};
</script>

<style scoped>
</style>