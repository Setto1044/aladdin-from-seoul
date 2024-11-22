import { defineStore } from 'pinia'

const useUserStore = defineStore('user', {
  state: () => ({
    isLoggedIn: false,
    memberInfo: {
      userid: null,
      username: null,
      name: null,
      nickname: null,
      email: null,
      grade: null,
      bio: null,
      profileImagePath: null,
    },
  }),
  getters: {
    getUserName: (state) => state.memberInfo?.username || '',
    getUserId: (state) => state.memberInfo?.userid || '',
    getUserGrade: (state) => state.memberInfo.grade || '',
  },
  actions: {
    setLoginStatus(status) {
      this.isLoggedIn = status
    },
    setMemberInfo(info) {
      this.memberInfo = info || {} // Ensure it's defined
    },
    clearMemberInfo() {
      this.isLoggedIn = false
      this.memberInfo = {
        userid: null,
        name: null,
        nickname: null,
        email: null,
        grade: null,
        bio: null,
        profileImagePath: null,
      }
    },
    logout() {
      this.clearMemberInfo()
      localStorage.removeItem('isLoggedIn')
      localStorage.removeItem('memberInfo')
    },
  },
  persist: {
    // 브라우저 스토리지에 저장할 필드 설정
    key: 'user-store',
    storage: localStorage, // 또는 sessionStorage
  },
})

export default useUserStore
