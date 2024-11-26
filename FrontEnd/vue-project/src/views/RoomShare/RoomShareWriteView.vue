<template>
  <div class="property-form">
    <h2>매물글 쓰기</h2>
    <div v-if="isLoading" class="loading-overlay">Submitting...</div>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <draggable v-model="formData.imagePreviews" class="image-preview" @end="onDragEnd">
          <template #item="{ element, index }">
            <div class="image-container">
              <!-- <span class="image-order">{{ index + 1 }}</span> -->
              <img :src="element" alt="Image preview" />
              <button
                type="button"
                class="delete-button"
                @click="removeImage(index)"
                src="@/assets/button/close-button.png"
              >
                <img src="@/assets/button/close-button.png" alt="btnImages" class="btnImages" />
              </button>
            </div>
          </template>
        </draggable>
        <div class="dropzone" @dragover.prevent @drop.prevent="handleImageDrop">
          Drag and drop your images here
          <input type="file" id="imageFiles" multiple @change="handleImageUpload" ref="fileInput" />
        </div>

        <!-- Cropper 모달 -->
        <div v-if="showCropper" class="modal-overlay">
          <div class="modal-content">
            <img ref="cropperImage" :src="currentImageUrl" alt="To crop" />
            <button type="button" @click="cropImage" class="crop-button">Crop</button>
            <button type="button" @click="closeCropper" class="cancel-button">Cancel</button>
          </div>
        </div>
      </div>

      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" v-model="formData.title" required />
      </div>

      <div class="form-group">
        <label for="detail">설명</label>
        <textarea id="detail" v-model="formData.detail" required></textarea>
      </div>

      <div class="form-group address-group">
        <label for="address">주소</label>
        <div class="address-container">
          <button type="button" class="postcode-button" @click="execDaumPostcode">
            우편번호 찾기
          </button>
          <div class="address-inputs">
            <input
              type="text"
              id="postcode"
              v-model="formData.postcode"
              placeholder="우편번호"
              readonly
            />
            <input
              type="text"
              id="roadAddress"
              v-model="formData.address"
              placeholder="도로명주소"
              readonly
              required
            />
          </div>
        </div>
      </div>

      <div class="form-group">
        <label for="houseSize">평수 (m²)</label>
        <input
          type="number"
          id="houseSize"
          v-model.number="formData.houseSize"
          step="0.01"
          required
        />
      </div>

      <div class="form-group">
        <label for="price">가격 (만 원)</label>
        <input type="number" id="price" v-model.number="formData.price" required />
      </div>

      <div class="form-group">
        <label for="pricePer">방세 구분</label>
        <select id="pricePer" v-model="formData.pricePer">
          <option value="WEEK">매주</option>
          <option value="MONTH">매월</option>
        </select>
      </div>

      <div class="form-group form-group-inline">
        <div class="date-input">
          <label for="rentStart">시작일</label>
          <input type="date" id="rentStart" v-model="formData.rentStart" required />
        </div>
        <div class="date-input">
          <label for="rentEnd">종료일</label>
          <input type="date" id="rentEnd" v-model="formData.rentUntil" required />
        </div>
      </div>

      <div class="form-group">
        <label for="hashtags">해시태그</label>
        <div class="hashtag-input">
          <div class="input-container">
            <div class="hashtag" v-for="(tag, index) in formData.hashtags" :key="index">
              #{{ tag }}
              <span class="remove-tag" @click="removeTag(index)">x</span>
            </div>
            <input
              id="input-hashtag"
              type="text"
              v-model="currentTag"
              @keydown.enter.prevent="addTag"
              @keyup.space="addTag"
              @keydown.backspace="removeLastTag"
              placeholder="스페이스바 혹은 엔터를 통해 입력"
            />
          </div>
        </div>
      </div>

      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.css'
import axios from 'axios'
import useUserStore from '@/stores/user-store' // Example: your Pinia store for user info

export default {
  components: {
    draggable,
  },
  data() {
    return {
      showCropper: false,
      currentImageFile: null,
      currentImageUrl: '',
      cropperInstance: null,
      currentTag: '',
      formData: {
        title: '',
        detail: '',
        address: '',
        postcode: '',
        price: null,
        houseSize: null,
        pricePer: 'month',
        rentStart: '',
        rentUntil: '',
        hashtags: [],
        imageFiles: [],
        imagePreviews: [],
      },
    }
  },
  methods: {
    handleImageDrop(event) {
      const files = Array.from(event.dataTransfer.files)
      this.handleImageUpload({ target: { files } })
    },
    execDaumPostcode() {
      new daum.Postcode({
        oncomplete: (data) => {
          this.formData.postcode = data.zonecode
          this.formData.address = data.roadAddress

          if (data.autoRoadAddress) {
            this.guideText = `(예상 도로명 주소: ${data.autoRoadAddress})`
          } else {
            this.guideText = ''
          }
        },
      }).open()
    },
    addTag() {
      const trimmedTag = this.currentTag.trim()
      if (trimmedTag && !this.formData.hashtags.includes(trimmedTag)) {
        this.formData.hashtags.push(trimmedTag)
      }
      this.currentTag = ''
    },
    removeTag(index) {
      this.formData.hashtags.splice(index, 1)
    },
    removeLastTag(event) {
      if (this.currentTag === '' && this.formData.hashtags.length > 0) {
        this.formData.hashtags.pop()
      }
    },
    handleImageUpload(event) {
      const files = Array.from(event.target.files)
      if (files.length > 0) {
        const file = files[0] // 첫 번째 파일만 처리 (여러 개 처리하려면 반복문으로 수정)
        const reader = new FileReader()

        reader.onload = (e) => {
          this.currentImageUrl = e.target.result // 모달에 표시할 이미지 URL 설정
          this.showCropper = true // 자르기 모달 표시

          this.$nextTick(() => {
            this.initCropper() // Cropper 초기화
          })
        }

        reader.readAsDataURL(file)
        this.currentImageFile = file // 자르기 후 원본 파일 업데이트
      }
    },
    initCropper() {
      const imageElement = this.$refs.cropperImage
      this.cropperInstance = new Cropper(imageElement, {
        viewMode: 1,
      })
    },
    cropImage() {
      const canvas = this.cropperInstance.getCroppedCanvas()
      canvas.toBlob((blob) => {
        // 원본 파일 이름 가져오기
        const originalName = this.currentImageFile.name

        // 고유한 이름 생성 (원본 이름 + 타임스탬프)
        const uniqueName = `${originalName.split('.')[0]}_${Date.now()}.${blob.type.split('/')[1]}`

        // Blob을 File 객체로 변환하면서 원본 이름 유지
        const croppedFile = new File([blob], uniqueName, { type: blob.type })

        // 크롭된 파일의 미리보기 URL 생성
        const newImageUrl = URL.createObjectURL(blob)

        // 크롭된 파일과 미리보기를 배열에 추가
        this.formData.imageFiles.push(croppedFile)
        this.formData.imagePreviews.push(newImageUrl)

        // 크롭퍼 닫기
        this.closeCropper()
      }, this.currentImageFile.type) // 원본 파일의 MIME 타입 사용
    },
    closeCropper() {
      if (this.cropperInstance) {
        this.cropperInstance.destroy()
        this.cropperInstance = null
      }
      this.currentImageFile = null
      this.currentImageUrl = ''
      this.showCropper = false
    },
    removeImage(index) {
      this.formData.imageFiles.splice(index, 1)
      this.formData.imagePreviews.splice(index, 1)
      console.log(this.formData.imageFiles)
    },
    onDragEnd(event) {
      // 드래그된 요소의 이전 인덱스와 새 인덱스를 가져옴
      const { oldIndex, newIndex } = event
      if (oldIndex !== newIndex) {
        // imageFiles 배열의 요소를 이동하여 imagePreviews와 동일하게 맞춤
        const movedFile = this.formData.imageFiles.splice(oldIndex, 1)[0]
        this.formData.imageFiles.splice(newIndex, 0, movedFile)
      }
      console.log(this.formData.imageFiles)
      console.log(this.formData.imagePreviews)
    },
    validateFormData() {
      if (!this.formData.title.trim()) return false
      if (!this.formData.detail.trim()) return false
      if (!this.formData.address.trim() || !this.formData.postcode.trim()) return false
      if (!this.formData.price || !this.formData.houseSize) return false
      if (new Date(this.formData.rentStart) > new Date(this.formData.rentUntil)) return false
      return true
    },
    async handleImageFileUpload(event) {
      const files = Array.from(event.target.files)

      for (const file of files) {
        const reader = new FileReader()
        reader.onload = (e) => {
          const base64String = e.target.result // Base64 문자열
          this.formData.imageFiles.push(base64String) // Base64를 배열에 추가
          this.formData.imagePreviews.push(base64String) // 미리보기용
        }
        reader.readAsDataURL(file) // Base64로 변환
      }
    },

    submitForm() {
      const formData = new FormData()

      // 텍스트 데이터 추가
      formData.append('membersUsername', useUserStore().memberInfo.username)
      formData.append('title', this.formData.title)
      formData.append('detail', this.formData.detail)
      formData.append('address', this.formData.address)
      formData.append('postcode', this.formData.postcode)
      formData.append('price', this.formData.price)
      formData.append('houseSize', this.formData.houseSize)
      formData.append('pricePer', this.formData.pricePer)
      formData.append('rentStart', this.formData.rentStart)
      formData.append('rentUntil', this.formData.rentUntil)
      formData.append('hashtags', this.formData.hashtags.join(', ')) // 쉼표로 구분된 문자열로 변환

      // 이미지 파일 추가
      for (const file of this.formData.imageFiles) {
        formData.append('images[]', file) // 배열 형태로 서버로 전송
      }

      // 디버깅: FormData의 내용을 확인
      for (const [key, value] of formData.entries()) {
        console.log(`${key}:`, value) // key와 value를 출력
      }

      // Axios POST 요청
      axios
        .post('http://localhost:8080/aladin/boards', formData, {
          headers: {
            'Content-Type': 'multipart/form-data', // multipart 형식으로 전송
          },
        })
        .then((response) => {
          if (response.data.success) {
            alert(response.data.message)
            this.$router.push('/share') // 성공 시 페이지 이동
          } else {
            alert('등록 실패: ' + response.data.message)
          }
        })
        .catch((error) => {
          console.error('Error during registration:', error)
          alert('등록 중 오류가 발생했습니다.')
        })
    },

    updateOptions() {
      this.formData.options = this.formData.optionsString.split(',').map((option) => option.trim())
    },
    updateImageUrls() {
      this.formData.imageUrls = this.formData.imageUrlsString.split(',').map((url) => url.trim())
    },
  },
}
</script>

<style scoped>
.property-form {
  max-width: 720px;
  margin: 0 auto;
  background-color: #ffffff;
  padding: 24px;
  font-family: 'Arial', sans-serif;
}
.main-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
  font-family: 'SCore7';
}

h1 {
  text-align: center;
  color: #333;
  font-size: 24px;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

input,
textarea,
select {
  width: 100%;
  padding: 12px 14px;
  border-radius: 8px;
  border: 1px solid #d1d1d1;
  font-size: 14px;
  font-family: inherit;
  transition:
    border-color 0.3s ease,
    box-shadow 0.3s ease;
}

input:focus,
textarea:focus,
select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 4px rgba(0, 123, 255, 0.3);
}

button {
  background-color: #007bff;
  color: white;
  padding: 12px 18px;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button:active {
  transform: translateY(0);
}

.hashtag-input {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 10px;
}

.input-container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

#input-hashtag {
  border: none;
  outline: none;
  padding: 5px;
  flex-grow: 1;
  min-width: 150px;
}

.hashtag {
  display: inline-flex;
  align-items: center;
  background-color: #f0f0f0;
  color: #555;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 14px;
  gap: 8px;
}

.remove-tag {
  background-color: transparent;
  color: #ff4d4f;
  cursor: pointer;
  font-size: 14px;
  border: none;
}

.remove-tag:hover {
  color: #ff0000;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 12px;
  margin-bottom: 10px;
}

.image-container {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.delete-button {
  position: absolute;
  top: 3px;
  right: 3px;
  background-color: transparent; /* 배경색 제거 */
  border: none;
  cursor: pointer;
  padding: 0;
  width: 18px;
  height: 18px;
}

.delete-button img {
  width: 100%; /* 버튼 크기에 맞게 이미지 크기 조정 */
  height: 100%;
  object-fit: contain;
  display: block;
  border-radius: 50%;
  transition:
    transform 0.2s ease,
    opacity 0.2s ease;
}

.delete-button:hover img {
  transform: scale(1.2); /* 호버 시 확대 효과 */
  opacity: 0.8; /* 살짝 투명하게 */
}

.dropzone {
  border: 2px dashed #007bff;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  color: #007bff;
  background-color: #f8f9fa;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.dropzone:hover {
  background-color: #e8f0fc;
}

.form-group-inline {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.date-input {
  flex: 1;
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
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  max-width: 500px;
  width: 100%;
  text-align: center;
}

.crop-button,
.cancel-button {
  margin-top: 12px;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}

.crop-button {
  background-color: #28a745;
  color: white;
}

.cancel-button {
  background-color: #dc3545;
  color: white;
}

.crop-button:hover {
  background-color: #218838;
}

.cancel-button:hover {
  background-color: #c82333;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
</style>
