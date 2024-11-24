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

            const markerPosition = new kakao.maps.LatLng(latitude, longitude)
            const marker = new kakao.maps.Marker({
              position: markerPosition,
              map: this.mapInstance,
            })

            this.markers.push(marker)

            // Add click event listener
            kakao.maps.event.addListener(marker, 'click', () => {
              this.handleItemClick(board)
            })
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
/* Styles remain the same */
</style>
