<template>
  <div class="modal">
    <ul>
      <li v-for="option in options" :key="option" @click="selectOption(option)">
        {{ option }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      options: ['월세', '전세'], // Example options; replace as needed
    }
  },
  methods: {
    selectOption(option) {
      this.$emit('select', option)
    },
  },
}
</script>

<style scoped>
.modal {
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 50;
}
ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
li {
  padding: 8px 10px;
  cursor: pointer;
}
li:hover {
  background-color: #f0f0f0;
}
</style>
