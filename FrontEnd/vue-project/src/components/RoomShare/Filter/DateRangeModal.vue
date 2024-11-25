<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal" :style="{ top: `${modalPosition.top}px`, left: `${modalPosition.left}px` }">
      <h2 class="title">날짜 범위 선택</h2>
      <p class="subtitle">시작 날짜와 종료 날짜를 선택하세요</p>
      <!-- 현재 범위 표시 -->
      <div class="date-range-label">{{ dateRangeLabel }}</div>
      <div class="date-picker">
        <label for="start-date">시작 날짜:</label>
        <input type="date" id="start-date" v-model="internalStartDate" />
      </div>
      <div class="date-picker">
        <label for="end-date">종료 날짜:</label>
        <input type="date" id="end-date" v-model="internalEndDate" />
      </div>
      <div class="actions">
        <button @click="confirmSelection">확인</button>
        <button @click="resetDateRange">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    startDate: {
      type: String,
      default: '', // 초기 시작 날짜
    },
    endDate: {
      type: String,
      default: '', // 초기 종료 날짜
    },
    buttonRef: {
      type: Object,
      required: true, // 버튼 참조 필수
    },
  },
  data() {
    return {
      internalStartDate: this.startDate, // 선택된 시작 날짜
      internalEndDate: this.endDate, // 선택된 종료 날짜
      modalPosition: { top: 0, left: 0 }, // 모달 위치 저장
    }
  },
  computed: {
    dateRangeLabel() {
      if (!this.internalStartDate && !this.internalEndDate) {
        return '전체 날짜'
      }
      if (!this.internalStartDate) {
        return `~${this.internalEndDate}`
      }
      if (!this.internalEndDate) {
        return `${this.internalStartDate}~`
      }
      return `${this.internalStartDate} ~ ${this.internalEndDate}`
    },
  },
  mounted() {
    this.calculateModalPosition()
  },
  methods: {
    confirmSelection() {
      this.$emit('update', {
        startDate: this.internalStartDate,
        endDate: this.internalEndDate,
      })
      this.closeModal()
    },
    resetDateRange() {
      this.internalStartDate = this.startDate
      this.internalEndDate = this.endDate
      this.closeModal()
    },
    closeModal() {
      this.$emit('close')
    },
    calculateModalPosition() {
      const buttonRect = this.buttonRef.getBoundingClientRect()
      this.modalPosition = {
        top: buttonRect.bottom + window.scrollY + 8, // 버튼 아래 배치
        left: buttonRect.left + window.scrollX, // 버튼의 왼쪽 정렬
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
  background: transparent; /* 오버레이 배경 제거 */
}

.modal {
  position: absolute;
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  width: 250px; /* 모달 너비 */
}

.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

.date-picker {
  margin-bottom: 12px;
}

.date-picker label {
  display: block;
  font-size: 14px;
  margin-bottom: 4px;
}

.actions {
  display: flex;
  justify-content: space-between;
}

button {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #f0f0f0;
}

button:active {
  background-color: #e0e0e0;
}

button:first-of-type {
  background-color: #007bff;
  color: white;
}

button:first-of-type:hover {
  background-color: #0056b3;
}
</style>
