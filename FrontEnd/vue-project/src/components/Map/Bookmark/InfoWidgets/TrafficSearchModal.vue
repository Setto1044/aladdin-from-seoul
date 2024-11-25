<template>
  <div v-if="showModal" class="modal-overlay">
    <div class="modal-content">
      <div class="title">주소 검색</div>
      <div class="address-input">
        <input type="text" v-model="currentAddress" placeholder="Type your address..." />
        <button @click="searchAddress">검색</button>
      </div>

      <!-- 검색 결과 -->
      <div class="results">
        <div
          v-for="(result, index) in results"
          :key="result.id"
          class="result-item"
          @click="confirmAddress(result)"
        >
          <p>{{ result.place_name }}</p>
          <p>{{ result.road_address_name || result.address_name }}</p>
        </div>
      </div>

      <!-- 검색 결과가 없을 때 -->
      <div v-if="noResults" class="no-results">검색 결과가 없습니다.</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    showModal: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['close', 'select'],
  data() {
    return {
      currentAddress: '',
      results: [],
      noResults: false,
      isLoading: false, // 데이터 로딩 중 상태
    }
  },
  methods: {
    async searchAddress(reset = true) {
      if (!this.currentAddress) {
        alert('주소를 입력해주세요!')
        return
      }

      if (reset) {
        this.results = []
        this.isEnd = false
      }

      this.fetchMoreResults()
    },
    async fetchMoreResults() {
      if (this.isLoading || this.isEnd) return

      this.isLoading = true
      try {
        const response = await fetch(
          `https://dapi.kakao.com/v2/local/search/keyword.json?query=${encodeURIComponent(
            this.currentAddress,
          )}&page=${this.page}`,
          {
            headers: {
              Authorization: `KakaoAK ${import.meta.env.VITE_KakaoAK}`,
            },
          },
        )
        const data = await response.json()

        this.noResults = data.meta.total_count === 0

        this.results.push(...data.documents)
      } catch (error) {
        console.error('검색 오류:', error)
        alert('검색 중 오류가 발생했습니다.')
      } finally {
        this.isLoading = false
      }
    },
    confirmAddress(selected) {
      const addressData = {
        name: selected.place_name,
        roadAddress: selected.road_address_name,
        jibunAddress: selected.address_name,
        x: selected.x,
        y: selected.y,
      }
      this.$emit('select', addressData)
      this.closeModal()
    },
    closeModal() {
      this.results = []
      this.noResults = false
      this.currentAddress = ''
      this.isLoading = false
      this.$emit('close')
    },
  },
}
</script>

<style scoped>
.modal-overlay {
  position: absolute; /* 상위 컴포넌트를 기준으로 배치 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgb(218, 218, 218);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10; /* 상위 요소의 다른 컨텐츠보다 위로 배치 */
}
.modal-content {
  background: white;
  border-radius: 8px;
  width: 300px; /* 너비 줄이기 */
  max-width: 90%;
  text-align: center;
  box-shadow: 0 2px 3px 0 rgba(34, 36, 38, 0.15);
  padding: 10px;
  position: relative;
  overflow: auto; /* 콘텐츠가 넘칠 경우 스크롤 */
  max-height: 80vh; /* 최대 높이 제한 */
}
.address-input {
  margin-bottom: 10px; /* 여백 줄이기 */
}
.address-input input {
  width: calc(100% - 60px); /* 버튼 크기에 맞춰 동적 계산 */
  padding: 8px;
  margin-right: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.address-input button {
  padding: 8px 12px; /* 크기 줄이기 */
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.address-input button:hover {
  background: #0056b3;
}
.results {
  text-align: left;
  margin-bottom: 10px;
  padding: 0;
  list-style-type: none;
  max-height: 200px;
  overflow-y: auto; /* 세로 스크롤 활성화 */
}
.result-item {
  padding: 8px;
  border-bottom: 1px solid #ddd; /* 리스트 구분선 */
  cursor: pointer;
  transition:
    background 0.3s,
    transform 0.2s; /* hover 효과에 부드러운 전환 추가 */
}
.result-item:last-child {
  border-bottom: none; /* 마지막 항목 구분선 제거 */
}
.result-item:hover {
  background: #f9f9f9; /* 배경색 변경 */
  transform: scale(1.02); /* 약간 확대 */
}
.result-item p {
  margin: 0; /* 텍스트 여백 제거 */
  line-height: 1.4; /* 가독성 향상 */
}
.result-item p:first-child {
  font-weight: bold; /* 이름을 강조 */
  font-size: 13px; /* 이름 크기 */
  color: #333; /* 이름 색상 */
}
.result-item p:last-child {
  font-size: 10px; /* 주소 크기 */
  color: #666; /* 주소 색상 */
}
.no-results {
  color: red;
  margin-bottom: 10px; /* 여백 줄이기 */
}
.actions {
  display: flex;
  justify-content: center;
}
.actions button {
  padding: 8px 12px; /* 크기 줄이기 */
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.actions button:hover {
  background: #c82333;
}
</style>
