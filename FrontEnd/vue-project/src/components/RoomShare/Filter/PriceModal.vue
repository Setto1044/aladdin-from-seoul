<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal" :style="{ top: `${modalPosition.top}px`, left: `${modalPosition.left}px` }">
      <h2 class="modal-title">가격</h2>

      <!-- 매매 슬라이더 -->
      <div class="slider-container">
        <p>가격: {{ formattedPrice }}</p>
        <Slider
          v-model="internalPriceRange"
          :min="0"
          :max="100"
          range
          :tooltipFormatter="(value) => `${sliderToRent(value)}만 원`"
          class="custom-slider"
          @change="updatePrice"
        ></Slider>

        <!-- 숫자 표시 -->
        <div class="slider-values">
          <span>{{ sliderToRent(internalPriceRange[0]) }}만 원</span> ~
          <span>{{ sliderToRent(internalPriceRange[1]) }}만 원</span>
        </div>
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
      default: () => [0, 350], // 초기 최소/최대 값
    },
  },
  data() {
    return {
      internalPriceRange: [...this.priceRange], // 슬라이더의 초기값 (범위)
      modalPosition: { top: 0, left: 0 }, // 모달 위치
    }
  },
  computed: {
    formattedPrice() {
      const [min, max] = this.internalPriceRange.map(this.sliderToRent)
      return `${min}만 원 ~ ${max}만 원`
    },
  },
  watch: {
    internalPriceRange: {
      handler(newVal) {
        // 부모 컴포넌트로 업데이트 값 전달
        const convertedRange = newVal.map(this.sliderToRent)
        this.$emit('update', convertedRange)
      },
      deep: true,
    },
  },
  mounted() {
    this.calculateModalPosition()
  },
  methods: {
    sliderToRent(value) {
      // 슬라이더 값 → 금액
      if (value <= 50) {
        return value * 1 // 0~50 구간
      } else if (value <= 80) {
        return 50 + (value - 50) * 2 // 50~150 구간
      } else {
        return 150 + (value - 80) * 5 // 150~350 구간
      }
    },
    rentToSlider(price) {
      // 금액 → 슬라이더 값
      if (price <= 50) {
        return price * 1 // 0~50 구간
      } else if (price <= 150) {
        return 50 + (price - 50) / 2 // 50~150 구간
      } else {
        return 80 + (price - 150) / 5 // 150~350 구간
      }
    },
    formatPrice(value) {
      const price = this.sliderToRent(value)
      return `${price}만 원`
    },
    updatePrice() {
      this.$emit('update', {
        minPrice: this.internalPriceRange[0],
        maxPrice: this.internalPriceRange[1],
      })
    },
    closeModal() {
      this.$emit('close')
    },
    calculateModalPosition() {
      const buttonRect = this.buttonRef.getBoundingClientRect()
      const modalWidth = 400 // 모달 너비
      const modalHeight = 200 // 모달 높이

      let top = buttonRect.bottom + window.scrollY + 8
      let left = buttonRect.left + window.scrollX

      // 화면 밖으로 벗어나는지 확인
      if (left + modalWidth > window.innerWidth) {
        left = window.innerWidth - modalWidth - 8 // 오른쪽 여백
      }
      if (top + modalHeight > window.innerHeight) {
        top = buttonRect.top + window.scrollY - modalHeight - 8 // 위쪽 배치
      }

      this.modalPosition = { top, left }
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
