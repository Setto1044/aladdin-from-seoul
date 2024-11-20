<template>
  <div class="filter-bar">
    <div class="filter-option">
      <button @click="toggleModal('transaction')">
        {{ selectedTransactionType || '월세, 전세' }} ⚙️
      </button>
      <filter-modal
        v-if="activeModal === 'transaction'"
        @close="closeModal"
        @select="updateOption('transaction', $event)"
      />
    </div>

    <div class="filter-option">
      <button @click="toggleModal('roomSize')">{{ selectedRoomType || '방크기' }} ⚙️</button>
      <room-size-modal
        v-if="activeModal === 'roomSize'"
        @close="closeModal"
        @select="updateOption('roomSize', $event)"
      />
    </div>

    <div class="filter-option">
      <button @click="toggleModal('approvalDate')">
        {{ selectedApprovalDate || '사용승인일' }} ⚙️
      </button>
      <approval-date-modal
        v-if="activeModal === 'approvalDate'"
        @close="closeModal"
        @select="updateOption('approvalDate', $event)"
      />
    </div>

    <div class="filter-option">
      <button @click="toggleModal('additionalFilters')">
        {{ selectedAdditionalFilters || '추가필터' }} ⚙️
      </button>
      <additional-filters-modal
        v-if="activeModal === 'additionalFilters'"
        @close="closeModal"
        @select="updateOption('additionalFilters', $event)"
      />
    </div>

    <button @click="resetFilters">🔄</button>
  </div>
</template>

<script>
import FilterModal from '@/components/FilterModal.vue'
import RoomSizeModal from '@/components/RoomSizeModal.vue'
import ApprovalDateModal from '@/components/ApprovalDateModal.vue'
import AdditionalFiltersModal from '@/components/AdditionalFiltersModal.vue'

export default {
  components: {
    FilterModal,
    RoomSizeModal,
    ApprovalDateModal,
    AdditionalFiltersModal,
  },
  data() {
    return {
      selectedTransactionType: '',
      selectedRoomType: '',
      selectedApprovalDate: '',
      selectedAdditionalFilters: '',
      activeModal: null,
    }
  },
  methods: {
    toggleModal(modalType) {
      this.activeModal = this.activeModal === modalType ? null : modalType
    },
    closeModal() {
      this.activeModal = null
    },
    updateOption(optionType, selectedValue) {
      switch (optionType) {
        case 'transaction':
          this.selectedTransactionType = selectedValue
          break
        case 'roomSize':
          this.selectedRoomType = selectedValue
          break
        case 'approvalDate':
          this.selectedApprovalDate = selectedValue
          break
        case 'additionalFilters':
          this.selectedAdditionalFilters = selectedValue
          break
      }
      this.closeModal()
    },
    resetFilters() {
      this.selectedTransactionType = ''
      this.selectedRoomType = ''
      this.selectedApprovalDate = ''
      this.selectedAdditionalFilters = ''
    },
  },
}
</script>

<style scoped>
.filter-bar {
  display: flex;
  gap: 10px;
  align-items: center;
}
.filter-option {
  position: relative;
}
button {
  background-color: transparent;
  border: 1px solid #ccc;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #f0f0f0;
}
</style>
