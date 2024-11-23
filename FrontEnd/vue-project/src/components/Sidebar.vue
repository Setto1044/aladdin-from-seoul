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
  width: 300px;
  background: white;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  overflow-y: auto;
  transition: transform 0.3s ease;
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
  background: #ff4d4f;
  color: white;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  font-size: 16px;
  cursor: pointer;
}

.close-button:hover {
  background: #d9363e;
}
</style>
