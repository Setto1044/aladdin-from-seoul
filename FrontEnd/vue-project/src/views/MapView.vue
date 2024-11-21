<template>
  <div class="property-map">
    <FilterBar></FilterBar>

    <div class="map-section">
      <div class="sidebar-container">
        <!-- Sidebar 1 -->
        <Sidebar1
          class="sidebar sidebar1"
          :isOpen="isSidebar1Open"
          :openSidebar2="toggleSidebar2"
          @select-item="openSidebar2"
          @close1="handleCloseSidebar12"
        />

        <!-- Sidebar 2 -->
        <Sidebar2 class="sidebar sidebar2" :isOpen="isSidebar2Open" @close="handleCloseSidebar2" />
      </div>
      <!-- Map -->
      <MapComponent
        ref="mapComponent"
        @click="handleMapClick"
        @marker-clicked="handleMarkerClick"
        class="map-component"
      />
    </div>
  </div>
</template>

<script>
import MapComponent from '@/components/MapComponent.vue'
import Sidebar1 from '@/components/MapSidebar1.vue'
import Sidebar2 from '@/components/MapSidebar2.vue'
import FilterBar from '@/components/Filter/FilterBar.vue'

export default {
  name: 'MapView',
  components: {
    Sidebar1,
    Sidebar2,
    MapComponent,
    FilterBar,
  },
  data() {
    return {
      isSidebar1Open: false,
      isSidebar2Open: false,
    }
  },
  mounted() {
    this.updateNavHeight()
    window.addEventListener('resize', this.updateNavHeight) // Recalculate on window resize
  },
  beforeDestroy() {
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
      this.isSidebar2Open = false // Close Sidebar 2 when Sidebar 1 is toggled
    },
    toggleSidebar2() {
      this.isSidebar2Open = true
    },
    openSidebar2(params) {
      console.log(params)
      this.isSidebar2Open = true
    },
    handleMapClick() {
      console.log('MapComponent clicked')
      // this.isSidebar1Open = false
      // this.isSidebar2Open = false
    },
    handleCloseSidebar2() {
      this.isSidebar2Open = false
    },
    handleCloseSidebar12() {
      this.isSidebar1Open = false
      this.isSidebar2Open = false
    },
    handleMarkerClick(house) {
      console.log('Marker clicked in parent:', house)
      // Perform actions with the marker data (e.g., open a sidebar or display details)
      this.toggleSidebar1()
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
