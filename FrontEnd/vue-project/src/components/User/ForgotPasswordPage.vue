<template>
  <div class="password-recovery-form">
    <h1>비밀번호 찾기</h1>
    <p>
      가입시 등록하신 성함과 아이디, 이메일을 입력하시면, 이 이메일로 임시 비밀번호를
      전송해드립니다.
    </p>

    <form @submit.prevent="handleRecovery">
      <div class="form-group">
        <label for="username">아이디</label>
        <input
          type="text"
          id="username"
          v-model="formData.username"
          placeholder="아이디"
          required
        />
      </div>

      <div class="form-group email-group">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          v-model="formData.email"
          placeholder="가입하신 이메일 주소"
          required
        />
        <button type="button" @click="sendVerificationCode">전송</button>
      </div>

      <div class="form-group">
        <label for="verificationCode">인증번호 입력</label>
        <input
          type="text"
          id="verificationCode"
          v-model="formData.verificationCode"
          placeholder="인증번호 입력"
        />
        <button type="button" @click="verifyCode">확인</button>
      </div>

      <button type="submit" class="confirm-button">확인</button>
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
        email: '',
        verificationCode: '',
      },
      validation: {
        emailVerified: false,
        verificationMessage: '',
      },
      isVerificationSent: false,
    }
  },
  watch: {
    'formData.username'(newVal) {
      // 아이디가 변경되면 인증 상태 초기화
      this.resetVerification()
    },
    'formData.email'(newVal) {
      // 이메일이 변경되면 인증 상태 초기화
      this.resetVerification()
    },
  },
  methods: {
    resetVerification() {
      this.validation.emailVerified = false
      this.validation.verificationMessage = ''
      this.isVerificationSent = false
    },
    async sendVerificationCode() {
      if (!this.formData.email.trim()) {
        alert('이메일을 입력해주세요.')
        return
      }

      try {
        const response = await axios.post('http://localhost:8080/aladin/email', {
          username: this.formData.username,
          email: this.formData.email,
          isRegister: false,
        })

        if (response.data.isOk) {
          this.isVerificationSent = true
          alert('인증번호가 이메일로 전송되었습니다.')
        } else {
          alert('이메일 전송에 실패했습니다. 다시 시도해주세요.')
        }
      } catch (error) {
        console.error('Error sending verification code:', error)
        alert('이메일 전송 중 문제가 발생했습니다.')
      }
    },
    async verifyCode() {
      if (!this.formData.verificationCode.trim()) {
        alert('인증번호를 입력해주세요.')
        return
      }

      try {
        const response = await axios.post('http://localhost:8080/aladin/email/authentication', {
          email: this.formData.email,
          authentication: this.formData.verificationCode,
        })

        if (response.data.isSuccess) {
          this.validation.emailVerified = true
          this.validation.verificationMessage = '인증번호가 확인되었습니다.'
          alert('인증이 완료되었습니다.')
        } else {
          this.validation.emailVerified = false
          this.validation.verificationMessage = '잘못된 인증번호입니다.'
          alert('인증번호가 일치하지 않습니다.')
        }
      } catch (error) {
        console.error('Error verifying code:', error)
        alert('인증 중 문제가 발생했습니다.')
      }
    },
    async handleRecovery() {
      if (!this.validation.emailVerified) {
        alert('먼저 이메일 인증을 완료해주세요.')
        return
      }

      this.$router.push({ name: 'chpw', params: { username: this.formData.username } })
    },
  },
}
</script>

<style scoped>
.password-recovery-form {
  max-width: 400px;
  margin: 0 auto;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
h1 {
  font-size: 24px;
  text-align: center;
  margin-bottom: 15px;
}
p {
  font-size: 14px;
  text-align: center;
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 15px;
}
input {
  width: calc(100% - 60px);
  padding: 8px;
  margin-bottom: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
input[type='text'],
input[type='email'] {
  display: inline-block;
  width: calc(100% - 60px);
}
button {
  background-color: #000;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;
}
button:hover {
  background-color: #333;
}
.confirm-button {
  width: 100%;
  background-color: #000;
}
.email-group {
  display: flex;
  align-items: center;
}
</style>
