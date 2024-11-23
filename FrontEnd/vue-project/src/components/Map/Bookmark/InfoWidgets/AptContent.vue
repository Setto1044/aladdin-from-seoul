<template>
  <div class="apt-content">
    <div class="property-info">
      <p class="property-name">{{ item.aptName }} ({{ item.buildYear }})</p>
      <p class="property-address">
        {{ item.sidoName }} {{ item.gugunName }} {{ item.dongName }} {{ item.jibun }}
      </p>
      <p class="property-price">매매 {{ item.dealAmount }}만 원</p>
      <p class="property-detail">{{ item.excluUseAr }}㎡, {{ item.floor }}층 {{ item.aptDong }}</p>
      <p class="property-confirmation">
        확인일: {{ item.dealYear }}. {{ item.dealMonth }}. {{ item.dealDay }}
      </p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      required: true,
    },
  },
}
</script>

<style scoped>
.property-info {
  flex: 1;
  text-align: left;
}
.property-price {
  color: #ff0000;
}
.property-confirmation {
  color: #888;
}
.property-name {
  font-family: 'Score7';
}
</style>
