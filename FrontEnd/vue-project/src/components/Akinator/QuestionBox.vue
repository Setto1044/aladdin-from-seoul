<template>
  <div class="question-box">
    <p>{{ question }}</p>
    <button @click="submitAnswer('Yes')">Yes</button>
    <button @click="submitAnswer('No')">No</button>
    <button @click="submitAnswer('I don\'t know')">I don't know</button>
  </div>
</template>

<script>
export default {
  props: {
    question: {
      type: String,
      required: true,
    },
  },
  methods: {
    submitAnswer(answer) {
      this.$emit('answer', answer)
    },
  },
}
</script>

<style scoped>
.question-box {
  text-align: center;
  margin: 20px 0;
}
button {
  margin: 5px;
  padding: 10px 20px;
  font-size: 16px;
}
</style>
