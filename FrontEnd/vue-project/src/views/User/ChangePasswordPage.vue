<template>
  <div class="password-reset">
    <h3>비밀번호 변경</h3>

    <!-- 비밀번호 입력 -->
    <form @submit.prevent="handlePasswordChange">
      <div class="form-group">
        <label for="password">새 비밀번호</label>
        <input
          type="password"
          id="password"
          v-model="formData.password"
          placeholder="Enter new password"
          required
          @input="checkPasswordMatch"
        />
      </div>

      <div class="form-group">
        <label for="confirmPassword">비밀번호 확인</label>
        <input
          type="password"
          id="confirmPassword"
          v-model="confirmPassword"
          placeholder="Re-enter your password"
          required
          @input="checkPasswordMatch"
        />
        <span
          v-if="validation.passwordMessage"
          :class="{ error: !validation.password, success: validation.password }"
        >
          {{ validation.passwordMessage }}
        </span>
      </div>

      <button type="submit" :disabled="!validation.password || isSubmitting">
        {{ isSubmitting ? '처리 중...' : '비밀번호 변경' }}
      </button>
    </form>
  </div>
</template>
<script>
import axios from 'axios'

export default {
  name: 'PasswordReset',
  props: ['username'], // params로 전달된 username을 props로 받음
  mounted() {
    console.log('Received username via params:', this.username)
  },

  data() {
    return {
      formData: {
        password: '', // 새 비밀번호
      },
      confirmPassword: '',
      isSubmitting: false,
      validation: {
        password: false, // 비밀번호 일치 여부
        passwordMessage: '',
      },
    }
  },
  methods: {
    // 비밀번호 일치 여부 확인
    checkPasswordMatch() {
      if (this.formData.password === this.confirmPassword) {
        this.validation.password = true
        this.validation.passwordMessage = '비밀번호가 일치합니다.'
      } else {
        this.validation.password = false
        this.validation.passwordMessage = '비밀번호가 일치하지 않습니다.'
      }
    },

    // 비밀번호 변경 요청 처리
    async handlePasswordChange() {
      if (!this.validation.password) {
        alert('비밀번호가 일치하지 않습니다. 확인 후 다시 입력해주세요.')
        return
      }

      this.isSubmitting = true

      try {
        const response = await axios.put('http://localhost:8080/aladin/members/password', {
          username: this.username, // Replace with dynamic username if available
          password: this.formData.password,
        })
        console.log(this.username, this.formData.password)
        if (response.data.success) {
          alert(response.data.message)
          this.resetForm()
        } else {
          console.error('Error updating password:', response.data.message)
          alert('비밀번호 변경 중 오류가 발생했습니다. 다시 시도해주세요.')
        }
      } catch (error) {
        console.error('Request error:', error)
        alert('서버 요청 중 문제가 발생했습니다. 나중에 다시 시도해주세요.')
      } finally {
        this.isSubmitting = false
      }
    },

    // 폼 초기화
    resetForm() {
      this.formData.password = ''
      this.confirmPassword = ''
      this.validation.password = false
      this.validation.passwordMessage = ''
    },
  },
}
</script>

<style scoped>
.password-reset {
  max-width: 400px;
  margin: 2rem auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.form-group {
  margin-bottom: 1rem;
}

input {
  width: 100%;
  padding: 0.5rem;
  margin-top: 0.3rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  background-color: #3498db;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:disabled {
  background-color: #95a5a6;
  cursor: not-allowed;
}

span.error {
  color: red;
  font-size: 0.9rem;
}

span.success {
  color: green;
  font-size: 0.9rem;
}
</style>
