<template>
  <div class="property-map">
    <FilterBar></FilterBar>

    <div class="map-section">
      <!-- 작은 탭 열기 버튼 -->
      <button class="open-sidebar-button" @click="toggleSidebar1">☰</button>

      <div class="sidebar-container">
        <!-- Sidebar 1 -->
        <Sidebar class="sidebar1" :isOpen="isSidebar1Open" @close="handleCloseSidebar12">
          <!-- 탭 -->
          <div class="tab">
            <span @click="aptClick" :class="{ active: apt }">매매</span>
            <span @click="shareClick" :class="{ active: share }">방 나누기</span>
          </div>
          <!-- Sidebar 1 내부 콘텐츠 -->
          <AptInfoPanel v-if="apt" @select-item="openSidebar2" :mapInstance="mapInstance" />
          <ShareRoomInfoPanel v-if="share" @select-item="openSidebar2" :mapInstance="mapInstance" />
        </Sidebar>

        <!-- Sidebar 2 -->
        <Sidebar class="sidebar2" :isOpen="isSidebar2Open" @close="handleCloseSidebar2">
          <!-- Sidebar 2 내부 콘텐츠 -->
          <!-- Pass property details as props -->
          <!-- <PropertyDetails :property="selectedItem" /> -->
          <ContentDisplayPanel v-if="apt" :tab="apt" :item="selectedItem" />
          <ContentDisplayPanel
            v-if="share"
            :tab="apt"
            :id="selectedItem.item.roomBoardVo.id"
            :item="selectedItem"
          />
          <NearbyStopsPanel
            ref="nearbyStopsPanel"
            :key="`nearby-${isSidebar2Open}`"
            :lat="selectedItem.latitude"
            :lng="selectedItem.longitude"
            :mapInstance="mapInstance"
            :isSidebarOpen="isSidebar2Open"
            :selectedItem="selectedItem"
            @beforeClose="handleBeforeCloseSidebar"
          />
          <TrafficHeatPanel
            :lat="selectedItem.latitude"
            :lng="selectedItem.longitude"
          ></TrafficHeatPanel>
          <LocalReviewPanel :selectedItem="selectedItem"></LocalReviewPanel>
        </Sidebar>
      </div>
      <!-- Map -->
      <MapComponent
        ref="mapComponent"
        @click="handleMapClick"
        @marker-clicked="handleMarkerClick"
        @map-created="handleMapCreated"
        class="map-component"
      />
    </div>
  </div>
</template>

<script>
import MapComponent from '@/components/Map/Bookmark/MapComponent.vue'
import FilterBar from '@/components/Map/Filter/FilterBar.vue'
import Sidebar from '@/components/Map/Util/SidebarBookmark.vue'
import AptInfoPanel from '@/components/Map/Bookmark/AptInfoPanel.vue'
import ShareRoomInfoPanel from '@/components/Map/Bookmark/ShareRoomInfoPanel.vue'
import PropertyDetails from '@/components/Map/PropertyDetails.vue'
import ContentDisplayPanel from '@/components/Map/Bookmark/InfoWidgets/ContentDisplayPanel.vue'
import NearbyStopsPanel from '@/components/Map/Bookmark/InfoWidgets/NearbyStopsPanel.vue'
import LocalReviewPanel from '@/components/Map/Bookmark/InfoWidgets/LocalReviewPanel.vue'
import TrafficHeatPanel from '@/components/Map/Bookmark/InfoWidgets/TrafficHeatPanel.vue'

import { ref } from 'vue'

export default {
  name: 'MapView',
  components: {
    MapComponent,
    FilterBar,
    Sidebar,
    AptInfoPanel,
    PropertyDetails,
    ShareRoomInfoPanel,
    ContentDisplayPanel,
    NearbyStopsPanel,
    LocalReviewPanel,
    TrafficHeatPanel,
  },
  data() {
    return {
      isSidebar1Open: true,
      isSidebar2Open: false,
      selectedMarker: null,
      selectedItem: null, // This must be initialized
      apt: true,
      share: false,
      mapInstance: null,
    }
  },
  setup() {},
  mounted() {
    this.updateNavHeight()
    window.addEventListener('resize', this.updateNavHeight) // Recalculate on window resize
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateNavHeight)
  },
  watch: {
    isSidebar2Open(newVal) {
      console.log('Sidebar2 open state changed:', newVal)
      if (!newVal) {
        // 사이드바가 닫힐 때 추가 정리 작업
        if (this.$refs.nearbyStopsPanel) {
          this.$refs.nearbyStopsPanel.clearMarkers()
          this.$refs.nearbyStopsPanel.resetData()
        }
      }
    },
  },
  methods: {
    handleMapCreated(map) {
      this.mapInstance = map // MapInstance를 저장
      console.log('맵 붙었다우 MapInstance received in parent:', this.mapInstance)
    },
    aptClick() {
      console.log('매매 클릭')
      this.apt = true
      this.share = false
      this.handleCloseSidebar2()
      console.log('apt:', this.apt, 'share:', this.share)
    },
    shareClick() {
      console.log('방 나누기 클릭')
      this.share = true
      this.apt = false
      this.handleCloseSidebar2()
      console.log('apt:', this.apt, 'share:', this.share)
    },

    updateNavHeight() {
      const navBar = document.querySelector('.top-bar') // Replace with your nav bar selector
      if (navBar) {
        const navHeight = navBar.offsetHeight
        document.documentElement.style.setProperty('--nav-height', `${navHeight}px`)
      }
    },
    toggleSidebar1() {
      console.log('오픈')
      this.isSidebar1Open = !this.isSidebar1Open
      this.isSidebar2Open = false // Close Sidebar 2 when Sidebar 1 is toggled
    },
    toggleSidebar2() {
      this.isSidebar2Open = true
    },
    openSidebar1() {
      this.isSidebar1Open = true
    },
    openSidebar2(selectedItem) {
      console.log('Sidebar2 open', selectedItem)
      this.selectedItem = selectedItem // Set the selected item
      this.isSidebar2Open = true // Open Sidebar2
    },
    handleMapClick() {
      console.log('MapComponent clicked')
      // this.isSidebar1Open = false
      // this.isSidebar2Open = false
    },
    handleCloseSidebar2() {
      console.log('Closing Sidebar2')
      // NearbyStopsPanel의 마커를 먼저 제거
      if (this.$refs.nearbyStopsPanel) {
        this.$refs.nearbyStopsPanel.clearMarkers()
      }
      this.isSidebar2Open = false // Close Sidebar2
      this.selectedItem = null // Reset the selected item
    },
    handleBeforeCloseSidebar() {
      // NearbyStopsPanel에서 정리 작업이 필요한 경우를 위한 메소드
      console.log('Handling before close actions')
    },
    handleCloseSidebar12() {
      this.isSidebar1Open = false
      this.isSidebar2Open = false
      this.selectedMarker = null
    },
    handleMarkerClick(house) {
      console.log(this.selectedMarker, house)
      if (this.selectedMarker?.aptSeq == house.aptSeq) {
        // 이미 선택된 마커를 클릭했을 때: 열려 있으면 닫지 않음
        console.log('이미 선택된 마커를 클릭했을 때: 열려 있으면 닫지 않음')
      } else {
        // 다른 마커를 클릭했을 때: 선택된 마커 갱신
        console.log('다른 마커를 클릭했을 때: 선택된 마커 갱신')
        this.selectedMarker = house
        this.openSidebar1() // 사이드바 1 열기
      }
    },
  },
}
</script>

<style scoped>
.sidebar-container {
  display: flex; /* Flexbox를 사용해 가로 정렬 */
  flex-direction: row; /* 사이드바를 가로로 나란히 배치 */
  width: 100%;
  height: 100%; /* 전체 높이를 차지 */
}
/* Main container */
.property-map {
  display: flex;
  flex-direction: column; /* FilterBar on top, Map Section below */
  height: calc(98vh - var(--nav-height)); /* Subtract nav bar height */
  position: relative; /* Required for absolutely positioned children */
}

.map-section {
  flex: 1; /* Occupy all the remaining height below FilterBar */
  position: relative; /* For absolutely positioned content if needed */
  overflow: hidden; /* Prevent content overflow if necessary */
}

/* MapComponent */
.map-component {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1; /* Ensure the map is below the sidebars */
}

/* FilterBar */
.filter-bar {
  z-index: 100; /* FilterBar above everything */
  position: relative;
}

.sidebar {
  height: calc(93vh - var(--nav-height)); /* Subtract nav bar height */
  overflow-y: auto;
}

.tab {
  display: flex;
  justify-content: space-around; /* 버튼을 균등 분배 */
  padding: 10px;
  background-color: #f9f9f9; /* 전체 배경색 */
  border-bottom: 1px solid #ddd; /* 탭 하단 경계선 */
  z-index: 100; /* 다른 요소보다 위에 표시 */
}

.tab span {
  flex: 1; /* 버튼 크기를 균등하게 분배 */
  text-align: center;
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 500;
  color: #555; /* 기본 텍스트 색상 */
  cursor: pointer;
  transition:
    color 0.3s ease,
    background-color 0.3s ease; /* 부드러운 전환 효과 */
  border-radius: 5px; /* 버튼 모서리를 살짝 둥글게 */
}

.tab span:hover {
  background-color: #f0f0f0; /* 마우스 오버 시 배경색 변경 */
  color: #000; /* 마우스 오버 시 텍스트 색상 변경 */
}

.tab span.active {
  background-color: #007bff; /* 활성화된 탭 배경색 */
  color: #fff; /* 활성화된 탭 텍스트 색상 */
  font-weight: bold; /* 활성화된 탭 강조 */
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1); /* 약간의 그림자 */
}

.open-sidebar-button {
  position: absolute;
  top: 20px; /* 맵의 위쪽에 위치 */
  left: 20px; /* 맵의 왼쪽에 위치 */
  z-index: 100; /* 맵 위에 오버레이되도록 설정 */
  background-color: #007bff; /* 파란색 배경 */
  color: white; /* 흰색 텍스트 */
  border: none;
  border-radius: 50%; /* 동그랗게 만듦 */
  width: 40px;
  height: 40px;
  font-size: 18px; /* 텍스트 크기 */
  display: flex;
  justify-content: center;
  align-items: center; /* 버튼 내용 중앙 정렬 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3); /* 그림자 효과 */
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

.open-sidebar-button:hover {
  background-color: #0056b3; /* 호버 시 더 짙은 파란색 */
  transform: scale(1.1); /* 버튼 크기 살짝 확대 */
}

.open-sidebar-button:active {
  transform: scale(0.95); /* 클릭 시 약간 축소 */
}
</style>
