<template>
  <div class="auth-container">
    <div class="form-section">
      <div class="welcome-container">
        <h1>서비스에 오신 것을 환영합니다</h1>
        <p>매월 새롭게 업데이트 되는 다양한 UI/UX 디자인을 제약과 걱정 없이 무료로 사용해 보세요</p>
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
.auth-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: stretch;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  height: 100vh; /* 전체 화면 높이 설정 */
  box-sizing: border-box;
}
.form-section {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-sizing: border-box;
}

.form-container {
  width: 100%;
  max-width: 400px; /* 고정된 너비 설정 */
  height: 400px; /* 고정된 높이 설정 */
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
}

.image-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  padding: 20px;
  height: 100%; /* 부모 컨테이너 높이에 맞춤 */
  box-sizing: border-box;
  position: relative;
}

.slider-image {
  width: 100%;
  height: auto;
  max-height: 100%;
  object-fit: cover;
  border-radius: 15px;
}

.tabs {
  display: flex;
  margin-bottom: 20px;
}

.tabs button {
  margin-right: 10px;
  padding: 10px 20px;
  border: none;
  background: #eee;
  cursor: pointer;
  border-radius: 5px;
}

.tabs button.active {
  background: #007bff;
  color: white;
}

.submit-button {
  width: 100%;
  padding: 10px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-button:hover {
  background: #0056b3;
}
</style>
