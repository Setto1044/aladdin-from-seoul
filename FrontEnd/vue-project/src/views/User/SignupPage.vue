<template>
  <div class="signup-form">
    <h1>Sign Up</h1>
    <form @submit.prevent="handleSignup">
      <div class="form-group">
        <label for="username">Username</label>
        <input
          type="text"
          id="username"
          v-model="formData.username"
          placeholder="Enter your username"
          required
          @input="resetValidation('username')"
        />
        <button type="button" @click="validateUsername">아이디 중복 확인</button>
        <span
          v-if="validation.usernameMessage"
          :class="{ error: !validation.username, success: validation.username }"
        >
          {{ validation.usernameMessage }}
        </span>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
          type="password"
          id="password"
          v-model="formData.password"
          placeholder="Enter your password"
          required
        />
      </div>

      <div class="form-group">
        <label for="name">Name</label>
        <input
          type="text"
          id="name"
          v-model="formData.name"
          placeholder="Enter your real name"
          required
        />
      </div>

      <div class="form-group">
        <label for="nickname">Nickname</label>
        <input
          type="text"
          id="nickname"
          v-model="formData.nickname"
          placeholder="Enter your nickname"
        />
      </div>

      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          v-model="formData.email"
          placeholder="Enter your email"
          required
          @input="resetValidation('email')"
        />
        <button type="button" @click="validateEmail">이메일 중복 확인</button>
        <button type="button" @click="requestEmailVerification" :disabled="isEmailVerificationSent">
          이메일 인증 요청
        </button>
        <span
          v-if="validation.emailMessage"
          :class="{ error: !validation.email, success: validation.email }"
        >
          {{ validation.emailMessage }}
        </span>
      </div>

      <div class="form-group" v-if="isEmailVerificationSent">
        <label for="verificationCode">Verification Code</label>
        <input
          type="text"
          id="verificationCode"
          v-model="verificationCode"
          placeholder="Enter the verification code"
        />
        <button type="button" @click="verifyEmailCode">인증 코드 확인</button>
        <span
          v-if="validation.emailVerificationMessage"
          :class="{ error: !validation.emailVerified, success: validation.emailVerified }"
        >
          {{ validation.emailVerificationMessage }}
        </span>
      </div>

      <div class="form-group">
        <label for="grade">Member Grade</label>
        <select id="grade" v-model="formData.grade">
          <option value="NORMAL">Normal</option>
          <option value="BUSINESS">Business</option>
          <option value="ADMIN">Admin</option>
        </select>
      </div>

      <button type="submit">Sign Up</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      formData: {
        username: '',
        password: '',
        name: '',
        nickname: '',
        email: '',
        grade: 'NORMAL',
      },
      verificationCode: '',
      isEmailVerificationSent: false,
      validation: {
        username: null,
        email: null,
        emailVerified: null,
        usernameMessage: '',
        emailMessage: '',
        emailVerificationMessage: '',
      },
    }
  },
  methods: {
    async validateUsername() {
      if (!this.formData.username.trim()) {
        this.validation.usernameMessage = '아이디를 입력해주세요.'
        this.validation.username = false
        return
      }

      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/members/validate/username/${this.formData.username}`,
        )
        this.validation.username = response.data.success
        this.validation.usernameMessage = response.data.success
          ? '사용 가능한 아이디입니다.'
          : '이미 사용 중인 아이디입니다.'
      } catch (error) {
        console.error('Error checking username:', error)
        this.validation.usernameMessage = '아이디 확인 중 오류가 발생했습니다.'
      }
    },

    async validateEmail() {
      if (!this.formData.email.trim()) {
        this.validation.emailMessage = '이메일을 입력해주세요.'
        this.validation.email = false
        return
      }

      if (!this.isValidEmailFormat(this.formData.email)) {
        this.validation.emailMessage = '올바른 이메일 형식을 입력해주세요.'
        this.validation.email = false
        return
      }

      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/members/validate/email/${this.formData.email}`,
        )
        this.validation.email = response.data.success
        this.validation.emailMessage = response.data.success
          ? '사용 가능한 이메일입니다.'
          : '이미 사용 중인 이메일입니다.'
      } catch (error) {
        console.error('Error checking email:', error)
        this.validation.emailMessage = '이메일 확인 중 오류가 발생했습니다.'
      }
    },

    async requestEmailVerification() {
      if (!this.formData.email.trim() || !this.validation.email) {
        alert('먼저 이메일 중복 확인을 완료해주세요.')
        return
      }

      try {
        const response = await axios.post('http://localhost:8080/aladin/email', {
          email: this.formData.email,
        })
        console.log(response)
        if (response.data.isOk) {
          alert('인증 이메일이 발송되었습니다. 이메일을 확인해주세요.')
          this.isEmailVerificationSent = true
          this.validation.emailVerificationMessage = ''
        } else {
          alert('이메일 인증 요청에 실패했습니다. 다시 시도해주세요.')
        }
      } catch (error) {
        console.error('Error sending verification email:', error)
        alert('이메일 인증 요청 중 문제가 발생했습니다.')
      }
    },

    async verifyEmailCode() {
      try {
        const response = await axios.post('http://localhost:8080/aladin/email/authentication', {
          email: this.formData.email,
          authentication: this.verificationCode,
        })

        if (response.data.isSuccess) {
          this.validation.emailVerified = true
          this.validation.emailVerificationMessage = '이메일 인증이 완료되었습니다.'
        } else {
          this.validation.emailVerified = false
          this.validation.emailVerificationMessage = '인증 코드가 올바르지 않습니다.'
        }
      } catch (error) {
        console.error('Error verifying email code:', error)
        this.validation.emailVerificationMessage = '이메일 인증 중 오류가 발생했습니다.'
      }
    },

    isValidEmailFormat(email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return emailRegex.test(email)
    },

    resetValidation(field) {
      if (field === 'username') {
        this.validation.username = null
        this.validation.usernameMessage = ''
      }
      if (field === 'email') {
        this.validation.email = null
        this.validation.emailMessage = ''
        this.isEmailVerificationSent = false
        this.validation.emailVerified = null
        this.validation.emailVerificationMessage = ''
      }
    },

    async handleSignup() {
      if (this.validation.username === false) {
        alert('이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.')
        return
      }

      if (this.validation.email === false) {
        alert('이미 사용 중인 이메일입니다. 다른 이메일을 입력해주세요.')
        return
      }

      if (!this.validation.emailVerified) {
        alert('이메일 인증을 완료해주세요.')
        return
      }

      try {
        const response = await axios.post('http://localhost:8080/aladin/members', {
          username: this.formData.username,
          password: this.formData.password,
          name: this.formData.name,
          nickname: this.formData.nickname,
          email: this.formData.email,
          grade: this.formData.grade,
        })

        if (response.data.success) {
          alert(response.data.message)
          this.$router.push('/login')
        } else {
          alert('회원가입에 실패했습니다: ' + response.data.message)
        }
      } catch (error) {
        console.error('Error during signup:', error)
        alert('회원가입 중 문제가 발생했습니다. 다시 시도해주세요.')
      }
    },
  },
}
</script>

<style scoped>
.signup-form {
  max-width: 500px;
  margin: 0 auto;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.form-group {
  margin-bottom: 15px;
}
input,
textarea,
select {
  width: 100%;
  padding: 8px;
  margin-bottom: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
button {
  background-color: #28a745;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #218838;
}
.error {
  color: red;
  font-size: 12px;
  margin-top: 5px;
  display: block;
}
.success {
  color: green;
  font-size: 12px;
  margin-top: 5px;
  display: block;
}

.success {
  color: green;
  font-size: 12px;
  margin-top: 5px;
  display: block;
}
</style>
