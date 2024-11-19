<template>
  <div v-if="isOpen" class="sidebar">
    <button class="close-button" @click="closeSidebar">X</button>
    <slot></slot>
    <!-- 사이드바 내부의 콘텐츠는 자식 컴포넌트에서 삽입 -->
  </div>
</template>

<script>
export default {
  name: 'SidebarBase',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    closeSidebar() {
      this.$emit('close') // 사이드바를 닫기 위한 이벤트 발생
    },
  },
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  right: 0;
  width: 300px;
  height: 100%;
  background-color: #fff;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}
</style>
