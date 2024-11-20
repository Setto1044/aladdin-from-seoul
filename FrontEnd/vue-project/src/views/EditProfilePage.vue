<template>
  <div class="profile-edit-form">
    <h1>프로필 수정</h1>
    <form @submit.prevent="handleProfileUpdate">
      <div class="form-group">
        <label for="username">아이디</label>
        <input
          type="text"
          id="username"
          v-model="formData.username"
          placeholder="아이디"
          disabled
        />
      </div>

      <div class="form-group">
        <label for="name">실명</label>
        <input type="text" id="name" v-model="formData.name" placeholder="실명" />
      </div>

      <div class="form-group">
        <label for="nickname">별명</label>
        <input type="text" id="nickname" v-model="formData.nickname" placeholder="별명" />
      </div>

      <div class="form-group">
        <label for="email">이메일</label>
        <input type="email" id="email" v-model="formData.email" placeholder="이메일" />
      </div>

      <!-- Profile image upload -->
      <div class="form-group">
        <label for="profileImage">프로필 사진</label>
        <div class="image-upload-container">
          <div class="image-preview">
            <img
              :src="formData.newProfileImagePath || formData.profileImagePath || defaultImage"
              alt="Profile Preview"
            />
            <button type="button" class="upload-button" @click="triggerFileInput">업로드</button>
          </div>
          <input type="file" id="profileImage" ref="fileInput" @change="handleImageUpload" hidden />
        </div>
      </div>

      <!-- Modal for cropping -->
      <div v-if="showCropperModal" class="modal-overlay">
        <div class="modal-content">
          <img ref="imageToCrop" alt="Crop Preview" />
          <button type="button" @click="cropImage">이미지 자르고 저장</button>
        </div>
      </div>

      <div class="image-action-buttons">
        <button type="button" @click="revertToDefaultImage">원본 이미지로 돌리기</button>
      </div>

      <div class="form-group">
        <label for="bio">소개글</label>
        <textarea id="bio" v-model="formData.bio" placeholder="자기소개"></textarea>
      </div>

      <button type="submit">수정 완료</button>
    </form>
  </div>
</template>

<script>
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.css'

export default {
  data() {
    return {
      defaultImage: 'https://via.placeholder.com/150', // Initial blank/default image
      cropper: null,
      showCropperModal: false,
      formData: {
        id: 1, // Assuming this is needed for updates
        username: 'user123',
        name: '홍길동',
        nickname: '길동이',
        email: 'user123@example.com',
        profileImagePath: 'https://via.placeholder.com/150', // Existing profile image URL
        newProfileImagePath: '',
        grade: 'NORMAL',
        bio: '안녕하세요, 저는 길동입니다.',
        status: 'ACTIVE',
        createdAt: '2023-01-01T00:00:00', // Placeholder example
        lastJoinedAt: '2024-01-01T00:00:00', // Placeholder example
        deletedAt: null, // Assuming this is null if the user is active
      },
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.value = '' // Clear the input to allow re-uploading the same file
      this.$refs.fileInput.click()
    },
    handleImageUpload(event) {
      const file = event.target.files[0]
      if (file) {
        const reader = new FileReader()
        reader.onload = (e) => {
          // Show the modal before accessing the image element
          this.showCropperModal = true

          // Ensure the DOM is updated before accessing the img element
          this.$nextTick(() => {
            if (this.$refs.imageToCrop) {
              this.$refs.imageToCrop.src = e.target.result
              this.initializeCropper()
            } else {
              console.error('Image element not found')
            }
          })
        }
        reader.readAsDataURL(file)
      }
    },
    initializeCropper() {
      if (this.cropper) {
        this.cropper.destroy()
      }
      this.cropper = new Cropper(this.$refs.imageToCrop, {
        aspectRatio: 1,
        viewMode: 2,
        autoCropArea: 1,
      })
    },
    cropImage() {
      if (this.cropper) {
        const croppedImage = this.cropper.getCroppedCanvas().toDataURL('image/jpeg')
        this.formData.profileImagePath = croppedImage // Confirm the cropped image
        this.formData.newProfileImagePath = '' // Clear the temporary path
        this.cropper.destroy()
        this.cropper = null
        this.showCropperModal = false // Hide the modal after cropping
      }
    },
    revertToDefaultImage() {
      this.formData.newProfileImagePath = ''
      this.formData.profileImagePath = this.defaultImage // Reset to initial blank image
    },
    handleProfileUpdate() {
      console.log('Updated profile data:', this.formData)
      alert('프로필이 성공적으로 수정되었습니다.')
    },
  },
}
</script>

<style scoped>
/* Existing styles */
.profile-edit-form {
  max-width: 500px;
  margin: 0 auto;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
}
</style>
