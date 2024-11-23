<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal" :style="{ top: `${modalPosition.top}px`, left: `${modalPosition.left}px` }">
      <h2 class="modal-title">거래유형</h2>
      <p class="modal-subtitle">중복선택 가능</p>

      <!-- 거래유형 버튼 -->
      <div class="transaction-type">
        <button
          v-for="type in transactionTypes"
          :key="type"
          :class="{ active: selectedTransactionTypes.includes(type) }"
          @click="toggleTransactionType(type)"
        >
          {{ type }}
        </button>
      </div>

      <h2 class="modal-title">가격</h2>

      <!-- 매매 슬라이더 -->
      <div class="slider-container">
        <p>
          관리비 <span class="limit-label">{{ maintenanceFeeLabel }}</span>
        </p>
        <Slider
          v-model="maintenanceFeeRange"
          :min="0"
          :max="5"
          :formatter="formatMaintenanceFee"
          class="custom-slider"
          @change="updateMaintenanceFee"
        ></Slider>
      </div>

      <!-- 보증금 슬라이더 -->
      <div class="slider-container">
        <p>
          보증금 <span class="limit-label">{{ depositLabel }}</span>
        </p>
        <Slider
          v-model="depositRange.value"
          :min="0"
          :max="10"
          :formatter="formatPrice"
          class="custom-slider"
          @change="updateDeposit"
        ></Slider>
      </div>

      <!-- 월세 슬라이더 -->
      <div class="slider-container">
        <p>
          월세 <span class="limit-label">{{ rentLabel }}</span>
        </p>
        <Slider
          v-model="rentRange.value"
          :min="0"
          :max="10"
          :formatter="formatPrice"
          class="custom-slider"
          @change="updateRent"
        ></Slider>
      </div>
    </div>
  </div>
</template>

<script>
import Slider from '@vueform/slider'

export default {
  components: { Slider },
  props: {
    buttonRef: {
      type: Object,
      required: true,
    },
    initialTransactionTypes: {
      type: Array,
      default: () => [],
    },
    initialMaintenanceFeeRange: {
      // 새로운 슬라이더 값
      type: Array,
      default: () => [0, 5],
    },
    initialDepositRange: {
      type: Array,
      default: () => [0, 10],
    },
    initialRentRange: {
      type: Array,
      default: () => [0, 10],
    },
  },
  data() {
    return {
      transactionTypes: ['매매', '월당', '주당'],
      selectedTransactionTypes: [...this.initialTransactionTypes],
      maintenanceFeeRange: [...this.initialMaintenanceFeeRange],
      depositRange: { value: [...this.initialDepositRange] },
      rentRange: { value: [...this.initialRentRange] },
      modalPosition: { top: 0, left: 0 },
    }
  },
  computed: {
    depositLabel() {
      const [min, max] = this.depositRange.value
      if (max === 10) return `${this.formatPrice(min)} ~ 무제한`
      return `${this.formatPrice(min)} ~ ${this.formatPrice(max)}`
    },
    rentLabel() {
      const [min, max] = this.rentRange.value
      if (max === 10) return `${this.formatPrice(min)} ~ 무제한`
      return `${this.formatPrice(min)} ~ ${this.formatPrice(max)}`
    },
    maintenanceFeeLabel() {
      const [start, end] = this.maintenanceFeeRange
      const scale = 5 // 관리비 범위 (단위: 5만 원)
      if (start === 0 && end === 5) return '전체'
      if (start === 0) return `~${end * scale}만 원`
      if (end === 5) return `${start * scale}만 원~`
      return `${start * scale}만 원~${end * scale}만 원`
    },
  },
  mounted() {
    this.calculateModalPosition()
  },
  methods: {
    formatPrice(value) {
      return value === 0 ? '최소' : value === 10 ? '최대' : `${value * 100}만 원`
    },
    toggleTransactionType(type) {
      if (this.selectedTransactionTypes.includes(type)) {
        this.selectedTransactionTypes = this.selectedTransactionTypes.filter((t) => t !== type)
      } else {
        this.selectedTransactionTypes.push(type)
      }
      this.$emit('update-transaction-types', this.selectedTransactionTypes)
    },
    formatMaintenanceFee(value) {
      return `${value * 5}만 원` // 관리비 범위 단위
    },
    updateMaintenanceFee() {
      this.$emit('update-maintenance-fee-range', this.maintenanceFeeRange)
    },
    updateDeposit() {
      this.$emit('update-deposit-range', this.depositRange.value)
    },
    updateRent() {
      this.$emit('update-rent-range', this.rentRange.value)
    },
    closeModal() {
      this.$emit('close')
    },
    calculateModalPosition() {
      const buttonRect = this.buttonRef.getBoundingClientRect()
      this.modalPosition = {
        top: buttonRect.bottom + window.scrollY + 8,
        left: buttonRect.left + window.scrollX,
      }
    },
  },
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 999;
  background: transparent;
}

.modal {
  position: absolute;
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  width: 300px;
}

.modal-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
}

.modal-subtitle {
  font-size: 14px;
  color: gray;
  margin-bottom: 16px;
}

.transaction-type button {
  background-color: #f0f0f0;
  border: none;
  padding: 8px 12px;
  margin-right: 8px;
  border-radius: 4px;
  cursor: pointer;
}

.transaction-type button.active {
  background-color: #007bff;
  color: white;
}

.slider-container {
  margin-bottom: 16px;
}

.limit-label {
  font-weight: bold;
}

.custom-slider {
  margin-top: 8px;
}
</style>
