<template>
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
export default {
  data() {
    return {
      username: localStorage.getItem('savedUsername') || '',
      password: '',
      rememberMe: !!localStorage.getItem('savedUsername'),
    }
  },
  methods: {
    handleLogin() {
      // Add your login logic here (e.g., API call)
      console.log('Username:', this.username)
      console.log('Password:', this.password)

      if (this.rememberMe) {
        localStorage.setItem('savedUsername', this.username)
      } else {
        localStorage.removeItem('savedUsername')
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
