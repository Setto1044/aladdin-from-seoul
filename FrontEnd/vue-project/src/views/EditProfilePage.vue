<template>
  <div class="edit-profile-page">
    <header class="edit-profile-header">
      <h1 class="edit-profile-title">Edit Your Profile</h1>
      <p class="edit-profile-subtitle">Update your personal information or delete your account</p>
    </header>

    <main class="edit-profile-content">
      <section class="profile-info-section">
        <h2 class="section-title">Personal Information</h2>
        <form @submit.prevent="updateProfile" class="profile-form">
          <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" v-model="user.username" class="form-input" />
          </div>

          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" v-model="user.email" class="form-input" />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" v-model="user.password" class="form-input" />
          </div>

          <button type="submit" class="btn-primary">Save Changes</button>
        </form>
      </section>

      <section class="delete-account-section">
        <h2 class="section-title">Delete Account</h2>
        <p class="warning-message">
          Warning: Deleting your account is permanent and cannot be undone.
        </p>
        <button @click="deleteAccount" class="btn-danger">Delete Account</button>
      </section>
    </main>
  </div>
</template>

<script>
export default {
  name: 'EditProfilePage',
  data() {
    return {
      user: {
        username: 'John Doe',
        email: 'johndoe@example.com',
        password: '',
      },
    }
  },
  methods: {
    updateProfile() {
      // Logic to update user profile
      alert('Profile updated successfully!')
    },
    deleteAccount() {
      // Logic to delete user account
      if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
        alert('Account deleted successfully.')
        this.$router.push('/')
      }
    },
  },
}
</script>

<style scoped>
.edit-profile-page {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.edit-profile-header {
  text-align: center;
  margin-bottom: 20px;
}

.edit-profile-title {
  font-size: 28px;
  font-weight: bold;
}

.edit-profile-subtitle {
  font-size: 16px;
  color: #666;
}

.edit-profile-content {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.section-title {
  font-size: 22px;
  margin-bottom: 10px;
}

.profile-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
}

.form-input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  outline: none;
}

.btn-primary {
  padding: 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.delete-account-section {
  text-align: center;
}

.warning-message {
  color: #ff0000;
  margin-bottom: 20px;
}

.btn-danger {
  padding: 10px;
  background-color: #dc3545;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
</style>
