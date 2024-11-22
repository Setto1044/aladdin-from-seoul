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
        />
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
        />
        <span
          v-if="validation.emailMessage"
          :class="{ error: !validation.email, success: validation.email }"
        >
          {{ validation.emailMessage }}
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
      validation: {
        username: null, // null means not checked, true/false for validity
        email: null, // null means not checked, true/false for validity
        usernameMessage: '',
        emailMessage: '',
      },
    }
  },
  watch: {
    'formData.username': function (newUsername) {
      if (newUsername) {
        this.validateUsername(newUsername)
      } else {
        this.validation.username = null
        this.validation.usernameMessage = ''
      }
    },
    'formData.email': function (newEmail) {
      if (newEmail) {
        if (this.isValidEmailFormat(newEmail)) {
          this.validateEmail(newEmail)
        } else {
          this.validation.email = false
          this.validation.emailMessage = '올바른 이메일 형식을 입력해주세요.'
        }
      } else {
        this.validation.email = null
        this.validation.emailMessage = ''
      }
    },
  },
  methods: {
    async validateUsername(username) {
      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/members/validate/username/${username}`,
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
    async validateEmail(email) {
      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/members/validate/email/${email}`,
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
    isValidEmailFormat(email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return emailRegex.test(email)
    },
    async handleSignup() {
      // Ensure username and email are validated
      if (this.validation.username === false) {
        alert('이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.')
        return
      }
      if (this.validation.email === false) {
        alert('이미 사용 중인 이메일입니다. 다른 이메일을 입력해주세요.')
        return
      }

      try {
        console.log('Sending signup data:', this.formData)

        const response = await axios.post('http://localhost:8080/aladin/members', {
          username: this.formData.username,
          password: this.formData.password,
          name: this.formData.name,
          nickname: this.formData.nickname,
          email: this.formData.email,
          grade: this.formData.grade,
        })

        if (response.data.success) {
          alert(response.data.message) // Display success message
          console.log('Signup success:', response.data)

          // Optional: Redirect to login page after signup
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
</style>
