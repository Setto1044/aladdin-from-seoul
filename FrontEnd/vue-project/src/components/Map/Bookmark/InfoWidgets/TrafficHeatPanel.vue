<template>
  <div class="traffic-info">
    <!-- 혼잡도 정보 -->
    <div class="traffic-content">
      <h3 class="traffic-title">
        🚥 혼잡도 정보
        <button class="action-button-inline" @click="toggleModal">🔍︎주소</button>
      </h3>

      <div v-if="loading" class="loading-message">로딩 중...</div>
      <div v-if="error" class="error-message">{{ error }}</div>

      <div v-if="paths.length" class="results">
        <div class="results-container">
          <Carousel v-bind="config">
            <Slide v-for="(path, index) in paths" :key="index" class="result-item">
              <div class="result-card">
                <div class="result-header">
                  <p>
                    <strong>[경로 {{ index + 1 }}] </strong>
                  </p>
                  <p>
                    총 소요: {{ path.info.totalTime }}분 ({{
                      (path.info.totalDistance / 1000).toFixed(2)
                    }}km)
                  </p>
                </div>

                <div class="subway-line-draggable">
                  <SubwayLine
                    v-if="path.pathType === 1"
                    :stations="uiPaths[index]"
                    class="subway-line"
                  />
                </div>

                <div class="result-details">
                  <div v-for="(subPath, i) in path.subPath" :key="i" class="subpath-info">
                    <p>
                      <span class="label" v-if="subPath.trafficType === 1">지하철:</span>
                      <span class="label" v-if="subPath.trafficType === 2">버스:</span>
                      <span class="label" v-if="subPath.trafficType === 3">도보:</span>
                      <template v-if="subPath.trafficType === 1">{{
                        getSubwayInfo(subPath)
                      }}</template>
                      <template v-if="subPath.trafficType === 2">{{
                        getBusInfo(subPath)
                      }}</template>
                      <template v-if="subPath.trafficType === 3">
                        {{ subPath.distance }}m ({{ subPath.sectionTime }}분)
                      </template>
                    </p>
                  </div>
                </div>
              </div>
            </Slide>
            <template #addons>
              <Navigation />
            </template>
          </Carousel>
        </div>
      </div>
    </div>

    <!-- 모달 -->
    <div v-if="showModal" class="traffic-controls">
      <SearchModal :showModal="showModal" @close="toggleModal" @select="handleAddressSelect" />
    </div>
  </div>
</template>

<script>
import SearchModal from '@/components/Map/Bookmark/InfoWidgets/TrafficSearchModal.vue'
import SubwayLine from '@/components/Map/Bookmark/InfoWidgets/SubwayLine.vue'
import axios from 'axios'
import 'vue3-carousel/dist/carousel.css'
import { Carousel, Slide, Navigation } from 'vue3-carousel'
import { ref } from 'vue'

const lineColors = {
  '수도권 1호선': '#015EAB', // 파란색
  '수도권 2호선': '#1EB158', // 초록색
  '수도권 3호선': '#F17D32', // 주황색
  '수도권 4호선': '#01A9E5', // 노란색
  '수도권 5호선': '#9C60A6', // 보라색
  '수도권 6호선': '#C57436', // 갈색
  '수도권 7호선': '#667516', // 회색
  '수도권 8호선': '#EB2471', // 핑크색
  '수도권 9호선': '#B7AA8D', // 빨간색
  '수도권 9호선(급행)': '#B7AA8D', // 빨간색
  '수도권 공항철도': '#008BCB', // 청록색
  '수도권 신분당선': '#CD023A', // 진한 주황색
}

function getColorByLineName(lineName) {
  return lineColors[lineName] || '#ADB5BD' // 기본값은
}

export default {
  components: { SearchModal, SubwayLine, Carousel, Slide, Navigation },
  props: {
    lat: { type: String, required: true },
    lng: { type: String, required: true },
  },
  data() {
    return {
      config: {
        itemsToShow: 1, // 한 화면에 보여줄 카드 수
        wrapAround: true, // 순환 슬라이더
        transition: 500, // 슬라이드 전환 속도
        pauseAutoplayOnHover: true,
        mouseDrag: false, // 마우스 드래그 비활성화
      },
      showModal: true,
      paths: [], // 경로 데이터
      allPaths: [], // 정제된 경로
      loading: false,
      error: null,
      uiPaths: [], // UI용 경로 데이터
    }
  },
  methods: {
    toggleModal() {
      this.showModal = !this.showModal
    },
    async handleAddressSelect(result) {
      try {
        await this.fetchPubTransPath(this.lng, this.lat, result.x, result.y)

        const stationNames = this.formatPathsForAPI(this.allPaths)
        const congestionData = await this.fetchAllCongestionData(stationNames)

        this.uiPaths = this.processPaths(this.allPaths, congestionData)
        console.log('UI Paths:', this.uiPaths)
      } catch (error) {
        console.error('처리 오류:', error)
        this.error = '데이터 처리 중 오류가 발생했습니다.'
      }
    },
    async fetchPubTransPath(SX, SY, EX, EY) {
      const apiUrl = 'https://api.odsay.com/v1/api/searchPubTransPathT'
      const params = {
        SX,
        SY,
        EX,
        EY,
        apiKey: 'L9Bb/WkVpUljVkQ8xPoBjOoUt53vUsF7lV9mkpOrBWA',
        OPT: 1,
      }

      this.loading = true
      this.error = null

      try {
        const { data } = await axios.get(apiUrl, { params })
        if (data.result?.path) {
          this.paths = data.result.path.slice(0, 3)
          this.allPaths = this.extractPathsByRoute(this.paths)
        } else {
          this.error = '유효한 경로 데이터를 찾을 수 없습니다.'
        }
      } catch (error) {
        console.error('API 요청 오류:', error)
        this.error = 'API 요청 중 문제가 발생했습니다.'
      } finally {
        this.loading = false
      }
    },
    async fetchCongestionData(stationNames) {
      try {
        const response = await axios.post('http://localhost:8080/aladin/subway', { stationNames })
        console.log(response.data, stationNames)
        return response.data
      } catch (error) {
        throw new Error('혼잡도 API 요청 오류: ' + error.message)
      }
    },
    async fetchAllCongestionData(stationNames) {
      return Promise.all(
        stationNames.map(async (path) => {
          const names = path.map((station) => station.stationName)
          return this.fetchCongestionData(names)
        }),
      )
    },
    processPaths(paths, congestionData) {
      // 중복을 제거한 리스트
      const uniqueStationNames = [...new Set(paths.flat().map((station) => station.stationName))]

      // 혼잡도 매핑
      const mappedCongests = uniqueStationNames.map((name, index) => {
        const morningCongest = congestionData[0]?.data?.morningCongests[index] || 0
        return { name, morningCongest }
      })

      // 원래 paths에 혼잡도를 다시 매핑
      return paths.map((path) =>
        path.map((station) => {
          const matchingCongest = mappedCongests.find((item) => item.name === station.stationName)
          console.log(matchingCongest, station)
          return {
            name: station.stationName,
            color: getColorByLineName(station.lineName), // 호선 기반 색상
            lineColor: matchingCongest
              ? this.getLineColorByCongest(matchingCongest.morningCongest)
              : '#000000', // 혼잡도 기반 색상
            originalIndex: uniqueStationNames.indexOf(station.stationName),
          }
        }),
      )
    },
    formatPathsForAPI(paths) {
      return paths.map((path) => {
        const uniqueStations = []
        path.forEach((station, index) => {
          const stationName = station.stationName.endsWith('역')
            ? station.stationName
            : `${station.stationName}역`

          if (
            !uniqueStations.length ||
            uniqueStations[uniqueStations.length - 1].stationName !== stationName
          ) {
            uniqueStations.push({ ...station, stationName })
          }
        })
        return uniqueStations
      })
    },
    getSubwayInfo(subPath) {
      const lineNames = subPath.lane.map((line) => line.name).join(', ')
      return `${lineNames} (${subPath.startName} → ${subPath.endName}, ${subPath.stationCount}역)`
    },
    getBusInfo(subPath) {
      const busNumbers = subPath.lane.map((line) => line.busNo).join(', ')
      return `${busNumbers} (${subPath.startName} → ${subPath.endName})`
    },
    extractPathsByRoute(paths) {
      return paths.map((path) =>
        path.subPath
          .filter((subPath) => subPath.trafficType === 1 && subPath.passStopList)
          .flatMap((subPath) =>
            subPath.passStopList.stations.map((station) => ({
              stationName: station.stationName,
              lineName: subPath.lane[0]?.name || '알 수 없는 노선',
            })),
          ),
      )
    },
    getLineColorByCongest(congest) {
      if (congest <= 1.5) return '#28a745' // 낮음: 녹색
      if (congest <= 2.5) return '#ffc107' // 보통: 노란색
      if (congest <= 3.5) return '#fd7e14' // 혼잡: 주황색
      return '#dc3545' // 매우 혼잡: 빨간색
    },
  },
}
</script>

<style scoped>
.traffic-title {
  font-size: 20px;
  font-weight: bold;
  color: #92bf3d;
  margin-bottom: 15px;
  border-bottom: 2px solid #92bf3d;
  padding-bottom: 8px;
}

.action-button-inline {
  background: none; /* 배경 제거 */
  border: none; /* 테두리 제거 */
  color: #2053d2; /* 텍스트 색상 (파란색으로 강조) */
  font-size: 16px; /* 부모 요소의 글자 크기를 상속 */
  font-weight: normal; /* 일반 텍스트와 유사하게 보이도록 설정 */
  cursor: pointer; /* 클릭 가능하다는 느낌 제공 */
  padding: 0; /* 여백 제거 */
  text-decoration: underline dotted; /* 점선 밑줄로 강조 */
  margin-left: 5px; /* 제목과 간격 조정 */
  display: inline; /* 텍스트처럼 인라인으로 배치 */
  transition: color 0.3s ease; /* 색상 변경 효과 */
}

.action-button-inline:hover {
  color: #103a91; /* 호버 시 색상 진하게 변경 */
  text-decoration: underline; /* 밑줄 강조 */
}

.traffic-info {
  position: relative;
  height: 570px;
  background: #f5f7fa;
  overflow: hidden;
}

.traffic-content {
  height: 100%;
  padding: 15px;
  display: flex;
  flex-direction: column;
}

.results {
  flex-grow: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.results-container {
  flex-grow: 1;
  height: 455px;
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #e4e8ee;
  scrollbar-width: thin;
  scrollbar-color: #a0a0a0 #e0e0e0;
}

/* Custom scrollbar for webkit browsers */
.results-container::-webkit-scrollbar {
  width: 8px;
}

.results-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.results-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

.results-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.result-item {
  display: flex;
  flex-direction: column;
  padding: 8px 12px; /* 패딩 조정으로 공간 최적화 */
  border-bottom: 1px solid #eaeef3;
  background-color: #ffffff;
  transition:
    background-color 0.2s ease,
    transform 0.1s ease;
}

.result-item:hover {
  background-color: #f8f9fa; /* 호버 시 약간의 배경 강조 */
  transform: scale(1.01); /* 호버 시 살짝 확대 */
}

.result-item:last-child {
  border-bottom: none;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
  color: #2c3e50;
  margin-bottom: 6px; /* 헤더와 내용 간격 */
}

.result-header p {
  margin: 0;
  font-weight: 600;
}

.result-details {
  display: flex;
  flex-wrap: wrap; /* 작은 화면에서 줄바꿈 */
  gap: 6px; /* 항목 간격 */
  font-size: 0.8rem;
  color: #54657e;
  line-height: 1.4; /* 가독성 개선 */
}

.result-details p {
  margin: 0;
  display: inline; /* 인라인 텍스트로 정렬 */
}

.result-card {
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  width: 100%;
  box-sizing: border-box;
  background: #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.result-header {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  text-align: center; /* 중앙 정렬 */
  white-space: nowrap; /* 한 줄로 표시 */
  overflow: hidden; /* 넘치는 내용 숨김 */
  text-overflow: ellipsis; /* 말줄임표 처리 */
}

.result-details {
  flex-grow: 1; /* 가변적 내용 영역 */
  height: 290px; /* 고정된 영역 크기 */
  border-top: 1px solid #ddd;
  padding-top: 10px;
  color: #333;
  font-size: 12px;
  line-height: 1.5;
}

.subpath-info {
  margin-bottom: 8px;
}

.label {
  font-weight: bold;
  margin-right: 5px;
  color: #5c8126;
}

.subway-line-draggable {
  display: flex;
  overflow-x: auto; /* 수평 스크롤 활성화 */
  gap: 10px;
  cursor: grab; /* 드래그 가능 커서 */
}

.station-item {
  min-width: 150px; /* 각 역의 최소 너비 */
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.station-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.station-detail {
  color: #666;
}
</style>
