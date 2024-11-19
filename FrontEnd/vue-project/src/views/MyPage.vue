<template>
  <div class="profile-page">
    <section class="user-info-section">
      <div class="avatar"></div>
      <div class="user-details">
        <h2 class="user-name">John Doe</h2>
        <span class="user-type">Pro User</span>
        <p class="welcome-message">Welcome to your personalized page</p>
      </div>
      <div class="user-actions">
        <button class="btn-secondary">Log Out</button>
        <button class="btn-primary" @click="goToEditPage">Edit Profile</button>
      </div>
      <img
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
        alt=""
        class="background-image"
      />
    </section>

    <section class="user-posts-section">
      <div class="posts-image-container">
        <section class="popular-properties">
          <div class="property-grid">
            <HouseCardComponent></HouseCardComponent>
            <HouseCardComponent></HouseCardComponent>
          </div>
        </section>
      </div>
      <MyPageDetailComponent
        title="내가 올린 글"
        description="내가 올린 글을 봅니다"
        url="/listings/2"
      ></MyPageDetailComponent>
      <img
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
        alt=""
        class="background-image"
      />
    </section>

    <section class="user-interests-section">
      <MyPageDetailComponent
        title="관심목록"
        description="내가 찜한 관심목록을 봅니다"
        url="/favorites"
      ></MyPageDetailComponent>
      <div class="interests-image-container">
        <section class="popular-properties">
          <div class="property-grid">
            <HouseUserCardComponent></HouseUserCardComponent>
            <HouseUserCardComponent></HouseUserCardComponent>
          </div>
        </section>
      </div>
      <img
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/4375c0390c2ebe6f7cc8a48da21680487a9b51a064afdf60421a0e7a8892be8b?placeholderIfAbsent=true&apiKey=1cde1290a1ae40d2a6a843379a06e85e"
        alt=""
        class="background-image"
      />
    </section>
  </div>
</template>

<script>
import HouseCardComponent from '@/components/HouseCardComponent.vue'
import HouseUserCardComponent from '@/components/HouseUserCardComponent.vue'
import MyPageDetailComponent from '@/components/MyPageDetailSection.vue'

export default {
  components: {
    HouseCardComponent,
    HouseUserCardComponent,
    MyPageDetailComponent,
  },
  methods: {
    goToEditPage() {
      this.$router.push({ path: '/mypage/edit' })
    },
  },
}
</script>

<style scoped></style>
