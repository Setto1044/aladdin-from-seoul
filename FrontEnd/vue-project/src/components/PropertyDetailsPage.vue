<template>
  <div class="property-details-page">
    <header class="property-header">
      <h1 class="property-title">{{ property.title }}</h1>
      <p class="property-price">매매 {{ property.price }}</p>
      <p class="property-agent">
        {{ property.agent }} -
        <a :href="`tel:${property.phone}`" class="agent-phone">{{ property.phone }}</a>
      </p>
      <button class="btn-report">허위매물 신고</button>
    </header>

    <nav class="property-tabs">
      <button
        v-for="tab in tabs"
        :key="tab"
        @click="selectedTab = tab"
        :class="{ 'active-tab': selectedTab === tab }"
        class="tab-button"
      >
        {{ tab }}
      </button>
    </nav>

    <main class="property-info">
      <div v-if="selectedTab === '매물정보'" class="info-section">
        <table class="property-table">
          <tbody>
            <tr v-for="(value, key) in property.details" :key="key">
              <th>{{ key }}</th>
              <td>{{ value }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else>
        <p>해당 탭의 정보는 준비 중입니다.</p>
      </div>
    </main>

    <MapItem :item="property" @click.native="handleMapItemClick(property)"></MapItem>
  </div>
</template>

<script>
import MapItem from '@/components/MapItem.vue'

export default {
  name: 'PropertyDetailsPage',
  components: {
    MapItem,
  },
  data() {
    return {
      property: {
        title: '상록우성 303동 9층',
        price: '11억 6,000',
        agent: '박공부동산중개사무소',
        phone: '031-718-0018',
        details: {
          매물특징: '23. 남향 전세안고',
          '공급/전용면적': '74.18㎡ / 55.14㎡ (전용률 74%)',
          '해당동/층수': '9/15층',
          '방수/욕실수': '2/1개',
          관리비: '15만 7,000원',
          '기보증금/월세': '4억/-',
          방향: '남향(거실 기준)',
          현관구조: '복도식',
          난방: '난방(방식/연료): 지역난방/열병합',
          입주가능일: '2026년 03월 하순',
          총주차대수: '205대(세대당 1.16대)',
          '해당면적 세대수': '160세대',
          '건축물 용도': '공동주택',
          매물번호: '2457162700',
        },
      },
      tabs: ['매물정보', '시세/실거래가', '동호수/공시지가', '학교정보'],
      selectedTab: '매물정보',
    }
  },
  methods: {
    handleMapItemClick(property) {
      this.property = property
    },
  },
}
</script>

<style scoped>
.property-details-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.property-header {
  text-align: left;
  margin-bottom: 20px;
}

.property-title {
  font-size: 24px;
  font-weight: bold;
}

.property-price {
  font-size: 18px;
  color: #ff0000;
}

.property-agent {
  font-size: 14px;
  color: #666;
}

.agent-phone {
  color: #007bff;
  text-decoration: none;
}

.btn-report {
  background-color: #dc3545;
  color: #fff;
  border: none;
  padding: 10px;
  cursor: pointer;
  border-radius: 4px;
  margin-top: 10px;
}

.property-tabs {
  display: flex;
  gap: 10px;
  margin: 20px 0;
}

.tab-button {
  padding: 10px;
  border: none;
  background-color: #f1f1f1;
  cursor: pointer;
  border-radius: 4px;
}

.active-tab {
  background-color: #007bff;
  color: #fff;
}

.property-info {
  margin-top: 20px;
}

.property-table {
  width: 100%;
  border-collapse: collapse;
}

.property-table th,
.property-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
}

.property-table th {
  background-color: #f9f9f9;
  font-weight: bold;
}
</style>
