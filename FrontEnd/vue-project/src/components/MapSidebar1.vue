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
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  width: 300px;
  height: 100%;
  background-color: #f0f0f0;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
  padding-top: 80px;
  overflow-y: auto; /* Enable vertical scrolling */
  max-height: 100%; /* Ensure the sidebar uses full viewport height */
}
.sidebar1 {
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
