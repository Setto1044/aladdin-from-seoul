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
export default {
  data() {
    return {
      formData: {
        username: '',
        email: '',
        verificationCode: '',
      },
    }
  },
  methods: {
    sendVerificationCode() {
      // Logic to send verification code via email
      alert('Verification code sent to your email address.')
    },
    verifyCode() {
      // Logic to verify the input verification code
      alert('Verification code verified.')
    },
    handleRecovery() {
      // Handle the password recovery process (e.g., send data to the backend)
      console.log('Password recovery data:', this.formData)
      alert('Password recovery process completed.')
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
