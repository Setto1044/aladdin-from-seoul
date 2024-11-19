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
      </div>

      <div class="form-group">
        <label for="profileImage">Profile Image URL</label>
        <input
          type="url"
          id="profileImage"
          v-model="formData.profileImagePath"
          placeholder="Enter profile image URL"
        />
      </div>

      <div class="form-group">
        <label for="bio">Bio</label>
        <textarea id="bio" v-model="formData.bio" placeholder="Tell us about yourself"></textarea>
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
export default {
  data() {
    return {
      formData: {
        username: '',
        password: '',
        name: '',
        nickname: '',
        email: '',
        profileImagePath: '',
        grade: 'NORMAL',
        bio: '',
      },
    }
  },
  methods: {
    handleSignup() {
      console.log('Signup data:', this.formData)
      // Add your logic to send the signup data to the server (e.g., API call)
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
</style>
