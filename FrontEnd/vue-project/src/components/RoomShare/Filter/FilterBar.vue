<!--
  1.
    - 거래 금액
    - 전용 면적
    - 사용승인일
  
  2.
    - 월/주단위
    - 거래 금액
    - 전용 면적
    - 사용승인일
    - 태그
-->

<template>
  <div class="filter-bar">
    <div class="filter-option">
      <button ref="priceButton" @click="toggleModal('price')" class="dropdown-button">
        {{ priceButtonLabel }}
        <span class="arrow" :class="{ rotated: activeModal == 'price' }"></span>
      </button>
      <PriceModal
        v-if="activeModal === 'price'"
        v-model:priceRange="pricevalue"
        :buttonRef="priceButtonRef"
        @update="updatePrice"
        @close="closeModal"
      />
    </div>

    <div class="filter-option">
      <button ref="roomSizeButton" @click="toggleModal('roomSize')" class="dropdown-button">
        {{ roomSizeLabel }}
        <span class="arrow" :class="{ rotated: activeModal == 'roomSize' }"></span>
      </button>
      <room-size-modal
        v-if="activeModal === 'roomSize'"
        v-model:roomSizeRange="roomsizevalue"
        :buttonRef="roomSizeButtonRef"
        @update="updateRoomSize"
        @close="closeModal"
      />
    </div>

    <div class="filter-option">
      <button ref="usedDateButton" @click="toggleModal('usedDate')" class="dropdown-button">
        {{ usedDateLabel }}
        <span class="arrow" :class="{ rotated: activeModal === 'usedDate' }"></span>
      </button>
      <DateRangeModal
        v-if="activeModal === 'usedDate'"
        :buttonRef="usedDateButtonRef"
        :startDate="selectedStartDate"
        :endDate="selectedEndDate"
        @update="updateDateRange"
        @close="closeModal"
      />
    </div>

    <div class="filter-option">
      <button ref="hashtagButton" @click="toggleModal('additionalFilters')" class="dropdown-button">
        {{ hashtagsLabel }}
        <span class="arrow" :class="{ rotated: activeModal == 'additionalFilters' }"></span>
      </button>
      <HashtagModal
        v-if="activeModal === 'additionalFilters'"
        :buttonRef="$refs.hashtagButton"
        :initialHashtags="hashtags"
        @update="updateHashtags"
        @close="closeModal"
      />
    </div>

    <form v-if="$route.meta.showSearchBar" class="header-search-form">
      <input
        type="text"
        id="search-input"
        class="header-search-input"
        placeholder="해시태그 검색하기"
        aria-label="Search in site"
      />
    </form>

    <button type="button" @click="search" class="header-search-button" aria-label="Submit search">
      <img
        loading="lazy"
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/ff75430b99a5dc1ef509cf04ee77ca85091d2f5a3a9e255ba3ad5b4f13bcf5f9?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
        class="header-search-icon"
        alt="Search"
      />
    </button>
  </div>
</template>

<script>
import PriceModal from '@/components/RoomShare/Filter/PriceModal.vue'
import RoomSizeModal from '@/components/RoomShare/Filter/RoomSizeModal.vue'
import DateRangeModal from '@/components/RoomShare/Filter/DateRangeModal.vue'
import HashtagModal from '@/components/RoomShare/Filter/HashtagModal.vue'
import axios from 'axios' // Axios 사용

export default {
  components: {
    PriceModal,
    RoomSizeModal,
    DateRangeModal,
    HashtagModal,
  },
  data() {
    return {
      hashtags: [], // 태그 리스트
      pricevalue: [0, 10],
      rentRange: [0, 10],
      roomsizevalue: [0, 6],
      datevalue: [null, null],
      selectedApprovalDate: '전체',
      activeModal: null,
      selectedStartDate: '', // 선택된 시작 날짜
      selectedEndDate: '', // 선택된 종료 날짜
      // 필터 변경 여부를 추적하는 플래그들 추가
      isPriceModified: false,
      isRoomSizeModified: false,
    }
  },
  computed: {
    priceButtonRef() {
      return this.$refs.priceButton
    },
    hashtagsLabel() {
      if (this.hashtags.length === 0) return '태그 선택'
      return this.hashtags.join(', ') // 태그 라벨 표시
    },
    priceButtonLabel() {
      if (!this.isPriceModified) return '가격'
      const [start, end] = this.pricevalue
      return `${start}원~${end}원`
    },
    roomSizeButtonRef() {
      return this.$refs.roomSizeButton
    },
    approvalDateButtonRef() {
      return this.$refs.approvalDateButton
    },

    roomSizeLabel() {
      if (!this.isRoomSizeModified) return '방 크기'
      const [start, end] = this.roomsizevalue
      const scale = 10

      const startValue = start * scale
      const endValue = end * scale

      if (start === 0 && end === 6) return '전체'
      if (start === 0) return `~${endValue}평`
      if (end === 6) return `${startValue}평~`
      return `${startValue}평~${endValue}평`
    },
    usedDateButtonRef() {
      return this.$refs.usedDateButton
    },
    usedDateLabel() {
      // 시작 날짜와 종료 날짜 라벨 표시
      if (!this.selectedStartDate && !this.selectedEndDate) {
        return '기간'
      }
      if (this.selectedStartDate && this.selectedEndDate) {
        return `${this.selectedStartDate} ~ ${this.selectedEndDate}`
      }
      return this.selectedStartDate || this.selectedEndDate || '기간'
    },
  },
  methods: {
    toggleModal(modalType) {
      this.activeModal = this.activeModal === modalType ? null : modalType
    },
    closeModal() {
      this.activeModal = null
    },
    updateRoomSize({ minSize, maxSize }) {
      this.roomsizevalue[0] = minSize // 태그 업데이트
      this.roomsizevalue[1] = maxSize
    },
    updatePrice({ minPrice, maxPrice }) {
      this.pricevalue[0] = minPrice // 태그 업데이트
      this.pricevalue[1] = maxPrice
    },
    updateHashtags(newHashtags) {
      this.hashtags = newHashtags // 태그 업데이트
    },
    updateDateRange({ startDate, endDate }) {
      // DateRangeModal에서 전달된 날짜 데이터를 상위 컴포넌트의 데이터에 반영
      this.selectedStartDate = startDate
      this.selectedEndDate = endDate
    },
    resetFilters() {
      this.hashtags = []
      this.pricevalue = [0, 10]
      this.rentRange = [0, 10]
      this.roomsizevalue = [0, 6]
      this.datevalue = [null, null]
      this.selectedApprovalDate = '전체'
      this.selectedStartDate = ''
      this.selectedEndDate = ''
      // 필터 수정 플래그 초기화
      this.isPriceModified = false
      this.isRoomSizeModified = false
    },
    search() {
      // 검색 조건 생성
      const filters = {
        hashtags: this.hashtags.length > 0 ? this.hashtags.join(',') : null,
        rentFrom: this.selectedStartDate || null,
        rentTo: this.selectedEndDate || null,
        // 필터가 수정된 경우에만 값을 포함
        priceMin: this.isPriceModified ? this.pricevalue[0] : null,
        priceMax: this.isPriceModified ? this.pricevalue[1] : null,
        sizeMin: this.isRoomSizeModified ? this.roomsizevalue[0] * 10 : null,
        sizeMax: this.isRoomSizeModified ? this.roomsizevalue[1] * 10 : null,
      }
      console.log(filters)

      // null이나 undefined 값을 제외하고 쿼리 파라미터 생성
      const queryParams = Object.entries(filters)
        .filter(([_, value]) => value !== null && value !== undefined)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&')

      // 쿼리 파라미터가 비어있는 경우 전체 조회
      const url = queryParams
        ? `http://localhost:8080/aladin/boards/filter?${queryParams}`
        : 'http://localhost:8080/aladin/boards/filter'

      axios
        .get(url)
        .then((response) => {
          console.log('검색 결과:', response.data)
        })
        .catch((error) => {
          console.error('검색 요청 실패:', error)
        })
    },
  },
}
</script>

<style scoped>
/* 필터바 컨테이너 */
.filter-bar {
  display: flex;
  gap: 16px;
  align-items: center;
  padding: 8px 16px;
  background-color: #f9f9f9;
}

/* 드롭다운 버튼 */
.dropdown {
  position: relative;
}

.dropdown-button {
  padding: 8px 16px;
  font-size: 14px;
  color: #333;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 140px;
}

.dropdown-button:hover {
  background-color: #f0f0f0;
  border-color: #bbb;
}

/* 화살표 기본 스타일 */
.arrow {
  margin-left: 8px;
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-top: 6px solid #333; /* 기본: 아래 방향 */
  transition: transform 0.3s ease; /* 애니메이션 효과 */
}

/* 화살표 회전 */
.arrow.rotated {
  transform: rotate(180deg); /* 위 방향 */
}

.header-search-form {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 5px;
  overflow: hidden;
}

.header-search-input {
  border: none;
  padding: 8px 12px;
  font-size: 0.9rem;
  outline: none;
  width: 200px;
}

.header-search-button {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s ease;
}

.header-search-button:hover {
  background-color: #f4f4f4;
}

.header-search-icon {
  width: 20px; /* 검색 아이콘 크기 */
  height: 20px;
}
</style>
