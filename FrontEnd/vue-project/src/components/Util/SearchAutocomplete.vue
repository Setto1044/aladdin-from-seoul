<template>
  <div class="search-form">
    <h3>주택 검색</h3>
    <input
      type="text"
      v-model="query"
      placeholder="검색어를 입력하세요"
      @input="handleInput"
      autocomplete="off"
    />
    <ul class="autocomplete-results">
      <li v-for="(result, index) in results" :key="index" class="autocomplete-item">
        <button @click="selectResult(result.code, result.name)">
          {{ result.name }}
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'

function decrementLastNumber(str) {
  return str.replace(/(\d+)-(\d+)/, (match, part1, part2) => {
    const decremented = parseInt(part2) - 1
    return `${part1}-${decremented}`
  })
}

export default {
  data() {
    return {
      query: '',
      results: [],
      debounceTimer: null,
    }
  },
  methods: {
    handleInput(event) {
      // input 이벤트만 사용하고 그 시점의 value를 사용
      const value = event.target.value
      if (this.debounceTimer) clearTimeout(this.debounceTimer)

      this.debounceTimer = setTimeout(() => {
        this.fetchResults(value)
      }, 300)
    },

    async fetchResults(value) {
      if (value.trim().length > 0) {
        try {
          console.log('Sending request with query:', value)

          const response = await fetch(
            `http://localhost:8080/aladin/house/search?keyword=${encodeURIComponent(value.trim())}`,
          )

          if (!response.ok) {
            throw new Error('Network response was not ok')
          }

          const data = await response.json()

          if (data.success) {
            this.results = data.data.map((item) => {
              const [code, name] = item.split(', ')
              return { code, name }
            })
          } else {
            this.results = []
            console.error('Failed to fetch results:', data.message)
          }
        } catch (error) {
          console.error('Error fetching data:', error)
        }
      } else {
        this.results = []
      }
    },

    selectResult(code, name) {
      console.log(`Selected result: ${name} (Code: ${code})`)
      if (code.includes('-')) {
        // 하이픈이 있는 경우 로직
        console.log('Code contains a hyphen. Performing logic for hyphenated code.')
        // window.location.href = `http://localhost:8080/aladin/house/hyphen?code=${code}`
        this.fetchAptAndSearch(code, name)
      } else {
        // 하이픈이 없는 경우 로직

        // 1. 법정동코드를 통해 주소 알아내기
        // 2. 주소를 통해 lat, lng 알아내기
        // 2. center 이동
        this.fetchRegionAndSearch(code)
      }
    },
    async fetchAptAndSearch(code, name) {
      const newCode = decrementLastNumber(code)
      console.log(
        `http://localhost:8080/aladin/house/cards/aptname/${name}?cursorId=${newCode}&size=1`,
      )
      const houseResponse = await axios.get(
        `http://localhost:8080/aladin/house/cards/aptname/${name}?cursorId=${newCode}&size=1`,
      )
      if (houseResponse.data.success) {
        // 집 카드 데이터 전달
        const houseCard = houseResponse.data.data[0]
        console.log('라우터 이동 전 데이터 (searchHouseCard):', houseCard)

        this.$router.push({
          name: 'map',
          params: {
            searchHouseCard: JSON.stringify(houseCard), // 객체는 문자열로 변환
          },
        })
      } else {
        console.error('Failed fetchAptAndSearch')
      }
    },
    async fetchRegionAndSearch(code) {
      try {
        // 첫 번째 API 호출
        const regionResponse = await axios.get(
          `https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=${code}&is_ignore_zero=true`,
        )

        if (regionResponse.status != 200) {
          throw new Error('Region API response was not ok')
        }

        const regionData = await regionResponse.data
        if (!regionData.regcodes || regionData.regcodes.length === 0) {
          throw new Error('No region data found')
        }
        console.log(regionData)
        const regionName = regionData.regcodes[0].name
        console.log('Region name:', regionName)

        // 두 번째 API 호출
        const kakaoResponse = await axios.get(
          `https://dapi.kakao.com/v2/local/search/address.json`,
          {
            params: {
              query: regionName, // 주소 값
            },
            headers: {
              Authorization: `KakaoAK ${import.meta.env.VITE_KakaoAK}`, // Kakao API 키
            },
          },
        )

        if (kakaoResponse.status != 200) {
          throw new Error('Kakao API response was not ok')
        }

        const kakaoData = kakaoResponse.data
        console.log('Kakao API results:', kakaoData)

        // lat, lng 추출
        const lat = kakaoData.documents[0]?.y
        const lng = kakaoData.documents[0]?.x

        if (lat && lng) {
          // 위도/경도 데이터 전달
          const latLng = { latitude: lat, longitude: lng }
          console.log('라우터 이동 전 데이터 (searchLatLng):', latLng)

          this.$router.push({
            name: 'map',
            params: {
              searchLatLng: JSON.stringify(latLng), // 객체는 문자열로 변환
            },
          })
        } else {
          console.error('Failed to extract lat/lng')
          alert('좌표 데이터를 가져올 수 없습니다.')
        }
      } catch (error) {
        console.error('Error during region and Kakao search:', error)
        alert('지역 검색 중 오류가 발생했습니다.')
      }
    },
  },
}
</script>

<style scoped>
.search-form {
  max-width: 500px;
  margin: auto;
  padding: 20px;
}
input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.autocomplete-results {
  list-style: none;
  padding: 0;
  margin: 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  max-height: 200px;
  overflow-y: auto;
}
.autocomplete-item {
  margin: 5px 0;
}
button {
  background: none;
  border: none;
  text-align: left;
  width: 100%;
  padding: 10px;
  cursor: pointer;
}
button:hover {
  background-color: #f0f0f0;
}
</style>
