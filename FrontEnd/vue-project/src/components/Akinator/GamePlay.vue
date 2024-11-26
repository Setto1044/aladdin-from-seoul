<template>
  <div class="Akinator-Game">
    <!-- 게임 제목 -->
    <div class="chat-header">
      <h1>Akinator Chat</h1>
    </div>

    <!-- 시작 버튼 -->
    <div v-if="!isGameStarted" class="start-screen">
      <button @click="startGame" class="start-button">게임 시작</button>
    </div>

    <!-- 채팅 화면 -->
    <div v-else class="chat-container">
      <div class="chat-box">
        <!-- 채팅 메시지 -->
        <div
          v-for="(message, index) in chatMessages"
          :key="index"
          :class="['message', message.isUser ? 'user-message' : 'bot-message']"
        >
          <!-- 프로필 이미지 -->
          <img
            v-if="message.isUser"
            src="./user-avatar.png"
            alt="User Avatar"
            class="avatar user-avatar"
          />
          <img v-else src="./bot-avatar.png" alt="Bot Avatar" class="avatar bot-avatar" />
          <!-- 말풍선 -->
          <div class="bubble">
            <p v-html="message.text" class="bubble-text"></p>
          </div>
        </div>

        <!-- 로딩 상태 -->
        <div v-if="isLoading" class="loading">
          <p>입력 중...</p>
        </div>
      </div>

      <!-- 응답 입력 버튼 -->
      <div v-if="!result" class="input-container">
        <button @click="handleAnswer('예')" class="input-button">예</button>
        <button @click="handleAnswer('아니오')" class="input-button">아니오</button>
        <button @click="handleAnswer('모르겠음')" class="input-button">모르겠음</button>
      </div>

      <!-- 결과 및 다시 시작 -->
      <div v-if="result" class="result-section">
        <div class="message bot-message">
          <div class="bubble">
            <!-- <p>{{ result }}</p> -->
            당신에게 딱 맞는 장소:
            <a href="#" @click.prevent="handleResultClick(result)">{{ result }}</a>
          </div>
        </div>
        <button @click="restartGame" class="restart-button">다시 시작</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      isGameStarted: false, // 게임 시작 여부
      chatMessages: [], // 채팅 메시지 저장
      answers: [], // 사용자의 답변
      currentQuestion: '', // 현재 질문
      result: null, // 결과
      isLoading: false, // 로딩 상태
    }
  },
  methods: {
    startGame() {
      this.isGameStarted = true // 게임 시작
      this.addBotMessage('안녕하세요! Akinator 게임에 오신 것을 환영합니다.')
      this.fetchNextQuestion()
    },
    async fetchNextQuestion() {
      try {
        this.isLoading = true
        const prompt = this.generatePrompt()
        const response = await axios.post(
          'https://api.openai.com/v1/chat/completions',
          {
            model: 'gpt-4o-mini',
            messages: [
              { role: 'system', content: 'You are a helpful assistant.' },
              { role: 'user', content: prompt },
            ],
            max_tokens: 50,
            temperature: 0.7,
          },
          {
            headers: {
              Authorization: `Bearer ${import.meta.env.VITE_OPENAI_API_KEY}`,
            },
          },
        )
        this.currentQuestion = response.data.choices[0].message.content.trim()
        this.addBotMessage(this.currentQuestion)
        this.isLoading = false
        console.log(this.currentQuestion)
      } catch (error) {
        console.error('Error fetching question:', error)
        this.isLoading = false
      }
    },
    generatePrompt() {
      if (this.answers.length === 0) {
        return `
# 서울 자치구 추천 챗봇 프롬프트

## 역할 및 규칙

### 기본 설정
- 목적: 사용자에게 가장 적합한 서울시 자치구 추천
- 사용자 응답 제한: "예", "아니오", "모르겠음"만 가능
- 질문 형식: "~~보다 ~~를 선호하나요?" 형태로 통일
- 부가 설명 금지: 질문에 다른 부연 설명 첨부 불가
- 대화 시작 시 자기소개: "서울의 멋진 장소를 추천해드리죠." 외 금지

### 질문 가이드라인
- 응답 명확성: "예", "아니오", "모르겠음"으로 답할 수 있는 질문만 허용
- 이전 질문과 겹치지 않도록 질문을 생성하고, 항상 선호도를 비교하는 질문을 작성
- 질문 길이: 50자 미만으로 제한
- 질문 주제: 도시 인프라, 주변 시설, 유흥, 분위기 관련 선호도
- 질문 개수: 최대 6개로 제한
- 비교 질문: 반드시 "[옵션1]보다 [옵션2]를 더 선호하나요?"와 같이 두가지 대조되는 옵션을 질문으로 제시
- 6개 질문 후: 추가 질문 없이 결과 제시
- 예시 질문 중복 금지: 아래 예시 질문을 중복되지 않게 제공
- 추가 금지 키워드: [시골, 바다, 산, 숲, 강, 해변]

### 예시 질문
1. 조용한 주거지보다 핫플레이스가 많은 지역을 선호하나요?
3. 문화시설보다 학군을 더 선호하나요?
4. 대형 마트보다 로컬 맛집 외식을 선호하나요?
5. 교통시설보다 주차시설이 좋은 지역을 선호하나요?
6. 신축 아파트(높은 금액)보다 원룸(낮은 금액)을 선호하나요?

### 추천 프로세스
1. 사용자의 답변을 바탕으로 최적의 자치구 추천
2. 친근하고 접근하기 쉬운 대화 방식 유지
3. 6개 질문 완료 후 즉시 결과 제시 및 대화 종료

### 언어
- 모든 질문과 답변은 한국어로 진행


### 결과 제시 형식
[사용자 질문에 가장 부합하는 1개 서울 자치구]"

### 결과 선택지
서울 자치구 = [종로구, 중구, 용산구, 성동구, 광진구, 동대문구, 중랑구, 성북구, 강북구, 도봉구, 노원구, 은평구, 서대문구, 마포구, 양천구, 강서구, 구로구, 금천구, 영등포구, 동작구, 관악구, 서초구, 강남구, 송파구, 강동구]
서울 자치구 중 사용자 선택 기록에 가장 부합하는 자치구를 제공
`
      }
      const answerHistory = this.answers
        .map((a) => `Question: ${a.question}, Answer: ${a.answer}`)
        .join('\n')
      if (this.answers.length >= 6) {
        return `\n지금까지의 답변${answerHistory}을 바탕으로 "[서울의 자치구명]" 형식으로 결과만 알려주세요.
        ### 서울 자치구명
        주어진 다음 배열 중 1개 [종로구, 중구, 용산구, 성동구, 광진구, 동대문구, 중랑구, 성북구, 강북구, 도봉구, 노원구, 은평구, 서대문구, 마포구, 양천구, 강서구, 구로구, 금천구, 영등포구, 동작구, 관악구, 서초구, 강남구, 송파구, 강동구]
        `
      } else {
        return `${answerHistory}\n다음 한 개의 질문을 "~~보다 ~~를 선호하나요?" 형식으로만 작성해주세요. 50자를 넘지 않아야 합니다. 
        절대로 질문에 추가하면 안되는 금지 키워드: [시골, 바다, 산, 숲, 강, 해변, 자연]
        `
      }
    },
    handleAnswer(answer) {
      this.addUserMessage(answer)
      this.answers.push({ question: this.currentQuestion, answer })
      if (this.answers.length >= 6) {
        this.fetchResult()
      } else {
        this.fetchNextQuestion()
      }
    },
    async fetchResult() {
      try {
        const answerHistory = this.answers
          .map((a) => `Question: ${a.question}, Answer: ${a.answer}`)
          .join('\n')

        const response = await axios.post(
          'https://api.openai.com/v1/chat/completions',
          {
            model: 'gpt-4o-mini',
            messages: [
              {
                role: 'system',
                content: `당신은 서울시 자치구 추천 시스템입니다. 
                다음 대화 내용 ${answerHistory} 중 질문과
                사용자의 답변을 바탕으로 가장 적합한 서울시 자치구를 추천합니다.`,
              },
              {
                role: 'user',
                content: `${answerHistory}\n이 답변들을 바탕으로 가장 적합한 서울시 자치구를 추천해주세요. "자치구명" 형식으로 응답해주세요.`,
              },
            ],
            max_tokens: 100,
            temperature: 0.7,
          },
          {
            headers: {
              Authorization: `Bearer ${import.meta.env.VITE_OPENAI_API_KEY}`,
            },
          },
        )

        this.result = response.data.choices[0].message.content.trim()
        this.addBotMessage('추천 장소를 말씀드리죠.')
      } catch (error) {
        console.error('Error fetching result:', error)
        this.addBotMessage('죄송합니다. 결과를 가져오는 데 실패했습니다.')
      }
    },
    addBotMessage(text) {
      this.chatMessages.push({ text, isUser: false })
    },
    addUserMessage(text) {
      this.chatMessages.push({ text, isUser: true })
    },
    restartGame() {
      this.isGameStarted = false
      this.chatMessages = []
      this.answers = []
      this.currentQuestion = ''
      this.result = null
    },
    async handleResultClick(address) {
      address = '서울특별시 ' + this.removeQuotes(address)
      console.log(address)

      try {
        // 두 번째 API 호출
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
        console.error('Error fetching Kakao API:', error)
        alert('주소를 찾을 수 없습니다.')
      }
    },
    removeQuotes(str) {
      if (!str) return '' // null 또는 undefined 처리
      return str.replace(/^["']|["']$/g, '')
    },
  },
}
</script>

<style scoped>
/* 공통 스타일 */
.Akinator-Game {
  text-align: center;
  font-family: Arial, sans-serif;
  height: calc(100vh); /* Subtract nav bar height  - var(--nav-height)*/
  width: 100%;
  margin: 0 auto;
  background-color: #007bff;
  padding: 20px;
}

.chat-header {
  background-color: #007bff;
  color: white;
  padding: 10px;
  border-radius: 8px 8px 0 0;
}

/* 말풍선 스타일 */
.chat-box {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin: 10px;
  height: 400px;
  max-height: 400px;
  overflow-y: auto;
}

.message {
  display: flex;
  align-items: flex-end;
  gap: 10px;
}

.user-message {
  flex-direction: row-reverse;
}

.bot-message {
  flex-direction: row;
}

.bubble {
  max-width: 70%;
  padding: 5px;
  border-radius: 15px;
  word-break: break-word;
  position: relative;
}

.bubble-text {
  margin: 0.3rem;
}

.user-message .bubble {
  background-color: #daf8cb;
  color: #333;
  border-radius: 15px 15px 0 15px;
}

.bot-message .bubble {
  background-color: #e8e8e8;
  color: #333;
  border-radius: 15px 15px 15px 0;
}

/* 아바타 스타일 */
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.user-avatar {
  margin-left: 10px;
}

.bot-avatar {
  margin-right: 10px;
}

/* 시작 버튼 */
.start-button {
  font-size: 18px;
  padding: 12px 24px;
  background-color: #f9a825;
  /* 밝은 노란색 */
  color: white;
  border: none;
  border-radius: 25px;
  /* 둥근 버튼 */
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.3s ease,
    background-color 0.3s ease;
}

.start-button:hover {
  background-color: #f57f17;
  /* 진한 노란색 */
  transform: scale(1.1);
  /* 살짝 확대 */
}

.start-button:active {
  transform: scale(0.95);
  /* 클릭 시 축소 */
}

/* 응답 입력 버튼 */
.input-button {
  flex: 1;
  margin: 0 5px;
  padding: 12px 18px;
  font-size: 16px;
  font-weight: bold;
  color: white;
  border: none;
  border-radius: 25px;
  /* 둥근 버튼 */
  cursor: pointer;
  background: linear-gradient(45deg, #42a5f5, #1e88e5);
  /* 그라데이션 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

.input-button:hover {
  background: linear-gradient(45deg, #1e88e5, #1565c0);
  /* 더 진한 색상 */
  transform: translateY(-2px);
  /* 살짝 위로 */
}

.input-button:active {
  transform: translateY(1px);
  /* 클릭 시 아래로 */
}

/* 결과 화면 다시 시작 버튼 */
.restart-button {
  font-size: 16px;
  padding: 10px 20px;
  background-color: #8e24aa;
  /* 보라색 */
  color: white;
  border: none;
  border-radius: 25px;
  /* 둥근 버튼 */
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.3s ease,
    background-color 0.3s ease;
}

.restart-button:hover {
  background-color: #7b1fa2;
  /* 진한 보라색 */
  transform: scale(1.05);
  /* 살짝 확대 */
}

.restart-button:active {
  transform: scale(0.95);
  /* 클릭 시 축소 */
}
</style>
