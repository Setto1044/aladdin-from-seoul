<template>
  <div class="map-container">
    <div class="map-overlay">
      <div id="map-background" style="width: 100%; height: 100vh" />
      <div class="map-info"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, watchEffect } from 'vue'
import axios from 'axios'

// 부모 컴포넌트로 이벤트를 전달하는 emit 정의
const emit = defineEmits(['marker-clicked', 'house-card-clicked'])
const props = defineProps({
  searchHouseCard: Object,
})

// 전역 변수로 관리
let mapInstance = null
let clustererInstance = null
let currentZoomLevel = null
let polygons = []
let overlays = [] // 이름 오버레이를 저장할 배열

const checkMapAndSearchHouseCard = () => {
  if (mapInstance && props.searchHouseCard) {
    console.log('mapInstance와 searchHouseCard가 모두 준비됨:', props.searchHouseCard)

    // 지도 중심 설정
    const { latitude, longitude } = props.searchHouseCard
    mapInstance.setCenter(new kakao.maps.LatLng(latitude, longitude))
    console.log('지도 중심이 설정되었습니다:', latitude, longitude)

    // 이벤트 발생
    emit('house-card-clicked', props.searchHouseCard)
  } else {
    console.log('mapInstance 또는 searchHouseCard가 준비되지 않음')
  }
}

// searchHouseCard 값 변경 시 이벤트 발생
watchEffect(() => {
  if (props.searchHouseCard) {
    console.log('searchHouseCard 값 감지')
    checkMapAndSearchHouseCard()
  }
})

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

  const zoomLevel = mapInstance.getLevel()
  console.log(zoomLevel)
  if (zoomLevel > 5) return // 줌 레벨 6 초과 시 API 호출 중단

  const bounds = mapInstance.getBounds()
  const swLatLng = bounds.getSouthWest()
  const neLatLng = bounds.getNorthEast()

  const params = {
    latA: swLatLng.getLat(),
    latB: neLatLng.getLat(),
    lngA: swLatLng.getLng(),
    lngB: neLatLng.getLng(),
  }

  console.log('지도 변경 : api 호출')

  try {
    // API 호출
    const response = await axios.get('http://localhost:8080/aladin/house/cards/range', { params })
    if (response.data?.data?.houseCards) {
      displayMarkers(response.data.data.houseCards)
    }
  } catch (error) {
    console.error('Failed to fetch house data:', error)
  }
}

// 폴리곤 제거
const clearPolygons = () => {
  polygons.forEach((polygon) => polygon.setMap(null))
  overlays.forEach((overlay) => overlay.setMap(null))
  polygons = []
  overlays = []
}

// 클러스터러 초기화
const clearMarkersAndClusterer = () => {
  if (clustererInstance) {
    clustererInstance.clear()
  }
}

// 면적 중심 계산 함수
const calculatePolygonCentroid = (path) => {
  let area = 0 // 다각형 면적
  let centroidX = 0 // 중심 x좌표
  let centroidY = 0 // 중심 y좌표

  const n = path.length

  for (let i = 0; i < n; i++) {
    const current = path[i]
    const next = path[(i + 1) % n] // 마지막 점 이후 첫 점으로 이어짐

    const x0 = current.getLng() // 현재 점의 x (Longitude)
    const y0 = current.getLat() // 현재 점의 y (Latitude)
    const x1 = next.getLng() // 다음 점의 x
    const y1 = next.getLat() // 다음 점의 y

    const crossProduct = x0 * y1 - x1 * y0 // 벡터 외적
    area += crossProduct
    centroidX += (x0 + x1) * crossProduct
    centroidY += (y0 + y1) * crossProduct
  }

  area = area / 2 // 면적 계산
  centroidX = centroidX / (6 * area) // 중심 x좌표
  centroidY = centroidY / (6 * area) // 중심 y좌표

  return new kakao.maps.LatLng(centroidY, centroidX)
}

// 폴리곤 클릭 이벤트 등록 함수
const registerPolygonEvents = (polygon, name, center) => {
  // 클릭 이벤트
  kakao.maps.event.addListener(polygon, 'click', () => {
    const currentLevel = mapInstance.getLevel()

    if (currentLevel >= 6 && currentLevel <= 7) {
      // 구 폴리곤 클릭 시: 중심으로 이동, 5레벨로 확대
      console.log(`${name} 클릭됨, 중심으로 이동 및 5레벨로 확대`)
      mapInstance.setCenter(center) // 지도 중심 설정
      mapInstance.setLevel(5) // 지도 레벨 설정
    } else if (currentLevel >= 8) {
      // 시 폴리곤 클릭 시: 중심으로 이동, 7레벨로 확대
      console.log(`${name} 클릭됨, 중심으로 이동 및 7레벨로 확대`)
      mapInstance.setCenter(center) // 지도 중심 설정
      mapInstance.setLevel(7) // 지도 레벨 설정
    }
  })
}

// 폴리곤 표시
const displayArea = (coordinates, name) => {
  const path = coordinates[0].map((coord) => new kakao.maps.LatLng(coord[1], coord[0]))

  const polygon = new kakao.maps.Polygon({
    path: path,
    strokeWeight: 2,
    strokeColor: '#3d4249',
    strokeOpacity: 0.8,
    strokeStyle: 'dashed',
    fillColor: '#e9eae8',
    fillOpacity: 0.5,
  })

  polygon.setMap(mapInstance)
  polygons.push(polygon)

  // 중심 위치 계산
  const center = calculatePolygonCentroid(path)

  const customOverlay = new kakao.maps.CustomOverlay({
    map: mapInstance,
    position: center,
    content: `<div class="polygon-overlay" style="background: white; solid gray; padding: 5px;">${name}</div>`,
  })

  overlays.push(customOverlay)

  // 폴리곤에 이벤트 등록 (클릭 시 레벨 변경 포함)
  registerPolygonEvents(polygon, name, center)
}

// GeoJSON 로드 및 표시
const loadGeoJson = async (url) => {
  clearPolygons()
  try {
    const response = await axios.get(url)
    const geoJsonData = response.data

    geoJsonData.features.forEach((feature) => {
      const coordinates = feature.geometry.coordinates
      const name = feature.properties.SIG_KOR_NM
      displayArea(coordinates, name)
    })
  } catch (error) {
    console.error('Failed to load GeoJSON:', error)
  }
}

const displayMarkers = (houseData) => {
  console.log('Clearing existing markers...')
  clustererInstance.clear()

  console.log('Adding new markers...')
  const newOverlays = houseData.map((house) => {
    const content = `<div class="custom-overlay">
        <div class="overlay-content">
          <div class="overlay-title">${house.aptName}</div>
          <div class="overlay-price">${house.latestDealAmount}만원</div>
        </div>
        <div class="overlay-tail"></div>
      </div>
    `

    const overlay = new kakao.maps.CustomOverlay({
      position: new kakao.maps.LatLng(house.latitude, house.longitude),
      content: content,
      yAnchor: 1.3, // 오버레이 기준점을 꼬리에 맞춤
    })

    // 이벤트를 content에 추가하려면 DOM 객체를 직접 접근해야 함
    const overlayElement = document.createElement('div')
    overlayElement.innerHTML = content

    // 마우스 오버/아웃 이벤트 추가
    overlayElement.addEventListener('mouseover', () => {
      overlay.setZIndex(999) // 오버레이를 앞으로 가져오기
    })

    overlayElement.addEventListener('mouseout', () => {
      overlay.setZIndex(1) // 오버레이를 뒤로 보내기
    })

    overlayElement.addEventListener('click', () => {
      console.log('Overlay clicked:', house)
      emit('marker-clicked', house) // 클릭 이벤트 발생
      // 지도 중심을 마커 위치로 이동
      mapInstance.setCenter(new kakao.maps.LatLng(house.latitude, house.longitude))
    })

    // 커스텀 오버레이에 이벤트가 적용된 DOM 요소 설정
    overlay.setContent(overlayElement)
    overlay.setMap(mapInstance) // 지도에 오버레이 추가

    return overlay
  })

  // 클러스터러에 오버레이 추가
  clustererInstance.addMarkers(newOverlays)
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
      center: new kakaoMaps.LatLng(37.571924, 126.975391),
      level: 4,
    })

    // 초기값 확인
    checkMapAndSearchHouseCard()

    // 클러스터러 인스턴스 생성
    clustererInstance = new kakaoMaps.MarkerClusterer({
      map: mapInstance,
      averageCenter: true,
      minLevel: 4,
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
    // 줌 변경 이벤트 등록
    kakaoMaps.event.addListener(mapInstance, 'zoom_changed', async () => {
      const zoomLevel = mapInstance.getLevel()
      if (zoomLevel === currentZoomLevel) return
      currentZoomLevel = zoomLevel

      if (zoomLevel >= 8) {
        clearMarkersAndClusterer()
        await loadGeoJson('sido.json')
      } else if (zoomLevel >= 6 && zoomLevel < 8) {
        clearMarkersAndClusterer()
        await loadGeoJson('seoul_gson.json')
      } else if (zoomLevel <= 5) {
        clearPolygons()
        await fetchHouseData()
      }
    })

    // 초기 데이터 로드
    await fetchHouseData() // 초기 마커 표시
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

<style>
.custom-overlay {
  position: relative;
  width: 120px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  text-align: center;
  font-size: 12px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  padding: 10px;
}

/* 말풍선 꼬리 */
.custom-overlay .overlay-tail {
  position: absolute;
  bottom: -10px; /* 말풍선 꼬리가 아래쪽으로 살짝 나옴 */
  left: 50%; /* 가로 중앙 정렬 */
  transform: translateX(-50%); /* 정렬 보정 */
  width: 0;
  height: 0;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 10px solid #fff; /* 꼬리 색상 (배경색과 동일해야 함) */
  z-index: 1; /* 부모와 겹치지 않도록 설정 */
}

/* 오버레이 그림자와 꼬리를 구분하기 위해 추가 */
.custom-overlay {
  position: relative;
  background-color: #fff;
  border: 1px solid #ccc;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  overflow: visible; /* 말풍선 꼬리가 잘리지 않게 설정 */
}

/* 오버레이 제목 */
.custom-overlay .overlay-title {
  font-weight: bold;
  font-size: 14px; /* 기본 글자 크기 */
  overflow: hidden; /* 넘칠 경우 숨김 */
  white-space: nowrap; /* 한 줄로 제한 */
  text-overflow: ellipsis; /* 넘칠 경우 ... 표시 */
}

/* 오버레이 가격 */
.custom-overlay .overlay-price {
  color: green;
  font-size: 12px;
  margin-top: 5px;
  overflow: hidden; /* 넘칠 경우 숨김 */
  white-space: nowrap; /* 한 줄로 제한 */
  text-overflow: ellipsis; /* 넘칠 경우 ... 표시 */
}
</style>
