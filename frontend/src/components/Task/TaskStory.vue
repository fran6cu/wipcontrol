<template>
  <div>
  
  <section class="taskContainer">
    <button class="comments__add" @click="showTaskForm" v-if="user && user.rol === 'ADMIN'">
      <img src="../../assets/icons/add.svg" alt="Add">
      Add a Task
    </button>
    
    <section class="wipContainer__list">
      <li class="wipContainer__list__item" v-for="task in tasks">
       <div class="wipContainer__list__item__container" @click="seeTaskDetails(task.id)">
          <p class="wipContainer__list__item__container--img">  {{ task.name }}  </p>
          <p class="wipContainer__list__item__container--title"> {{ task.name }} </p>
         
        </div>
      </li>
    </section>
   
    <v-pagination v-if="tasks.tasks" :length="tasks.totalPages" :total-visible="8" v-model="currentPage"
      prev-icon="ai-triangle-left-fill" next-icon="ai-triangle-right-fill"></v-pagination>
   
  </section>

  <ModalNewTask v-if="showAddTask" @close="hideForm" :action="add"></ModalNewTask>
</div>
</template>

<script>
import { getLoggedUser } from '../../store/user';
import ModalNewTask from '../Overlays/ModalNewTask.vue';

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
  name: "Tasks",
  data() {
    return {
      tasks: [],
      data: [],
      currentPage: 1,
      user: getLoggedUser(),
      showAddTask: false,
      isLoading: true,
    }
  },
  components: {
    ModalNewTask,
    },
  methods: {
    /**
     * @param {Number} page 
     * @returns {Promise} 
     */
    

  async getTasksByPage() {
  const apiUrl = import.meta.env.VITE_API_URL;
  try {
   
    const response = await fetch(`${apiUrl}/tasks/story/${this.$route.params.id}/page/0`);
    if (!response.ok) {
      throw new Error(`Error fetching tasks: ${response.statusText}`);
    }
    const data = await response.json();
    
    this.tasks = data.tasks;
    this.isLoading = false;
  } catch (error) {
    console.error('Error fetching tasks:', error);
    
  }
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
     this.getTasksByPage();
  },

  watch: {
    currentPage: function (val) {
      this.$router.push('/tasks?page=' + val);
    },
    $route() {
      
      this.getTasksByPage();
    }
  }
}
</script>