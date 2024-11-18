<template>
    <div v-if="isOpen" class="sidebar sidebar1">
      <button @click="openSidebar2">Open Sidebar 2</button>
      <!-- 다른 내용 -->
      <MapItem title="매물 A" />
      <MapItem title="매물 B" />
      <MapItem title="매물 C" />
    </div>
  </template>
  
  <script>
  import MapItem from '@/components/MapItem.vue';
  export default {
    components: {
      MapItem,
    },
    props: ['isOpen', 'openSidebar2'],
  };
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
  }
  .sidebar1 {
    transform: translateX(0);
  }
  </style>
  