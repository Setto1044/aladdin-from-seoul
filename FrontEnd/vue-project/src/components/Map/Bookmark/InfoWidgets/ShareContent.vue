<template>
  <div class="share-content">
    <div class="modal-content property-detail" @click.stop>
      <div class="expandable-content" :class="{ expanded: isExpanded }" ref="content">
        <!-- Main Image Slider -->
        <div class="carousel-wrapper">
          <Carousel :items-to-show="1" :wrap-around="true" v-model="currentSlide">
            <Slide v-for="(url, index) in imageUrls" :key="index">
              <div class="slider-image-container">
                <img :src="url" alt="Property Image" class="slider-image" />
              </div>
            </Slide>

            <template #addons>
              <Navigation />
              <Pagination />
            </template>
          </Carousel>
        </div>

        <!-- Thumbnail Slider -->
        <div class="thumbnails-wrapper">
          <Carousel
            :items-to-show="5"
            :wrap-around="true"
            v-model="currentSlide"
            :mouse-drag="false"
            :touch-drag="false"
          >
            <Slide v-for="(url, index) in imageUrls" :key="index">
              <div class="thumbnail-container">
                <img :src="url" alt="Thumbnail Image" class="thumbnail-image" />
              </div>
            </Slide>
          </Carousel>
        </div>

        <div class="card-author">
          <div class="author-avatar-container">
            <div
              class="author-avatar"
              :style="{
                backgroundImage: `url(${authorImageUrl || 'https://via.placeholder.com/150'})`,
              }"
            ></div>
          </div>
          <span class="author-name">{{ hostNickname }}</span>
        </div>

        <h2 class="property-title">{{ title }}</h2>
        <p class="property-address">{{ address }}</p>
        <p class="property-detail-text"><strong>Details:</strong> {{ detail }}{{ detail }}</p>
        <p class="property-size"><strong>House Size:</strong> {{ houseSize }} m²</p>
        <p class="property-price"><strong>Price:</strong> ${{ price }} / {{ pricePer }}</p>
        <!-- Rent Dates with Calendar -->
        <div class="calendar-section">
          <p class="property-rent-dates">
            <strong>Rent From:</strong> {{ formattedRentFrom }} <br />
            <strong>Rent To:</strong> {{ formattedRentTo }}
          </p>
          <VCalendar
            v-if="initialPage"
            :attributes="calendarAttrs"
            :initial-page="initialPage"
            :min-page="minPage"
            :max-page="maxPage"
            :color="calendarColor"
          />
        </div>

        <p class="property-tags">
          <strong>Tags:</strong>
          <span v-for="tag in tags" :key="tag" class="property-tag">{{ tag }}</span>
        </p>
      </div>
      <button v-if="isOverflowing" @click="toggleContent" class="toggle-button">
        {{ isExpanded ? '접기' : '더 보기' }}
      </button>
    </div>
  </div>
</template>

<script>
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel'
import 'vue3-carousel/dist/carousel.css'
import axios from 'axios'

export default {
  components: {
    Carousel,
    Slide,
    Navigation,
    Pagination,
  },
  props: {
    id: {
      type: Number,
      default: null,
    },
  },
  watch: {
    id: {
      immediate: true, // 컴포넌트가 생성될 때도 실행
      handler(newId) {
        if (newId) {
          // id가 있을 때만 데이터 불러오기
          console.log('Property ID changed:', newId)
          this.resetData() // 기존 데이터 초기화
          this.fetchPropertyDetails(newId)
        }
      },
    },
  },
  data() {
    return {
      currentSlide: 0,
      title: '',
      detail: '',
      address: '',
      price: 0,
      houseSize: 0,
      pricePer: '',
      rentFrom: '',
      rentTo: '',
      tags: [],
      views: 0,
      imageUrls: [],
      hostNickname: '',
      isExpanded: false, // 내용이 펼쳐진 상태인지 여부
      isOverflowing: false, // 내용이 300px을 초과하는지 여부
    }
  },
  methods: {
    resetData() {
      // 데이터 초기화
      this.currentSlide = 0
      this.title = ''
      this.detail = ''
      this.address = ''
      this.price = 0
      this.houseSize = 0
      this.pricePer = ''
      this.rentFrom = ''
      this.rentTo = ''
      this.tags = []
      this.views = 0
      this.imageUrls = []
      this.hostNickname = ''
      this.isExpanded = false
      this.isOverflowing = false
    },
    toggleContent() {
      this.isExpanded = !this.isExpanded // 펼침/접힘 상태 토글
    },
    checkOverflow() {
      this.$nextTick(() => {
        const contentElement = this.$refs.content
        if (contentElement && contentElement.scrollHeight > 300) {
          this.isOverflowing = true // 높이가 초과하면 "더 보기" 버튼 표시
        }
      })
    },
    async fetchPropertyDetails(propertyId) {
      try {
        const response = await axios.get(`http://localhost:8080/aladin/boards/${propertyId}`)
        if (response.data.success) {
          const { roomCardInfo, roomImageInfos, nickname } = response.data.data
          this.title = roomCardInfo.title
          this.detail = roomCardInfo.detail
          this.address = roomCardInfo.address
          this.price = roomCardInfo.price
          this.houseSize = roomCardInfo.houseSize
          this.pricePer = roomCardInfo.pricePer
          this.tags = JSON.parse(roomCardInfo.hashtags || '[]')
          this.views = roomCardInfo.views
          this.imageUrls = roomImageInfos.map((img) => img.url)
          this.hostNickname = nickname
          this.rentFrom = new Date(roomCardInfo.rentStart)
          this.rentTo = new Date(roomCardInfo.rentUntil)
        }
      } catch (error) {
        console.error('Error fetching property details:', error)
      }
    },
  },
}
</script>

<style scoped>
.modal-content {
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  height: 30%;
  /* max-height: 90vh; 최대 높이 설정 */
  padding: 10px;
  position: relative;
}

.share-content {
  max-height: 90vh; /* 최대 높이 설정 */
  overflow-y: auto; /* 스크롤 활성화 */
  padding: 16px;
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  margin: 0 auto;
}

.carousel-wrapper {
  width: 100%;
  max-width: 400px;
  margin-bottom: 16px;
}

.slider-image-container {
  width: 100%;
  height: 240px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border-radius: 8px;
}

.slider-image {
  width: auto;
  height: 100%;
  object-fit: cover;
}

.thumbnails-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}

.thumbnail-container {
  width: 60px;
  height: 60px;
  margin-right: 8px;
  border-radius: 4px;
  overflow: hidden;
}

.thumbnail-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-author {
  display: flex;
  align-items: center;
  margin: 16px 0;
}

.author-avatar-container {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 12px;
}

.author-avatar {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.author-name {
  font-weight: bold;
  font-size: 14px;
}

.property-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
}

.property-address,
.property-detail-text,
.property-size,
.property-price {
  font-size: 14px;
  margin-bottom: 4px;
}

.calendar-section {
  width: 100%;
  margin-top: 16px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.property-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 16px;
}

.property-tag {
  background-color: #e0e0e0;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

/* 미디어 쿼리 */
@media (max-width: 768px) {
  .share-content {
    padding: 12px;
  }

  .carousel-wrapper {
    max-width: 100%;
  }

  .slider-image-container {
    height: 200px;
  }

  .thumbnail-container {
    width: 50px;
    height: 50px;
  }

  .property-title {
    font-size: 18px;
  }

  .property-address,
  .property-detail-text,
  .property-size,
  .property-price {
    font-size: 12px;
  }

  .expandable-content {
    max-height: 300px; /* 기본 높이 */
    overflow: hidden; /* 넘치는 내용 숨김 */
    position: relative;
    transition: max-height 0.3s ease-in-out; /* 부드러운 확장 애니메이션 */
  }

  .expandable-content.expanded {
    max-height: none; /* 펼쳐진 상태에서는 제한 제거 */
  }

  .toggle-button {
    margin-top: 10px;
    background: none;
    border: none;
    color: #007bff;
    font-size: 14px;
    cursor: pointer;
    padding: 10px;
    text-align: left;
  }
}
</style>
