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
  position: fixed;
  left: 300px; /* Positioned after Sidebar 1 */
  top: 0;
  width: 300px;
  height: 100%;
  background-color: #e0e0e0;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
  padding-top: 80px;
}
.sidebar2 {
  transform: translateX(0);
  display: flex; /* Aligns children horizontally */
  flex-direction: column; /* Ensure content stacks vertically */
  align-items: flex-start; /* Aligns items at the start */
  overflow-y: auto; /* Enables vertical scrolling */
  max-height: 100%; /* Ensures the height constraint */
}
.property-details-wrapper {
  position: relative; /* Provides a positioning context for the button */
  width: 100%; /* Full width of the sidebar */
  height: 100%; /* Full height of the sidebar */
}
.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgb(0, 0, 0);
  color: white;
  border: none;
  font-size: 18px;
  cursor: pointer;
  z-index: 2; /* Ensures it overlaps the PropertyDetailsPage */
}
</style>
