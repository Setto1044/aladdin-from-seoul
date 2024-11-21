<template>
  <div class="map-container">
    <div class="map-overlay">
      <div id="map-background" style="width: 100%; height: 70vh" />
      <div class="map-info"></div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      map: null, // Kakao map 객체
    }
  },
  mounted() {
    this.initializeMap()
  },
  methods: {
    initializeMap() {
      const mapContainer = document.getElementById('map-background') // 지도를 표시할 div
      const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.978), // 초기 중심 좌표 (예: 서울 시청)
        level: 3, // 지도 확대 레벨
      }

      // 지도 생성
      this.map = new kakao.maps.Map(mapContainer, mapOption)

      // 지도가 이동하거나 확대/축소된 후 'idle' 이벤트가 발생할 때 getInfo 호출
      kakao.maps.event.addListener(this.map, 'idle', this.fetchHouseData)
    },
    fetchHouseData() {
      console.log('지도 변경')
      // 현재 지도 영역의 경계를 가져옵니다
      const bounds = this.map.getBounds()
      const swLatLng = bounds.getSouthWest() // 남서쪽 좌표
      const neLatLng = bounds.getNorthEast() // 북동쪽 좌표

      // API 호출할 데이터 준비
      const latA = swLatLng.getLat()
      const latB = neLatLng.getLat()
      const lngA = swLatLng.getLng()
      const lngB = neLatLng.getLng()

      const apiUrl = `http://localhost:8080/aladin/house/cards/range`
      const params = {
        latA,
        latB,
        lngA,
        lngB,
      }

      // Axios 호출
      axios
        .get(apiUrl, { params })
        .then((response) => {
          console.log('API 호출 성공:', response.data)
          // 필요 시 데이터를 Vue 상태에 저장
          this.handleApiResponse(response.data)
        })
        .catch((error) => {
          console.error('API 호출 오류:', error)
        })
    },
    handleApiResponse(data) {
      // API 응답 데이터를 처리합니다.
      console.log('처리된 데이터:', data)
      // 예: 상태에 저장하거나 화면에 렌더링
    },
    getInfo() {
      // 지도 정보 얻기
      const center = this.map.getCenter()
      const level = this.map.getLevel()
      const mapTypeId = this.map.getMapTypeId()
      const bounds = this.map.getBounds()
      const swLatLng = bounds.getSouthWest()
      const neLatLng = bounds.getNorthEast()

      // 메시지 작성
      const message = `
        지도 중심좌표: 위도 ${center.getLat()}, 경도 ${center.getLng()}<br>
        지도 레벨: ${level}<br>
        지도 타입: ${mapTypeId}<br>
        남서쪽 좌표: ${swLatLng.getLat()}, ${swLatLng.getLng()}<br>
        북동쪽 좌표: ${neLatLng.getLat()}, ${neLatLng.getLng()}
      `
      console.log(message) // 콘솔에 지도 정보를 출력
    },
  },
}
</script>

<style scoped></style>
