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

        <div class="property-detail-container">
          <p class="property-detail-text">
            <strong>Details:</strong>
            <span v-if="isExpanded || !isOverflowing" class="detail-content" ref="detailContent">
              {{ detail }}
              <span class="more-link" @click="toggleContent">간략히 보기</span>
            </span>
            <span v-else class="detail-content truncated" ref="detailContent">
              {{ detail.substring(0, 50) }}...
              <span class="more-link" @click="toggleContent">더 보기</span>
            </span>
          </p>
        </div>

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
            class="full-width-calendar"
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
    detail: {
      immediate: true,
      handler() {
        this.checkOverflow() // `detail`이 변경될 때마다 overflow 상태 확인
      },
    },
  },
  computed: {
    formattedRentFrom() {
      return this.formatDate(this.rentFrom)
    },
    formattedRentTo() {
      return this.formatDate(this.rentTo)
    },
  },
  mounted() {
    this.checkOverflow() // 컴포넌트가 마운트된 후 overflow 상태 확인
  },
  data() {
    return {
      calendarColor: 'blue',
      calendarAttrs: [],
      initialPage: null, // Initial page for the calendar
      minPage: null,
      maxPage: null,
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
      isOverflowing: true, // 내용이 300px을 초과하는지 여부
    }
  },
  methods: {
    setCalendarAttrs() {
      if (this.rentFrom && this.rentTo) {
        this.initialPage = {
          month: this.rentFrom.getMonth() + 1, // JavaScript months are 0-based
          year: this.rentFrom.getFullYear(),
        }
        this.minPage = {
          month: this.rentFrom.getMonth() + 1,
          year: this.rentFrom.getFullYear(),
        }
        this.maxPage = {
          month: this.rentTo.getMonth() + 1,
          year: this.rentTo.getFullYear(),
        }
        console.log('Initial Page:', this.initialPage) // Debugging
        console.log('Min Page:', this.minPage, 'Max Page:', this.maxPage) // Debugging

        this.calendarAttrs = [
          {
            key: 'rent-period',
            highlight: true,
            dates: { start: this.rentFrom, end: this.rentTo }, // Use the dates from API
          },
        ]
      } else {
        console.error('Rent dates are not set!')
      }
    },
    formatDate(date) {
      if (!date) return '' // Handle cases where the date is null or undefined
      const d = new Date(date) // Ensure it's a Date object
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0') // Months are 0-based
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
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
      this.isExpanded = !this.isExpanded
    },
    checkOverflow() {
      setTimeout(() => {
        const detailContent = this.$refs.detailContent
        if (detailContent) {
          this.isOverflowing = 50 < detailContent.offsetHeight
        }
      }, 100) // DOM이 완전히 렌더링될 때까지 기다림
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
          this.setCalendarAttrs() // Call method to update calendar attributes
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
    max-height: 150px; /* 초기 높이 제한 */
    overflow: hidden;
    transition: max-height 0.3s ease-in-out; /* 부드러운 확장 효과 */
  }

  .expandable-content.expanded {
    max-height: none; /* 펼쳐진 상태에서는 제한 제거 */
  }

  .property-detail-container {
    margin-bottom: 16px;
  }

  .property-detail-text {
    font-size: 14px;
    color: #666;
    line-height: 1.5;
  }

  .detail-content {
    display: inline-block;
    overflow: hidden; /* 기본적으로 숨김 처리 */
  }

  .detail-content.truncated {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .toggle-button {
    margin-top: 8px;
    background: none;
    border: none;
    color: #007bff;
    font-size: 14px;
    cursor: pointer;
    padding: 0;
    text-align: left;
    transition: color 0.3s ease;
  }

  .toggle-button:hover {
    color: #0056b3;
  }

  .more-link {
    color: #007bff; /* 버튼처럼 보이지 않게 링크 스타일 */
    font-weight: bold;
    cursor: pointer;
    display: inline;
    margin-left: 4px; /* 텍스트와 간격 */
    text-decoration: underline dotted; /* 밑줄을 점선으로 */
  }

  .more-link:hover {
    color: #0056b3; /* 호버 시 더 어두운 색상 */
    text-decoration: underline; /* 호버 시 밑줄 변경 */
  }
}
</style>
