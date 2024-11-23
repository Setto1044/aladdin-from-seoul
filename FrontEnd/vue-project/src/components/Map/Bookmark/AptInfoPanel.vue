<template>
  <div class="real-estate-detail">
    <!-- 매물 리스트 섹션 -->
    <section class="transaction-details" ref="transactionSection">
      <div v-if="isLoading && houseDeals.length === 0" class="loading-message">
        데이터를 불러오는 중입니다...
      </div>
      <template v-else>
        <div v-if="houseDeals.length > 0" class="deals-container">
          <!-- 거래 정보 표시 -->
          <MapItem
            v-for="deal in houseDeals"
            :key="deal.no"
            :item="deal"
            :complex="complex"
            @select-item="handleItemClick(deal)"
          />
        </div>
        <div v-else class="no-data">
          <p>조건에 맞는 거래 정보가 없습니다.</p>
        </div>

        <!-- Trigger for Intersection Observer -->
        <div v-show="houseDeals.length > 0" ref="loadMoreTrigger" class="load-more-trigger">
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
import MapItem from '@/components/Map/Bookmark/AptMapItem.vue'
import useUserStore from '@/stores/user-store' // Example: your Pinia store for user info

// 디바운스 유틸리티 함수
const debounce = (func, delay) => {
  let timer
  return function (...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      func.apply(this, args)
    }, delay)
  }
}

export default {
  name: 'AptInfoPanel',
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
      houseDeals: [],
      isLoading: false,
      cursorId: null,
      observer: null,
      hasMore: true,
      observerInitialized: false,
      username: null, // 사용자 이름 저장
    }
  },
  watch: {
    complex: {
      immediate: true,
      handler(newComplex) {
        if (newComplex) {
          this.resetData()
          this.fetchHouseDeals()
        }
      },
    },
    houseDeals: {
      handler() {
        this.$nextTick(() => {
          if (!this.observerInitialized && this.houseDeals.length > 0) {
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

    if (this.username) {
      await this.fetchHouseDeals() // 북마크 데이터를 먼저 로드
    }

    if (this.houseDeals.length > 0) {
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
      this.houseDeals = []
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

      const debouncedCallback = debounce((entries) => {
        const target = entries[0]
        if (target.isIntersecting && !this.isLoading && this.hasMore) {
          this.fetchHouseDeals(this.cursorId)
        }
      }, 300)

      this.observer = new IntersectionObserver(debouncedCallback, {
        root: this.$refs.transactionSection,
        threshold: 0.1,
        rootMargin: '100px',
      })

      this.observer.observe(this.$refs.loadMoreTrigger)
      this.observerInitialized = true
      console.log('Observer initialized and attached to loadMoreTrigger')
    },
    updateMarkers: debounce(function () {
      if (!this.mapInstance) return

      console.log('!!!!! Markers cleared and re-added')
      const markers = this.houseDeals.map((deal) => {
        const markerPosition = new kakao.maps.LatLng(deal.latitude, deal.longitude)
        const marker = new kakao.maps.Marker({
          position: markerPosition,
          map: this.mapInstance, // 지도에 바로 추가
        })

        // 마커 클릭 이벤트 등록
        kakao.maps.event.addListener(marker, 'click', () => {
          this.handleItemClick(deal) // 아이템 클릭 이벤트 호출
        })

        return marker
      })

      return markers
    }, 300),
    async fetchHouseDeals(cursorId = null, size = 5) {
      if (this.isLoading || !this.hasMore) return

      this.isLoading = true
      try {
        const url = `http://localhost:8080/aladin/bookmark/deal/${this.username}`

        const response = await axios.get(url)
        const data = response.data
        console.log('=====>', data)
        if (data.success) {
          const bookmarks = data.data
          this.houseDeals = bookmarks.map((bookmark) => ({
            ...bookmark,
            no: bookmark.dealNo,
            latitude: parseFloat(bookmark.latitude), // 좌표 변환
            longitude: parseFloat(bookmark.longitude),
          })) // `no` 필드 매핑
          console.log('~~~~~~~~~~~~~~>', this.houseDeals)
          this.cursorId =
            this.houseDeals.length > 0 ? this.houseDeals[this.houseDeals.length - 1].no : null
        } else {
          console.error('Error:', data.message)
          this.hasMore = false
        }
      } catch (error) {
        console.error('Error fetching house deals:', error.message)
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
