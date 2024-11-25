<template>
  <div class="real-estate-detail">
    <!-- Previous template code remains the same -->
    <section class="transaction-details" ref="transactionSection">
      <div v-if="isLoading && boardData.length === 0" class="loading-message">
        데이터를 불러오는 중입니다...
      </div>
      <template v-else>
        <div v-if="boardData.length > 0" class="board-container">
          <MapItem
            v-for="board in boardData"
            :key="board.roomBoardVo.id"
            :item="board"
            :complex="complex"
            @select-item="handleItemClick"
          />
        </div>
        <div v-else class="no-data">
          <p>조건에 맞는 게시글 정보가 없습니다.</p>
        </div>

        <div v-show="boardData.length > 0" ref="loadMoreTrigger" class="load-more-trigger">
          <p v-if="isLoading">로딩 중...</p>
          <p v-else-if="hasMore">더 많은 데이터를 로드하려면 스크롤하세요.</p>
          <p v-else>모든 데이터를 불러왔습니다.</p>
        </div>
      </template>
    </section>
  </div>
</template>

<script>
import axios from 'axios'
import MapItem from '@/components/Map/Bookmark/ShareRoomMapItem.vue'
import useUserStore from '@/stores/user-store'

export default {
  name: 'ShareRoomInfoPanel',
  components: {
    MapItem,
  },
  props: {
    mapInstance: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      boardData: [],
      isLoading: false,
      cursorId: null,
      hasMore: true,
      observer: null,
      observerInitialized: false,
      username: null,
      geocoder: null,
      markers: [], // Array to store markers
      markerCoordinates: new Map(), // Map to store coordinates for each board ID
    }
  },
  watch: {
    boardData: {
      handler() {
        this.$nextTick(() => {
          if (!this.observerInitialized && this.boardData.length > 0) {
            this.initializeObserver()
          }
          this.updateMarkers()
        })
      },
    },
  },
  async mounted() {
    const userStore = useUserStore()
    this.username = userStore.memberInfo.username
    this.geocoder = new kakao.maps.services.Geocoder()

    if (this.username) {
      await this.fetchBoardData()
    }

    if (this.boardData.length > 0) {
      this.$nextTick(() => {
        this.initializeObserver()
      })
    }
  },
  beforeDestroy() {
    this.cleanupObserver()
    this.clearMarkers()
  },
  methods: {
    resetData() {
      this.cleanupObserver()
      this.clearMarkers()
      this.boardData = []
      this.cursorId = null
      this.isLoading = false
      this.hasMore = true
      this.observerInitialized = false
      this.markers = []
      this.markerCoordinates.clear()
    },
    handleItemClick(board) {
      // Get coordinates from our stored map using the board ID
      const coordinates = this.markerCoordinates.get(board.roomBoardVo.id)
      if (coordinates) {
        console.log('handleItemClick called with:', {
          item: board,
          latitude: coordinates.latitude,
          longitude: coordinates.longitude,
        })
        // 지도 중심을 마커 위치로 이동
        this.mapInstance.setCenter(
          new kakao.maps.LatLng(coordinates.latitude, coordinates.longitude),
        )
        this.mapInstance.setLevel(3)
        this.$emit('select-item', {
          item: board,
          latitude: coordinates.latitude,
          longitude: coordinates.longitude,
        })
      } else {
        console.warn('No coordinates found for board:', board.roomBoardVo.id)
      }
    },
    cleanupObserver() {
      if (this.observer) {
        this.observer.disconnect()
        this.observer = null
      }
    },
    clearMarkers() {
      this.markers.forEach((marker) => {
        marker.setMap(null)
      })
      this.markers = []
      this.markerCoordinates.clear()
    },
    initializeObserver() {
      if (this.observerInitialized || !this.$refs.loadMoreTrigger) {
        return
      }

      this.observer = new IntersectionObserver(
        (entries) => {
          const target = entries[0]
          if (target.isIntersecting && !this.isLoading && this.hasMore) {
            this.fetchBoardData(this.cursorId)
          }
        },
        {
          root: this.$refs.transactionSection,
          threshold: 0.1,
          rootMargin: '100px',
        },
      )

      this.observer.observe(this.$refs.loadMoreTrigger)
      this.observerInitialized = true
    },
    async geocodeAddress(address) {
      return new Promise((resolve, reject) => {
        this.geocoder.addressSearch(address, (result, status) => {
          if (status === kakao.maps.services.Status.OK) {
            resolve(result[0])
          } else {
            reject(new Error(`Geocoding failed for address: ${address}`))
          }
        })
      })
    },
    async updateMarkers() {
      if (!this.mapInstance) return

      console.log('Updating markers...')
      this.clearMarkers()

      for (const board of this.boardData) {
        try {
          const result = await this.geocodeAddress(board.roomBoardVo.address)
          if (result) {
            const latitude = parseFloat(result.y)
            const longitude = parseFloat(result.x)

            // Store coordinates in our Map
            this.markerCoordinates.set(board.roomBoardVo.id, {
              latitude,
              longitude,
            })

            console.log('Coordinates for board', board.roomBoardVo.id, ':', {
              latitude,
              longitude,
            })

            const content = `<div class="custom-overlay-share">
                <div class="overlay-content-share">
                  <div class="overlay-title-share">${board.roomBoardVo.title}</div>
                  <div class="overlay-price-share">${board.roomBoardVo.pricePer} ${board.roomBoardVo.price} 만원</div>
                </div>
                <div class="overlay-tail-share"></div>
              </div>
            `

            const overlay = new kakao.maps.CustomOverlay({
              position: new kakao.maps.LatLng(latitude, longitude),
              content: content,
              yAnchor: 1.3, // 오버레이 기준점을 꼬리에 맞춤
            })

            // 이벤트를 content에 추가하려면 DOM 객체를 직접 접근해야 함
            const overlayElement = document.createElement('div')
            overlayElement.innerHTML = content

            // 마우스 오버/아웃 이벤트 추가
            // 마우스 오버/아웃 이벤트 추가
            overlayElement.addEventListener('mouseover', () => {
              if (board.thumbnailUrl) {
                // 말풍선 요소 생성
                const tooltip = document.createElement('div')
                tooltip.className = 'custom-tooltip'
                tooltip.innerHTML = `<img src="${board.thumbnailUrl}" alt="Apartment Photo" class="tooltip-image" />`

                // 오버레이를 DOM에 추가
                overlayElement.appendChild(tooltip)
                overlay.setZIndex(999) // 오버레이를 앞으로 가져오기
              }
            })

            overlayElement.addEventListener('mouseout', () => {
              // 말풍선 제거
              const tooltip = overlayElement.querySelector('.custom-tooltip')
              if (tooltip) {
                overlayElement.removeChild(tooltip)
              }
              overlay.setZIndex(1) // 오버레이를 뒤로 보내기
            })

            overlayElement.addEventListener('click', () => {
              this.handleItemClick(board)
            })

            // 커스텀 오버레이에 이벤트가 적용된 DOM 요소 설정
            overlay.setContent(overlayElement)
            overlay.setMap(this.mapInstance) // 지도에 오버레이 추가
          }
        } catch (error) {
          console.error('Error geocoding address:', error)
        }
      }
    },
    async fetchBoardData(cursorId = null, pageSize = 10) {
      if (this.isLoading || !this.hasMore) return

      this.isLoading = true
      try {
        const url = `http://localhost:8080/aladin/bookmark/board/${this.username}?pageSize=${pageSize}`

        const response = await axios.get(url)
        const data = response.data

        if (data.success) {
          const newBoards = data.data
          if (newBoards.length > 0) {
            this.boardData = [...this.boardData, ...newBoards]
            this.cursorId =
              this.boardData.length > 0
                ? this.boardData[this.boardData.length - 1].roomBoardVo.id
                : null
          }
          this.hasMore = newBoards.length === pageSize
        } else {
          console.error('Error:', data.message)
          this.hasMore = false
        }
      } catch (error) {
        console.error('Error fetching board data:', error.message)
        this.hasMore = false
      } finally {
        this.isLoading = false
      }
    },
  },
}
</script>

<style scoped>
.real-estate-detail {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.load-more-trigger {
  padding: 20px 0;
  text-align: center;
  color: #666;
  margin-top: 10px;
}

.loading-message,
.no-data {
  text-align: center;
  padding: 20px;
  color: #666;
}
</style>
