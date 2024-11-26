<script setup>
import 'vue3-carousel/dist/carousel.css'
import { Carousel, Slide, Navigation } from 'vue3-carousel'
import { ref } from 'vue'

const config = {
  autoplay: 2000,
  itemsToShow: 3, // 한 화면에 보여줄 카드 수
  wrapAround: true, // 순환 슬라이더
  transition: 500, // 슬라이드 전환 속도
  pauseAutoplayOnHover: true,
}
</script>

<template>
  <div class="neighborhood-content">
    <h2 class="neighborhood-title">✏️ {{ data.gugunName || '동네 정보' }}</h2>
    <div class="neighborhood-slider">
      <Carousel v-bind="config">
        <Slide>
          <div class="info-card dynamic-indicator">
            <div class="info-card-header">상권 변화 지표</div>
            <div class="info-card-body">
              <p>{{ data.businessChangeIndicator || '정보 없음' }}</p>
            </div>
          </div>
        </Slide>
        <Slide>
          <div class="info-card cctv-info">
            <div class="info-card-header">CCTV</div>
            <div class="info-card-body">
              <p>{{ simplifyNumber(data.cctv) || '정보 없음' }} 대 설치</p>
              <p class="highlight">{{ data.cctvRank || '정보 없음' }}위</p>
            </div>
          </div>
        </Slide>
        <Slide>
          <div class="info-card floating-population">
            <div class="info-card-header">유동 인구</div>
            <div class="info-card-body">
              <p>{{ simplifyNumber(data.totalFloatingPopulation) || '정보 없음' }} 명</p>
              <p class="highlight">{{ data.floatingPopulationRank || '정보 없음' }}위</p>
            </div>
          </div>
        </Slide>
        <Slide>
          <div class="info-card working-population">
            <div class="info-card-header">근로 인구</div>
            <div class="info-card-body">
              <p>{{ simplifyNumber(data.avgWorkingPopulation) || '정보 없음' }} 명</p>
              <p class="highlight">{{ data.workingPopulationRank || '정보 없음' }}위</p>
            </div>
          </div>
        </Slide>
        <Slide>
          <div class="info-card residential-population">
            <div class="info-card-header">거주 인구</div>
            <div class="info-card-body">
              <p>{{ simplifyNumber(data.totalResidentialPopulation) || '정보 없음' }} 명</p>
              <p class="highlight">{{ data.residentialPopulationRank || '정보 없음' }}위</p>
            </div>
          </div>
        </Slide>
        <Slide>
          <div class="info-card apartment-price">
            <div class="info-card-header">평균 아파트 가격</div>
            <div class="info-card-body">
              <p>{{ formatCurrency(data.avgApartmentPrice) || '정보 없음' }} 원</p>
              <p class="highlight">{{ data.apartmentPriceRank || '정보 없음' }}위</p>
            </div>
          </div>
        </Slide>
        <Slide>
          <div class="info-card apartment-complex">
            <div class="info-card-header">아파트 단지</div>
            <div class="info-card-body">
              <p>
                평균 단지 수: {{ simplifyNumber(data.avgApartmentComplexes) || '정보 없음' }} 개
              </p>
              <p class="highlight">{{ data.apartmentComplexesRank || '정보 없음' }}위</p>
            </div>
          </div>
        </Slide>

        <!-- 네비게이션 버튼 추가 -->
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <!-- 뉴스 섹션 -->
    <div class="news-section">
      <h3>📰 {{ data.gugunName || '동네' }} 뉴스</h3>
      <ul>
        <li v-for="news in newsData" :key="news.link" class="news-item">
          <a :href="news.link" target="_blank">
            <h4 v-html="news.title"></h4>
            <p v-html="news.description"></p>
            <span>{{ new Date(news.pubDate).toLocaleString() }}</span>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

const newsData = ref([]) // 뉴스 데이터를 저장할 ref

async function fetchNews(query) {
  try {
    const response = await axios.get(
      `/api/naver-news/v1/search/news.json?query=${query}&display=3&sort=sim`,
      {
        headers: {
          'X-Naver-Client-Id': import.meta.env.VITE_X_Naver_Client_Id,
          'X-Naver-Client-Secret': import.meta.env.VITE_X_Naver_Client_Secret,
        },
      },
    )
    console.log('뉴스으', query, response.data)
    if (response.data && response.data.items) {
      newsData.value = response.data.items // 뉴스 데이터 저장
      console.log('Fetched news:', newsData.value)
    }
  } catch (error) {
    console.error('Error fetching news:', error)
  }
}

function simplifyNumber(num) {
  if (num == null || isNaN(num)) return null

  if (num >= 100000000) {
    return `${Math.floor(num / 100000000)}억`
  } else if (num >= 10000) {
    return `${Math.floor(num / 10000)}만`
  } else if (num >= 1000) {
    return `${Math.floor(num / 1000)}천`
  } else if (num >= 100) {
    return `${Math.floor(num / 100)}백`
  } else {
    return `${num}`
  }
}

function formatCurrency(num) {
  const simplified = simplifyNumber(num)
  return simplified ? `${simplified}` : null
}

export default {
  props: {
    selectedItem: {
      type: Object,
      required: false,
    },
  },
  data() {
    return {
      data: {}, // 동네 정보를 저장할 객체
    }
  },
  watch: {
    selectedItem: {
      immediate: true,
      handler(newVal) {
        if (!newVal || !newVal.dongCode) {
          console.log('Selected item cleared or dongCode missing')
          this.resetData()
        } else {
          console.log('Selected item changed:', newVal)
          this.fetchData()
        }
      },
    },
  },
  methods: {
    simplifyNumber,
    formatCurrency,
    resetData() {
      console.log('Resetting data...')
      this.data = {} // 데이터 초기화
      newsData.value = [] // 뉴스 데이터 초기화
    },
    async fetchData() {
      try {
        let dongCode = this.selectedItem?.dongCode

        // 주소 값이 있는 경우 Kakao API 호출
        if (!dongCode && this.selectedItem?.item?.roomBoardVo?.address) {
          const address = this.selectedItem.item.roomBoardVo.address

          console.log('Fetching dongCode for address:', address)

          const kakaoResponse = await axios.get(
            `https://dapi.kakao.com/v2/local/search/address.json`,
            {
              params: {
                query: address, // 주소 값
              },
              headers: {
                Authorization: `KakaoAK ${import.meta.env.VITE_KakaoAK}`, // Kakao API 키
              },
            },
          )

          if (kakaoResponse.data.documents && kakaoResponse.data.documents.length > 0) {
            const firstResult = kakaoResponse.data.documents[0]
            dongCode = firstResult.address.b_code // 법정동 코드 가져오기
            console.log('DongCode fetched from Kakao API:', dongCode)
          } else {
            console.error('No results found for the address:', address)
            return
          }

          // gugunName 기반 뉴스 데이터 가져오기
          if (address.split(' ')[1]) {
            await fetchNews(address.split(' ')[1])
          }
        }

        // 동코드가 없는 경우 종료
        if (!dongCode) {
          console.error('DongCode is missing')
          return
        }

        // 상권 정보 API 호출
        const response = await axios.get(
          `http://localhost:8080/aladin/api/commercial-status/${dongCode}`,
        )

        if (response.data.success) {
          this.data = response.data.data || {}
          console.log('Fetched data:', this.data)

          // gugunName 기반 뉴스 데이터 가져오기
          if (this.data.gugunName) {
            await fetchNews(this.data.gugunName)
          }
        }
      } catch (error) {
        console.error('Error fetching neighborhood info:', error)
      }
    },
  },
  mounted() {
    // 초기 데이터 로드
    this.fetchData()
  },
}
</script>

<style scoped>
.neighborhood-content {
  padding: 15px;
  background-color: #f9f9f9;
  color: #333;
}

.neighborhood-title {
  font-size: 20px; /* 최소 16px, 최대 20px */
  font-weight: bold;
  color: #44afbe;
  margin-bottom: 15px;
  border-bottom: 2px solid #44afbe;
  padding-bottom: 8px;
}

/* 전체 컨테이너 */
.neighborhood-slider {
  font-family: Arial, sans-serif;
  max-width: 800px;
  text-align: center;
  padding-bottom: 20px;
}

.neighborhood-slider h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 24px;
  font-weight: bold;
}

.neighborhood-info {
  font-family: Arial, sans-serif;
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background-color: #f9f9f9;
  text-align: center;
}

.neighborhood-info h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 20px;
  font-weight: bold;
}

.info-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100px; /* 카드 너비 고정 */
  height: 150px; /* 카드 높이 고정 */
  overflow: hidden; /* 넘치는 내용 숨김 */
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.info-card-header {
  font-size: 14px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 10px;
  text-align: center; /* 중앙 정렬 */
  overflow: hidden; /* 내용 넘침 방지 */
  text-overflow: ellipsis; /* 말줄임표 처리 */
}

.info-card-body {
  font-size: 12px;
  color: #75767a;
}

.info-card-body .highlight {
  color: #000000; /* 주요 텍스트 강조 */
  font-size: 16px;
  font-weight: bold;
}

/* 비활성 슬라이드 상태 */
.carousel__slide {
  opacity: 0.6; /* 기본 상태는 흐림 */
  transform: scale(0.8); /* 기본 상태는 작게 */
}

.carousel__slide--prev,
.carousel__slide--next {
  transform: scale(0.8);
  z-index: 1; /* 우선순위 높임 */
}

.carousel__slide--active {
  transform: scale(1);
  z-index: 10;
  opacity: 1; /* 기본 상태는 흐림 */
  transition:
    transform 0.5s ease,
    opacity 0.5s ease;
}

.dynamic-indicator {
  background-color: #ffeb3b;
}

.cctv-info {
  background-color: #e1f5fe;
}

.floating-population {
  background-color: #e8f5e9;
}

.residential-population {
  background-color: #fffde7;
}

.apartment-price {
  background-color: #fbe9e7;
}

.apartment-complex {
  background-color: #f3e5f5;
}

.working-population {
  background-color: #ede7f6;
}

/* 뉴스 섹션 스타일 */
/* 뉴스 섹션 스타일 */
.news-section {
  padding: 15px;
  background-color: #f7f6f4;

  box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.1);
}

.news-section h3 {
  font-size: 18px;
  font-weight: bold;
  color: #31869e;
  margin-bottom: 15px;
  border-bottom: 2px solid #31869e;
  padding-bottom: 5px;
}

.news-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column; /* 세로로 정렬 */
  gap: 15px; /* 각 카드 간 간격 */
}

.news-item {
  background-color: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.news-item:hover {
  transform: translateY(-3px);
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.15);
}

.news-item a {
  text-decoration: none;
  color: #333;
  display: flex;
  flex-direction: column;
}

.news-item h4 {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap; /* 제목이 길 경우 생략 */
}

.news-item p {
  font-size: 12px;
  color: #666;
  margin: 0 0 10px;
  height: 3em; /* 두 줄로 제한 */
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.5;
}

.news-item span {
  font-size: 12px;
  color: #999;
  margin-top: auto; /* 항상 아래쪽에 위치 */
  align-self: flex-end;
}
</style>
