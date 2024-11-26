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
  overflow-y: auto;
}

.slot-wrapper {
  position: relative; /* 쌓임 맥락 생성 */
  overflow-y: auto; /* 슬롯 콘텐츠 스크롤 가능 */
  z-index: 10;
}

.sidebar.sidebar1 {
  left: 0; /* 왼쪽 사이드바 */
  width: 18%;
}

.sidebar.sidebar2 {
  right: 0; /* 오른쪽 사이드바 */
}

.close-button {
  position: fixed; /* 부모 요소와 상관없이 고정 */
  background: #6f5f9052; /*  배경 */
  color: #555; /* 텍스트 색상 */
  width: 25px; /* 버튼 너비 */
  height: 100%; /* 버튼 높이 */
  font-size: 16px; /* 글씨 크기 */
  font-weight: bold; /* 글씨 두께 */
  cursor: pointer; /* 클릭 가능 */
  display: flex;
  align-items: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 그림자 */
  z-index: 2; /* 버튼이 사이드바 아래로 보이도록 설정 */
  border: none;
  cursor: pointer;
  transition: all 0.3s ease; /* 부드러운 전환 효과 */
}

:root {
  --sidebar1-wrapper-width: 0px; /* 초기값 */
  --sidebar2-wrapper-width: 0;
}

.sidebar1 .close-button {
  left: calc(var(--sidebar1-wrapper-width)); /* .slot-wrapper 너비를 기준으로 이동 */
  justify-content: right;
  border-top-right-radius: 50px;
  border-bottom-right-radius: 50px;
  background: #f5f5f5a9; /* 밝은 회색 배경 */
  color: #000; /* 텍스트 색상 변경 */
}

.sidebar2 .close-button {
  right: calc(var(--sidebar2-wrapper-width)); /* .slot-wrapper 너비를 기준으로 이동 */
  justify-content: left;
  border-top-left-radius: 50px;
  border-bottom-left-radius: 50px;
  background: #f5f5f5a9; /* 밝은 회색 배경 */
  color: #000; /* 텍스트 색상 변경 */
}

.sidebar1 .close-button:hover {
  transform: translateX(10px);
  width: 40px; /* 버튼 너비 증가 */
  background: #da2b2bab; /* 밝은 회색 배경 */
  color: #ffffff; /* 텍스트 색상 변경 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.603); /* 그림자 */
}

.sidebar2 .close-button:hover {
  transform: translateX(-10px);
  width: 33px; /* 버튼 너비 증가 */
  background: #da2b2bab; /* 밝은 회색 배경 */
  color: #ffffff; /* 텍스트 색상 변경 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.603); /* 그림자 */
}
</style>
