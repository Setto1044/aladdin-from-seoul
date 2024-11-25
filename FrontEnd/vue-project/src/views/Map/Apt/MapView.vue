<template>
  <div class="property-map">
    <FilterBar @search-action="handleSearchClick"></FilterBar>

    <div class="map-section">
      <div class="sidebar-container">
        <!-- Sidebar 1 -->
        <Sidebar class="sidebar1" :isOpen="isSidebar1Open" @close="handleCloseSidebar12">
          <!-- Sidebar 1 내부 콘텐츠 -->
          <AptInfoPanel
            v-if="currentVersion"
            :complex="selectedMarker"
            @select-item="openSidebar2"
          />
          <AptNameInfoPanel v-else :query="searchQuery" @name-item="handleSearchComplexClick" />
        </Sidebar>
      </div>
      <!-- Map -->
      <MapComponent
        ref="mapComponent"
        @click="handleMapClick"
        @marker-clicked="handleMarkerClick"
        @house-card-clicked="handleHouseCardClicked"
        @search-lat-lng-event="handleLatLngClicked"
        :searchLatLng="localSearchLatLng"
        :searchHouseCard="localSearchHouseCard"
        class="map-component"
      />
    </div>
  </div>
</template>

<script>
import MapComponent from '@/components/Map/Apt/MapComponent.vue'
import FilterBar from '@/components/Map/Filter/FilterBar.vue'
import Sidebar from '@/components/Map/Util/Sidebar.vue'
import AptInfoPanel from '@/components/Map/Apt/AptInfoPanel.vue'
import AptNameInfoPanel from '@/components/Map/Apt/AptNameInfoPanel.vue'

export default {
  name: 'MapView',
  components: {
    MapComponent,
    FilterBar,
    Sidebar,
    AptInfoPanel,
    AptNameInfoPanel,
  },
  props: {
    searchLatLng: {
      type: Object,
      default: null,
    },
    searchHouseCard: {
      type: Object,
      default: null,
    },
  },
  watch: {
    searchLatLng(newValue) {
      this.localSearchLatLng = newValue // 로컬로 복사
    },
    searchHouseCard(newValue) {
      // props 변경 시 로컬 데이터 업데이트
      this.localSearchHouseCard = newValue
    },
  },
  data() {
    return {
      isSidebar1Open: false,
      selectedMarker: null,
      localSearchLatLng: this.searchLatLng, // 로컬 데이터로 복사
      localSearchHouseCard: this.searchHouseCard, // 로컬 데이터로 복사
      searchQuery: null,
      currentVersion: true,
    }
  },
  mounted() {
    this.updateNavHeight()
    window.addEventListener('resize', this.updateNavHeight) // Recalculate on window resize
    console.log('로컬', this.localSearchHouseCard, this.localSearchLatLng)
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.updateNavHeight)
  },
  methods: {
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
    },
    openSidebar1() {
      this.isSidebar1Open = true
    },
    handleMapClick() {
      console.log('MapComponent clicked')
      // this.isSidebar1Open = false
    },
    handleCloseSidebar12() {
      this.isSidebar1Open = false
      this.selectedMarker = null
      this.resetToVersionA()
    },
    handleHouseCardClicked(house) {
      this.localSearchHouseCard = null // 1회 사용 후 null로 초기화
      console.log('house-card-clicked 이벤트 발생:', house, ' 초기화->', this.localSearchHouseCard)
      if (this.selectedMarker?.aptSeq == house.aptSeq) {
        console.log('이미 선택된 하우스 카드 클릭')
      } else {
        console.log('다른 하우스 카드 클릭')
        this.selectedMarker = house
        this.openSidebar1()
      }
    },
    handleLatLngClicked() {
      this.localSearchLatLng = null // 1회 사용 후 null로 초기화
      console.log('handleLatLngClicked 이벤트 발생: 이동했음')
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
    handleSearchClick(query) {
      console.log('Search query:', query)
      this.searchQuery = query.trim() // 검색어 저장
      this.currentVersion = false // 검색 시 버전 B로 전환 (AptNameInfoPanel 활성화)
      this.openSidebar1() // 사이드바 1 열기
    },
    resetToVersionA() {
      this.currentVersion = true // 다시 버전 A로 전환
    },
    handleSearchComplexClick(house) {
      // 사이드바는 닫지 않음.
      console.log('선택하셧습니까?', house)
      this.localSearchHouseCard = house
      this.selectedMarker = house
      this.resetToVersionA()
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
}
</style>
