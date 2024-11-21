<template>
  <div v-if="isOpen" class="sidebar sidebar1">
    <!-- Content inside Sidebar1 -->
    <div class="property-details-wrapper">
      <button class="close-button" @click="closeSidebar">X</button>
      <RealEstateDetail @select-item="selectItem"></RealEstateDetail>
    </div>
  </div>
</template>

<script>
import RealEstateDetail from '@/components/RealEstateDetail.vue'

export default {
  components: {
    RealEstateDetail,
  },
  props: ['isOpen', 'openSidebar2'],
  methods: {
    closeSidebar() {
      this.$emit('close1') // 사이드바를 닫기 위한 이벤트 발생
    },
    selectItem(param) {
      this.$emit('select-item', param) // Emit event to the parent when an item is selected
    },
  },
}
</script>
<style scoped>
/* Sidebar 스타일 */
.sidebar {
  position: relative;
  background: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 300px; /* 사이드바 너비 */
  height: 100%; /* 사이드바 높이 */
  overflow-y: auto;
  z-index: 1000;
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
