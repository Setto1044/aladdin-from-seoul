<template>
  <button @click.stop="toggleSidebar1">Open Sidebar 1</button>
    <div class="property-map">
      <aside @click="handleOutsideClick">
        <Sidebar1 :isOpen="isSidebar1Open" :openSidebar2="toggleSidebar2" @click.stop />
        <Sidebar2 :isOpen="isSidebar2Open" @click.stop />
      </aside>
      
          <div class="map-container">
            <div class="map-overlay">
              <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/009a8c08cec76a4f9459a6869adf2ab88fb3598ee837e136054a2fd00ed506bf?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e" alt="Map background" class="map-background" />
              <div class="map-info">
                <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/0c373e77d902744930f38f210974560cdd3f84b91e159e7e12a8793c84cb2c58?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e" alt="Map icon" class="map-icon" />
                <p class="map-description">지도 상에 부동산 매물이 위치한 곳을 확인할 수 있습니다.</p>
              </div>
            </div>
          </div>
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/49197869899ef63b19bd0b09137f149a625d88499688d6c6147ef469df8cbee3?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e" alt="" class="decorative-image" />
       
    </div>
  </template>
  
  <script>
  import Sidebar1 from '@/components/MapSidebar1.vue';
  import Sidebar2 from '@/components/MapSidebar2.vue';

  export default {
    name: 'MapView',
    components: {
      Sidebar1,
      Sidebar2
    },
    data() {
      return {
        isSidebar1Open: false,
        isSidebar2Open: false
      };
    },
    methods: {
      toggleSidebar1() {
        this.isSidebar1Open = !this.isSidebar1Open;
        this.isSidebar2Open = false; // 사이드바1이 닫힐 때 사이드바2도 닫기
      },
      toggleSidebar2() {
        this.isSidebar2Open = true;
      },
      handleOutsideClick(event) {
        // 클릭된 대상이 사이드바가 아닌 경우 모든 사이드바 닫기
        if (!event.target.closest('.sidebar')) {
          this.isSidebar1Open = false;
          this.isSidebar2Open = false;
        }
      }
    },
    mounted() {
      // 전역 클릭 이벤트를 등록
      document.addEventListener('click', this.handleOutsideClick);
    },
    beforeUnmount() {
      // 컴포넌트가 제거되기 전에 이벤트 리스너 제거
      document.removeEventListener('click', this.handleOutsideClick);
    }
  };
  </script>
  
  <style scoped>
  
  </style>