<template>
  <Loader v-if="isLoading" />
  <section v-else-if="!isLoading && taskExists">
    <h2 class="ticketContainer__title">User Story</h2>
    <span class="ticketContainer__divider"></span>
    <section class="sliderDetails">
      <button class="sliderDetails__sliderButton" @click="showPreviousDates">
        <img src="../../assets/icons/left.svg" alt="Back">
      </button>
      <section class="sliderDetails__item" v-for="date in visibleDates" :key="date.date" @click="toggleClass(date.date)"
        v-bind:class="{ 'selected': date.date === selectedDate }">
        {{ date.formattedDate }}
      </section>
      <button class="sliderDetails__sliderButton" @click="showNextDates">
        <img src="../../assets/icons/right.svg" alt="Next">
      </button>
    </section>
   
  </section>
  <section v-else-if="!isLoading && !taskExists">
    <PointStory></PointStory>
  </section>
 
</template>

<script>
import Loader from '../Main/Loader.vue'
import PointStory from '../StoryDetails/PointStory.vue';


/**
 
 * @module Component/Backlog/BacklogStory
 * 
 * @property {Object} components - Components
 * @property {Object} components.Loader - Component Loader
 * @property {Object} components.PointStory - Component points
 * @property {Object} data - Data
 * @property {boolean} data.isLoading - is loading
 * @property {Object[]} data.storyTasks - story task
 * @property {Object} data.orderedTasks - task ordered
 * @property {string[]} data.dates - task dates
 * @property {string[]} data.visibleDates - visible or not
 * @property {Object[]} data.tasksList - task list
 * @property {string} data.selectedDate - Date selected
 * @property {number} data.index - index selected task
 * @property {boolean} data.taskExists - story with task
 * @property {boolean} data.showAddTask - show modal
 */
export default {
  name: "BacklogStoryDetails",
  components: {
    Loader,
    PointStory,
    BuyTicket
  },
  data() {
    return {
      isLoading: true,
      storyTasks: [],
      orderedTasks: {},
      dates: [],
      visibleDates: [],
      tasksList: [],
      selectedDate: "",
      index: 0,
      taskExists: true,
      
     
    }
  },
  /**
   * check dates
   */
  watch: {
    selectedDate: function (val) {
      this.showHours(val);
    }
  },
  methods: {
    /**
     * check task from story for today
     * 
     * @returns {Promise} - story tasks
     */
    async getStoryInfo() {
      const apiUrl = import.meta.env.VITE_API_URL;
      const response = await fetch(`${apiUrl}/scheds/afterToday/story?idStory=${this.$route.params.id}`)
      this.storyTasks = await response.json();
      if (this.storyTasks.hasOwnProperty("code")) {
        this.taskExists = false;
      }
    },
    /**
     * sort tasks by dates
     */
    sortDates() {
      let uniqueDates = new Set();

      // list with tasks
      this.storyTasks.forEach(task => {
        uniqueDates.add(task["date"])
      })

      // array with unique tasks
      this.dates = Array.from(uniqueDates)

      // ordered by dates
      this.dates.sort(function (a, b) {
        return new Date(a) - new Date(b)
      })
    },
    /**
     * new object ordered by key date with stories array value
     * stories ordered by name that returnan array with task id
     * @returns {Object} - Tasks ordered
     */
    sortTasksByDate() {
      let tasksByDate = {}
      this.dates.forEach(date => {
        tasksByDate[date] = {};

        // tasks ordered by date
        tasksByDate[date] = this.storyTasks
          .filter(task => task["date"] === date)
          .reduce((datetaskSum, task) => {
            if (datetaskSum[task.storyBacklog.id]) {
              datetaskSum[task.storyBacklog.id].tasks.push({ id: task.id, hour: task.hour })
            } else {
              datetaskSum[task.storyBacklog.id] = task.storyBacklog
              datetaskSum[task.storyBacklog.id].tasks = [{ id: task.id, hour: task.hour }]
               }

            datetaskSum[task.storyBacklog.id].tasks.sort(function (a, b) {
              return a.hour.localeCompare(b.hour)
            })

            return datetaskSum;
          }, {})


        tasksByDate[date] = Object.values(tasksByDate[date]).map(task => ({
          story: task,
           tasks: task.tasks.map(task => ({
            idtask: task.id,
            hourtask: task.hour.substring(0, 5)
          }))
        }))
      })
      return tasksByDate
    },
    /**
     * Show previous dates for tasks
     */
    showPreviousDates() {
      if (this.index > 0) {
        this.index -= 1;
        this.updateVisibleDates();
      }
    },
    /**
     * Show next date 
     * 
     */
    showNextDates() {
      if (this.index + 4 < this.dates.length) {
        this.index += 1;
        this.updateVisibleDates();
      }
    },
    /**
     * Update tasks 
     */
    updateVisibleDates() {
      this.visibleDates = []
      // get next 3
      const wantedDates = this.dates.slice(this.index, this.index + 3)
      // date format
      wantedDates.forEach(date => {
        let dateString;
        let dateParts = date.split("-")
        let formattedDate = new Date(dateParts[0], dateParts[1] - 1, dateParts[2])
        let today = new Date()
        let tomorrow = new Date(new Date().getTime() + (24 * 60 * 60 * 1000));

        // date format
        if (formattedDate.toDateString() === today.toDateString()) {
          dateString = "Today"
        } else if (formattedDate.toDateString() === tomorrow.toDateString()) {
          dateString = "Tomorrow"
        } else {
          const options = { day: "numeric", month: "2-digit", weekday: "long" };
          dateString = formattedDate.toLocaleDateString("es-ES", options).replace(",", "");
        }

        this.visibleDates.push({ date: date, formattedDate: dateString })
      })
    },
    /**
     * Show hours of the date filtering task by date
     * @param {string} date - Selected date
     */
    showHours(date) {
      const tasksList = {};
      for (const key in this.orderedTasks) {
        if (key === date) {
          tasksList[key] = this.orderedTasks[key];
        }
      }
      this.tasksList = tasksList[date];
    },
    /**
     * change date
     * @param {string} date - selected date
     */
    toggleClass(date) {
      this.selectedDate = date;
    },
    /**
     * change format
     * 
     * @param {string} date - Date format YYYY-MM-DD
     */
    formatDate(date) {
      let dateParts = date.split("-")
      let formattedDate = new Date(dateParts[0], dateParts[1] - 1, dateParts[2])
      const options = { day: "2-digit", month: "2-digit", year: "numeric" };
      return formattedDate.toLocaleDateString("es-ES", options)
    },
   
  },
  async mounted() {
    // load stories task
    
    this.isLoading = true;
    await this.getStoryInfo();
    if (this.taskExists) {
      this.sortDates();
      this.updateVisibleDates();
      this.orderedTasks = this.sortTasksByDate();
      this.selectedDate = this.dates[0];
      this.showHours(this.selectedDate);
      this.isLoading = false;
    } else {
      this.isLoading = false;
    }
  }
}
</script>