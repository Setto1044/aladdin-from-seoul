<template>
  <div class="property-info">
    <h2 class="property-title" @click="selectItem">{{ item.no }}</h2>
    <p class="property-price">매매 {{ item.dealAmount }}</p>
    <p class="property-detail">
      아파트 -{{ item.aptDong }} {{ item.excluUseAr }}평, {{ item.floor }}층, {{ item.direction }}
    </p>
    <p class="property-confirmation">
      확인일: {{ item.dealYear }} {{ item.dealMonth }} {{ item.dealDay }}
    </p>
  </div>
  <button class="favorite-button" @click="toggleFavorite(item)">
    <span v-if="item.isFavorite">★</span>
    <span v-else>☆</span>
  </button>
</template>

<script>
export default {
  name: 'MapItem',
  props: {
    item: {
      type: Object,
      required: true,
    },
  },
  methods: {
    selectItem() {
      this.$emit('select-item', this.item)
    },
    toggleFavorite(item) {
      item.isFavorite = !item.isFavorite
      this.$emit('toggle-favorite', item)
    },
  },
}
</script>

<style scoped>
.real-estate-detail {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.complex-info {
  margin-bottom: 20px;
}

.complex-title {
  font-size: 24px;
  font-weight: bold;
}

.complex-summary {
  color: #666;
}

.price-info {
  margin-top: 10px;
}

.recent-transaction {
  font-size: 18px;
  font-weight: bold;
}

.tab-buttons {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.tab-button {
  padding: 10px;
  border: none;
  background-color: #f1f1f1;
  cursor: pointer;
}

.filter-section {
  display: flex;
  gap: 10px;
  margin: 20px 0;
}

.property-list {
  margin-top: 20px;
}

.property-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.property-info {
  flex: 1;
}

.property-title {
  font-size: 18px;
  font-weight: bold;
}

.property-price {
  color: #ff0000;
}

.property-confirmation {
  color: #888;
}

.favorite-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
}
</style>
