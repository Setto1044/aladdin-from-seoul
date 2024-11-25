<template>
  <div class="modal-overlay" @click="$emit('close')">
    <div class="modal-content property-detail" @click.stop>
      <button @click="$emit('close')" class="close-button">X</button>

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
        <div class="action-buttons">
          <button v-if="isAuthor" class="edit-button" @click.stop="goToEditPage">Edit</button>
          <button v-if="isAuthor" class="delete-button" @click.stop="goToDeletePage">Delete</button>
          <!-- username이 있을, isAuthor 아닐 때만 즐겨찾기 버튼 표시 -->
          <button v-else class="favorite-button" @click="toggleBookmark(id)">
            <span v-if="isBookmarked">★</span>
            <span v-else>☆</span>
          </button>
        </div>
      </div>

      <h2 class="property-title">{{ title }}</h2>
      <p class="property-address">{{ address }}</p>
      <p class="property-detail-text"><strong>Details:</strong> {{ detail }}</p>
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
      <p class="property-views"><strong>Views:</strong> {{ views }}</p>

      <SharePostComment :postId="id"></SharePostComment>
    </div>
  </div>
</template>

<script>
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel'
import 'vue3-carousel/dist/carousel.css'
import useUserStore from '@/stores/user-store' // Example: your Pinia store for user info
import axios from 'axios'
import SharePostComment from '@/components/RoomShare/SharePostComment.vue'

export default {
  components: {
    Carousel,
    Slide,
    Navigation,
    Pagination,
    SharePostComment,
  },
  props: {
    id: Number,
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
      comments: [],
      hostUsername: '',
      hostNickname: '',
      hostImageUrls: '',
      isBookmarked: false, // 북마크 여부 상태
      userStore: null,
    }
  },
  mounted() {
    this.checkBookmarkStatus() // 컴포넌트가 생성될 때 북마크 상태 확인
  },
  watch: {
    id: {
      immediate: true,
      handler(newId) {
        if (newId) {
          this.fetchPropertyDetails(newId)
        }
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
    isAuthor() {
      const userStore = useUserStore()
      this.userStore = userStore // Example: Pinia store for user info
      return userStore.memberInfo.username === this.hostUsername // Compare with the author's username
    },
  },
  methods: {
    async checkBookmarkStatus() {
      // API를 통해 북마크 상태 확인
      console.log('북마크', this.userStore.memberInfo.username)
      if (!this.userStore.memberInfo.username) return

      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/bookmark/board/status?username=${this.userStore.memberInfo.username}&boardId=${this.id}`,
        )
        this.isBookmarked = response.data.success // 서버 응답에 따라 상태 업데이트
        console.log(
          '��마크 상태 확인 성공:',
          `http://localhost:8080/aladin/bookmark/board/status?username=${this.userStore.memberInfo.username}&roomboardsId=${this.id}`,
          response.data.success,
          this.id,
          this.userStore.memberInfo.username,
        )
      } catch (error) {
        console.error('북마크 상태 확인 실패:', error)
      }
    },

    async fetchPropertyDetails(propertyId) {
      try {
        const response = await axios.get(`http://localhost:8080/aladin/boards/${propertyId}`)
        console.log(response.data)
        if (response.data.success) {
          const { roomBoardVo, roomImageInfos, nickname, profileImagePath } = response.data.data
          this.title = roomBoardVo.title
          this.detail = roomBoardVo.detail
          this.address = roomBoardVo.address
          this.price = roomBoardVo.price
          this.houseSize = roomBoardVo.houseSize
          this.pricePer = roomBoardVo.pricePer
          this.tags = JSON.parse(roomBoardVo.hashtags || '[]')
          this.views = roomBoardVo.views
          this.imageUrls = roomImageInfos.map((img) => img.url)
          this.hostUsername = roomBoardVo.membersUsername
          this.hostNickname = nickname
          this.hostImageUrls = profileImagePath
          // Parse dates from API
          this.rentFrom = new Date(roomBoardVo.rentStart) // Convert to Date object
          this.rentTo = new Date(roomBoardVo.rentUntil) // Convert to Date object
          this.setCalendarAttrs() // Call method to update calendar attributes
        }
      } catch (error) {
        console.error('Error fetching property details:', error)
      }
    },
    async toggleBookmark(itemNo) {
      // 북마크 상태에 따라 POST 또는 DELETE 요청
      if (!this.userStore.memberInfo.username) {
        alert('로그인이 필요합니다!')
        return
      }

      try {
        if (this.isBookmarked) {
          // DELETE 요청 (북마크 해제)
          await axios.delete('http://localhost:8080/aladin/bookmark/board', {
            data: {
              username: this.userStore.memberInfo.username,
              roomboardsId: this.id,
            },
          })
          console.log('북마크 해제 성공:', this.id)
        } else {
          // POST 요청 (북마크 추가)
          await axios.post('http://localhost:8080/aladin/bookmark/board', {
            username: this.userStore.memberInfo.username,
            roomboardsId: this.id,
          })
          console.log('북마크 추가 성공:', this.id)
        }

        // 상태 반전
        this.isBookmarked = !this.isBookmarked
      } catch (error) {
        console.error('북마크 상태 변경 실패:', error)
      }
    },
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
    goToEditPage() {
      this.$router.push({
        name: 'share-edit', // Replace with the actual name of your route
        params: { id: this.id },
      })
    },
    goToDeletePage() {
      if (confirm('정말로 이 게시물을 삭제하시겠습니까?')) {
        axios
          .delete(`http://localhost:8080/aladin/boards/${this.id}`)
          .then((response) => {
            if (response.data.success) {
              alert('게시물이 성공적으로 삭제되었습니다.')
              this.$emit('close') // 모달 닫기
              this.$router.push({ name: 'share' }) // 삭제 후 다른 페이지로 이동
            } else {
              alert(`삭제 실패: ${response.data.message}`)
            }
          })
          .catch((error) => {
            console.error('게시물 삭제 중 오류 발생:', error)
            alert('삭제 중 문제가 발생했습니다. 다시 시도해주세요.')
          })
      }
    },
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
  overflow-y: auto;
  max-height: 90vh;
  padding: 20px;
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #ff5f5f;
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 20px;
  cursor: pointer;
  width: 30px;
  height: 30px;
}

.carousel-wrapper {
  margin-bottom: 20px;
}

.slider-image-container {
  width: 100%;
  height: 300px; /* Fixed height for consistency */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border-radius: 12px;
  background-color: #f0f0f0;
}

.slider-image {
  width: auto;
  height: 100%;
  object-fit: cover; /* Ensure image covers the container without distortion */
}

.thumbnails-wrapper {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

.thumbnail-container {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 8px;
  background-color: #f0f0f0;
  cursor: pointer;
}

.thumbnail-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.2s ease-in-out;
}

.thumbnail-container:hover .thumbnail-image {
  transform: scale(1.1);
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
}

.edit-button,
.delete-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.edit-button {
  background-color: #007bff;
  color: white;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.property-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.property-tag {
  background-color: #e0e0e0;
  padding: 4px 8px;
  border-radius: 8px;
  font-size: 12px;
}

.card-author {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.author-avatar-container {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  background-color: #f0f0f0;
}

.author-avatar {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-size: 16px;
  font-weight: bold;
}

.created-time {
  font-size: 14px;
  color: #999;
}

.calendar-section {
  margin-top: 20px;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 12px;
}

.calendar-section p {
  margin-bottom: 10px;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.card-author {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #ddd; /* Optional divider */
}

.author-avatar-container {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  background-color: #f0f0f0;
}

.author-avatar {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.author-name {
  font-size: 16px;
  font-weight: bold;
  flex-grow: 1; /* Push buttons to the right */
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.edit-button,
.delete-button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.edit-button {
  background-color: #007bff;
  color: white;
}

.edit-button:hover {
  background-color: #0056b3;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.delete-button:hover {
  background-color: #c82333;
}
</style>
