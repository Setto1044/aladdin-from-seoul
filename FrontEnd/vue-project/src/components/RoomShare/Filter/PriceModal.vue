<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal" :style="{ top: `${modalPosition.top}px`, left: `${modalPosition.left}px` }">
      <h2 class="modal-title">가격</h2>

      <!-- 매매 슬라이더 -->
      <div class="slider-container">
        <p>
          가격 <span class="limit-label">{{ maintenanceFeeLabel }}</span>
        </p>
        <Slider
          v-model="internalPriceRange"
          :min="0"
          :max="10"
          :formatter="formatPrice"
          class="custom-slider"
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
    priceRange: {
      type: Array,
      default: () => [0, 10],
    },
  },
  data() {
    return {
      internalPriceRange: [...this.priceRange], // 슬라이더 초기값
      modalPosition: { top: 0, left: 0 }, // 모달 위치
    }
  },
  computed: {
    priceLabel() {
      const [start, end] = this.internalPriceRange
      const scale = 5 // 관리비 범위 (단위: 5만 원)

      if (start === 0 && end === 5) return '전체'
      if (start === 0) return `~${end * scale}만 원`
      if (end === 5) return `${start * scale}만 원~`
      return `${start * scale}만 원~${end * scale}만 원`
    },
  },
  watch: {
    internalPriceRange: {
      handler(newVal) {
        // 상위 컴포넌트에 실시간 값 반영
        this.$emit('update:priceRange', newVal)
      },
      deep: true, // 배열 값의 변경 감지
    },
  },
  mounted() {
    this.calculateModalPosition()
  },
  methods: {
    formatPrice(value) {
      return value === 0 ? '최소' : value === 10 ? '최대' : `${value * 100}만 원`
    },
    updatePrice() {
      this.$emit('update:priceRange', this.internalPriceRange)
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
