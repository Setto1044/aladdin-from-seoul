<template>
  <div class="real-estate-list">
    <h1 class="list-title">단지 정보</h1>
    <section class="complex-list">
      <!-- 단지 목록 출력 -->
      <div v-if="isLoading && complexes.length === 0" class="loading-message">
        데이터를 불러오는 중입니다...
      </div>
      <template v-else>
        <div v-if="complexes.length > 0" class="complex-container">
          <div
            v-for="complex in complexes"
            :key="complex.aptSeq"
            class="complex-card"
            @click="handleItemClick(complex)"
          >
            <h3 class="complex-name">{{ complex.aptName }}</h3>
            <p class="complex-summary">
              {{ complex.sidoName }} {{ complex.gugunName }} {{ complex.dongName }}
              {{ complex.jibun }} ({{ complex.buildYear }}년 준공)
            </p>
            <div class="price-info">
              <p>최근 매매 실거래가: {{ complex.latestDealAmount }}만 원</p>
            </div>
          </div>
        </div>
        <div v-else class="no-data">
          <p>조건에 맞는 단지 정보가 없습니다.</p>
        </div>

        <!-- 페이징 관련 메시지 -->
        <div v-show="complexes.length > 0" ref="loadMoreTrigger" class="load-more-trigger">
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

export default {
  name: 'AptInfoPanel',
  props: {
    query: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      complexes: [], // 단지 목록 저장
      isLoading: false,
      cursorId: null, // 페이징에 필요한 cursorId
      hasMore: true,
      observer: null,
      observerInitialized: false,
    }
  },
  watch: {
    query: {
      immediate: true, // 초기화 시에도 실행
      handler(newQuery) {
        if (newQuery) {
          console.log('으악')
          this.resetData() // 기존 데이터 초기화
          this.fetchComplexes(newQuery) // 새로운 검색어로 데이터 로드
        }
      },
    },
    complexes: {
      handler() {
        this.$nextTick(() => {
          if (!this.observerInitialized && this.complexes.length > 0) {
            this.initializeObserver()
          }
        })
      },
    },
  },
  mounted() {
    if (this.complexes.length > 0) {
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
      this.complexes = []
      this.cursorId = null
      this.hasMore = true
      this.observerInitialized = false
    },
    cleanupObserver() {
      if (this.observer) {
        this.observer.disconnect()
        this.observer = null
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
            this.fetchComplexes(this.query, this.cursorId)
          }
        },
        {
          root: null,
          threshold: 0.1,
          rootMargin: '100px',
        },
      )

      this.observer.observe(this.$refs.loadMoreTrigger)
      this.observerInitialized = true
      console.log('Observer initialized and attached to loadMoreTrigger')
    },
    async fetchComplexes(query, cursorId = null, size = 10) {
      if (this.isLoading || !this.hasMore) return

      this.isLoading = true
      try {
        console.log('젠장')
        const url =
          `http://localhost:8080/aladin/house/cards/aptname/${encodeURIComponent(query.trim())}?size=${size}` +
          (cursorId ? `&cursorId=${cursorId}` : '')

        const response = await axios.get(url)
        const data = response.data
        console.log('��장2', data)

        if (data.success) {
          const newComplexes = data.data
          if (newComplexes.length > 0) {
            this.complexes = [...this.complexes, ...newComplexes]
            this.cursorId = newComplexes[newComplexes.length - 1].aptSeq // 다음 요청의 cursorId 설정
          }
          this.hasMore = newComplexes.length === size
        } else {
          console.error('Error:', data.message)
          this.hasMore = false
        }
      } catch (error) {
        console.error('Error fetching complexes:', error.message)
        this.hasMore = false
      } finally {
        this.isLoading = false
      }
    },
    handleItemClick(item) {
      console.log('Clicked item:', item) // 디버깅용 로그
      this.$emit('name-item', item) // 상위 컴포넌트로 이벤트 전달
    },
  },
}
</script>

<style scoped>
/* Styles for better visibility */
.complex-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
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

.complex-summary {
  margin: 5px 0;
  font-size: 14px;
  color: #555;
}

.price-info {
  margin-top: 10px;
  font-size: 14px;
  color: #333;
}

.price {
  font-weight: bold;
  color: #007bff;
}
</style>
