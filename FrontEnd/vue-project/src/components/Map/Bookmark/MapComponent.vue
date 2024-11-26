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
  font-size: 13px; /* 기본 글자 크기 */
  overflow: hidden; /* 넘칠 경우 숨김 */
  white-space: nowrap; /* 한 줄로 제한 */
  text-overflow: ellipsis; /* 넘칠 경우 ... 표시 */
}

/* 오버레이 가격 */
.custom-overlay .overlay-price {
  color: #bf6b8d;
  font-size: 11px;
  margin-top: 5px;
  overflow: hidden; /* 넘칠 경우 숨김 */
  white-space: nowrap; /* 한 줄로 제한 */
  text-overflow: ellipsis; /* 넘칠 경우 ... 표시 */
}

.custom-overlay-share {
  position: relative;
  width: 120px;
  background-color: #2c2c2c;
  border: 1px solid #444;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  text-align: center;
  font-size: 12px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  padding: 10px;
}

/* 말풍선 꼬리 */
.custom-overlay-share .overlay-tail-share {
  position: absolute;
  bottom: -10px; /* 말풍선 꼬리가 아래쪽으로 살짝 나옴 */
  left: 50%; /* 가로 중앙 정렬 */
  transform: translateX(-50%); /* 정렬 보정 */
  width: 0;
  height: 0;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 10px solid #2c2c2c; /* 꼬리 색상 (배경색과 동일해야 함) */
  z-index: 1; /* 부모와 겹치지 않도록 설정 */
}

/* 오버레이 다크모드 */
.custom-overlay-share {
  position: relative;
  background-color: #2c2c2c; /* 다크모드 배경색 */
  border: 1px solid #444; /* 다크모드 테두리색 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 다크모드 그림자 */
  overflow: visible; /* 말풍선 꼬리가 잘리지 않게 설정 */
  color: #f2f2f2; /* 다크모드 텍스트 색상 */
}

/* 오버레이 제목 */
.custom-overlay-share .overlay-title-share {
  font-weight: bold;
  font-size: 13px;
  color: #fff; /* 제목 텍스트 색상 */
  overflow: hidden; /* 넘칠 경우 숨김 */
  white-space: nowrap; /* 한 줄로 제한 */
  text-overflow: ellipsis; /* 넘칠 경우 ... 표시 */
}

/* 오버레이 가격 */
.custom-overlay-share .overlay-price-share {
  color: #ff8c94; /* 강조된 텍스트 색상 */
  font-size: 11px;
  margin-top: 5px;
  overflow: hidden; /* 넘칠 경우 숨김 */
  white-space: nowrap; /* 한 줄로 제한 */
  text-overflow: ellipsis; /* 넘칠 경우 ... 표시 */
}

.custom-tooltip {
  position: absolute;
  bottom: 110%; /* 부모 요소 위에 나타나도록 설정 */
  left: 50%;
  transform: translateX(-50%);
  max-width: 100px; /* 최대 너비 설정 (필요에 따라 조절)*/
  max-height: 150px;
  background-color: #ffffff3a;
  border: 1px solid rgba(0, 0, 0, 0.2); /* 약간 더 진한 테두리 */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* 그림자를 더 크게 설정 */
  border-radius: 8px;
  padding: 2px;
  z-index: 1000;
}

.tooltip-image {
  max-width: 100px;
  max-height: 150px;
  object-fit: cover;
  border-radius: 4px;
}
</style>
