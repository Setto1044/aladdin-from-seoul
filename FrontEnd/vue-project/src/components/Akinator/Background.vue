<template>
  <div class="Akinator-Game">
    <!-- 외곽 꾸미기 이미지 -->
    <div class="top-left-image">
      <img src="@/assets/flower.png" alt="Top Left Decoration" />
    </div>
    <div class="top-right-image">
      <img src="@/assets/lamp.png" alt="Top Right Decoration" />
    </div>
    <div class="bottom-left-image">
      <img src="@/assets/star.png" alt="Bottom Left Decoration" />
    </div>
    <div class="bottom-right-image">
      <img src="@/assets/cloud.png" alt="Bottom Right Decoration" />
    </div>
    <div class="bottom-image">
      <img src="@/assets/scroll.png" alt="Bottom Right Decoration" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'ParallaxBackground',
}
</script>

<style scoped>
/* 외곽 이미지 */
.top-left-image,
.top-right-image,
.bottom-left-image,
.bottom-right-image,
.bottom-image {
  position: absolute;
}

.top-left-image {
  top: 1;
  left: 24%;
  scale: 2.3;
}

.top-right-image {
  top: 16%;
  right: 14%;
  scale: 4;
}

.bottom-left-image {
  bottom: 19%;
  left: 14%;
  scale: 5;
}

.bottom-right-image {
  bottom: 10%;
  right: 10%;
  scale: 3;
}

.bottom-image {
  bottom: 5%;
  right: 47%;
  scale: 1;
}

.top-left-image img,
.top-right-image img,
.bottom-left-image img,
.bottom-right-image img,
.bottom-image img {
  width: 100px;
  height: 100px;
  object-fit: contain;
  opacity: 0.7; /* 투명도 조정 */
}

/* 채팅 레이아웃 */
.akinator-chat {
  width: 40%;
  margin: 0 auto;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.chat-header {
  background-color: #007bff;
  color: white;
  padding: 10px;
  border-radius: 8px 8px 0 0;
}

.chat-container {
  padding: 20px;
  height: calc(100vh - 100px);
  overflow-y: auto;
}

.chat-box {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 버튼 스타일 */
.start-button,
.input-button,
.restart-button {
  margin: 10px;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 25px;
  cursor: pointer;
}
</style>
