<template>
  <div class="apt-content">
    <div class="property-info">
      <p class="property-name">{{ item.aptName }}</p>
      <p class="property-address">
        {{ item.sidoName }} {{ item.gugunName }} {{ item.dongName }} {{ item.jibun }}
      </p>
      <p class="property-price">매매 {{ item.dealAmount }}만 원</p>
      <p class="property-detail">{{ item.excluUseAr }}㎡, {{ item.floor }}층 {{ item.aptDong }}</p>
      <p class="property-confirmation">
        확인일: {{ item.dealYear }}. {{ item.dealMonth }}. {{ item.dealDay }}
      </p>

      <!-- 표 형식으로 추가 정보 표시 -->
      <table v-if="details && Object.keys(details).length > 0" class="details-table">
        <tbody>
          <tr v-if="details.householdCount">
            <th colspan="2">세대수</th>
            <td colspan="2">{{ details.householdCount }}</td>
          </tr>
          <tr v-if="details.floorRange">
            <th colspan="2">층수</th>
            <td colspan="2">{{ details.floorRange }}</td>
          </tr>
          <tr v-if="details.parkingCount">
            <th colspan="2">주차대수</th>
            <td colspan="2">{{ details.parkingCount }}</td>
          </tr>
          <!-- 용적률과 건폐율 나란히 표시 -->
          <tr v-if="details.floorAreaRatio || details.buildingCoverageRatio">
            <th>용적률</th>
            <td>{{ details.floorAreaRatio || '정보 없음' }}%</td>
            <th>건폐율</th>
            <td>{{ details.buildingCoverageRatio || '정보 없음' }}%</td>
          </tr>
          <tr v-if="details.constructionCompany">
            <th colspan="2">건설사</th>
            <td colspan="2">{{ details.constructionCompany }}</td>
          </tr>
          <tr v-if="details.heatingType">
            <th colspan="2">난방방식</th>
            <td colspan="2">{{ details.heatingType }}</td>
          </tr>
          <tr v-if="details.managementDigit">
            <th colspan="2">관리비 계정</th>
            <td colspan="2">{{ details.managementDigit }}</td>
          </tr>
          <tr v-if="details.areaSizes">
            <th colspan="2">면적</th>
            <td colspan="2">{{ details.areaSizes }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  props: {
    item: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      details: {}, // 추가 정보를 저장할 객체
    }
  },
  async mounted() {
    try {
      const response = await axios.get(
        `http://localhost:8080/aladin/house/detail/${this.item.aptSeq}`,
      )
      if (response.data.success && response.data.data) {
        this.details = response.data.data
      }
    } catch (error) {
      console.error('Error fetching apartment details:', error)
    }
  },
}
</script>

<style scoped>
.property-info {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  margin: 10px 0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

.property-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 6px;
}

.property-address {
  font-size: 13px;
  color: #555;
  margin-bottom: 6px;
}

.property-price {
  font-size: 14px;
  font-weight: bold;
  color: #ff5722;
  margin-bottom: 6px;
}

.property-detail {
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
}

.property-confirmation {
  font-size: 11px;
  color: #999;
  margin-top: 8px;
  font-style: italic;
}

.details-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
  font-size: 12px; /* 표를 작게 만듦 */
  table-layout: fixed; /* 열 너비 고정 */
}

.details-table th,
.details-table td {
  border: 1px solid #ddd;
  padding: 6px; /* 패딩 줄임 */
  text-align: left;
}

.details-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #333;
}

/* 1,3 열과 2,4 열의 너비를 동일하게 설정 */
.details-table th:nth-child(1),
.details-table th:nth-child(3),
.details-table td:nth-child(1),
.details-table td:nth-child(3) {
  width: 25%; /* 1번과 3번 열 */
}

.details-table th:nth-child(2),
.details-table th:nth-child(4),
.details-table td:nth-child(2),
.details-table td:nth-child(4) {
  width: 25%; /* 2번과 4번 열 */
}

.details-table td {
  color: #555;
}

.details-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.details-table tr:hover {
  background-color: #f1f1f1;
}
</style>
