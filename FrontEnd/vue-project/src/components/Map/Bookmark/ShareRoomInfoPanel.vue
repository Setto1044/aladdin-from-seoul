<template>
  <div class="real-estate-detail">
    <!-- 게시글 리스트 섹션 -->
    <section class="transaction-details" ref="transactionSection">
      <div v-if="isLoading && boardData.length === 0" class="loading-message">
        데이터를 불러오는 중입니다...
      </div>
      <template v-else>
        <div v-if="boardData.length > 0" class="board-container">
          <!-- 게시글 정보 표시 -->
          <MapItem
            v-for="board in boardData"
            :key="board.roomBoardVo.id"
            :item="board"
            :complex="complex"
            @select-item="handleItemClick(board)"
          />
        </div>
        <div v-else class="no-data">
          <p>조건에 맞는 게시글 정보가 없습니다.</p>
        </div>

        <!-- Trigger for Intersection Observer -->
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
import useUserStore from '@/stores/user-store' // Example: your Pinia store for user info

export default {
  name: 'ShareRoomInfoPanel',
  components: {
    MapItem,
  },
  props: {
    mapInstance: {
      type: Object,
      required: true, // MapInstance를 필수로 받음
    },
  },
  data() {
    return {
      boardData: [], // 게시글 데이터
      isLoading: false, // 로딩 상태
      cursorId: null, // 페이지네이션 커서
      hasMore: true, // 추가 데이터 여부
      observer: null, // Intersection Observer
      observerInitialized: false,
      username: null, // 사용자 이름 저장
      geocoder: null, // Geocoder 인스턴스
    }
  },
  watch: {
    boardData: {
      handler() {
        this.$nextTick(() => {
          if (!this.observerInitialized && this.boardData.length > 0) {
            this.initializeObserver()
          }
          this.updateMarkers() // 마커 업데이트
        })
      },
    },
  },
  async mounted() {
    const userStore = useUserStore()
    this.username = userStore.memberInfo.username // Pinia에서 사용자 정보 가져오기
    this.geocoder = new kakao.maps.services.Geocoder() // Geocoder 초기화

    if (this.username) {
      await this.fetchBoardData() // 게시글 데이터를 먼저 로드
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
    },
    handleItemClick(item) {
      this.$emit('select-item', item) // Emit the clicked item to the parent component
    },
    cleanupObserver() {
      if (this.observer) {
        this.observer.disconnect()
        this.observer = null
      }
    },
    clearMarkers() {
      if (this.mapInstance) {
        const overlays = this.mapInstance.getOverlays()
        overlays.forEach((overlay) => overlay.setMap(null))
      }
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
    geocodeAddress(address, callback) {
      this.geocoder.addressSearch(address, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          callback(result[0]) // 첫 번째 결과 반환
        } else {
          console.error(`Geocoding failed for address: ${address}`)
          callback(null)
        }
      })
    },
    updateMarkers() {
      if (!this.mapInstance) return

      console.log('Updating markers...')
      this.boardData.forEach((board) => {
        const address = board.roomBoardVo.address

        this.geocodeAddress(address, (result) => {
          if (result) {
            const markerPosition = new kakao.maps.LatLng(result.y, result.x)
            const marker = new kakao.maps.Marker({
              position: markerPosition,
              map: this.mapInstance, // 지도에 바로 추가
            })

            // 마커 클릭 이벤트 등록
            kakao.maps.event.addListener(marker, 'click', () => {
              this.handleItemClick(board) // 아이템 클릭 이벤트 호출
            })
          }
        })
      })
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
.complex-title {
  font-family: 'Score7';
}
.section-title {
  font-family: 'Score5';
}

.real-estate-detail {
  padding: 18px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.transaction-details {
  margin-top: 5px;
  flex-grow: 1;
  overflow-y: auto;
  position: relative;
}

.deals-container {
  min-height: 200px;
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
