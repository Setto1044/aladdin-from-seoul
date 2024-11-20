<template>
  <div class="modal-overlay" v-if="show" @click="$emit('close')">
    <div class="modal-content property-detail" @click.stop>
      <button @click="$emit('close')" class="close-button">X</button>
      <!-- 메인 이미지 슬라이더 -->
      <div class="carousel-wrapper">
        <Carousel :items-to-show="1" :wrap-around="true" v-model="currentSlide">
          <Slide v-for="(url, index) in imageUrls" :key="index">
            <img :src="url" alt="Property Image" class="slider-image" />
          </Slide>

          <template #addons>
            <Navigation />
            <Pagination />
          </template>
        </Carousel>
      </div>

      <!-- 썸네일 슬라이더 -->
      <div class="thumbnails-wrapper">
        <Carousel
          :items-to-show="5"
          :wrap-around="true"
          v-model="currentSlide"
          :mouse-drag="false"
          :touch-drag="false"
        >
          <Slide v-for="(url, index) in imageUrls" :key="index">
            <img :src="url" alt="Thumbnail Image" class="thumbnail-image" />
          </Slide>
        </Carousel>
      </div>

      <h2 class="property-title">{{ title }}</h2>
      <p class="property-description">{{ description }}</p>
      <p class="property-detail-text"><strong>Details:</strong> {{ detail }}</p>
      <p class="property-address"><strong>Address:</strong> {{ address }}</p>
      <p class="property-size"><strong>House Size:</strong> {{ houseSize }} m²</p>
      <p class="property-price"><strong>Price:</strong> ${{ price }} / {{ pricePer }}</p>
      <p class="property-rent-dates">
        <strong>Rent From:</strong> {{ rentFrom }} <br />
        <strong>Rent To:</strong> {{ rentTo }}
      </p>
      <p class="property-tags">
        <strong>Tags:</strong>
        <span v-for="tag in tags" :key="tag" class="property-tag">{{ tag }} </span>
      </p>
      <p class="property-views"><strong>Views:</strong> {{ views }}</p>
      <p class="property-host-id"><strong>Host ID:</strong> {{ hostId }}</p>
      <div class="property-comments">
        <h3 class="comments-title">Comments:</h3>
        <ul class="comments-list">
          <li v-for="(comment, index) in comments" :key="index" class="comment-item">
            {{ comment }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel'
import 'vue3-carousel/dist/carousel.css'
import { ref } from 'vue'

export default {
  components: {
    Carousel,
    Slide,
    Navigation,
    Pagination,
  },
  props: {
    show: Boolean,
    id: Number,
    title: String,
    description: String,
    detail: String,
    address: String,
    price: Number,
    houseSize: Number,
    pricePer: String,
    latitude: String,
    longitude: String,
    rentFrom: String,
    rentTo: String,
    tags: Array,
    views: Number,
    hostId: Number,
    imageUrls: Array,
    comments: Array,
  },
  setup() {
    const currentSlide = ref(0)

    const slideTo = (nextSlide) => {
      currentSlide.value = nextSlide
    }

    return {
      currentSlide,
      slideTo,
    }
  },
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  max-width: 800px;
  width: 90%;
  animation: fadeIn 0.3s ease-out;
  overflow-y: auto; /* 세로 스크롤바 추가 */
  max-height: 80vh; /* 모달의 최대 높이 지정 */
  padding: 20px;
  position: relative;
  z-index: 1001;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.close-button {
  position: absolute;
  top: 20px;
  right: 20px;
  background: transparent;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #333;
}

.close-button:hover {
  color: #f44336;
}

.property-slider {
  max-width: 800px;
  margin: 0 auto;
}

.slider-image {
  width: 100%;
  height: auto;
  border-radius: 12px;
}

.property-detail h2 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 16px;
}

.property-detail p {
  margin-bottom: 12px;
  line-height: 1.5;
  color: #444;
}

.property-tags .property-tag {
  display: inline-block;
  background-color: #f1f1f1;
  padding: 4px 8px;
  border-radius: 12px;
  margin: 2px;
  font-size: 12px;
}

.property-tags .property-tag:hover {
  background-color: #e0e0e0;
}

.carousel-wrapper {
  margin-bottom: 20px;
}
.thumbnails-wrapper {
  margin-bottom: 20px;
}

.slider-image {
  width: 100%;
  border-radius: 8px;
  cursor: pointer;
}

.thumbnail-image {
  width: 100%;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
  filter: brightness(70%); /* 기본 밝기 조정 (어둡게) */
  transition: filter 0.3s ease; /* 부드러운 전환 효과 */
}

.thumbnail-image {
  height: 80px;
  object-fit: cover;
}
</style>
