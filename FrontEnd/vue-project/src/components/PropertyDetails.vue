<template>
  <div class="property-details">
    <header class="property-header">
      <h1 class="property-title">{{ property.no }}</h1>
      <p class="property-price">매매 {{ property.dealAmount }}</p>
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
  </div>
</template>

<script>
export default {
  name: 'PropertyDetails',
  props: {
    property: {
      type: Object,
      required: true, // Parent must pass property details
    },
  },
  data() {
    return {
      tabs: ['매물정보', '시세/실거래가', '동호수/공시지가', '학교정보'],
      selectedTab: '매물정보',
    }
  },
}
</script>

<style scoped>
.property-details {
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
