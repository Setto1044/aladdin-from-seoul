<template>
  <div class="comment-section">
    <h3>댓글</h3>

    <!-- 댓글 작성 폼 -->
    <form @submit.prevent="addComment">
      <textarea
        v-model="newComment"
        placeholder="댓글을 입력하세요"
        rows="3"
        :disabled="isSubmitting"
        class="comment-textarea"
      ></textarea>
      <div>
        <button type="submit" :disabled="!newComment.trim() || isSubmitting">
          {{ isSubmitting ? '작성 중...' : '댓글 작성' }}
        </button>
      </div>
    </form>

    <!-- 댓글 리스트 -->
    <div class="comments-container" ref="commentsContainer">
      <ul class="comments-list">
        <li
          v-for="(comment, index) in comments"
          :key="comment.id"
          class="comment-item"
          ref="commentRefs"
        >
          <div class="comment-content">
            <strong>{{ comment.membersUsername }}</strong>
            <!-- 수정 모드 -->
            <div v-if="editingIndex === index" class="edit-mode">
              <textarea v-model="editCommentContent" class="edit-textarea" rows="3"></textarea>
              <div class="edit-buttons">
                <button @click="confirmEdit(comment.id, index)" class="btn-confirm">확인</button>
                <button @click="cancelEdit" class="btn-cancel">취소</button>
              </div>
            </div>
            <!-- 일반 모드 -->
            <template v-else>
              <p>{{ comment.comment }}</p>
              <div class="comment-footer">
                <small>작성일: {{ formatDate(comment.createdAt) }}</small>
                <div v-if="isAuthor(comment.membersUsername)" class="action-buttons">
                  <button
                    v-if="isAuthor(comment.membersUsername)"
                    class="edit-button"
                    @click="startEdit(index, comment.comment)"
                  >
                    수정
                  </button>
                  <button
                    v-if="isAuthor(comment.membersUsername)"
                    class="delete-button"
                    @click="deleteComment(comment.id, index)"
                  >
                    삭제
                  </button>
                </div>
              </div>
            </template>
          </div>
        </li>
      </ul>
      <!-- 로딩 상태 표시 -->
      <div v-if="isLoading" class="loading">
        <div class="loading-spinner"></div>
        <p>댓글 불러오는 중...</p>
      </div>

      <!-- 더 이상 데이터가 없을 때 -->
      <div v-if="!hasMoreData && comments.length > 0" class="no-more-data">
        모든 댓글을 불러왔습니다.
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import useUserStore from '@/stores/user-store'

export default {
  name: 'CommentSection',
  props: {
    postId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      comments: [],
      lastCursorId: null,
      isLoading: false,
      hasMoreData: true,
      isSubmitting: false,
      newComment: '',
      pageSize: 5,
      observer: null,
      editingIndex: null,
      editCommentContent: '',
    }
  },
  methods: {
    isAuthor(commentAuthor) {
      const userStore = useUserStore()
      return userStore.memberInfo.username === commentAuthor
    },
    setupIntersectionObserver() {
      this.observer = new IntersectionObserver(
        (entries) => {
          const target = entries[0]
          if (target.isIntersecting && this.hasMoreData && !this.isLoading) {
            this.fetchComments()
          }
        },
        {
          root: this.$refs.commentsContainer,
          threshold: 0.1,
        },
      )
    },

    observeLastComment() {
      this.$nextTick(() => {
        const commentRefs = this.$refs.commentRefs
        if (commentRefs && commentRefs.length > 0) {
          const lastComment = commentRefs[commentRefs.length - 1]
          if (lastComment) {
            this.observer?.observe(lastComment)
          }
        }
      })
    },

    async fetchComments() {
      if (this.isLoading) return

      this.isLoading = true

      try {
        const response = await axios.get(
          `http://localhost:8080/aladin/comments/all/${this.postId}`,
          {
            params: {
              cursorId: this.lastCursorId,
              pageSize: this.pageSize,
            },
          },
        )

        if (response.data.success) {
          const newComments = response.data.data

          if (newComments.length > 0) {
            this.comments.push(...newComments)
            this.lastCursorId = newComments[newComments.length - 1].id
          }

          this.hasMoreData = newComments.length === this.pageSize
        } else {
          console.error('댓글 조회 실패:', response.data.message)
        }
      } catch (error) {
        console.error('서버 요청 중 오류:', error)
      } finally {
        this.isLoading = false
        this.observeLastComment()
      }
    },

    async addComment() {
      if (!this.newComment.trim() || this.isSubmitting) return

      this.isSubmitting = true
      const userStore = useUserStore()

      try {
        const response = await axios.post(`http://localhost:8080/aladin/comments`, {
          roomboardsId: this.postId,
          membersUsername: userStore.memberInfo.username,
          comment: this.newComment,
        })

        if (response.data.success) {
          console.log('Comment added successfully:', response.data.message)
          this.newComment = '' // Clear the input field
          this.lastCursorId = null // Reset cursor for fetching
          this.comments = [] // Clear the current comments
          this.hasMoreData = true // Reset pagination state
          await this.fetchComments() // Fetch updated comments
        } else {
          console.error('Failed to add comment:', response.data.message || 'Unknown error')
        }
      } catch (error) {
        console.error('Error while adding comment:', error)
      } finally {
        this.isSubmitting = false
      }
    },

    async deleteComment(commentId, index) {
      if (!confirm('정말로 삭제하시겠습니까?')) return

      try {
        const response = await axios.delete(`http://localhost:8080/aladin/comments/${commentId}`)

        if (response.data.success) {
          this.comments.splice(index, 1)
        }
      } catch (error) {
        console.error('댓글 삭제 실패:', error)
      }
    },

    startEdit(index, currentContent) {
      this.editingIndex = index
      this.editCommentContent = currentContent
    },

    async confirmEdit(commentId, index) {
      try {
        const response = await axios.patch(`http://localhost:8080/aladin/comments`, {
          id: commentId,
          comment: this.editCommentContent,
        })

        if (response.data.success) {
          this.comments[index].comment = this.editCommentContent
          this.cancelEdit()
        }
      } catch (error) {
        console.error('댓글 수정 실패:', error)
      }
    },

    cancelEdit() {
      this.editingIndex = null
      this.editCommentContent = ''
    },

    formatDate(dateTime) {
      const date = new Date(dateTime)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(
        2,
        '0',
      )}-${String(date.getDate()).padStart(2, '0')}`
    },
  },
  mounted() {
    this.setupIntersectionObserver()
    this.fetchComments()
  },
  beforeDestroy() {
    if (this.observer) {
      this.observer.disconnect()
    }
  },
}
</script>
<style scoped>
.comment-section {
  max-width: 800px;
  margin: 2rem auto;
  padding: 0 1rem;
  border-top: 1px solid #e0e0e0;
}

.comments-container {
  max-height: 600px;
  overflow-y: auto;
  /* border: 1px solid #eee; */
  border-radius: 8px;
  padding: 1rem;
  margin-top: 1rem;
}

textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 1rem;
  resize: vertical;
  min-height: 80px;
  font-family: inherit;
}

.comments-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.comment-item {
  padding: 1rem;
  border-bottom: 1px solid #eee;
  transition: background-color 0.2s;
}

.comment-item:hover {
  background-color: #f8f9fa;
}

.comment-content {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
  color: #666;
}

.loading-spinner {
  width: 30px;
  height: 30px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

.no-more-data {
  text-align: center;
  padding: 1rem;
  color: #666;
}

button {
  margin-left: 88%; /* 버튼을 오른쪽 끝으로 밀기 */
  padding: 6px 12px; /* 여유로운 내부 여백 */
  border: 1px solid transparent; /* 기본적으로 테두리 투명 */
  background-color: transparent; /* 배경 투명 */
  color: #2054d2; /* 버튼 텍스트 색상 */
  border-radius: 4px; /* 둥근 모서리 */
  cursor: pointer; /* 클릭 가능 표시 */
  font-size: 14px; /* 적당한 폰트 크기 */
  transition: all 0.3s ease; /* 부드러운 전환 효과 */
}

button:hover:not(:disabled) {
  background-color: #f0f8ff; /* 살짝 밝은 배경 */
  color: #0056b3; /* 텍스트 색상 어둡게 */
}

button:disabled {
  background-color: #95a5a6;
  cursor: not-allowed;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.comment-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 0.5rem;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
}

.edit-button {
  margin-left: auto; /* 버튼을 오른쪽 끝으로 밀기 */
  padding: 6px 12px; /* 여유로운 내부 여백 */
  border: 1px solid transparent; /* 기본적으로 테두리 투명 */
  background-color: transparent; /* 배경 투명 */
  color: #2054d2; /* 버튼 텍스트 색상 */
  border-radius: 4px; /* 둥근 모서리 */
  cursor: pointer; /* 클릭 가능 표시 */
  font-size: 14px; /* 적당한 폰트 크기 */
  transition: all 0.3s ease; /* 부드러운 전환 효과 */
}

.edit-button:hover {
  background-color: #f0f8ff; /* 살짝 밝은 배경 */
  color: #0056b3; /* 텍스트 색상 어둡게 */
}

.edit-button:active {
  background-color: #e0f0ff; /* 누를 때 더 밝은 배경 */
  border-color: #2054d2; /* 테두리 색상 어둡게 */
  color: #003d7a; /* 텍스트 색상 더 어둡게 */
}

.delete-button {
  margin-left: auto; /* 버튼을 오른쪽 끝으로 밀기 */
  padding: 6px 12px; /* 여유로운 내부 여백 */
  border: 1px solid transparent; /* 기본적으로 테두리 투명 */
  background-color: transparent; /* 배경 투명 */
  color: #f14e42; /* 버튼 텍스트 색상 */
  border-radius: 4px; /* 둥근 모서리 */
  cursor: pointer; /* 클릭 가능 표시 */
  font-size: 14px; /* 적당한 폰트 크기 */
  transition: all 0.3s ease; /* 부드러운 전환 효과 */
}

.delete-button:hover {
  background-color: #fce6d9; /* 살짝 밝은 배경 */
  color: #d03d3e; /* 텍스트 색상 어둡게 */
}

.delete-button:active {
  background-color: #fce6d9; /* 누를 때 더 밝은 배경 */
  border-color: #f14e42; /* 테두리 색상 어둡게 */
  color: #ad2f3a; /* 텍스트 색상 더 어둡게 */
}

.comment-textarea {
  resize: none;
  margin: 0px;
}

.edit-mode {
  width: 100%;
}

.edit-textarea {
  margin: 0.5rem 0;
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ced4da;
  border-radius: 4px;
  min-height: 60px;
}

.edit-buttons {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
  margin-top: 0.5rem;
}

.btn-confirm {
  background-color: #198754;
  color: white;
  padding: 0.4rem 1rem;
  font-size: 0.9rem;
}

.btn-confirm:hover {
  background-color: #157347;
}

.btn-cancel {
  background-color: #6c757d;
  color: white;
  padding: 0.4rem 1rem;
  font-size: 0.9rem;
}

.btn-cancel:hover {
  background-color: #5c636a;
}

.comment-item {
  display: flex;
  flex-direction: column;
  padding: 1rem;
  border-bottom: 1px solid #eee;
}

.comment-content {
  width: 100%;
}

/* 기존 button 스타일 수정 */
button {
  font-weight: normal;
  white-space: nowrap;
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}
</style>
