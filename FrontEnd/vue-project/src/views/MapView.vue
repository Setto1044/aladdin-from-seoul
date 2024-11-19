<template>
  <button @click.stop="toggleSidebar1">Open Sidebar 1</button>
  <div class="property-map">
    <aside @click="handleOutsideClick">
      <Sidebar1 :isOpen="isSidebar1Open" :openSidebar2="toggleSidebar2" @click.stop />
      <Sidebar2 :isOpen="isSidebar2Open" @click.stop />
    </aside>

    <MapComponent />
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
      this.isSidebar1Open = !this.isSidebar1Open
      this.isSidebar2Open = false // 사이드바1이 닫힐 때 사이드바2도 닫기
    },
    toggleSidebar2() {
      this.isSidebar2Open = true
    },
    handleOutsideClick(event) {
      // 클릭된 대상이 사이드바가 아닌 경우 모든 사이드바 닫기
      if (!event.target.closest('.sidebar')) {
        this.isSidebar1Open = false
        this.isSidebar2Open = false
      }
    },
  },
  mounted() {
    // 전역 클릭 이벤트를 등록
    document.addEventListener('click', this.handleOutsideClick)
  },
  beforeUnmount() {
    // 컴포넌트가 제거되기 전에 이벤트 리스너 제거
    document.removeEventListener('click', this.handleOutsideClick)
  },
}
</script>

<style scoped></style>
