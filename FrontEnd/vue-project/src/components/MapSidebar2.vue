<template>
    <div v-if="isOpen" class="sidebar sidebar2">
      <p>Sidebar 2 Content</p>
      <!-- 다른 내용 -->
    </div>
  </template>
  
  <script>
  export default {
    props: ['isOpen'],
  };
  </script>
  
  <style scoped>
  .sidebar {
    position: fixed;
    left: 300px; /* 사이드바1 너비 뒤에 나타나도록 */
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
  }
  </style>
  