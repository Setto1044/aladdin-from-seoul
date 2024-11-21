<template>
  <div v-if="isOpen" class="sidebar sidebar2">
    <!-- 다른 내용 -->
    <div class="property-details-wrapper">
      <button class="close-button" @click="closeSidebar">X</button>
      <PropertyDetailsPage></PropertyDetailsPage>
    </div>
  </div>
</template>

<script>
import PropertyDetailsPage from '@/components/PropertyDetailsPage.vue'
export default {
  components: {
    PropertyDetailsPage,
  },
  props: ['isOpen'],
  methods: {
    closeSidebar() {
      this.$emit('close') // 사이드바를 닫기 위한 이벤트 발생
    },
  },
}
</script>

<style scoped>
.sidebar {
  position: relative;
  background: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 300px; /* 사이드바 너비 */
  height: 100%; /* 사이드바 높이 */
  overflow-y: auto;
  z-index: 1000;
}

.property-details-wrapper {
  position: relative; /* Provides a positioning context for the button */
  width: 100%; /* Full width of the sidebar */
  height: 100%; /* Full height of the sidebar */
}

/* Close 버튼 스타일 */
.close-button {
  position: absolute;
  top: 16px; /* 상단 여백 */
  right: 16px; /* 오른쪽 여백 */
  background-color: #ff4d4f;
  color: #ffffff;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s ease;
}

.close-button:hover {
  background-color: #d9363e;
}
</style>
