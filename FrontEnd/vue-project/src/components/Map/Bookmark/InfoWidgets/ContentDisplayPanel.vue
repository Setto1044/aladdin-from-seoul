<template>
  <div>
    <AptContent v-if="tab" :item="item" />
    <ShareContent v-else :id="id" />
  </div>
</template>

<script>
import ShareContent from './ShareContent.vue'
import AptContent from './AptContent.vue'

export default {
  components: {
    ShareContent,
    AptContent,
  },
  props: {
    tab: {
      type: Boolean,
      default: false, // 기본값 설정
    },
    id: {
      type: Number,
      default: null, // 기본값으로 null 설정
    },
    item: {
      type: Object,
      default: () => ({}), // 빈 객체를 기본값으로 설정
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
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  max-width: 800px;
  width: 90%;
  overflow-y: auto;
  max-height: 90vh;
  padding: 20px;
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #ff5f5f;
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 20px;
  cursor: pointer;
  width: 30px;
  height: 30px;
}

.carousel-wrapper {
  margin-bottom: 20px;
}

.slider-image-container {
  width: 100%;
  height: 300px; /* Fixed height for consistency */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border-radius: 12px;
  background-color: #f0f0f0;
}

.slider-image {
  width: auto;
  height: 100%;
  object-fit: cover; /* Ensure image covers the container without distortion */
}

.thumbnails-wrapper {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

.thumbnail-container {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 8px;
  background-color: #f0f0f0;
  cursor: pointer;
}

.thumbnail-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.2s ease-in-out;
}

.thumbnail-container:hover .thumbnail-image {
  transform: scale(1.1);
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
}

.edit-button,
.delete-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.edit-button {
  background-color: #007bff;
  color: white;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.property-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.property-tag {
  background-color: #e0e0e0;
  padding: 4px 8px;
  border-radius: 8px;
  font-size: 12px;
}

.card-author {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.author-avatar-container {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  background-color: #f0f0f0;
}

.author-avatar {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-size: 16px;
  font-weight: bold;
}

.created-time {
  font-size: 14px;
  color: #999;
}

.calendar-section {
  margin-top: 20px;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 12px;
}

.calendar-section p {
  margin-bottom: 10px;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.card-author {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #ddd; /* Optional divider */
}

.author-avatar-container {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  background-color: #f0f0f0;
}

.author-avatar {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.author-name {
  font-size: 16px;
  font-weight: bold;
  flex-grow: 1; /* Push buttons to the right */
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.edit-button,
.delete-button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.edit-button {
  background-color: #007bff;
  color: white;
}

.edit-button:hover {
  background-color: #0056b3;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.delete-button:hover {
  background-color: #c82333;
}

.property-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.property-tag {
  background-color: #e0e0e0;
  padding: 4px 8px;
  border-radius: 8px;
  font-size: 12px;
}

.property-info {
  flex: 1;
  text-align: left;
}

.property-price {
  color: #ff0000;
}

.property-confirmation {
  color: #888;
}

.property-name {
  font-family: 'Score7';
}
</style>
