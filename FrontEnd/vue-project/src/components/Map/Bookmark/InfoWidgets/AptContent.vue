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
      console.log(`http://localhost:8080/aladin/house/detail/${this.item.aptSeq}`)
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
  background: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 12px;
}

.property-name {
  font-size: 18px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 8px;
}

.property-address {
  font-size: 14px;
  color: #555555;
  margin-bottom: 8px;
}

.property-price {
  font-size: 16px;
  font-weight: bold;
  color: #d32f2f;
  margin-bottom: 8px;
}

.property-detail {
  font-size: 14px;
  color: #666666;
  margin-bottom: 8px;
}

.property-confirmation {
  font-size: 12px;
  color: #999999;
  margin-top: 10px;
  font-style: italic;
}

.details-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 5px; /* 행 간격 추가 */
  margin-top: 12px;
  font-size: 13px;
  table-layout: fixed;
}

.details-table th,
.details-table td {
  padding: 8px;
  text-align: left;
}

.details-table th {
  background-color: #f4f6f8;
  font-weight: bold;
  color: #333333;
  border-bottom: 2px solid #e0e0e0;
}

.details-table td {
  background-color: #ffffff;
  color: #555555;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.details-table tr:hover td {
  background-color: #f9f9f9;
  transition: background-color 0.3s ease;
}

/* 1,3 열과 2,4 열의 너비 균등 분배 */
.details-table th:nth-child(1),
.details-table th:nth-child(3),
.details-table td:nth-child(1),
.details-table td:nth-child(3) {
  width: 30%; /* 1번과 3번 열 */
}

.details-table th:nth-child(2),
.details-table th:nth-child(4),
.details-table td:nth-child(2),
.details-table td:nth-child(4) {
  width: 20%; /* 2번과 4번 열 */
}

/* 행 배경색 */
.details-table tr:nth-child(even) td {
  background-color: #f9f9f9;
}

.details-table tr:nth-child(odd) td {
  background-color: #ffffff;
}
</style>
