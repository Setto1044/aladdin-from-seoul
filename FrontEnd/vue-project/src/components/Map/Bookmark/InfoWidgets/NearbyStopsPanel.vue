<template>
  <div class="transport-info">
    <h2 class="section-title">🚌 교통 정보</h2>

    <div class="bus-info">
      <h3 class="sub-title">버스 정류장 (총 {{ busCount }}개)</h3>
      <ul class="station-list">
        <li v-for="station in busStations" :key="station.stationName" class="station-item compact">
          <strong class="station-name">{{ station.stationName }}</strong>
          <span class="station-detail">버스 개수: {{ station.busCount }}</span>
        </li>
      </ul>
    </div>

    <div class="subway-info">
      <h3 class="sub-title">지하철역 (총 {{ subwayCount }}개)</h3>
      <ul class="station-list">
        <li
          v-for="station in subwayStations"
          :key="station.stationName"
          class="station-item compact"
        >
          <strong class="station-name">{{ station.stationName }}</strong>
          <span class="station-detail">노선명: {{ station.subwayLaneName }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    lat: {
      type: String,
      required: true,
    },
    lng: {
      type: String,
      required: true,
    },
    mapInstance: {
      type: Object, // Map instance passed from parent
      required: true,
    },
    isSidebarOpen: {
      type: Boolean,
      required: true,
    },
    selectedItem: {
      type: Object,
      required: true,
    },
  },
  beforeDestroy() {
    // 컴포넌트가 제거되기 전에 마커 정리
    console.log('NearbyStopsPanel being destroyed')
    this.clearMarkers()
    this.resetData()
  },
  data() {
    return {
      apiUrl: 'https://api.odsay.com/v1/api/pointBusStation',
      busStations: [],
      subwayStations: [],
      busCount: 0,
      subwayCount: 0,
      busMarkers: [], // To track bus stop markers
      subwayMarkers: [], // To track subway station markers
    }
  },
  watch: {
    lat: 'fetchData', // 위도 변경 시 데이터 요청
    lng: 'fetchData', // 경도 변경 시 데이터 요청
    isSidebarOpen: {
      immediate: true,
      handler(newVal, oldVal) {
        console.log('Sidebar open state changed:', newVal, oldVal)

        if (!newVal) {
          // 사이드바가 닫힐 때
          console.log('Sidebar closed, clearing markers...')
          this.clearMarkers()
          this.resetData()
        } else if (newVal && this.lat && this.lng) {
          // 사이드바가 열릴 때
          console.log('Sidebar opened, fetching data...')
          this.fetchData()
        }
      },
    },
    selectedItem: {
      immediate: true,
      handler(newVal) {
        if (!newVal) {
          console.log('Selected item cleared')
          this.clearMarkers()
          this.resetData()
        }
      },
    },
  },
  methods: {
    async fetchData() {
      try {
        // 데이터 초기화
        this.resetData()

        const response = await fetch(
          `https://api.odsay.com/v1/api/pointBusStation?lang=0&x=${this.lng}&y=${this.lat}&radius=600&apiKey=${import.meta.env.VITE_ODSAY_DEV_API_KEY}`,
        )
        const data = await response.json()
        console.log('API response:', data)

        const processedData = this.processData(data)
        this.busStations = processedData.busData.slice(0, 6) // Only 6 bus stops
        this.subwayStations = processedData.subwayData

        this.busCount = processedData.busCount
        this.subwayCount = processedData.subwayCount

        // Add markers for the fetched data
        this.addMarkers()
      } catch (error) {
        console.error('Error fetching data:', error)
      }
    },
    processData(data) {
      const busStations = data.result.lane.filter((station) => station.stationClass === 1)
      const subwayStations = data.result.lane.filter((station) => station.stationClass === 2)

      const busData = busStations.map((station) => ({
        stationName: station.stationName,
        busCount: station.busList.length,
        x: station.x,
        y: station.y,
      }))

      const subwayData = subwayStations.map((station) => ({
        stationName: station.stationName,
        subwayLaneName: station.subwayLaneName,
        x: station.x,
        y: station.y,
      }))

      return {
        busData,
        subwayData,
        busCount: busStations.length,
        subwayCount: subwayStations.length,
      }
    },
    addMarkers() {
      this.clearMarkers() // Ensure no duplicate markers

      var busMarkerImage = new kakao.maps.MarkerImage(
        'https://map.pstatic.net/resource/api/v2/image/maps/selected-marker/bus_stop.png?version=3',
        new kakao.maps.Size(43, 57),
        new kakao.maps.Point(13, 34),
      )
      this.busMarkers = this.busStations.map((station) => {
        const position = new kakao.maps.LatLng(station.y, station.x)
        const marker = new kakao.maps.Marker({
          position,
          map: this.mapInstance,
        })
        marker.setImage(busMarkerImage)
        return marker
      })

      var subwayMarkerImage = new kakao.maps.MarkerImage(
        'https://map.pstatic.net/resource/api/v2/image/maps/selected-marker/subway.png?version=11',
        new kakao.maps.Size(43, 57),
        new kakao.maps.Point(13, 34),
      )
      this.subwayMarkers = this.subwayStations.map((station) => {
        const position = new kakao.maps.LatLng(station.y, station.x)
        const marker = new kakao.maps.Marker({
          position,
          map: this.mapInstance,
        })
        marker.setImage(subwayMarkerImage)
        return marker
      })

      console.log('Markers added:', this.busMarkers, this.subwayMarkers)
    },
    clearMarkers() {
      console.log('Clearing markers...')

      if (this.busMarkers && Array.isArray(this.busMarkers)) {
        this.busMarkers.forEach((marker) => {
          if (marker && typeof marker.setMap === 'function') {
            marker.setMap(null)
          }
        })
        this.busMarkers = []
      }

      if (this.subwayMarkers && Array.isArray(this.subwayMarkers)) {
        this.subwayMarkers.forEach((marker) => {
          if (marker && typeof marker.setMap === 'function') {
            marker.setMap(null)
          }
        })
        this.subwayMarkers = []
      }

      console.log('Markers cleared')
    },

    resetData() {
      console.log('Resetting data and markers...')
      this.busStations = []
      this.subwayStations = []
      this.busCount = 0
      this.subwayCount = 0
      this.clearMarkers() // 마커도 제거
    },
  },
  mounted() {
    this.fetchData()
  },
}
</script>

<style scoped>
/* 전체 컨테이너 스타일 */
.transport-info {
  padding: 15px;
  background-color: #f9f9f9;
  color: #333;
}

/* 섹션 제목 */
.section-title {
  font-size: 20px;
  font-weight: bold;
  color: #2053d2;
  margin-bottom: 15px;
  border-bottom: 2px solid #2053d2;
  padding-bottom: 8px;
}

/* 서브 타이틀 */
.sub-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #555;
  border-bottom: 1px solid #ddd;
  padding-bottom: 4px;
}

/* 정류장 목록 스타일 */
.station-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

/* 각 정류장 아이템 */
.station-item {
  background-color: white;
  margin-bottom: 6px;
  padding: 10px 12px;
  border-radius: 4px;
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.station-item.compact {
  padding: 8px 10px;
  font-size: 14px;
}

/* .station-item:hover {
  transform: translateY(-2px);
  box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.15);
} */

/* 정류장 이름 스타일 */
.station-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-right: 10px;
}

/* 정류장 세부 정보 */
.station-detail {
  font-size: 12px;
  color: #666;
  text-align: right;
  white-space: nowrap;
}
</style>
