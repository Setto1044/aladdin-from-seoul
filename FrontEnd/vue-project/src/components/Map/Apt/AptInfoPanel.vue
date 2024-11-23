<template>
  <div class="real-estate-detail">
    <!-- 단지 정보 섹션 -->
    <section class="complex-info">
      <h1 class="complex-title">{{ complex.aptName }}</h1>
      <p class="complex-summary">
        {{ complex.buildYear }} / {{ complex.minSize }}㎡ ~ {{ complex.maxSize }}㎡
      </p>
      <p class="complex-summary">
        {{ complex.sidoName }} {{ complex.gugunName }} {{ complex.dongName }} {{ complex.jibun }}
      </p>
      <div class="price-info">
        <div class="recent-transaction">
          최근 매매 실거래가: <span class="price">{{ complex.latestDealAmount }}만 원</span>
        </div>
        <div class="expected-prices">
          <p>매매가: {{ complex.minDealAmount }}만 원 ~ {{ complex.maxDealAmount }}만 원</p>
        </div>
      </div>
    </section>

    <!-- 매물 리스트 섹션 -->
    <h3 class="section-title">거래 정보</h3>
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
import MapItem from '@/components/Map/Apt/MapItem.vue'

export default {
  name: 'AptInfoPanel',
  components: {
    MapItem,
  },
  props: {
    complex: {
      type: Object,
      required: true,
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
    }
  },
  watch: {
    complex: {
      immediate: true,
      handler(newComplex) {
        if (newComplex && newComplex.aptSeq) {
          this.resetData()
          this.fetchHouseDeals(newComplex.aptSeq)
        }
      },
    },
    houseDeals: {
      handler() {
        this.$nextTick(() => {
          if (!this.observerInitialized && this.houseDeals.length > 0) {
            this.initializeObserver()
          }
        })
      },
    },
  },
  mounted() {
    // 초기 데이터가 있을 경우를 대비한 초기화
    if (this.houseDeals.length > 0) {
      this.$nextTick(() => {
        this.initializeObserver()
      })
    }
  },
  beforeDestroy() {
    this.cleanupObserver()
  },
  methods: {
    resetData() {
      this.cleanupObserver()
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
    initializeObserver() {
      // observer가 이미 존재하면 초기화하지 않음
      if (this.observerInitialized || !this.$refs.loadMoreTrigger) {
        return
      }

      this.observer = new IntersectionObserver(
        (entries) => {
          const target = entries[0]
          if (target.isIntersecting && !this.isLoading && this.hasMore && this.complex.aptSeq) {
            this.fetchHouseDeals(this.complex.aptSeq, this.cursorId)
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
      console.log('Observer initialized and attached to loadMoreTrigger')
    },
    async fetchHouseDeals(aptSeq, cursorId = null, size = 5) {
      if (this.isLoading || !this.hasMore) return

      this.isLoading = true
      try {
        const url =
          `http://localhost:8080/aladin/house/deals/${aptSeq}?size=${size}` +
          (cursorId ? `&cursorId=${cursorId}` : '')

        const response = await axios.get(url)
        const data = response.data

        if (data.success) {
          const newDeals = data.data.housedeals
          if (newDeals.length > 0) {
            this.houseDeals = [...this.houseDeals, ...newDeals]
            this.cursorId = newDeals[newDeals.length - 1].no
          }
          this.hasMore = newDeals.length === size
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
