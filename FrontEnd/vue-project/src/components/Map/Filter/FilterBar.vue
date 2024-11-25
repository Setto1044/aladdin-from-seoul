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
        placeholder="Search in site"
        aria-label="Search in site"
        v-model="newAptName"
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
  </div>
</template>

<script>
import TransactionModal from '@/components/Map/Filter/PriceAndTransactionModal.vue'
import RoomSizeModal from '@/components/Map/Filter/RoomSizeModal.vue'
import ApprovalDateModal from '@/components/Map/Filter/ApprovalDateModal.vue'
import AdditionalFiltersModal from '@/components/Map/Filter/AdditionalFiltersModal.vue'

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
      newAptName: '',
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
      this.$emit('search-action', this.newAptName)
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
