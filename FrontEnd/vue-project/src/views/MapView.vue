<template>
  <div class="property-map">
    <aside>
      <button @click="toggleSidebar1"></button>
      <Sidebar1
        class="sidebar"
        :isOpen="isSidebar1Open"
        :openSidebar2="toggleSidebar2"
        @select-item="openSidebar2"
        @click.stop
        @close1="handleCloseSidebar12"
      />
      <Sidebar2 class="sidebar" :isOpen="isSidebar2Open" @close="handleCloseSidebar2" @click.stop />
    </aside>

    <MapComponent ref="mapComponent" @click="handleMapClick" />
  </div>
</template>

<script>
import MapComponent from '@/components/MapComponent.vue'
import Sidebar1 from '@/components/MapSidebar1.vue'
import Sidebar2 from '@/components/MapSidebar2.vue'

export default {
  name: 'MapView',
  components: {
    Sidebar1,
    Sidebar2,
    MapComponent,
  },
  data() {
    return {
      isSidebar1Open: false,
      isSidebar2Open: false,
    }
  },
  methods: {
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
  },
}
</script>

<style scoped>
/* Add necessary styles */
</style>
