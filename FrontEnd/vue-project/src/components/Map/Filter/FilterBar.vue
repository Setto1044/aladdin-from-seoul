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
      <button ref="transactionButton" @click="toggleModal('transaction')" class="dropdown-button">
        {{ transactionButtonLabel }}
        <span class="arrow" :class="{ rotated: activeModal == 'transaction' }"></span>
      </button>
      <transaction-modal
        v-if="activeModal === 'transaction'"
        :buttonRef="transactionButtonRef"
        :initialTransactionTypes="transactionTypes"
        :initialDepositRange="depositRange"
        :initialRentRange="rentRange"
        :initialMaintenanceFeeRange="maintenanceFeeRange"
        @close="closeModal"
        @update-transaction-types="updateTransactionTypes"
        @update-deposit-range="updateDepositRange"
        @update-rent-range="updateRentRange"
        @update-maintenance-fee-range="updateMaintenanceFeeRange"
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
        @close="closeModal"
      />
    </div>

    <div class="filter-option">
      <button ref="approvalDateButton" @click="toggleModal('approvalDate')" class="dropdown-button">
        {{ selectedApprovalDate || '사용승인일' }}
        <span class="arrow" :class="{ rotated: activeModal == 'approvalDate' }"></span>
      </button>
      <approval-date-modal
        v-if="activeModal === 'approvalDate'"
        :buttonRef="approvalDateButtonRef"
        :initialSelectedOption="selectedApprovalDate"
        @select="updateApprovalDate"
        @close="closeModal"
      />
    </div>

    <div class="filter-option">
      <button @click="toggleModal('additionalFilters')" class="dropdown-button">
        {{ selectedAdditionalFilters || '추가필터' }}
        <span class="arrow" :class="{ rotated: activeModal == 'additionalFilters' }"></span>
      </button>
      <additional-filters-modal
        v-if="activeModal === 'additionalFilters'"
        @close="closeModal"
        @select="updateOption('additionalFilters', $event)"
      />
    </div>

    <form
      v-if="$route.meta.showSearchBar"
      class="header-search-form"
      @submit.prevent="searchAptName"
    >
      <input
        type="text"
        id="search-input"
        class="header-search-input"
        placeholder="검색어를 입력하세요"
        aria-label="Search in site"
        v-model="query"
        @input="handleInput"
        autocomplete="off"
      />
      <button type="submit" class="header-search-button" aria-label="Submit search">
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/ff75430b99a5dc1ef509cf04ee77ca85091d2f5a3a9e255ba3ad5b4f13bcf5f9?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
          class="header-search-icon"
          alt="Search"
        />
      </button>
    </form>
    <ul class="autocomplete-results">
      <li v-for="(result, index) in results" :key="index" class="autocomplete-item">
        <button @click="selectResult(result.code, result.name)">
          {{ result.name }}
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
import TransactionModal from '@/components/Map/Filter/PriceAndTransactionModal.vue'
import RoomSizeModal from '@/components/Map/Filter/RoomSizeModal.vue'
import ApprovalDateModal from '@/components/Map/Filter/ApprovalDateModal.vue'
import AdditionalFiltersModal from '@/components/Map/Filter/AdditionalFiltersModal.vue'
import axios from 'axios'

function decrementLastNumber(str) {
  return str.replace(/(\d+)-(\d+)/, (match, part1, part2) => {
    const decremented = parseInt(part2) - 1
    return `${part1}-${decremented}`
  })
}

export default {
  components: {
    TransactionModal,
    RoomSizeModal,
    ApprovalDateModal,
    AdditionalFiltersModal,
  },
  data() {
    return {
      transactionTypes: [], // 선택된 거래유형 (e.g., ['매매', '월당'])
      maintenanceFeeRange: [0, 5], // 관리비 범위 (새로운 슬라이더)
      depositRange: [0, 10], // 보증금 범위
      rentRange: [0, 10], // 월세 범위
      roomsizevalue: [0, 6], // 초기 방크기 (전체)
      selectedApprovalDate: '전체', // 승인일 초기값
      activeModal: null,
      query: '',
      results: [],
      debounceTimer: null,
    }
  },
  computed: {
    transactionButtonRef() {
      return this.$refs.transactionButton
    },
    transactionButtonLabel() {
      // 거래유형 버튼의 라벨 표시
      if (this.transactionTypes.length === 0) return '거래유형'
      return this.transactionTypes.join(', ')
    },
    roomSizeButtonRef() {
      return this.$refs.roomSizeButton
    },
    approvalDateButtonRef() {
      return this.$refs.approvalDateButton
    },
    roomSizeLabel() {
      const [start, end] = this.roomsizevalue
      const scale = 10

      const startValue = start * scale
      const endValue = end * scale

      if (start === 0 && end === 6) return '전체'
      if (start === 0) return `~${endValue}평`
      if (end === 6) return `${startValue}평~`
      return `${startValue}평~${endValue}평`
    },
  },
  methods: {
    toggleModal(modalType) {
      this.activeModal = this.activeModal === modalType ? null : modalType
    },
    closeModal() {
      this.activeModal = null
    },
    updateMaintenanceFeeRange(range) {
      this.maintenanceFeeRange = range // 관리비 범위 업데이트
    },
    updateTransactionTypes(selectedTypes) {
      this.transactionTypes = selectedTypes // 거래유형 업데이트
    },
    updateDepositRange(range) {
      this.depositRange = range // 보증금 범위 업데이트
    },
    updateRentRange(range) {
      this.rentRange = range // 월세 범위 업데이트
    },
    resetFilters() {
      this.transactionTypes = []
      this.depositRange = [0, 10]
      this.rentRange = [0, 10]
    },
    updateApprovalDate(option) {
      this.selectedApprovalDate = option // 선택된 옵션 업데이트
      this.closeModal() // 모달 닫기
    },
    searchAptName() {
      this.$emit('search-action', this.query)
    },
    handleInput(event) {
      // input 이벤트만 사용하고 그 시점의 value를 사용
      const value = event.target.value
      if (this.debounceTimer) clearTimeout(this.debounceTimer)

      this.debounceTimer = setTimeout(() => {
        this.fetchResults(value)
      }, 300)
    },

    async fetchResults(value) {
      if (value.trim().length > 0) {
        try {
          console.log('Sending request with query:', value)

          const response = await fetch(
            `http://localhost:8080/aladin/trie/search?keyword=${encodeURIComponent(value.trim())}`,
          )

          if (!response.ok) {
            throw new Error('Network response was not ok')
          }

          const data = await response.json()

          if (data.success) {
            this.results = data.data.map((item) => {
              const [code, name] = item.split(', ')
              return { code, name }
            })
          } else {
            this.results = []
            console.error('Failed to fetch results:', data.message)
          }
        } catch (error) {
          console.error('Error fetching data:', error)
        }
      } else {
        this.results = []
      }
    },

    selectResult(code, name) {
      console.log(`Selected result: ${name} (Code: ${code})`)
      if (code.includes('-')) {
        // 하이픈이 있는 경우 로직
        console.log('Code contains a hyphen. Performing logic for hyphenated code.')
        // window.location.href = `http://localhost:8080/aladin/house/hyphen?code=${code}`
        this.fetchAptAndSearch(code, name)
      } else {
        // 하이픈이 없는 경우 로직

        // 1. 법정동코드를 통해 주소 알아내기
        // 2. 주소를 통해 lat, lng 알아내기
        // 2. center 이동
        this.fetchRegionAndSearch(code)
      }
    },
    async fetchAptAndSearch(code, name) {
      const newCode = decrementLastNumber(code)
      console.log(
        `http://localhost:8080/aladin/house/cards/aptname/${name}?cursorId=${newCode}&size=1`,
      )
      const houseResponse = await axios.get(
        `http://localhost:8080/aladin/house/cards/aptname/${name}?cursorId=${newCode}&size=1`,
      )
      if (houseResponse.data.success) {
        // 집 카드 데이터 전달
        const houseCard = houseResponse.data.data[0]
        console.log('라우터 이동 전 데이터 (searchHouseCard):', houseCard)

        this.$router.push({
          name: 'map',
          params: {
            searchHouseCard: JSON.stringify(houseCard), // 객체는 문자열로 변환
          },
        })
      } else {
        console.error('Failed fetchAptAndSearch')
      }
    },
    async fetchRegionAndSearch(code) {
      try {
        // 첫 번째 API 호출
        const regionResponse = await axios.get(
          `https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=${code}&is_ignore_zero=true`,
        )

        if (regionResponse.status != 200) {
          throw new Error('Region API response was not ok')
        }

        const regionData = await regionResponse.data
        if (!regionData.regcodes || regionData.regcodes.length === 0) {
          throw new Error('No region data found')
        }
        console.log(regionData)
        const regionName = regionData.regcodes[0].name
        console.log('Region name:', regionName)

        // 두 번째 API 호출
        const kakaoResponse = await axios.get(
          `https://dapi.kakao.com/v2/local/search/address.json`,
          {
            params: {
              query: regionName, // 주소 값
            },
            headers: {
              Authorization: `KakaoAK ${import.meta.env.VITE_KakaoAK}`, // Kakao API 키
            },
          },
        )

        if (kakaoResponse.status != 200) {
          throw new Error('Kakao API response was not ok')
        }

        const kakaoData = kakaoResponse.data
        console.log('Kakao API results:', kakaoData)

        // lat, lng 추출
        const lat = kakaoData.documents[0]?.y
        const lng = kakaoData.documents[0]?.x

        if (lat && lng) {
          // 위도/경도 데이터 전달
          const latLng = { latitude: lat, longitude: lng }
          console.log('라우터 이동 전 데이터 (searchLatLng):', latLng)

          this.$router.push({
            name: 'map',
            params: {
              searchLatLng: JSON.stringify(latLng), // 객체는 문자열로 변환
            },
          })
        } else {
          console.error('Failed to extract lat/lng')
          alert('좌표 데이터를 가져올 수 없습니다.')
        }
      } catch (error) {
        console.error('Error during region and Kakao search:', error)
        alert('지역 검색 중 오류가 발생했습니다.')
      }
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
  padding: 12px 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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
  position: relative; /* 부모 요소를 기준으로 자식의 absolute 위치 설정 */
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 5px;
  overflow: hidden;
  margin-left: auto; /* 검색창을 오른쪽으로 이동 */
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

.autocomplete-results {
  position: absolute; /* 부모 요소(header-search-form)를 기준으로 배치 */
  top: 100%; /* 검색창 바로 아래 */
  right: 13px; /* 검색창 내부의 오른쪽에 위치 */
  width: 240px; /* 결과창의 너비 */
  background: rgba(255, 255, 255, 0.897);
  border: 1px solid #ccc;
  border-radius: 0px;
  list-style: none;
  /* padding: 8px; */
  margin: 0;
  padding: 0; /* 내부 여백 제거 */
  max-height: 200px; /* 최대 높이 설정 */
  overflow-y: auto;
  z-index: 1000; /* 다른 요소보다 위에 표시 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
  list-style: none; /* 기본 목록 스타일 제거 */
}

.autocomplete-item {
  margin: 0; /* 개별 아이템 margin 제거 */
  padding: 0; /* padding 초기화 */
}

.autocomplete-item button {
  background: none;
  border: none;
  text-align: left;
  width: 100%;
  padding: 8px;
  cursor: pointer;
  font-size: 14px;
}

.autocomplete-item button:hover {
  background-color: #f0f0f0;
}
</style>
