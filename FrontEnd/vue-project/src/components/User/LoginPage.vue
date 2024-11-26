<template>
  <div class="login-form">
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

      <button type="submit">Login</button>
    </form>
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
/* 전체 로그인 폼 컨테이너 */
.login-form {
  max-width: 360px;
  margin: 40px auto;
  padding: 20px 30px;
  font-family: 'Roboto', sans-serif;
}

/* 입력 폼 그룹 */
.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-size: 0.9rem;
  color: #333;
  margin-bottom: 8px;
}

input {
  width: 100%;
  padding: 10px 15px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
  transition:
    border-color 0.3s,
    box-shadow 0.3s;
}

input:focus {
  border-color: #007bff;
  box-shadow: 0 0 4px rgba(0, 123, 255, 0.3);
  outline: none;
}

/* 버튼 스타일 */
button {
  width: 100%;
  background-color: #007bff;
  color: white;
  font-size: 1rem;
  padding: 12px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition:
    background-color 0.3s,
    transform 0.2s;
}

button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button:active {
  background-color: #004085;
  transform: translateY(0);
}

/* "기억하기" 섹션 */
.remember-me {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  color: #555;
  margin-bottom: 15px;
}

.remember-me input {
  margin-right: 8px;
  width: auto;
  height: auto;
}

/* 링크 스타일 */
.links {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.links a {
  font-size: 0.9rem;
  color: #007bff;
  text-decoration: none;
  transition: color 0.3s;
}

.links a:hover {
  color: #0056b3;
  text-decoration: underline;
}

/* 반응형 디자인 */
@media (max-width: 480px) {
  .login-form {
    padding: 15px 20px;
  }

  button {
    font-size: 0.9rem;
    padding: 10px;
  }

  label {
    font-size: 0.85rem;
  }

  input {
    font-size: 0.9rem;
  }
}
</style>
