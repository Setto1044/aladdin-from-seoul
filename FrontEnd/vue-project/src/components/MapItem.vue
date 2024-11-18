<template>
    <div class="property-item">
      <div class="property-icon">🏠</div>
      <h2 class="property-title">{{ title }}</h2>
    </div>
  </template>
  
  <script>
  export default {
    name: 'MapItem',
    props: {
      title: {
        type: String,
        required: true
      }
    }
  }
  </script>
  
  <style scoped>
  
  </style>