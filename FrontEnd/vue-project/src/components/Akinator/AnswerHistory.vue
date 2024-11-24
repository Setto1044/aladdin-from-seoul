<template>
  <div class="answer-history">
    <h3>Answer History</h3>
    <ul>
      <li v-for="(answer, index) in answers" :key="index">
        <strong>{{ answer.question }}</strong
        >: {{ answer.answer }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    answers: {
      type: Array,
      required: true,
    },
  },
}
</script>

<style scoped>
.answer-history {
  margin: 20px 0;
  text-align: left;
}
ul {
  list-style: none;
  padding: 0;
}
li {
  margin: 5px 0;
}
</style>
