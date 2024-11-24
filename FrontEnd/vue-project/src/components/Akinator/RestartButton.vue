<template>
  <button @click="$emit('restart')">Restart Game</button>
</template>

<style scoped>
button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
}
</style>
