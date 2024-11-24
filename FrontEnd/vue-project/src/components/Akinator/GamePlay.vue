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
            <p>결과: {{ result }}</p>
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
      } catch (error) {
        console.error('Error fetching question:', error)
        this.isLoading = false
      }
    },
    generatePrompt() {
      if (this.answers.length === 0) {
        return `당신은 사용자가 서울특별시 내에서 자신에게 가장 잘 어울리는 동을 맞출 수 있도록 돕는 역할을 합니다.
          사용자는 당신이 던지는 질문에 '예', '아니오', '모르겠음'으로 답할 수 있습니다.
          답변을 통해 사용자의 취향과 선호도를 바탕으로 적합한 서울의 동을 제안하세요.
          대화는 친근하고 사용자가 편안하게 느낄 수 있도록 진행됩니다.`
      }
      const answerHistory = this.answers
        .map((a) => `Question: ${a.question}, Answer: ${a.answer}`)
        .join('\n')
      return `${answerHistory}\nWhat is the next question?`
    },
    handleAnswer(answer) {
      this.addUserMessage(answer)
      this.answers.push({ question: this.currentQuestion, answer })
      if (this.answers.length >= 20) {
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
              { role: 'system', content: 'You are a helpful assistant.' },
              {
                role: 'user',
                content: `${answerHistory}\nBased on the answers, what is the user thinking of?`,
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
        this.addBotMessage(`결과는: ${this.result}`)
      } catch (error) {
        console.error('Error fetching result:', error)
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
  },
}
</script>

<style scoped>
/* 공통 스타일 */
.Akinator-Game {
  text-align: center;
  font-family: Arial, sans-serif;
  height: 565px;
  max-width: 600px;
  margin: 0 auto;
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
  background-color: #f9a825; /* 밝은 노란색 */
  color: white;
  border: none;
  border-radius: 25px; /* 둥근 버튼 */
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.3s ease,
    background-color 0.3s ease;
}

.start-button:hover {
  background-color: #f57f17; /* 진한 노란색 */
  transform: scale(1.1); /* 살짝 확대 */
}

.start-button:active {
  transform: scale(0.95); /* 클릭 시 축소 */
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
  border-radius: 25px; /* 둥근 버튼 */
  cursor: pointer;
  background: linear-gradient(45deg, #42a5f5, #1e88e5); /* 그라데이션 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

.input-button:hover {
  background: linear-gradient(45deg, #1e88e5, #1565c0); /* 더 진한 색상 */
  transform: translateY(-2px); /* 살짝 위로 */
}

.input-button:active {
  transform: translateY(1px); /* 클릭 시 아래로 */
}

/* 결과 화면 다시 시작 버튼 */
.restart-button {
  font-size: 16px;
  padding: 10px 20px;
  background-color: #8e24aa; /* 보라색 */
  color: white;
  border: none;
  border-radius: 25px; /* 둥근 버튼 */
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.3s ease,
    background-color 0.3s ease;
}

.restart-button:hover {
  background-color: #7b1fa2; /* 진한 보라색 */
  transform: scale(1.05); /* 살짝 확대 */
}

.restart-button:active {
  transform: scale(0.95); /* 클릭 시 축소 */
}
</style>
