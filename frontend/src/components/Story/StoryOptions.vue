<template>
 
  <section class="containerInfo">
    <section class="adminButtons">
     
      <button class="deleteOption" @click="showStoryForm" v-if="user && user.rol === 'ADMIN'">
        <img src="../../assets/icons/trash.svg" alt="Delete">
        Delete
      </button>
      <button class="editOption" @click="showEditModal" v-if="user && user.rol === 'ADMIN'">
        <img src="../../assets/icons/edit.svg" alt="Edit">
        Edit
      </button>
      <button class="otherOption"  @click="goToBacklog" v-if="user && user.rol === 'ADMIN'"><img src="../../assets/icons/item.svg" alt="Backlog">Backlog</button>
      <button class="otherOption"  @click="addToSprint" v-if="user && user.rol === 'ADMIN'"><img src="../../assets/icons/item.svg" alt="Sprint">Add to Sprint</button>
      <button class="otherOption"  @click="addPoints" v-if="user && user.rol === 'ADMIN'"><img src="../../assets/icons/item.svg" alt="Points">Add US Points</button>
      <button class="otherOption"  @click="goBack"><img src="../../assets/icons/home.svg" alt="Back">Go Back</button>    
      </section>
    
    <section class="containerInfo__details">
      <h1 class="containerInfo__details--title">{{ storyName }}</h1>
      <p class="containerInfo__details--data">Asigned to: {{ storyAssigned }}</p>
      <p class="containerInfo__details--data"> Estimation: {{ storyDuration }}</p>
      <p class="containerInfo__details--data"> Status: {{ storyStatus }}</p>
      <p class="containerInfo__details--data"> Sprint: {{ storySprint }}</p>
      <p class="containerInfo__details--data"> Points: {{ storyPoints }}</p>
    </section>
  </section>
 
  <ModalConfirmation v-if="showModal" @close="showModal = false" @delete="deleteStory"
    :msg="'Are you sure?'"></ModalConfirmation>
  <ModalNewStory v-if="showEditStory" @close="showEditStory = false" :action="'edit'" :toEdit="storyData"></ModalNewStory>
  <ModalAddSprint v-if="showAddSprint" @close="showAddSprint = false" :action="'edit'" :toEdit="storyData"></ModalAddSprint> 
  <ModalAddPoints v-if="showAddPoints" @close="showAddPoints = false" :action="'edit'" :toEdit="storyData"></ModalAddPoints>

</template>

<script>
import { getLoggedUser } from '../../store/user';
import ModalConfirmation from '../Overlays/ModalConfirmation.vue';
import ModalNewStory from '../Overlays/ModalNewStory.vue';
import ModalAddSprint from '../Overlays/ModalAddSprint.vue';
import ModalAddPoints from '../Overlays/ModalAddPoints.vue';

/**
 
 * @module Component/StoryDetails/storyOptions
 * 
 * @property {Object} props - Properties
 * @property {string} props.storyName - story name
 * @property {string} props.storyAssigned - story assigned 
 * @property {string} props.storyEstimation - story estimation  
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
  name: "storyOptions",
  props: [
    "storyStatus",
    "storySprint",
    "storyName",
    "storyAssigned",
    "storyDuration",
    "storyPoints",
    "storyData"
  ],
  components: {
    ModalConfirmation,
    ModalNewStory,
    ModalAddPoints,
    ModalAddSprint
  },
  data() {
    return {
      user: getLoggedUser(),
      showModal: false,
      showEditStory: false,
      showAddPoints: false,
      showAddSprint: false,
      
    }
  },

  
  methods: {

    goBack() {
       this.$router.go(-1);
    },
   
    addPoints() {
      this.showAddPoints = true;
    },

    addToSprint() {
      this.showAddSprint = true;
    },
   
    goToBacklog() {
      this.$router.push('/backlog');
    },
    /**
     * show modal delete story
     */
    showStoryForm() {
      this.showModal = true;
    },
    /**
     * show modal edit story
     */
    showEditModal() {
      this.showEditStory = true;
    },
    /**
     * delete story
     */
    deleteStory() {
      const apiUrl = import.meta.env.VITE_API_URL;
      const storyId = this.$route.params.id;

      const options = {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      }


      fetch(`${apiUrl}/delStory/${storyId}`, options)
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