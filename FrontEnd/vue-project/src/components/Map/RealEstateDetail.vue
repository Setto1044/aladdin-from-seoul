<template>
  <div class="real-estate-detail">
    <!-- 단지 정보 섹션 -->
    <section class="complex-info">
      <h1 class="complex-title">{{ complex.name }}</h1>
      <p class="complex-summary">
        {{ complex.builtYear }} / {{ complex.minSize }}㎡ ~ {{ complex.maxSize }}㎡
      </p>
      <div class="price-info">
        <div class="recent-transaction">
          최근 매매 실거래가: <span class="price">{{ complex.recentPrice }}</span>
        </div>
        <div class="expected-prices">
          <p>매매가: {{ complex.expectedSalePrice }}</p>
        </div>
      </div>
      <!-- <div class="tab-buttons">
        <button v-for="tab in tabs" :key="tab" class="tab-button">{{ tab }}</button>
      </div> -->
    </section>

    <!-- 필터 섹션 -->
    <section class="filter-section">
      <select v-model="selectedTransactionType">
        <option v-for="type in transactionTypes" :key="type" :value="type">{{ type }}</option>
      </select>
    </section>

    <!-- 매물 리스트 섹션 -->
    <section class="property-list">
      <div v-for="property in properties" :key="property.id" class="property-item">
        <MapItem :item="property" @select-item="selectItem"></MapItem>
        <!-- <div class="property-info">
          <h2 class="property-title">{{ property.name }}</h2>
          <p class="property-price">매매 {{ property.price }}</p>
          <p class="property-detail">
            아파트 - {{ property.size }}, {{ property.floor }}층, {{ property.direction }}
          </p>
          <p class="property-confirmation">확인일: {{ property.confirmationDate }}</p>
        </div>
        <button class="favorite-button" @click="toggleFavorite(property)">
          <span v-if="property.isFavorite">★</span>
          <span v-else>☆</span>
        </button>  -->
      </div>
    </section>
  </div>
</template>

<script>
import MapItem from '@/components/Map/Apt/MapItem.vue'

export default {
  name: 'RealEstateDetail',
  components: {
    MapItem,
  },
  methods: {
    selectItem(param) {
      this.$emit('select-item', param)
    },
    toggleFavorite(property) {
      property.isFavorite = !property.isFavorite
    },
  },
  data() {
    return {
      complex: {
        name: '분당더샵스타파크(주상복합)',
        units: 378,
        floors: 45,
        builtYear: '2007.02.06',
        minSize: '1',
        maxSize: '20',
        recentPrice: '16억 7,000',
        expectedSalePrice: '15억 9,000~17억 2,000',
        expectedRentPrice: '전세가 10억~11억',
      },
      tabs: ['단지정보', '시세/실거래가', '동호수/공시지가'],
      transactionTypes: ['전체거래방식', '매매', '전세'],
      sizes: ['전체면적', '116㎡', '152㎡'],
      buildings: ['전체동', '101동', '102동'],
      selectedTransactionType: '전체거래방식',
      selectedSize: '전체면적',
      selectedBuilding: '전체동',
      properties: [
        {
          id: 1,
          name: '분당더샵스타파크(주상복합) 101동',
          price: '16억 8,000',
          size: '152㎡',
          floor: '19/35층',
          direction: '남향',
          confirmationDate: '확인 24.11.15',
          isFavorite: false,
        },
        {
          id: 2,
          name: '분당더샵스타파크(주상복합) 101동',
          price: '16억 8,000',
          size: '153㎡',
          floor: '24/35층',
          direction: '남동향',
          confirmationDate: '확인 24.11.18',
          isFavorite: false,
        },
        {
          id: 3,
          name: '분당더샵스타파크(주상복합) 103동',
          price: '16억 8,000',
          size: '153㎡',
          floor: '24/35층',
          direction: '남동향',
          confirmationDate: '확인 24.11.18',
          isFavorite: false,
        },
        {
          id: 4,
          name: '분당더샵스타파크(주상복합) 104동',
          price: '16억 8,000',
          size: '153㎡',
          floor: '24/35층',
          direction: '남동향',
          confirmationDate: '확인 24.11.18',
          isFavorite: false,
        },
      ],
    }
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
