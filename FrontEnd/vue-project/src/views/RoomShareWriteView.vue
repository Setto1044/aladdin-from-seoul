<template>
  <div class="property-form">
    <h1>매물글 쓰기</h1>
    <div v-if="isLoading" class="loading-overlay">Submitting...</div>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="imageFiles">Upload Images</label>
        <draggable v-model="formData.imagePreviews" class="image-preview" @end="onDragEnd">
          <template #item="{ element, index }">
            <div class="image-container">
              <span class="image-order">{{ index + 1 }}</span>
              <img :src="element" alt="Image preview" />
              <button type="button" @click="removeImage(index)" class="delete-button">X</button>
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
        <label for="pricePer">Price Per</label>
        <select id="pricePer" v-model="formData.pricePer">
          <option value="WEEK">Week</option>
          <option value="MONTH">Month</option>
        </select>
      </div>

      <div class="form-group form-group-inline">
        <div class="date-input">
          <label for="rentStart">Rent Start Date</label>
          <input type="date" id="rentStart" v-model="formData.rentStart" required />
        </div>
        <div class="date-input">
          <label for="rentEnd">Rent End Date</label>
          <input type="date" id="rentEnd" v-model="formData.rentUntil" required />
        </div>
      </div>

      <div class="form-group">
        <label for="hashtags">Hashtags</label>
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
              placeholder="Type and press space or enter"
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
  max-width: 600px;
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
.hashtag-input {
  display: flex;
  flex-wrap: wrap;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 5px;
  gap: 5px;
}
.input-container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 5px;
  width: 100%; /* 부모 요소의 너비만큼 */
}

#input-hashtag {
  width: 100%; /* 부모 요소의 너비만큼 차지 */
  border: none;
  outline: none;
  padding: 5px;
}

.hashtag {
  display: inline-flex;
  align-items: center;
  background-color: #e0e0e0;
  padding: 5px 10px;
  border-radius: 4px;
  margin-bottom: 5px; /* 각 태그 간의 하단 여백 추가 */
}
.remove-tag {
  margin-left: 5px;
  cursor: pointer;
  color: red;
}
.image-order {
  position: absolute;
  top: 5px;
  left: 5px;
  background-color: rgba(0, 0, 0, 0.7);
  color: #fff;
  padding: 2px 5px;
  border-radius: 4px;
  font-size: 12px;
}
.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}
.image-container {
  position: relative;
  width: 100px;
  height: 100px;
}
.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}
.delete-button {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: rgba(255, 0, 0, 0.8);
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  cursor: pointer;
  font-size: 12px;
}

.form-group-inline {
  display: flex;
  justify-content: space-between;
  gap: 20px; /* 요소 사이의 간격 */
}

.date-input {
  flex: 1; /* 동일한 너비로 배치 */
}

.date-input label {
  display: block; /* 라벨이 인풋 상단에 표시되도록 설정 */
  margin-bottom: 5px; /* 라벨과 인풋 간격 */
}

.address-group {
  margin-bottom: 15px;
}

.address-container {
  display: flex;
  align-items: flex-start;
  gap: 10px; /* 버튼과 입력 필드 사이의 간격 */
}

.postcode-button {
  flex-shrink: 0; /* 버튼의 크기를 유지 */
  padding: 6px 12px; /* 버튼의 패딩 조정 */
  font-size: 14px; /* 버튼 폰트 크기 */
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.postcode-button:hover {
  background-color: #0056b3;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6); /* 불투명도를 더 높여줌 */
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
  max-width: 90%; /* 모달의 최대 너비 */
  width: 500px; /* 모달의 너비 */
  max-height: 80%; /* 모달의 최대 높이 */
  overflow-y: auto; /* 내용이 넘칠 경우 스크롤 */
  display: flex;
  flex-direction: column;
  align-items: center;
}

.modal-content img {
  max-width: 100%; /* 모달 너비에 맞춰 이미지 크기 조정 */
  max-height: 400px; /* 이미지 최대 높이 */
  width: auto;
  height: auto;
  margin-bottom: 15px; /* 버튼과의 간격 */
}

.crop-button,
.cancel-button {
  margin-top: 10px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.crop-button {
  background-color: #28a745;
  color: #fff;
}

.cancel-button {
  background-color: #dc3545;
  color: #fff;
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
