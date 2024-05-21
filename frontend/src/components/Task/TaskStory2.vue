<template>
  <section class="wipContainer">
    <button class="comments__add" @click="showTaskForm" v-if="user && user.rol === 'ADMIN'">
      <img src="../assets/icons/add.svg" alt="Add">
      Add a Task
    </button>
    <ul class="wipContainer__list" v-if="tasksList.tasks">
      <li class="wipContainer__list__item" v-for="story in tasksList.tasks">
        <div class="wipContainer__list__item__container" @click="seeTaskDetails(story.id)">
          <p class="wipContainer__list__item__container--img">  {{ task.name }}  </p>
          <p class="wipContainer__list__item__container--title"> {{ task.name }} </p>
        </div>
      </li>
    </ul>
    <v-pagination v-if="tasksList.tasks" :length="tasksList.totalPages" :total-visible="8" v-model="currentPage"
      prev-icon="ai-triangle-left-fill" next-icon="ai-triangle-right-fill"></v-pagination>
    <ErrorComp v-else messageError="There are no tasks"></ErrorComp>
  </section>
  <ModalNewTask v-if="showAddTask" @close="hideForm" :action="add"></ModalNewTask>
</template>

<script>
import { getLoggedUser } from '../../store/user';
import ModalNewTask from './components/Overlays/ModalNewTask.vue';
import ErrorComp from './components/Main/Error.vue';

/**
 
 * @module View/Backlog
 * 
 * @property {Object} data 
 * @property {Array} data.tasksList 
 * @property {Number} data.currentPage
 * @property {Object} data.user 
 * @property {Boolean} data.showAddTask 
 * @property {Object} components 
 * @property {Object} components.ModalNewTask
 * @property {Object} components.ErrorComp
 */
export default {
  name: "Task",
  data() {
    return {
      tasksList: [],
      currentPage: 1,
      user: getLoggedUser(),
      showAddTask: false,
    }
  },
  components: {
    ModalNewTask,
    ErrorComp
  },
  methods: {
    /**
     * @param {Number} page 
     * @returns {Promise} 
     */
    async getTasksByPage(page) {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/tasks/page?numberPage=${page}`)
        .then(response => response.json())
        .then(data => {
          this.tasksList = data;
        });
    },
    /**
     * @param {Number} taskId 
     */
    seeTaskDetails(taskId) {
      this.$router.push(`/task/${taskId}`);
    },
    /**
     * show form to add task
     */
    showTaskForm() {
      this.showAddTask = true;
    },
    /**
     * hide form to add task
     */
    hideForm() {
      this.showAddTask = false;
    }
  },
 
  mounted() {
    this.currentPage = parseInt(this.$route.query.page) || 1;
    this.getTasksByPage(this.currentPage - 1);
  },

  watch: {
    currentPage: function (val) {
      this.$router.push('/tasks?page=' + val);
    },
    $route() {
      this.currentPage = parseInt(this.$route.query.page) || 1;
      this.getTasksByPage(this.currentPage - 1);
    }
  }
}
</script>