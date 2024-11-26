<template>
  <div class="auth-container">
    <div class="form-section">
      <div class="welcome-container">
        <h1>알라딘 프롬 서울에 오신 것을 환영합니다</h1>
        <p>손쉬운 부동산 매물 조회와 전전세 양도 게시판 사용!</p>
      </div>
      <div class="tabs">
        <button :class="{ active: activeTab === 'login' }" @click="activeTab = 'login'">
          로그인
        </button>
        <button :class="{ active: activeTab === 'register' }" @click="activeTab = 'register'">
          회원가입
        </button>
        <button :class="{ active: activeTab === 'forgot' }" @click="activeTab = 'forgot'">
          비밀번호 찾기
        </button>
      </div>

      <div class="form-container">
        <component :is="activeFormComponent" />
      </div>
    </div>
    <div class="image-section">
      <Carousel v-bind="config">
        <Slide v-for="(slide, index) in slides" :key="index">
          <img :src="slide.image" :alt="slide.alt" class="slider-image" />
        </Slide>
      </Carousel>
    </div>
  </div>
</template>

<script>
import { Carousel, Slide } from 'vue3-carousel'
import LoginForm from '@/components/User/LoginPage.vue'
import RegisterForm from '@/components/User/SignupPage.vue'
import ForgotForm from '@/components/User/ForgotPasswordPage.vue'

export default {
  components: {
    Carousel,
    Slide,
    LoginForm,
    RegisterForm,
    ForgotForm,
  },
  data() {
    return {
      activeTab: 'login', // 'login', 'register', 'forgot'
      loginForm: {
        id: '',
        password: '',
      },
      registerForm: {
        id: '',
        password: '',
        confirmPassword: '',
        email: '',
      },
      idError: false,
      slides: [
        { image: '/slider/img1.png', alt: 'Slide 1' },
        { image: '/slider/img2.png', alt: 'Slide 2' },
        { image: '/slider/img3.png', alt: 'Slide 3' },
        { image: '/slider/img4.png', alt: 'Slide 4' },
        { image: '/slider/img5.png', alt: 'Slide 5' },
        { image: '/slider/img6.png', alt: 'Slide 6' },
      ],
      config: {
        autoplay: 2000, // 수정: 자동 재생 설정
        wrapAround: true, // 순환 슬라이더
      },
    }
  },
  computed: {
    activeFormComponent() {
      switch (this.activeTab) {
        case 'register':
          return 'RegisterForm'
        case 'forgot':
          return 'ForgotPasswordForm'
        default:
          return 'LoginForm'
      }
    },
  },
}
</script>

<style scoped>
/* 전체 컨테이너 */
.auth-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: stretch;
  padding: 20px;
  height: 90vh; /* 화면 높이에 맞춤 */
  max-width: 1200px;
  margin: auto;
  gap: 20px;
  box-sizing: border-box;
}

/* 폼 섹션 */
.form-section {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.welcome-container h1 {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 10px;
}

.welcome-container p {
  font-size: 1rem;
  color: #555;
  line-height: 1.5;
}

/* 탭 버튼 */
.tabs {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.tabs button {
  padding: 10px 20px;
  font-size: 0.9rem;
  color: #555;
  background-color: #f1f1f1;
  border: 1px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tabs button.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.tabs button:hover {
  background-color: #e9f3ff;
  color: #007bff;
}

/* 폼 컨테이너 */
.form-container {
  width: 100%;
  max-width: 400px;
  margin: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

/* 이미지 섹션 */
.image-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.slider-image {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 12px;
}

/* 제출 버튼 */
.submit-button {
  width: 100%;
  padding: 12px;
  font-size: 1rem;
  color: white;
  background-color: #007bff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-button:hover {
  background-color: #0056b3;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .auth-container {
    flex-direction: column;
    height: auto;
    gap: 40px;
  }

  .form-section,
  .image-section {
    width: 100%;
  }

  .form-container {
    max-width: 100%;
  }
}
</style>
