<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal" :style="{ top: `${modalPosition.top}px`, left: `${modalPosition.left}px` }">
      <h2 class="title">방크기</h2>
      <p class="subtitle">매물유형별 기준면적</p>
      <!-- 현재 범위 표시 -->
      <div class="slider-label">{{ roomSizeLabel }}</div>
      <Slider
        v-model="internalRoomSizeRange"
        :min="0"
        :max="6"
        :formatter="formatRoomSize"
        class="custom-slider"
      ></Slider>
      <!-- 범위 구간 라벨 -->
      <div class="slider-ticks">
        <span class="slider-tick">10평 미만</span>
        <span class="slider-tick">30평대</span>
        <span class="slider-tick">60평 이상</span>
      </div>
    </div>
  </div>
</template>

<script>
import Slider from '@vueform/slider'

export default {
  components: { Slider },
  props: {
    roomSizeRange: {
      type: Array,
      default: () => [0, 6],
    },
    buttonRef: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      internalRoomSizeRange: [...this.roomSizeRange], // 슬라이더 초기값
      modalPosition: { top: 0, left: 0 }, // 모달 위치
    }
  },
  computed: {
    roomSizeLabel() {
      const [start, end] = this.internalRoomSizeRange
      const scale = 10

      const startValue = start * scale
      const endValue = end * scale

      if (start === 0 && end === 6) return '전체'
      if (start === 0) return `~${endValue}평`
      if (end === 6) return `${startValue}평~`
      return `${startValue}평~${endValue}평`
    },
  },
  watch: {
    internalRoomSizeRange: {
      handler(newVal) {
        // 상위 컴포넌트에 실시간 값 반영
        this.$emit('update:roomSizeRange', newVal)
      },
      deep: true, // 배열 값의 변경 감지
    },
  },
  mounted() {
    this.calculateModalPosition()
  },
  methods: {
    formatRoomSize(value) {
      const scale = 10
      if (value === 0) return '10평 미만'
      if (value === 6) return '60평 이상'
      return `${value * scale}평대`
    },
    updateRoomSize() {
      // 슬라이더 값을 상위 컴포넌트에 즉시 전달
      this.$emit('update:roomSizeRange', this.internalRoomSizeRange)
    },
    closeModal() {
      this.$emit('close')
      this.$emit('update:roomSizeRange', this.internalRoomSizeRange) // 마지막 값 반영
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
  width: 100vw;
  height: 100vh;
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

.title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 4px;
}

.subtitle {
  font-size: 14px;
  color: gray;
  margin-bottom: 16px;
}

.slider-label {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 12px;
}

.custom-slider {
  margin-bottom: 16px;
}

.slider-ticks {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: gray;
}

.slider-tick {
  text-align: center;
}
</style>
