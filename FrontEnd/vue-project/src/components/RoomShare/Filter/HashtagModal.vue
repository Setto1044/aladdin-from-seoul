<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal" :style="{ top: `${modalPosition.top}px`, left: `${modalPosition.left}px` }">
      <div class="title">해시태그 입력</div>
      <div class="hashtag-input">
        <div class="input-container">
          <div class="hashtag" v-for="(tag, index) in hashtags" :key="index">
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
      <div class="actions">
        <button @click="confirmTags">확인</button>
        <button @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    buttonRef: {
      type: Object,
      required: true,
    },
    initialHashtags: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      hashtags: [...this.initialHashtags],
      currentTag: '',
      modalPosition: { top: 0, left: 0 },
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.calculateModalPosition()
    })
  },
  methods: {
    calculateModalPosition() {
      if (!this.buttonRef) {
        console.error('Button reference not found')
        return
      }
      const buttonRect = this.buttonRef.getBoundingClientRect()
      this.modalPosition = {
        top: buttonRect.bottom + window.scrollY + 8,
        left: buttonRect.left + window.scrollX,
      }
    },
    addTag() {
      const maxTags = 10
      const trimmedTag = this.currentTag.trim()
      const isValid = /^[a-zA-Z0-9가-힣]+$/.test(trimmedTag)
      if (this.hashtags.length >= maxTags) {
        alert(`최대 ${maxTags}개의 태그만 추가할 수 있습니다.`)
        return
      }
      if (trimmedTag && isValid && !this.hashtags.includes(trimmedTag)) {
        this.hashtags.push(trimmedTag)
      }
      this.currentTag = ''
    },
    removeTag(index) {
      this.hashtags.splice(index, 1)
    },
    removeLastTag() {
      if (this.currentTag === '' && this.hashtags.length > 0) {
        this.hashtags.pop()
      }
    },
    confirmTags() {
      this.$emit('update', this.hashtags)
      this.closeModal()
    },
    closeModal() {
      this.$emit('close')
      this.currentTag = ''
      this.hashtags = [...this.initialHashtags]
    },
  },
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 999;
  background: transparent; /* 오버레이 배경을 약간 투명하게 설정 */
}

.modal {
  position: absolute;
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1); /* 더 부드러운 그림자 */
  width: 300px; /* 모달 크기 */
  animation: fadeIn 0.3s ease-in-out;
}

.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 12px;
  text-align: center; /* 제목을 중앙 정렬 */
}

.hashtag-input {
  margin-bottom: 16px;
}

.input-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px;
  max-height: 100px; /* 최대 높이 설정 */
  overflow-y: auto; /* 태그가 많아지면 스크롤 활성화 */
}

.hashtag {
  background: #007bff;
  color: white;
  padding: 4px 8px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.hashtag .remove-tag {
  margin-left: 4px;
  cursor: pointer;
  font-weight: bold;
}

.hashtag .remove-tag:hover {
  color: #ff4444; /* 삭제 버튼에 호버 효과 추가 */
}

input {
  border: none;
  outline: none;
  flex: 1;
  font-size: 14px;
  padding: 4px;
}

.actions {
  display: flex;
  justify-content: space-between;
}

button {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

button:first-of-type {
  background: #007bff;
  color: white;
  font-weight: bold;
}

button:first-of-type:hover {
  background: #0056b3;
}

button:last-of-type {
  background: #ddd;
  color: black;
}

button:last-of-type:hover {
  background: #ccc;
}
</style>
