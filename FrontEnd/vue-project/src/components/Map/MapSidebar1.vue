<template>
  <div v-if="isOpen" class="sidebar sidebar1">
    <div class="property-details-wrapper">
      <button class="close-button" @click="closeSidebar">X</button>

      <!-- 거래 정보 섹션 -->
      <section class="transaction-details">
        <h2 class="section-title">거래 정보</h2>
        <div v-if="isLoading && houseDeals.length === 0" class="loading-message">
          데이터를 불러오는 중입니다...
        </div>
        <div v-else-if="houseDeals.length > 0">
          <!-- 거래 정보 표시 -->
          <RealEstateDetailItem v-for="deal in houseDeals" :key="deal.no" :deal="deal" />

          <div ref="loadMoreTrigger" class="load-more-trigger">
            <p v-if="isLoading">로딩 중...</p>
          </div>
        </div>
        <div v-else>
          <p>조건에 맞는 거래 정보가 없습니다.</p>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import RealEstateDetailItem from '@/components/Map/RealEstateDetail.vue'
// import TransactionDetails from '@/components/TransactionDetails.vue'

export default {
  components: {
    RealEstateDetailItem,
    TransactionDetails,
  },
  props: ['isOpen', 'selectedMarker'],
  data() {
    return {
      houseDeals: [], // 전체 거래 데이터
      isLoading: false, // 로딩 상태 관리
      currentPage: 1, // 현재 페이지
      totalPages: null, // 총 페이지 수
      observer: null, // Intersection Observer
      observerAttached: false, // Observer 설정 상태
    }
  },
  watch: {
    selectedMarker: {
      immediate: true,
      handler(newMarker) {
        console.log('selectedMarker changed')
        if (newMarker && newMarker.aptSeq) {
          this.resetData()
          this.fetchHouseDeals(this.selectedMarker.aptSeq) // 초기화된 상태에서 첫 데이터 로드
        }
      },
    },
  },
  mounted() {
    this.setupIntersectionObserver()
  },
  beforeDestroy() {
    if (this.observer) this.observer.disconnect()
  },
  methods: {
    closeSidebar() {
      this.$emit('close1')
    },
    resetData() {
      this.houseDeals = []
      this.currentPage = 1
      this.totalPages = null
      this.isLoading = false
    },
    setupIntersectionObserver() {
      this.observer = new IntersectionObserver(
        ([entry]) => {
          if (entry.isIntersecting && !this.isLoading && this.cursorId) {
            this.fetchHouseDeals(this.selectedMarker.aptSeq, this.cursorId) // 커서를 이용하여 다음 데이터를 로드
          }
        },
        {
          root: null,
          threshold: 0.1,
        },
      )
      this.$nextTick(() => {
        if (this.$refs.loadMoreTrigger) {
          this.observer.observe(this.$refs.loadMoreTrigger)
        }
      })
    },
    async fetchHouseDeals(aptSeq, cursorId = null, size = 5) {
      if (this.isLoading) return // 이미 로딩 중이면 중단

      this.isLoading = true
      try {
        // API 호출 URL 구성
        const url =
          `http://localhost:8080/aladin/house/deals/${aptSeq}?size=${size}` +
          (cursorId ? `&cursorId=${cursorId}` : '')

        const response = await axios.get(url)
        const data = response.data

        if (data.success) {
          // 새 데이터를 기존 데이터에 추가
          this.houseDeals = [...this.houseDeals, ...data.data.housedeals]

          // 다음 요청에 사용할 커서 ID 업데이트
          this.cursorId = data.data.housedeals.length
            ? data.data.housedeals[data.data.housedeals.length - 1].no
            : null // 마지막 데이터의 `no`를 커서 ID로 사용
        } else {
          const errorMessage = data.message || '알 수 없는 오류가 발생했습니다.'
          console.error('Failed to fetch house deals:', errorMessage)
        }
      } catch (error) {
        console.error(
          'Error fetching house deals:',
          error.message || 'API 호출 중 문제가 발생했습니다.',
        )
      } finally {
        this.isLoading = false
      }
    },
  },
}
</script>

<style scoped>
/* Sidebar 스타일 */
.sidebar {
  position: relative;
  background: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 300px;
  height: 100%;
  overflow-y: auto;
  z-index: 1000;
}

/* Close 버튼 스타일 */
.close-button {
  position: absolute;
  top: 16px;
  right: 16px;
  background-color: #ff4d4f;
  color: #ffffff;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s ease;
}

.close-button:hover {
  background-color: #d9363e;
}

.loading-message {
  text-align: center;
  font-size: 16px;
  color: #666;
}

.load-more-trigger {
  text-align: center;
  padding: 10px;
  font-size: 14px;
  color: #999;
}
</style>
