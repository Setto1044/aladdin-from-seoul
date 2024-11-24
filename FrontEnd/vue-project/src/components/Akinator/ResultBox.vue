<template>
  <div class="result-box">
    <h2>Result</h2>
    <p>{{ result }}</p>
  </div>
</template>

<script>
export default {
  props: {
    result: {
      type: String,
      required: true,
    },
  },
}
</script>

<style scoped>
.result-box {
  text-align: center;
  margin: 20px 0;
}
</style>
