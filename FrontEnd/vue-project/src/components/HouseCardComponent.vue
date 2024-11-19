<template>
    <article class="property-card">
        <div class="property-image-container">
            <img src="" alt="Modern Condo" class="property-image">
            <span class="property-tag">{{tag}}</span>
            <h3 class="property-title">{{title}}</h3>
        </div>
        <div class="property-content">
            <p class="property-description">{{description}}</p>
            <p class="property-price">{{ price }}</p>
            <div class="property-actions">
            <button class="action-button" aria-label="Add to favorites">⭐</button>
            <button class="action-button" aria-label="View comments">💬</button>
            </div>
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