<template>
  <div class="room-share-page">
    <div class="page-header">
      <h2 class="main-title">ㅇㅇ님의 글보기</h2>
    </div>
    <main class="main-content">
      <div class="post-list">
        <div class="post-row">
          <!-- 카드 리스트 -->
          <CardRow :cards="cardData" @card-click="handleCardClick" />
        </div>
      </div>
      <PropertyDetailModal
        v-if="selectedCard"
        :show="showModal"
        @close="closeModal"
        v-bind="selectedCard"
      />
    </main>
  </div>
</template>

<script>
import CardRow from '@/components/RoomShare/SharePostCardList.vue'
import PropertyDetailModal from '@/components/RoomShare/SharePostDetail.vue'

export default {
  components: {
    CardRow,
    PropertyDetailModal,
  },
  data() {
    return {
      cardData: [
        {
          id: 1,
          title: 'Card 1',
          description: 'Description 1',
          detail: 'Detailed description of the property.',
          address: '123 Main St, City',
          price: 1500,
          houseSize: 45.5,
          pricePer: 'month',
          latitude: '37.7749',
          longitude: '-122.4194',
          rentFrom: '2024-01-01',
          rentTo: '2024-12-31',
          tags: ['#Balcony', '#Elevator', '#Balcony', '#Elevator', '#Balcony', '#Elevator'],
          views: 100,
          hostId: 123,
          imageUrls: ['https://example.com/image1.jpg'],
          comments: ['Great place!', 'Very cozy!'],
        },
        {
          id: 2,
          title: 'Card 2',
          description: 'Beautiful 2-bedroom apartment.',
          detail: 'Spacious and modern apartment with all amenities included.',
          address: '456 Elm St, City',
          price: 2000,
          houseSize: 60.0,
          pricePer: 'month',
          latitude: '37.7849',
          longitude: '-122.4294',
          rentFrom: '2024-02-01',
          rentTo: '2024-12-31',
          tags: ['#Balcony', '#Elevator', '#Balcony', '#Elevator', '#Balcony', '#Elevator'],
          views: 250,
          hostId: 124,
          imageUrls: [
            'https://cdn.class101.net/images/3e1377ef-0370-454f-8829-251668e8d7bd',
            'https://health.chosun.com/site/data/img_dir/2023/01/10/2023011001501_0.jpg',
          ],
          comments: ['Spacious rooms!', 'Great neighborhood!'],
        },
        {
          id: 3,
          title: 'Card 3',
          description: 'Cozy studio in downtown.',
          detail: 'Ideal for single professionals or students.',
          address: '789 Maple Ave, City',
          price: 1200,
          houseSize: 35.0,
          pricePer: 'month',
          latitude: '37.7649',
          longitude: '-122.4094',
          rentFrom: '2024-03-01',
          rentTo: '2024-12-31',
          tags: ['#Balcony', '#Elevator', '#Balcony', '#Elevator', '#Balcony', '#Elevator'],
          views: 150,
          hostId: 125,
          imageUrls: ['https://example.com/image4.jpg'],
          comments: ['Perfect for short stays.', 'Love the view!'],
        },
        {
          id: 4,
          title: 'Card 4',
          description: 'Luxury villa with garden.',
          detail: 'Beautiful villa perfect for family living.',
          address: '321 Oak Rd, Suburb',
          price: 5000,
          houseSize: 150.0,
          pricePer: 'month',
          latitude: '37.7949',
          longitude: '-122.4594',
          rentFrom: '2024-05-01',
          rentTo: '2024-12-31',
          tags: ['#Garden', '#Pool'],
          views: 300,
          hostId: 126,
          imageUrls: ['https://example.com/image5.jpg', 'https://example.com/image6.jpg'],
          comments: ['Amazing garden!', 'Spacious and luxurious.'],
        },
        {
          id: 5,
          title: 'Card 5',
          description: 'Modern condo with city view.',
          detail: 'Located in the heart of the city with a stunning skyline view.',
          address: '987 Pine St, City',
          price: 2500,
          houseSize: 75.0,
          pricePer: 'month',
          latitude: '37.7549',
          longitude: '-122.4194',
          rentFrom: '2024-06-01',
          rentTo: '2024-12-31',
          tags: ['#Balcony', '#Gym'],
          views: 200,
          hostId: 127,
          imageUrls: ['https://example.com/image7.jpg'],
          comments: ['Great location.', 'Love the balcony view!'],
        },
        {
          id: 6,
          title: 'Card 6',
          description: 'Affordable room for rent.',
          detail: 'Cozy and budget-friendly room with shared amenities.',
          address: '654 Cedar St, City',
          price: 800,
          houseSize: 20.0,
          pricePer: 'month',
          latitude: '37.7349',
          longitude: '-122.3894',
          rentFrom: '2024-04-01',
          rentTo: '2024-12-31',
          tags: ['#Shared Kitchen', '#Near Public Transport'],
          views: 180,
          hostId: 128,
          imageUrls: ['https://example.com/image8.jpg'],
          comments: ['Good value for the price.', 'Convenient location.'],
        },
        {
          id: 7,
          title: 'Card 7',
          description: 'Penthouse with panoramic view.',
          detail: 'Top-floor penthouse offering a 360-degree city view.',
          address: '159 Skyview Dr, City',
          price: 7000,
          houseSize: 200.0,
          pricePer: 'month',
          latitude: '37.8149',
          longitude: '-122.4894',
          rentFrom: '2024-07-01',
          rentTo: '2024-12-31',
          tags: ['#Terrace', '#Private Elevator'],
          views: 500,
          hostId: 129,
          imageUrls: ['https://example.com/image9.jpg', 'https://example.com/image10.jpg'],
          comments: ['The view is breathtaking!', 'Feels like a dream home.'],
        },
        {
          id: 8,
          title: 'Card 8',
          description: 'Suburban house with garage.',
          detail: 'Family-friendly house with a spacious garage.',
          address: '753 Willow Ln, Suburb',
          price: 1800,
          houseSize: 100.0,
          pricePer: 'month',
          latitude: '37.7749',
          longitude: '-122.3694',
          rentFrom: '2024-02-15',
          rentTo: '2024-12-31',
          tags: ['#Garage', '#Backyard'],
          views: 220,
          hostId: 130,
          imageUrls: ['https://example.com/image11.jpg'],
          comments: ['Perfect for families.', 'Nice backyard space.'],
        },
      ],
      showModal: false,
      selectedCard: null,
    }
  },
  methods: {
    handleCardClick(card) {
      this.selectedCard = card
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.selectedCard = null
    },
    goToMyPosts() {
      // 내 글 보기 기능 로직
      this.$router.push({ path: 'share/user/1' })
    },
    createNewPost() {
      // 글 작성 기능 로직
      this.$router.push({ name: 'share-write' })
    },
  },
}
</script>

<style scoped>
.room-share-page {
  padding: 20px;
  background-color: #f9f9f9; /* 페이지 배경을 약간 밝게 변경 */
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.page-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.main-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.button-group {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.action-button {
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  background-color: #007bff;
  color: #fff;
  transition:
    background-color 0.3s,
    transform 0.2s;
}

.action-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.create-button {
  background-color: #28a745;
}

.create-button:hover {
  background-color: #218838;
}

.search-form {
  display: flex;
  align-items: center;
  margin-top: 10px;
  width: 100%;
  max-width: 600px;
}

.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px 0 0 8px;
  font-size: 14px;
}

.search-button {
  padding: 10px 20px;
  border: none;
  border-radius: 0 8px 8px 0;
  background-color: #007bff;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0056b3;
}

.post-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.post-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}
</style>
