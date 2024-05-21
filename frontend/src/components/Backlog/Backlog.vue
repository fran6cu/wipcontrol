<template>
  <Loader v-if="isLoading" />
  <section v-else-if="!isLoading && tasksList !== undefined && tasksList.length > 0" class="sliderAfterToday">
    <button class="sliderAfterToday__sliderButton" @click="showPreviousDates">
      <img src="../../assets/icons/left.svg" alt="Back">
    </button>
    <section class="sliderAfterToday__item" v-for="date in visibleDates" @click="toggleClass(date.date)"
      v-bind:class="{ 'selected': date.date === selectedDate }">
      {{ date.formattedDate }}
    </section>
    <button class="sliderAfterToday__sliderButton" @click="showNextDates">
      <img src="../../assets/icons/right.svg" alt="Next">
    </button>
  </section>
  <section v-if="!isLoading && tasksList !== undefined && tasksList.length > 0" class="infoStorys">
    <section class="infoStorys__item" v-for="storyTask in tasksList">
      <section class="infoStorys__item__data">
        
        <p class="infoStorys__item__data--title" @click="seeStoryDetails(storyTask.story.id)">
          {{ storyTask.story.name }}
        </p>
        
      </section>
      
    </section>
  </section>
  <ErrorComp v-if="!isLoading && tasksList === undefined || tasksList.length === 0"
    msgError="No story scheduled" />
  
</template>

<script>
/**
 
 * @module Component/Backlog/BacklogGeneral
 * 
 * @property {Object} components - Component backlog
 * @property {Object} components.Loader - Component loader
 * @property {Object} components.ErrorComp - Component Error 
 * @property {Object} data - Component data
 * @property {Array} data.afterToday - Scheds Array from today
 * @property {Array} data.dates - Scheds Array 
 * @property {Array} data.visibleDates - scheds array slider
 * @property {number} data.index - index
 * @property {string} data.selectedDate - Selected date 
 * @property {Array} data.tasksList - Array selected date
 * @property {boolean} data.orderedTasks - ordered scheds 
 * @property {boolean} data.isLoading - flag is loading
 */
import Loader from '../Main/Loader.vue'
import ErrorComp from '../Main/Error.vue'

export default {
  name: "Backlog",
  
  components: { Loader, ErrorComp},
  data() {
    return {
      afterToday: [],
      dates: [],
      visibleDates: [],
      index: 0,
      selectedDate: "",
      tasksList: [],
      orderedTasks: {},
      isLoading: false,
      
    }
  },
  /** 
   * Detects when date is changed 
   */
  watch: {
    selectedDate: function (val) {
      this.showHours(val)
    }
  },
  methods: {
    /**
     * call api to get task from today
     * @returns {Promise} - task aftertoday promise
     */
    async getAfterTodayTasks() {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/scheds/afterToday`)
        .then(response => response.json())
        .then(data => {
          this.afterToday = data
          this.isLoading = false
        })
    },
    /**
     * task dates ordered from today 
     */
    sortDates() {
      let uniqueDates = new Set();

      // list with task dates 
      if (this.afterToday && this.afterToday.length > 0) {
        this.afterToday.forEach(task => {
          uniqueDates.add(task["date"])
        })
      }

      // Array with dates 
      this.dates = Array.from(uniqueDates)

      // dates ordered
      this.dates.sort(function (a, b) {
        return new Date(a) - new Date(b)
      })
    },
    /**
     * show previous dates 
     */
    showPreviousDates() {
      if (this.index > 0) {
        this.index -= 1;
        this.updateVisibleDates();
      }
    },
    /**
     * Show next dates 
     */
    showNextDates() {
      if (this.index + 4 < this.dates.length) {
        this.index += 1;
        this.updateVisibleDates();
      }
    },
    /**
     * update dates 
     */
    updateVisibleDates() {
      this.visibleDates = []
      // Get 3 dates to show 
      const wantedDates = this.dates.slice(this.index, this.index + 3)
      // format dates
      wantedDates.forEach(date => {
        let dateString;
        let dateParts = date.split("-")
        let formattedDate = new Date(dateParts[0], dateParts[1] - 1, dateParts[2])
        let today = new Date()
        let tomorrow = new Date(new Date().getTime() + (24 * 60 * 60 * 1000));

        // format date
        if (formattedDate.toDateString() === today.toDateString()) {
          dateString = "Today"
        } else if (formattedDate.toDateString() === tomorrow.toDateString()) {
          dateString = "Tomorrow"
        } else {
          const options = { day: "numeric", month: "2-digit", weekday: "long" };
          dateString = formattedDate.toLocaleDateString("en-US", options).replace(",", "");
        }

        this.visibleDates.push({ date: date, formattedDate: dateString })
      })
    },
    /**
     * show hours from dates filtering by date 
     * @param {string} date - selected date
     */
    showHours(date) {
      this.tasksList = Object.fromEntries(
        Object.entries(this.orderedTasks)
          .filter(([key, value]) => key === date)
      )

      this.tasksList = this.tasksList[date]
    },
    /**
     * Change date
     * @param {string} date - selected date
     */
    toggleClass(date) {
      this.selectedDate = date;
    },
    /**
     * redirects to story
     * @param {number} storyID - story id
     */
    seeStoryDetails(storyID) {
      this.$router.push(`story/${storyID}`)
    },
    /**
     * new object with kay date ordered and the stories for this day
     * @returns {Object} - task ordered by date
     */
    sortTasksByDate() {
      let tasksByDate = {}
      this.dates.forEach(date => {
        tasksByDate[date] = {};

        // sort sched by date
        tasksByDate[date] = this.afterToday
          .filter(task => task["date"] === date)
          .reduce((dateTask, task) => {
            if (dateTask[task.storyBacklog.id]) {
              dateTask[task.storyBacklog.id].tasks.push({ id: task.id, hour: task.hour })
            } else {
              dateTask[task.storyBacklog.id] = task.storyBacklog
              dateTask[task.storyBacklog.id].tasks = [{ id: task.id, hour: task.hour }]
              
            }

            dateTask[task.storyBacklog.id].tasks.sort(function (a, b) {
              return a.hour.localeCompare(b.hour)
            })

            return dateTask;
          }, {})

        // dates array with story and related tasks
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
    
  },
  
  async mounted() {
    this.isLoading = true
    await this.getAfterTodayTasks()
    this.sortDates()
    this.updateVisibleDates()
    this.orderedTasks = this.sortTasksByDate()
    this.selectedDate = this.dates[0]
  }
}
</script>
