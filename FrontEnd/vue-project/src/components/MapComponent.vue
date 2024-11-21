<template>
  <div class="map-container">
    <div class="map-overlay">
      <div id="map-background" style="width: 100%; height: 100vh" />
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
      const houseCards = [
        {
          aptSeq: '11110-117',
          dongCode: '1111011800',
          sidoName: '서울특별시',
          gugunName: '종로구',
          dongName: '내수동',
          aptName: '경희궁의아침4단지',
          jibun: '73',
          latestDealAmount: '190,000',
          excluUseAr: 124.17,
          buildYear: '2004',
          latitude: '37.5726308227784',
          longitude: '126.972440824541',
          views: 0,
          aptPhotoLink:
            'https://landthumb-phinf.pstatic.net/20220321_298/land_naver_1647823862977DhChY_JPEG/1614c087008ef740142cc8ef328db835.JPG?type=m400_350',
        },
      ]
      this.displayMarkers(houseCards)
      // Axios 호출
      // axios
      //   .get(apiUrl, { params })
      //   .then((response) => {
      //     console.log('API 호출 성공:', response.data)
      //     // 필요 시 데이터를 Vue 상태에 저장
      //     this.handleApiResponse(response.data)
      //     this.displayMarkers(response.data.data.houseCards)
      //   })
      //   .catch((error) => {
      //     console.error('API 호출 오류:', error)
      //   })
    },
    handleApiResponse(data) {
      // API 응답 데이터를 처리합니다.
      // console.log('처리된 데이터:', data)
      // 예: 상태에 저장하거나 화면에 렌더링
    },
    displayMarkers(houseData) {
      console.log(houseData)

      for (let i = 0; i < houseData.length; i++) {
        const house = houseData[i]

        // 마커 생성
        const marker = new kakao.maps.Marker({
          map: this.map,
          position: new kakao.maps.LatLng(house.latitude, house.longitude),
        })

        // 인포윈도우 생성
        const infoWindow = new kakao.maps.InfoWindow({
          content: `<div style="padding:5px; font-size:12px;">${house.aptName}</div>`,
        })

        // 상태 변수: InfoWindow가 열려 있는지 여부를 추적
        let isInfoWindowOpen = false

        // Add click event to marker
        kakao.maps.event.addListener(marker, 'click', () => {
          console.log('Marker clicked:', house)
          // Emit event with marker data
          this.$emit('marker-clicked', house)
        })

        // 마커에 mouseover 이벤트 추가
        kakao.maps.event.addListener(marker, 'mouseover', () => {
          if (!isInfoWindowOpen) {
            infoWindow.open(this.map, marker) // 인포윈도우 열기
            isInfoWindowOpen = true
          }
          console.log('over', isInfoWindowOpen)
        })

        // 마커에 mouseout 이벤트 추가
        kakao.maps.event.addListener(marker, 'mouseout', () => {
          if (isInfoWindowOpen) {
            infoWindow.close() // 인포윈도우 닫기
            isInfoWindowOpen = false
          }
          console.log('out', isInfoWindowOpen)
        })
      }
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
