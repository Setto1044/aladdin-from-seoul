<template>
  <div class="property-item">
    <div class="property-info" @click="selectItem">
      <p class="property-price">매매 {{ item.dealAmount }}만 원</p>
      <p class="property-detail">{{ item.excluUseAr }}㎡, {{ item.floor }}층 {{ item.aptDong }}</p>
      <p class="property-confirmation">
        확인일: {{ item.dealYear }}. {{ item.dealMonth }}. {{ item.dealDay }}
      </p>
    </div>
    <!-- username이 있을 때만 즐겨찾기 버튼 표시 -->
    <button
      v-if="userStore.memberInfo.username"
      class="favorite-button"
      @click="toggleBookmark(item.no)"
    >
      <button class="bookmark-button on-bookmark-button" v-if="isBookmarked">
        <img src="@/assets/button/bookmark-on.png" alt="btnImages" class="btnImages" />
      </button>
      <button class="bookmark-button off-bookmark-button" v-else>
        <img src="@/assets/button/bookmark-off.png" alt="btnImages" class="btnImages" />
      </button>
    </button>
  </div>
</template>

<script>
import axios from 'axios'
import useUserStore from '@/stores/user-store'

export default {
  name: 'MapItem',
  props: {
    item: {
      type: Object,
      required: true,
    },
  },
  setup() {
    const userStore = useUserStore()
    return { userStore }
  },
  data() {
    return {
      isBookmarked: false, // 북마크 여부 상태
    }
  },
  mounted() {
    this.checkBookmarkStatus() // 컴포넌트가 생성될 때 북마크 상태 확인
  },
  methods: {
    async checkBookmarkStatus() {
      // API를 통해 북마크 상태 확인
      console.log('북마크', this.userStore.memberInfo.username)
      if (!this.userStore.memberInfo.username) return

      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/bookmark/deal/status?username=${this.userStore.memberInfo.username}&housedealsNo=${this.item.no}`,
        )
        this.isBookmarked = response.data.success // 서버 응답에 따라 상태 업데이트
        console.log(
          '��마크 상태 확인 성공:',
          response.data.success,
          this.item.no,
          this.userStore.memberInfo.username,
        )
      } catch (error) {
        console.error('북마크 상태 확인 실패:', error)
      }
    },
    async toggleBookmark(itemNo) {
      // 북마크 상태에 따라 POST 또는 DELETE 요청
      if (!this.userStore.memberInfo.username) {
        alert('로그인이 필요합니다!')
        return
      }

      try {
        if (this.isBookmarked) {
          // DELETE 요청 (북마크 해제)
          await axios.delete('http://localhost:8080/aladin/bookmark/deal', {
            data: {
              username: this.userStore.memberInfo.username,
              housedealsNo: itemNo,
            },
          })
          console.log('북마크 해제 성공:', itemNo)
        } else {
          // POST 요청 (북마크 추가)
          await axios.post('http://localhost:8080/aladin/bookmark/deal', {
            username: this.userStore.memberInfo.username,
            housedealsNo: itemNo,
          })
          console.log('북마크 추가 성공:', itemNo)
        }

        // 상태 반전
        this.isBookmarked = !this.isBookmarked
      } catch (error) {
        console.error('북마크 상태 변경 실패:', error)
      }
    },
    selectItem() {
      // 부모 컴포넌트로 선택 이벤트 전달
      this.$emit('select-item', this.item)
    },
  },
}
</script>

<style scoped>
.real-estate-detail {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.property-item {
  display: flex;
  justify-content: space-between; /* 정보와 버튼 좌우 배치 */
  align-items: center; /* 수직 중앙 정렬 */
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.property-item:hover {
  background-color: #f9f9f9; /* 배경색 변경 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 약간의 그림자 추가 */
  cursor: pointer; /* 마우스 포인터 변경 */
}

.property-info:hover {
  font-weight: bold; /* 텍스트 강조 */
  color: #333; /* 텍스트 색상 변경 */
}

.complex-info {
  margin-bottom: 20px;
}

.complex-title {
  font-size: 24px;
  font-weight: bold;
}

.complex-summary {
  color: #666;
}

.price-info {
  margin-top: 10px;
}

.recent-transaction {
  font-size: 18px;
  font-weight: bold;
}

.tab-buttons {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.tab-button {
  padding: 10px;
  border: none;
  background-color: #f1f1f1;
  cursor: pointer;
}

.filter-section {
  display: flex;
  gap: 10px;
  margin: 20px 0;
}

.property-list {
  margin-top: 20px;
}

.property-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.property-info {
  flex: 1;
  text-align: left;
}

.property-title {
  font-size: 18px;
  font-weight: bold;
}

.property-price {
  color: #ff0000;
}

.property-confirmation {
  color: #888;
}

.favorite-button {
  margin-left: auto; /* 오른쪽으로 이동 */
  text-align: center; /* 가운데 정렬 */
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
}

.bookmark-button {
  margin-left: auto; /* 오른쪽으로 이동 */
  text-align: center; /* 가운데 정렬 */
  background: none;
  border: none;
  cursor: pointer;
}

.btnImages {
  width: 20px;
}

.btnImages:hover {
  scale: 1.2;
  cursor: pointer; /* 마우스 포인터 변경 */
}
</style>
