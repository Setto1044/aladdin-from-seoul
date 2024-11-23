<template>
  <div class="map-container">
    <div class="map-overlay">
      <div id="map-background" style="width: 100%; height: 100vh" />
      <div class="map-info"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

// 부모 컴포넌트로 이벤트를 전달하는 emit 정의
const emit = defineEmits(['map-created'])

// `mapInstance`를 Vue `ref`로 관리
const mapInstance = ref(null)

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

onMounted(async () => {
  try {
    const kakaoMaps = await loadKakaoMap()
    const mapContainer = document.getElementById('map-background')

    // Kakao Map 인스턴스 생성
    mapInstance.value = new kakaoMaps.Map(mapContainer, {
      center: new kakaoMaps.LatLng(37.571924, 126.975391), // 초기 중심 좌표
      level: 4, // 초기 줌 레벨
    })

    // 생성된 `mapInstance`를 상위 컴포넌트로 emit
    emit('map-created', mapInstance.value)
  } catch (error) {
    console.error('Error loading Kakao Map:', error)
  }
})

onUnmounted(() => {
  // 컴포넌트 언마운트 시 필요한 정리 작업 (현재는 추가 작업 없음)
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
