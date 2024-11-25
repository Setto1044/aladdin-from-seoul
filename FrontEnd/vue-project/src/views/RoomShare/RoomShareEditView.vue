<template>
  <div class="property-form">
    <h1>매물글 수정하기</h1>
    <div v-if="formData">
      <form @submit.prevent="updateForm">
        <div class="form-group">
          <label>Images</label>
          <div class="image-preview">
            <div
              v-for="(element, index) in formData.imagePreviews"
              :key="index"
              class="image-container"
            >
              <span class="image-order">{{ index + 1 }}</span>
              <img :src="element" alt="Image preview" />
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

        <button type="submit">Update</button>
      </form>
    </div>
    <div v-else>
      <p>Loading...</p>
    </div>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.css'
import getPropertyById from '@/api/property'
import axios from 'axios'

export default {
  components: {
    draggable,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      showCropper: false,
      currentImageFile: null,
      currentImageUrl: '',
      cropperInstance: null,
      currentTag: '',
      formData: null,
    }
  },
  async created() {
    // 라우트 파라미터에서 ID 가져오기
    const id = this.$route.params.id
    console.log(`Fetching details for ID: ${id}`)
    try {
      // API 호출
      const response = await axios.get(`http://localhost:8080/aladin/boards/${id}`)
      if (response.data.success) {
        const { roomBoardVo, roomImageInfos, nickname, profileImagePath } = response.data.data

        // formData에 데이터 매핑
        this.formData = {
          id: roomBoardVo.id,
          title: roomBoardVo.title,
          detail: roomBoardVo.detail,
          address: roomBoardVo.address,
          postcode: roomBoardVo.postcode,
          price: roomBoardVo.price,
          houseSize: roomBoardVo.houseSize,
          pricePer: roomBoardVo.pricePer,
          views: roomBoardVo.views,
          rentStart: this.formatDate(new Date(roomBoardVo.rentStart)),
          rentUntil: this.formatDate(new Date(roomBoardVo.rentUntil)), // Date -> YYYY-MM-DD
          hashtags: JSON.parse(roomBoardVo.hashtags || '[]'), // `tags` 대신 `hashtags`
          createdAt: roomBoardVo.createdAt,
          updatedAt: roomBoardVo.updatedAt,
          images: roomImageInfos.map((img) => ({
            url: img.url,
            order: img.imageOrder,
          })),
          imagePreviews: roomImageInfos.map((img) => img.url), // 미리보기 URL
          imageFiles: [], // 초기에는 빈 배열
        }

        console.log('Loaded data:', this.formData)
      } else {
        console.error('Failed to fetch property details:', response.data.message)
      }
    } catch (error) {
      console.error('Error during data fetch:', error)
    }
  },

  methods: {
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0') // 0-based
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
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
        const newImageUrl = URL.createObjectURL(blob)
        this.formData.imageFiles.push(blob)
        this.formData.imagePreviews.push(newImageUrl)
        this.closeCropper()
      })
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
    updateForm() {
      // Validation (Example)
      if (!this.formData.title.trim()) {
        alert('Title is required!')
        return
      }

      if (this.formData.rentStart > this.formData.rentUntil) {
        alert('Rent start date cannot be later than the end date.')
        return
      }

      // FormData 생성
      const formData = new FormData()

      // 텍스트 데이터 추가
      formData.append('id', this.formData.id) // 수정할 글의 ID
      formData.append('title', this.formData.title)
      formData.append('detail', this.formData.detail)
      formData.append('address', this.formData.address)
      formData.append('postcode', this.formData.postcode)
      formData.append('price', this.formData.price)
      formData.append('houseSize', this.formData.houseSize)
      formData.append('pricePer', this.formData.pricePer.toUpperCase())
      formData.append('rentStart', this.formData.rentStart)
      formData.append('rentUntil', this.formData.rentUntil)
      formData.append('hashtags', this.formData.hashtags.join(','))

      // FormData 확인 (디버깅)
      for (const [key, value] of formData.entries()) {
        console.log(`${key}:`, value)
      }

      axios
        .patch(`http://localhost:8080/aladin/boards`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((response) => {
          if (response.data.success) {
            alert(response.data.message)
            this.$router.push({ name: 'share' }) // Navigate back to property list
          } else {
            alert('Failed to update the property.')
          }
        })
        .catch((error) => {
          console.error('Error updating the property:', error)
          alert('An error occurred. Please try again later.')
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
  overflow-y: auto;
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
</style>
