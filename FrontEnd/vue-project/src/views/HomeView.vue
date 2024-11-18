<template>
  
    <main>
      <section class="hero-section">
        <div class="hero-content">
          <h2 class="hero-title">Find Your Dream Home</h2>
          <p class="hero-description">Explore a wide range of properties for sale or rent</p>
          <form class="search-form" @submit.prevent="handleSearch">
            <label for="propertySearch" class="visually-hidden">Search for properties</label>
            <input type="text" id="propertySearch" class="search-input" placeholder="Search for properties..." aria-label="Search for properties">
            <button type="submit" class="search-button">Search</button>
          </form>
        </div>
        <div class="hero-image-container">
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e" alt="Illustration of a modern house" class="hero-image">
        </div>
      </section>
      <section class="featured-listings">
        <h2 class="section-title">Featured Listings</h2>
        <div class="listing-grid">
          <article class="listing-item">
            <span class="listing-icon">🏠</span>
            <div class="listing-details">
              <h3 class="listing-title">Luxury Villa</h3>
              <p class="listing-location">City Center</p>
            </div>
            <p class="listing-price">$1,200,000</p>
          </article>
          <article class="listing-item">
            <span class="listing-icon">🏢</span>
            <div class="listing-details">
              <h3 class="listing-title">Downtown Loft</h3>
              <p class="listing-location">Prime Location</p>
            </div>
            <p class="listing-price">$800,000</p>
          </article>
          <article class="listing-item">
            <span class="listing-icon">🏡</span>
            <div class="listing-details">
              <h3 class="listing-title">Family Home</h3>
              <p class="listing-location">Quiet Neighborhood</p>
            </div>
            <p class="listing-price">$500,000</p>
          </article>
        </div>
      </section>
      <section class="popular-properties">
        <h2 class="section-title">Popular Properties</h2>
        <div class="property-grid">
          <article class="property-card">
            <div class="property-image-container">
              <img src="" alt="Modern Condo" class="property-image">
              <span class="property-tag">Featured</span>
              <h3 class="property-title">Modern Condo</h3>
            </div>
            <div class="property-content">
              <p class="property-description">Urban Living</p>
              <p class="property-price">$600,000</p>
              <div class="property-actions">
                <button class="action-button" aria-label="Add to favorites">⭐</button>
                <button class="action-button" aria-label="View comments">💬</button>
              </div>
            </div>
          </article>
          <article class="property-card">
            <div class="property-image-container">
              <img src="" alt="Cozy Home" class="property-image">
              <span class="property-tag">New Listing</span>
              <h3 class="property-title">Cozy Home</h3>
            </div>
            <div class="property-content">
              <p class="property-description">Suburban Retreat</p>
              <p class="property-price">$400,000</p>
              <div class="property-actions">
                <button class="action-button" aria-label="Add to favorites">⭐</button>
                <button class="action-button" aria-label="View comments">💬</button>
              </div>
            </div>
          </article>
          <article class="property-card">
            <div class="property-image-container">
              <img src="" alt="Luxury Estate" class="property-image">
              <span class="property-tag">Hot Deal</span>
              <h3 class="property-title">Luxury Estate</h3>
            </div>
            <div class="property-content">
              <p class="property-description">Country Mansion</p>
              <p class="property-price">$1,800,000</p>
              <div class="property-actions">
                <button class="action-button" aria-label="Add to favorites">⭐</button>
                <button class="action-button" aria-label="View comments">💬</button>
              </div>
            </div>
          </article>
          <article class="property-card">
            <div class="property-image-container">
              <img src="" alt="Beachfront Villa" class="property-image">
              <span class="property-tag">Exclusive</span>
              <h3 class="property-title">Beachfront Villa</h3>
            </div>
            <div class="property-content">
              <p class="property-description">Seaside Paradise</p>
              <p class="property-price">$1,500,000</p>
              <div class="property-actions">
                <button class="action-button" aria-label="Add to favorites">⭐</button>
                <button class="action-button" aria-label="View comments">💬</button>
              </div>
            </div>
          </article>
        </div>
      </section>
      <section class="community-highlights">
        <h2 class="section-title">Community Highlights</h2>
        <p class="section-description">See what others are saying about their real estate experiences</p>
        <div class="post-grid">
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
          <article class="community-post">
            <header class="post-header">
              <div class="user-avatar"></div>
              <div class="user-info">
                <h3 class="user-name">PropertyGuru</h3>
                <p class="post-meta">Posted 1 day ago - Los Angeles</p>
              </div>
              <button class="post-options" aria-label="Post options">•••</button>
            </header>
            <div class="post-image-container">
              <img src="" alt="Luxury Listing" class="post-image">
              <h4 class="post-image-title">Luxury Listing</h4>
              <div class="post-image-pagination">
                <span class="pagination-dot"></span>
              </div>
            </div>
            <div class="post-content">
              <p class="post-text">Check out this stunning beachfront property for sale! #beachlife</p>
              <span class="post-tag">For Sale</span>
            </div>
          </article>
        </div>
      </section>
      <section class="subscription-form">
        <h2 class="section-title">Subscribe for Updates</h2>
        <p class="section-description">Never miss out on new property listings and real estate news</p>
        <form class="subscribe-form" @submit.prevent="handleSubscribe">
          <div class="form-group">
            <label for="emailInput">Email</label>
            <input type="email" id="emailInput" class="form-input" placeholder="Enter your email" required>
            <p class="form-info">We respect your privacy</p>
          </div>
          <div class="form-group">
            <fieldset>
              <legend>Preferences</legend>
              <div class="chip-group">
                <label class="chip">
                  <input type="radio" name="preference" value="buy"> Buy
                </label>
                <label class="chip">
                  <input type="radio" name="preference" value="rent"> Rent
                </label>
                <label class="chip">
                  <input type="radio" name="preference" value="both"> Both
                </label>
              </div>
            </fieldset>
            <p class="form-info">Choose your interest</p>
          </div>
          <button type="submit" class="subscribe-button">Subscribe</button>
        </form>
      </section>
    </main>
    
  
</template>

<script>
export default {
  name: 'AladinFromSeoul',
  methods: {
    handleSearch() {
      // Implement search functionality
    },
    handleSubscribe() {
      // Implement subscription functionality
    }
  }
}
</script>

<style scoped>

</style>