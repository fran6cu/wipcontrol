<template>
  <section class="modalSched__pickHours">
    <p class="modalSched__pickHours__title">Select time</p>
    <section v-for="(input, index) in hourList" :key="`hour - ${index}`" class="modalSched__pickHours__group">
      <input class="modalSched__pickHours__group--input" @blur="isValidHour(index, input.hour)"
        :class="{ 'error': showError[index] }" type="time" v-model="input.hour">
      <section class="inputButtons">
        <img v-if="hourList.length >= 0 && hourList.length < 5" @click="addHour" class="inputButtons__add"
          src="../../assets/icons/add.svg" alt="Add">
        <img v-if="hourList.length > 1" @click="deleteHour(index)" class="inputButtons__delete"
          src="../../assets/icons/cross.svg" alt="Delete">
      </section>
    </section>
    <Transition name="errorMessage">
      <section v-show="errorMessage !== null" class="formContainer__body__form--error">
        <ul class="list">
          <li class="item">{{ errorMessage }}</li>
        </ul>
      </section>
    </Transition>
    <button class="pickHourbtn" v-if="hourListValid" @click="addNewtask">
      Schedule Task
    </button>
  </section>
</template>

<script>

/**
 
 * @module Component/Overlays/PickHours
 * 
 * @property {Object} props - Properties
 * @property {Object} props.selectedtaskDate - Date selected
 * @property {Object} props.selectedtaskStory - US selected
 * @property {Object} data - Component data
 * @property {Array} data.hourList - Selected hours
 * @property {Boolean} data.hourListValid - Hours is ok
 * @property {Array} data.noNullList - Hours not null
 * @property {Array} data.showError - Errors
 * @property {String} data.errorMessage - msg error
 */
export default {
  name: "PickHours",
  props: [
    "selectedtaskDate",
    "selectedtaskStory",
    
  ],
  data() {
    return {
      hourList: [{ hour: null }],
      hourListValid: false,
      noNullList: [],
      showError: [],
      errorMessage: null
    }
  },
  watch: {
    /**
     * Check if hours is ok
     */
    hourList: {
      handler: function () {
        this.checkNotEmptyNotRepeat();
      },
      deep: true,
      inmediate: true
    },
    /**
     * Check msg error 
     */
    errorMessage: {
      handler: function () {
        if (this.errorMessage === null) {
          this.hourListValid = true;
        } else {
          this.hourListValid = false;
        }
      },
      deep: true,
      inmediate: true
    }
  },
  methods: {
    /**
     * Add hour to the list
     */
    addHour() {
      if (this.errorMessage !== null) {
        return;
      }
      this.hourList.push({ hour: null });
    },
    /**
     * Delete hour from the list
     * @param {Number} index r
     */
    deleteHour(index) {
      this.hourList.splice(index, 1);
    },
    /**
     * Check list is not empty and time do not overlap
     */
    isValidHour(index, hour) {
      if (this.errorMessage !== null) {
        this.verifyOverlapingHours();
        if (this.errorMessage !== null) {
          return;
        }
      }
      if (hour === null || hour === "") {
        this.errorMessage = "Can not be empty"
        this.showError[index] = true;
      } else {
        this.errorMessage = null;
        this.showError[index] = false;
        if (this.hourList.length > 1) {
          this.verifyOverlapingHours();
        }
      };
    },
    /**
     * Check hours do not overlap
     */
    verifyOverlapingHours() {
      const sortedHourList = this.hourList
        .slice()
        .filter(hour => hour.hour !== '' && hour.hour !== null)
        .sort((a, b) => {
          const dateA = new Date();
          dateA.setHours(parseInt(a.hour.split(":")[0]));
          dateA.setMinutes(parseInt(a.hour.split(":")[1]));

          const dateB = new Date();
          dateB.setHours(parseInt(b.hour.split(":")[0]));
          dateB.setMinutes(parseInt(b.hour.split(":")[1]));

          return dateA - dateB;
        })
        .map((hour, index) => {
          const realIndex = this.hourList.indexOf(hour);
          return {
            ...hour,
            realIndex
          }
        });

      let overlapingHours = new Set();
      sortedHourList.forEach((currentHour, index) => {
        if (index < sortedHourList.length - 1) {
          const hourA = currentHour.hour;
          const hourB = sortedHourList[index + 1].hour;

          const dateA = new Date();
          dateA.setHours(parseInt(hourA.split(":")[0]));
          dateA.setMinutes(parseInt(hourA.split(":")[1]));

          const dateB = new Date();
          dateB.setHours(parseInt(hourB.split(":")[0]));
          dateB.setMinutes(parseInt(hourB.split(":")[1]));

          const minutesDifference = Math.abs(dateB - dateA) / (1000 * 60);

          if (minutesDifference < this.selectedtaskStory.estimation + 20) {
            overlapingHours.add(currentHour.realIndex);
            overlapingHours.add(sortedHourList[index + 1].realIndex);
          } else {
            this.errorMessage = null;
            this.showError[currentHour.realIndex] = false;
            this.showError[sortedHourList[index + 1].realIndex] = false;
          }
        }
      });
      
      if (overlapingHours.size > 0) {
        this.errorMessage = `US estimation time (${this.selectedtaskStory.estimation} minutes) plus a time to relax (10 minutes)`;
        this.hourListValid = false;
        overlapingHours.forEach(indexHour => {
          this.showError[indexHour] = true;
        })
      } else {
        this.errorMessage = null;
      }
    },
    /**
     * Check hours is not empty o repeated
     */
    checkNotEmptyNotRepeat() {
      if (this.errorMessage !== null) {
        return;
      }
      const notNull = this.hourList.filter(hour => hour.hour !== null && hour.hour !== "");
      this.noNullList = notNull;
      if (this.noNullList.length === 0) {
        this.hourListValid = false;
        this.errorMessage = "Choose an hour";
        return;
      } else {
        this.hourListValid = true;
        this.errorMessage = null;
      }
      const uniqueHours = [...new Set(this.noNullList.map(hour => hour.hour))];
      const repeatError = uniqueHours.length !== this.noNullList.length;
      if (repeatError) {
        this.errorMessage = "Can not repeat hours";
        return;
      } else {
        this.errorMessage = null;
        this.showError = [];
      }
    },
    /**
     * Add hours to the schedule
     */
    async addNewtask() {
      const hourListString = this.hourList.map(hour => hour.hour).join(", ");
      const apiUrl = import.meta.env.VITE_API_URL;

      const newtask = {
        date: this.selectedtaskDate,
        hours: hourListString,
        id_story: this.selectedtaskStory.id,
       
      }

      await fetch(`${apiUrl}/scheds`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(newtask)
      })

      this.$router.go();
    }
  }
}
</script>