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
    setMemberEditInfo(info) {
      // Pinia 상태 업데이트 시 기존 구조를 유지하면서 새로운 데이터를 병합
      this.memberInfo = {
        ...this.memberInfo,
        userid: info.id, // 폼 데이터의 `id`를 Pinia의 `userid`에 매핑
        username: info.username,
        name: info.name,
        nickname: info.nickname,
        email: info.email,
        grade: info.grade,
        bio: info.bio,
        profileImagePath: info.profileImagePath,
      }
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
