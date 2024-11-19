<template>
  <div class="room-detail-page">
    <header class="room-header">
      <h1 class="room-title">{{ room.title }}</h1>
      <p class="room-subtitle">{{ room.subtitle }}</p>
    </header>

    <section class="room-images">
      <div class="image-slider">
        <button @click="prevImage" class="slider-button prev-button">&#8249;</button>
        <img
          :src="room.images[currentImageIndex]"
          :alt="`Room Image ${currentImageIndex + 1}`"
          class="room-image"
        />
        <button @click="nextImage" class="slider-button next-button">&#8250;</button>
      </div>
    </section>

    <section class="room-info">
      <div class="room-price">Price: {{ room.price }}</div>
      <div class="room-location">Location: {{ room.location }}</div>
      <div class="room-features">
        <h2>Features:</h2>
        <ul>
          <li v-for="(feature, index) in room.features" :key="index">{{ feature }}</li>
        </ul>
      </div>
      <div class="room-description">
        <h2>Description:</h2>
        <p>{{ room.description }}</p>
      </div>
    </section>

    <section class="contact-section">
      <button class="contact-button">Contact Agent</button>
      <button class="favorite-button" @click="toggleFavorite">
        {{ isFavorite ? 'Remove from Favorites' : 'Add to Favorites' }}
      </button>
    </section>
  </div>
</template>

<script>
export default {
  name: 'RoomDetailPage',
  props: {
    id: {
      type: String,
      default: '',
      required: false,
    },
  },
  data() {
    return {
      room: {
        title: 'Modern Condo in Seoul',
        subtitle: 'Spacious 2-bedroom apartment',
        images: [
          'https://www.imwood.co.kr/news/photo/202110/26213_50252_518.jpg',
          'https://vmspace.com/ActiveFile/spacem.org/board_img/93160908165cac5825a093.jpg',
          'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1iBAl24bxOd3RwEv43CgXsl-CgYS-SX0rBg&s',
        ],
        price: '$1,200/month',
        location: 'Seoul, South Korea',
        features: ['2 bedrooms', '1 bathroom', '50 sqm', 'Fully furnished', 'Near subway station'],
        description:
          'This modern condo in Seoul offers a spacious living area, fully equipped kitchen, and beautiful views of the city. Perfect for small families or young professionals.',
      },
      isFavorite: false,
      currentImageIndex: 0,
    }
  },
  methods: {
    toggleFavorite() {
      this.isFavorite = !this.isFavorite
    },
    nextImage() {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.room.images.length
    },
    prevImage() {
      this.currentImageIndex =
        (this.currentImageIndex - 1 + this.room.images.length) % this.room.images.length
    },
  },
}
</script>
<style scoped>
.room-detail-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.room-header {
  text-align: center;
  margin-bottom: 20px;
}

.room-title {
  font-size: 28px;
  font-weight: bold;
}

.room-subtitle {
  font-size: 18px;
  color: #666;
}

.room-images {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.image-slider {
  position: relative;
  width: 100%;
  max-width: 600px;
}

.room-image {
  width: 100%;
  border-radius: 8px;
}

.slider-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  border: none;
  padding: 10px;
  cursor: pointer;
  border-radius: 50%;
}

.prev-button {
  left: 10px;
}

.next-button {
  right: 10px;
}

.room-info {
  margin-bottom: 20px;
}

.room-price,
.room-location {
  font-size: 18px;
  margin-bottom: 10px;
}

.room-features ul {
  list-style: disc;
  padding-left: 20px;
}

.contact-section {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.contact-button,
.favorite-button {
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.contact-button {
  background-color: #007bff;
  color: #fff;
}

.favorite-button {
  background-color: #ffc107;
  color: #fff;
}
</style>
