<template>
  <div class="profile-edit-form">
    <h1>프로필 수정</h1>
    <form @submit.prevent="handleProfileUpdate">
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
            <button type="button" @click="revertToDefaultImage">원본 이미지로 돌리기</button>
          </div>
          <input type="file" id="profileImage" ref="fileInput" @change="handleImageUpload" hidden />
        </div>
      </div>

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

      <!-- 비밀번호 변경 -->
      <div class="form-group">
        <label for="newPassword">새 비밀번호</label>
        <input
          type="password"
          id="newPassword"
          v-model="formData.newPassword"
          placeholder="새 비밀번호"
        />
      </div>
      <div class="form-group">
        <label for="confirmPassword">새 비밀번호 확인</label>
        <input
          type="password"
          id="confirmPassword"
          v-model="formData.confirmPassword"
          placeholder="새 비밀번호 확인"
        />
      </div>

      <!-- 등급 변경 -->
      <div class="form-group">
        <label for="userGrade">등급</label>
        <select id="userGrade" v-model="formData.grade">
          <option value="NORMAL">NORMAL</option>
          <option value="BUSINESS">BUSINESS</option>
          <option value="ADMIN">ADMIN</option>
        </select>
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

      <!-- Modal for cropping -->
      <div v-if="showCropperModal" class="modal-overlay">
        <div class="modal-content">
          <img ref="imageToCrop" alt="Crop Preview" />
          <button type="button" @click="cropImage">이미지 자르고 저장</button>
        </div>
      </div>

      <div class="form-group">
        <label for="bio">소개글</label>
        <textarea id="bio" v-model="formData.bio" placeholder="자기소개"></textarea>
      </div>

      <button type="submit">수정 완료</button>
      <button type="button" class="btn-danger" @click="handleAccountDeletion">회원 탈퇴</button>
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
        newPassword: '',
        confirmPassword: '',
      },
    }
  },
  methods: {
    handleAccountDeletion() {
      const confirmDeletion = confirm('정말로 회원 탈퇴를 진행하시겠습니까?')
      if (confirmDeletion) {
        // 회원 탈퇴 로직
        alert('회원 탈퇴가 완료되었습니다.')
        this.$router.push({ path: '/' }) // 홈으로 이동
      }
    },
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
      if (this.formData.newPassword !== this.formData.confirmPassword) {
        alert('새 비밀번호가 일치하지 않습니다.')
        return
      }
      console.log('Updated profile data:', this.formData)
      alert('프로필이 성공적으로 수정되었습니다.')
    },
  },
}
</script>

<style scoped>
/* 기본 스타일 */
.profile-edit-form {
  max-width: 600px;
  margin: 0 auto;
  background-color: #ffffff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  font-family: 'Arial', sans-serif;
}

h1 {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

/* 폼 그룹 스타일 */
.form-group {
  margin-bottom: 16px;
}

label {
  display: block;
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 6px;
  color: #555;
}

input,
textarea {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
  background-color: #f9f9f9;
  transition: border-color 0.3s ease;
}

input:focus,
textarea:focus {
  border-color: #007bff;
  outline: none;
  background-color: #fff;
}

textarea {
  resize: none;
  height: 80px;
}

/* 이미지 업로드 스타일 */
.image-upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background-color: #f9f9f9;
  border: 1px dashed #ccc;
  border-radius: 12px;
  transition: background-color 0.3s ease;
}

.image-upload-container:hover {
  background-color: #f1f7ff;
}

.image-preview {
  position: relative;
  display: inline-block;
}

.image-preview img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 3px solid #ddd;
  object-fit: cover;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.image-preview img:hover {
  transform: scale(1.05);
}

.btn-danger {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-danger:hover {
  background-color: #b52d3a;
}

.upload-button {
  background-color: #007bff;
  color: #fff;
  font-size: 14px;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

.upload-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.image-action-buttons {
  display: flex;
  gap: 8px;
}

.image-action-buttons button {
  background-color: #ff6b6b;
  color: #fff;
  font-size: 14px;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}

.image-action-buttons button:hover {
  background-color: #e63946;
  transform: translateY(-2px);
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  text-align: center;
}

.modal-content img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin-bottom: 16px;
}

.modal-content button {
  background-color: #007bff;
  color: #fff;
  font-size: 14px;
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.modal-content button:hover {
  background-color: #0056b3;
}

/* 제출 버튼 스타일 */
button[type='submit'] {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  font-weight: bold;
  color: #fff;
  background-color: #28a745;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button[type='submit']:hover {
  background-color: #218838;
}

/* 등급 필드 전체 스타일 */
.form-group select {
  width: 100%; /* 드롭다운이 컨테이너의 전체 너비를 차지 */
  padding: 12px; /* 내부 여백 */
  font-size: 14px; /* 글씨 크기 */
  border: 1px solid #ddd; /* 기본 테두리 */
  border-radius: 8px; /* 둥근 테두리 */
  box-sizing: border-box; /* 패딩 포함 크기 계산 */
  background-color: #f9f9f9; /* 배경색 */
  color: #333; /* 글씨 색 */
  transition: border-color 0.3s ease; /* 포커스 시 부드러운 전환 */
}

/* 포커스 상태 스타일 */
.form-group select:focus {
  border-color: #007bff; /* 포커스 시 테두리 색상 변경 */
  outline: none; /* 기본 아웃라인 제거 */
  background-color: #fff; /* 포커스 시 배경색 변경 */
}

/* 드롭다운 화살표 스타일 (선택 사항, 브라우저 기본 스타일 대체) */
.form-group select::-ms-expand {
  display: none; /* IE 브라우저 확장 버튼 숨김 */
}

.form-group select {
  appearance: none; /* 기본 브라우저 스타일 제거 */
  -webkit-appearance: none; /* Safari 기본 스타일 제거 */
  -moz-appearance: none; /* Firefox 기본 스타일 제거 */
  background-image: url('data:image/svg+xml;charset=UTF-8,%3Csvg xmlns%3D%22http://www.w3.org/2000/svg%22 viewBox%3D%220 0 4 5%22%3E%3Cpath fill%3D%22%23333%22 d%3D%22M2 0L0 2h4zm0 5L0 3h4z%22/%3E%3C/svg%3E'); /* 드롭다운 화살표 아이콘 */
  background-repeat: no-repeat; /* 아이콘 중복 제거 */
  background-position: right 12px center; /* 아이콘 위치 */
  background-size: 12px; /* 아이콘 크기 */
}

/* 선택 옵션 스타일 (기본적으로 브라우저 스타일 사용) */
.form-group select option {
  padding: 10px; /* 옵션 내부 여백 */
  background-color: #fff; /* 옵션 배경색 */
  color: #333; /* 글씨 색 */
}
</style>
