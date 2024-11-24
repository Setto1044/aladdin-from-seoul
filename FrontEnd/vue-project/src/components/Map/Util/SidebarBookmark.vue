<template>
  <div class="sidebar" v-if="isOpen">
    <button class="close-button" @click="closeSidebar">X</button>
    <div class="slot-wrapper">
      <slot></slot>
    </div>
    <!-- 슬롯을 사용해 상위 컴포넌트에서 콘텐츠를 전달 -->
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['close'],
  methods: {
    closeSidebar() {
      this.$emit('close')
    },
    updateCloseButtonHeight() {
      // $el이 DOM 요소인지 확인
      if (this.$el instanceof HTMLElement) {
        const closeButton = this.$el.querySelector('.close-button')
        if (closeButton) {
          closeButton.style.height = `${this.$el.offsetHeight}px` // Sidebar 높이 동기화
        }
      } else {
        console.warn('$el is not a DOM element.')
      }
    },
  },
  watch: {
    isOpen(newValue) {
      if (newValue) {
        // isOpen이 true가 될 때 높이 업데이트
        this.$nextTick(() => {
          this.updateCloseButtonHeight()
        })
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.updateCloseButtonHeight()
      window.addEventListener('resize', this.updateCloseButtonHeight) // 창 크기 변경 시 업데이트
    })
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateCloseButtonHeight)
  },
}
</script>

<style scoped>
.sidebar {
  position: absolute;
  top: 0;
  height: 100%;
  width: 26%;
  background: white;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  transition: transform 0.3s ease;
  overflow-y: auto; /* 내부 콘텐츠 스크롤 가능 */
  overflow-x: visible; /* 버튼을 포함한 x축 컨텐츠 숨김 */
}

.slot-wrapper {
  position: relative; /* 쌓임 맥락 생성 */
  overflow-y: auto; /* 슬롯 콘텐츠 스크롤 가능 */
  z-index: 10;
}

.sidebar.sidebar1 {
  left: 0; /* 왼쪽 사이드바 */
}

.sidebar.sidebar2 {
  right: 0; /* 오른쪽 사이드바 */
}

.close-button {
  position: fixed; /* 부모 요소와 상관없이 고정 */
  top: 110px; /* 위쪽 기준 10px */
  background: #ffbdbd91; /* 흰색 배경 */
  color: #555; /* 텍스트 색상 */
  border: 1px solid #ddd; /* 연한 테두리 */
  width: 20px; /* 버튼 너비 */
  height: 30px; /* 버튼 높이 */
  font-size: 16px; /* 글씨 크기 */
  font-weight: bold; /* 글씨 두께 */
  cursor: pointer; /* 클릭 가능 */
  display: flex;
  align-items: center;
  justify-content: center; /* 텍스트 가운데 정렬 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 그림자 */
  z-index: 2; /* 버튼이 사이드바 위로 보이도록 설정 */
  transition:
    transform 0.2s ease,
    background-color 0.2s ease,
    color 0.2s ease;
}

.sidebar1 .close-button {
  right: calc(74% - 20px); /* 사이드바1의 바깥 오른쪽 */
}

.sidebar2 .close-button {
  left: calc(74% - 20px); /* 사이드바2의 바깥 왼쪽 */
}

.close-button:hover {
  background: #f5f5f5; /* 호버 시 밝은 회색 배경 */
  color: #000; /* 호버 시 텍스트 색상 변경 */
}
</style>
