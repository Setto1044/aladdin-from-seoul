<template>
  <div class="outer-container">
    <div class="line-container">
      <div class="line">
        <div v-for="(station, index) in stations" :key="index" class="station-wrapper">
          <!-- 역 원 -->
          <div class="station-info">
            <div
              class="circle"
              :style="{ backgroundColor: station.color || defaultStationColor }"
            ></div>
            <p class="station-name" :title="station.name">{{ station.name }}</p>
          </div>

          <!-- 오른쪽 선 (마지막 원에는 선 없음) -->
          <div
            v-if="index < stations.length - 1"
            class="line-segment right"
            :style="{ backgroundColor: station.lineColor || defaultLineColor }"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SubwayLine',
  props: {
    stations: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      defaultStationColor: '#28a745', // 기본 역 색상
      defaultLineColor: '#6f42c1', // 기본 선 색상
    }
  },
}
</script>

<style scoped>
/* 전체 컨테이너 */
.outer-container {
  display: flex;
  justify-content: flex-start; /* 왼쪽 정렬 */
  align-items: center;
  width: 100%;
  padding: 0 20px; /* 왼쪽/오른쪽 여백 추가 */
  overflow-x: auto; /* 긴 노선도 스크롤 가능 */
  background-color: #f8f9fa;
}

/* 노선 */
.line-container {
  display: flex;
  justify-content: flex-start; /* 왼쪽 정렬 */
  align-items: center;
  width: max-content; /* 내부 콘텐츠 크기만큼 조정 */
}

.line {
  display: flex;
  align-items: center;
}

/* 역 아이템 */
.station-wrapper {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  width: 70px; /* 고정 너비 설정 */
  position: relative;
}

.station-wrapper:last-child .station-info {
  margin-right: 35px; /* 마지막 원 오른쪽에 선 대신 간격 추가 */
}

/* 역 원과 이름 */
.station-info {
  display: flex;
  flex-direction: column; /* 이름을 원 아래 배치 */
  align-items: center;
  justify-content: center;
  width: 50px; /* 고정된 크기 설정 */
  margin-top: 40px; /* 원을 선과 맞추기 위해 위치 조정 */
}

/* 역 원 */
.circle {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #28a745;
  border: 2px solid #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin: 0 auto; /* 중앙 정렬 */
}

/* 역 이름 */
.station-name {
  margin-top: 8px;
  font-size: 0.7rem;
  color: #555555;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* 말줄임표 처리 */
  max-width: 100%; /* 고정 크기에 맞게 줄임 */
}

/* 선 */
.line-segment {
  height: 4px;
  width: 30px;
  background-color: #6f42c1;
}
</style>
