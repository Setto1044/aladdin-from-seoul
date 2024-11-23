<template>
  <div class="sidebar" v-if="isOpen">
    <button class="close-button" @click="closeSidebar">X</button>
    <slot></slot>
    <!-- 슬롯을 사용해 상위 컴포넌트에서 콘텐츠를 전달 -->
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  props: {
    isOpen: {
      type: Boolean,
      required: true, // 사이드바가 열리고 닫힘을 제어
    },
  },
  emits: ['close'], // 닫기 이벤트
  methods: {
    closeSidebar() {
      this.$emit('close') // 부모 컴포넌트로 닫기 이벤트 전송
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
}

slot {
  overflow-y: auto;
}

.sidebar.sidebar1 {
  left: 0; /* 왼쪽 사이드바 */
}

.sidebar.sidebar2 {
  right: 0; /* 오른쪽 사이드바 */
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none; /* 배경 없음 */
  color: #555; /* 차분한 회색 */
  border: none; /* 테두리 없음 */
  border-radius: 20%; /* 동글네모 */
  width: 25px; /* 작은 크기 */
  height: 25px;
  font-size: 16px; /* 작은 글씨 크기 */
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center; /* 텍스트 가운데 정렬 */
  transition:
    transform 0.2s ease,
    background-color 0.2s ease,
    color 0.2s ease;
}

.close-button:hover {
  transform: scale(1.2); /* 살짝 확대 */
  background: #f5f5f5; /* 밝은 회색 배경 */
  color: #000; /* 글씨 색상 변경 */
}
</style>
