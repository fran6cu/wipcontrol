<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="fade" appear mode="out-in">
        <section class="modal modalSched">
          <section class="modalSched__pickDate" v-if="!showPickHours">
            <p class="modalSched__pickDate__title">Select a date</p>
            
              <input class="modalSched__pickDate__input" v-model="pickedDate" type="date" :min="today">
          </section>
          
          <section class="modalSched__error" v-if="pickedDate !== null && !showPickHours">
            <p class="modalSched__error__text">Can not add more scheduled task</p>
          </section>
        
          <section class="modalSched__pickStory" v-if="pickedDate !== null && !showPickHours">
            <p class="modalSched__pickStory__title">Select User Story</p>
            <select class="modalSched__pickStory__input" v-model="selectedStory" v-on:change="checkStory">
              <option v-for="story in allStorys" :value="story">
                {{ story.name }}
              </option>
            </select>
          </section>
        
          <section v-if="pickedDate && selectedStory && !showPickHours">
            <button class="pickHourbtn" @click="showPickHours = true">
              Choose time
            </button>
          </section>
          
          <PickHours v-if="showPickHours" :selectedtaskDate="pickedDate" :selectedtaskStory="selectedStory" >
          </PickHours>
        </section>
      </Transition>
      <Transition name="fade" appear mode="out-in" @sessionAdded="$emit('close')">
      </Transition>
    </section>
  </Transition>
</template>

<script>

/**

 * @module Component/Overlays/AddSched
 * 
 * @property {Object} data 
 * @property {Object} data.today 
 * @property {Object} data.pickedSessions 
 * @property {Object} data.allSalas 
 * @property {Object} data.avalaibleSalas 
 * @property {Object} data.pickedDate 
 * @property {Object} data.allStorys 
 * @property {Object} data.selectedStory 
 * @property {Object} data.selectedSala 
 * @property {Object} data.showPickHours 
 * @property {Object} emits 
 * @property {Object} emits.close 
 * @property {Object} components 
 * @property {Object} components.PickHours 
 */
import PickHours from "./PickHours.vue";
export default {
  name: "AddSched",
  data() {
    return {
      today: this.whatDayIsToday(),
      pickedSessions: null,
      allSalas: null,
      avalaibleSalas: null,
      pickedDate: null,
      allStorys: null,
      selectedStory: null,
      selectedSala: null,
      showPickHours: false
    }
  },
  emits: ['close'],
  components: {
    PickHours
  },
  methods: {
    /**
     * Function to get today's date
     * @returns {String} 
     */
    whatDayIsToday() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0');
      const day = String(today.getDate()).padStart(2, '0');

      return `${year}-${month}-${day}`;
    },
    /**
     * Function to get all today's scheds 
     * @returns {Object} 
     */
    async getAfterTodayScheds() {
      const apiUrl = import.meta.env.VITE_API_URL;
      const response = await fetch(`${apiUrl}/scheds/afterToday`);
      const data = await response.json();
      return data;
    },
 
    /**
     * Function to get all US 
     * @returns {Object} 
     */
    async getAllStorys() {
      const apiUrl = import.meta.env.VITE_API_URL;
      const response = await fetch(`${apiUrl}/storiesMini`);
      const data = await response.json();
      if (data.code) {
        return [];
      }
      return data;
    },
   
  },
  async mounted() {
    this.pickedScheds = await this.getAfterTodayScheds();
    this.allStorys = await this.getAllStorys();
  }
}
</script>