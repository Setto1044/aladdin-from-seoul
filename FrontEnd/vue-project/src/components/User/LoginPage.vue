<template>
  <!-- <div class="form-group">
    <label for="loginId">아이디</label>
    <input type="text" id="loginId" v-model="loginForm.id" placeholder="아이디를 입력하세요" />
  </div>
  <div class="form-group">
    <label for="loginPassword">비밀번호</label>
    <input
      type="password"
      id="loginPassword"
      v-model="loginForm.password"
      placeholder="비밀번호를 입력하세요"
    />
  </div>
  <button type="submit" class="submit-button">로그인</button>
  <div class="sns-login">
    <p>SNS로 계속하기</p>
  </div> -->
  <div class="login-form">
    <h1>Login</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Username</label>
        <input
          type="text"
          id="username"
          v-model="username"
          placeholder="Enter your username"
          required
        />
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
          type="password"
          id="password"
          v-model="password"
          placeholder="Enter your password"
          required
        />
      </div>

      <div class="form-group remember-me">
        <input type="checkbox" id="rememberMe" v-model="rememberMe" />
        <label for="rememberMe">Remember Me</label>
      </div>

      <button type="submit">Login</button>
    </form>

    <div class="links">
      <router-link to="/forgot-password">Forgot Password?</router-link>
      <router-link to="/sign-up">Sign Up</router-link>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import useUserStore from '@/stores/user-store'
import { useRouter, useRoute } from 'vue-router'

export default {
  data() {
    return {
      username: localStorage.getItem('savedUsername') || '',
      password: '',
      rememberMe: !!localStorage.getItem('savedUsername'),
    }
  },
  setup() {
    const userStore = useUserStore()
    const router = useRouter()
    const route = useRoute()
    return { userStore, router, route }
  },
  methods: {
    async handleLogin() {
      // Add your login logic here (e.g., API call)
      console.log('Username:', this.username)
      console.log('Password:', this.password)

      try {
        console.log('Username:=>', this.username)
        console.log('Password:=>', this.password)
        const response = await axios.post('http://localhost:8080/aladin/members/login', {
          username: this.username,
          password: this.password,
        })
        console.log('API Response:', response)
        console.log('API Response:', response.data)

        // Check success status
        if (response.data.success) {
          console.log('Login Response:', response.data)

          // Store the user information in Pinia after successful login
          this.userStore.setLoginStatus(true)
          this.userStore.setMemberInfo({
            userid: this.username,
            username: response.data.data.username,
            name: response.data.data.name,
            nickname: response.data.data.nickname,
            email: response.data.data.email,
            grade: response.data.data.grade,
            bio: response.data.data.bio,
            profileImagePath: response.data.data.profileImagePath,
          })
          alert(response.data.message) // "로그인에 성공하였습니다."

          // Redirect to the previous page or home if no redirect is specified
          const redirectPath = this.route.query.redirect || '/'
          console.log(redirectPath)
          this.router.push(redirectPath)
        } else {
          alert('Login failed: ' + response.data.message)
        }
      } catch (error) {
        console.error('Login failed:', error)
        alert('An error occurred during login.')
      }

      // Clear password after login attempt for security
      this.password = ''
    },
  },
}
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: 0 auto;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.form-group {
  margin-bottom: 15px;
}
input {
  width: 100%;
  padding: 8px;
  margin-bottom: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
button {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
.remember-me {
  display: flex;
  align-items: center;
}
.remember-me input {
  margin-right: 5px;
}
.links {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}
.links a {
  color: #007bff;
  text-decoration: none;
}
.links a:hover {
  text-decoration: underline;
}
</style>
