<template>
 
  <section class="containerInfo">
    
    <section class="containerInfo__details">
      <h1 class="containerInfo__details--title">{{ taskName }}</h1>
      <p class="containerInfo__details--data"> Description: {{ taskDescription }}</p>
      <p class="containerInfo__details--data"> Status: {{ taskStatus }}</p>
      
    </section>
  </section>
  <section class="adminButtons">
    <button class="deleteOption" @click="showTaskForm" v-if="user && user.rol === 'ADMIN'">
      <img src="../../assets/icons/trash.svg" alt="Delete">
      Delete
    </button>
    <button class="editOption" @click="showEditModal" v-if="user && user.rol === 'ADMIN'">
      <img src="../../assets/icons/edit.svg" alt="Edit">
      Edit
    </button>
    <button class="otherOption"  @click="goBack"><img src="../../assets/icons/home.svg" alt="Back">Go Back</button>
    
  </section>
  <ModalConfirmation v-if="showModal" @close="showModal = false" @delete="deleteTask"
    :msg="'Are you sure?'"></ModalConfirmation>
  <ModalNewTask v-if="showEditTask" @close="showEditTask = false" :action="'edit'" :toEdit="taskData">
  </ModalNewTask>
</template>



<script>
import { getLoggedUser } from '../../store/user';
import ModalConfirmation from '../Overlays/ModalConfirmation.vue';
import ModalNewTask from '../Overlays/ModalNewTask.vue';

/**
 
 * @module Component/StoryDetails/storyOptions
 * 
 * @property {Object} props - Properties
 * @property {string} props.storyName - story name
 * @property {string} props.storyAssigned - story assigned 
 * @property {string} props.storyDuration - story duration  
 * @property {string} props.storyStatus - story status
 * @property {string} props.storySprint - story sprint
 * @property {Object} props.storyData - story data 
 * @property {Object} components - Components 
 * @property {Object} components.ModalConfirmacion - Component confirmation 
 * @property {Object} components.ModalNewStory - Component form edition
 * @property {Object} data - component data
 * @property {Object} data.user - User logged
 * @property {boolean} data.showModal - show modal confirmation 
 * @property {boolean} data.showEditStory - show modal edit story
 */
export default {
  name: "taskOptions",
  props: [
    "taskStatus",
    "taskName",
    "taskAssigned",
    "taskData",
    "taskDescription"
  ],
  components: {
    ModalConfirmation,
    ModalNewTask
  },
  data() {
    return {
      user: getLoggedUser(),
      showModal: false,
      showEditTask: false
    }
  },
  methods: {

    goBack() {
       this.$router.go(-1);
    },
    /**
     * show modal delete task
     */
    showTaskForm() {
      this.showModal = true;
    },
    /**
     * show modal edit task
     */
    showEditModal() {
      this.showEditTask = true;
    },
    /**
     * delete task
     */
    deleteTask() {
      const apiUrl = import.meta.env.VITE_API_URL;
      const taskId = this.$route.params.id;

      const options = {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      }


      fetch(`${apiUrl}/delTask/${taskId}`, options)
        .then(response => response.json())
        .then(data => {
          if (data.id) {
            this.$router.push('/');
          }
        })
        .catch(error => console.log(error));
    }
  }
}
</script>