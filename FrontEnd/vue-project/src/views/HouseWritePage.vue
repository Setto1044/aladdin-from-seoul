<template>
  <div class="property-form">
    <h1>Create a New Property Listing</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="title">Title</label>
        <input type="text" id="title" v-model="formData.title" required />
      </div>

      <div class="form-group">
        <label for="detail">Detail</label>
        <textarea id="detail" v-model="formData.detail" required></textarea>
      </div>

      <div class="form-group">
        <label for="address">Address</label>
        <div class="address-input">
          <input
            type="text"
            id="postcode"
            v-model="formData.postcode"
            placeholder="우편번호"
            readonly
          />
          <button type="button" @click="execDaumPostcode">우편번호 찾기</button>
        </div>
        <input
          type="text"
          id="roadAddress"
          v-model="formData.address"
          placeholder="도로명주소"
          readonly
          required
        />
      </div>

      <div class="form-group">
        <label for="price">Price</label>
        <input type="number" id="price" v-model.number="formData.price" required />
      </div>

      <div class="form-group">
        <label for="houseSize">House Size (m²)</label>
        <input type="number" id="houseSize" v-model.number="formData.houseSize" required />
      </div>

      <div class="form-group">
        <label for="pricePer">Price Per</label>
        <select id="pricePer" v-model="formData.pricePer">
          <option value="week">Week</option>
          <option value="month">Month</option>
        </select>
      </div>

      <div class="form-group">
        <label for="rentStart">Rent Start Date</label>
        <input type="date" id="rentStart" v-model="formData.rentStart" required />
      </div>

      <div class="form-group">
        <label for="rentEnd">Rent End Date</label>
        <input type="date" id="rentEnd" v-model="formData.rentUntil" required />
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

      <div class="form-group">
        <label for="imageFiles">Upload Images</label>
        <input type="file" id="imageFiles" multiple @change="handleImageUpload" ref="fileInput" />
        <div class="image-preview">
          <div v-for="(url, index) in formData.imagePreviews" :key="index" class="image-container">
            <img :src="url" alt="Image preview" />
            <button @click="removeImage(index)" class="delete-button">X</button>
          </div>
        </div>
      </div>

      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
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
      this.formData.imageFiles.push(...files)
      this.formData.imagePreviews.push(...files.map((file) => URL.createObjectURL(file)))

      // Reset the input value to allow re-uploading the same file
      this.$refs.fileInput.value = ''
    },
    removeImage(index) {
      this.formData.imageFiles.splice(index, 1)
      this.formData.imagePreviews.splice(index, 1)
      console.log(this.formData.imageFiles)
    },
    submitForm() {
      console.log('Form data submitted:', this.formData)
      // Add logic to submit form data
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
}
.hashtag {
  background-color: #e0e0e0;
  padding: 5px 10px;
  border-radius: 4px;
  display: flex;
  align-items: center;
}
.remove-tag {
  margin-left: 5px;
  cursor: pointer;
  color: red;
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
</style>
