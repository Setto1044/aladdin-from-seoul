<template>
  <div class="map-container">
    <div class="map-overlay">
      <div id="map-background" style="width: 100%; height: 100vh" />
      <div class="map-info"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import axios from 'axios'

// 부모 컴포넌트로 이벤트를 전달하는 emit 정의
const emit = defineEmits(['marker-clicked'])

// 전역 변수로 관리
let mapInstance = null
let clustererInstance = null

const loadKakaoMap = () => {
  return new Promise((resolve, reject) => {
    if (window.kakao && window.kakao.maps) {
      resolve(window.kakao.maps)
    } else {
      const script = document.createElement('script')
      script.src = 'https://dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_APP_KEY&libraries=clusterer'
      script.onload = () => resolve(window.kakao.maps)
      script.onerror = reject
      document.head.appendChild(script)
    }
  })
}

// 지도 영역의 데이터를 가져오는 함수
const fetchHouseData = async () => {
  if (!mapInstance) return

  const bounds = mapInstance.getBounds()
  const swLatLng = bounds.getSouthWest()
  const neLatLng = bounds.getNorthEast()

  const params = {
    latA: swLatLng.getLat(),
    latB: neLatLng.getLat(),
    lngA: swLatLng.getLng(),
    lngB: neLatLng.getLng(),
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
      latitude: '37.5726908227784',
      longitude: '126.972440824541',
      views: 0,
      aptPhotoLink:
        'https://landthumb-phinf.pstatic.net/20220321_298/land_naver_1647823862977DhChY_JPEG/1614c087008ef740142cc8ef328db835.JPG?type=m400_350',
    },
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
      longitude: '126.970440824541',
      views: 0,
      aptPhotoLink:
        'https://landthumb-phinf.pstatic.net/20220321_298/land_naver_1647823862977DhChY_JPEG/1614c087008ef740142cc8ef328db835.JPG?type=m400_350',
    },
  ]
  console.log('지도 변경 : api 호출')
  displayMarkers(houseCards)
  // try {
  //   // API 호출
  //   const response = await axios.get('http://localhost:8080/aladin/house/cards/range', { params })
  //   if (response.data?.data?.houseCards) {
  //     displayMarkers(response.data.data.houseCards)
  //   }
  // } catch (error) {
  //   console.error('Failed to fetch house data:', error)
  // }
}

// 마커 표시 함수
const displayMarkers = (houseData) => {
  if (!clustererInstance) return

  // 기존 마커 제거
  clustererInstance.clear()

  // 새 마커 생성
  const newMarkers = houseData.map((house) => {
    const marker = new kakao.maps.Marker({
      position: new kakao.maps.LatLng(house.latitude, house.longitude),
    })

    // 인포윈도우 설정
    const infoWindow = new kakao.maps.InfoWindow({
      content: `
        <div style="padding:5px;font-size:12px;">
          ${house.aptName}<br/>
          ${house.latestDealAmount}만원
        </div>
      `,
    })

    // 마커 이벤트 설정
    kakao.maps.event.addListener(marker, 'mouseover', () => {
      infoWindow.open(mapInstance, marker)
    })

    kakao.maps.event.addListener(marker, 'mouseout', () => {
      infoWindow.close()
    })

    kakao.maps.event.addListener(marker, 'click', () => {
      // 클릭 이벤트 처리
      console.log('Marker clicked:', house)
      emit('marker-clicked', house)
    })

    return marker
  })

  // 클러스터러에 마커 추가
  clustererInstance.addMarkers(newMarkers)
}

// 디바운스 함수
const debounce = (func, wait) => {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// 디바운스된 fetchHouseData
const debouncedFetchHouseData = debounce(fetchHouseData, 300)

onMounted(async () => {
  try {
    const kakaoMaps = await loadKakaoMap()
    const mapContainer = document.getElementById('map-background')

    // 맵 인스턴스 생성
    mapInstance = new kakaoMaps.Map(mapContainer, {
      center: new kakaoMaps.LatLng(36.2683, 127.6358),
      level: 14,
    })

    // 클러스터러 인스턴스 생성
    clustererInstance = new kakaoMaps.MarkerClusterer({
      map: mapInstance,
      averageCenter: true,
      minLevel: 6,
      gridSize: 60,
      styles: [
        {
          width: '50px',
          height: '50px',
          background: 'rgba(51, 153, 255, .8)',
          borderRadius: '25px',
          color: '#fff',
          textAlign: 'center',
          fontWeight: 'bold',
          lineHeight: '50px',
        },
      ],
    })

    // idle 이벤트 리스너 등록 (디바운스 적용)
    kakaoMaps.event.addListener(mapInstance, 'idle', debouncedFetchHouseData)

    // 초기 데이터 로드
    fetchHouseData()
  } catch (error) {
    console.error('Error loading Kakao Map:', error)
  }
})

// 컴포넌트 언마운트 시 정리
onUnmounted(() => {
  if (mapInstance) {
    // 이벤트 리스너 제거 등 정리 작업
    kakao.maps.event.removeListener(mapInstance, 'idle', debouncedFetchHouseData)
  }
})
</script>

<style scoped></style>
