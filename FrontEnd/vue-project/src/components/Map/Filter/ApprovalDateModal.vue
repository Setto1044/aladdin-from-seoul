<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal" :style="{ top: `${modalPosition.top}px`, left: `${modalPosition.left}px` }">
      <div class="title">사용승인일 선택</div>
      <ul>
        <li
          v-for="option in options"
          :key="option"
          :class="{ selected: option === selectedOption }"
          @click="selectOption(option)"
        >
          {{ option }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    buttonRef: {
      type: Object,
      required: true, // 상위 컴포넌트에서 버튼 참조 필수
    },
    initialSelectedOption: {
      type: String,
      default: '전체', // 초기값
    },
  },
  data() {
    return {
      options: ['전체', '5년 이내', '10년 이내', '15년 이내', '15년 이상'], // 옵션 목록
      selectedOption: this.initialSelectedOption, // 선택된 옵션
      modalPosition: { top: 0, left: 0 }, // 모달 위치
    }
  },
  mounted() {
    this.calculateModalPosition() // 모달 위치 계산
  },
  methods: {
    selectOption(option) {
      this.selectedOption = option // 내부 상태 업데이트
      this.$emit('select', option) // 상위 컴포넌트로 선택 이벤트 전달
    },
    closeModal() {
      this.$emit('close') // 모달 닫기 이벤트 전달
    },
    calculateModalPosition() {
      const buttonRect = this.buttonRef.getBoundingClientRect()
      this.modalPosition = {
        top: buttonRect.bottom + window.scrollY + 8, // 버튼 아래로 배치
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
  background: transparent; /* 오버레이 배경 제거 */
}

.modal {
  position: absolute;
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  width: 200px; /* 모달 너비 */
}

.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

li {
  padding: 8px 10px;
  cursor: pointer;
  border-radius: 4px;
}

li:hover {
  background-color: #f0f0f0;
}

li.selected {
  background-color: #007bff;
  color: white;
}
</style>
