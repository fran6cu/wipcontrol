<template>
 
  <section class="containerInfo">
    
    <section class="containerInfo__details">
      <h1 class="containerInfo__details--title">{{ sprintName }}</h1>
      
      <p class="containerInfo__details--data"> Status: {{ sprintStatus }}</p>
    </section>
  </section>
  <section class="adminButtons">
    
    <button class="otherOption"  @click="goBack"><img src="../../assets/icons/home.svg" alt="Back">Go Back</button>
  </section>
  <ModalConfirmation v-if="showModal" @close="showModal = false" @delete="deleteSprint"
    :msg="'Are you sure?'"></ModalConfirmation>
  <ModalNewSprint v-if="showEditSprint" @close="showEditSprint = false" :action="'edit'" :toEdit="sprintData">
  </ModalNewSprint>
</template>

<script>
import { getLoggedUser } from '../../store/user';
import ModalConfirmation from '../Overlays/ModalConfirmation.vue';
import ModalNewSprint from '../Overlays/ModalNewSprint.vue';

/**
 
 * @module Component/StoryDetails/sprintOptions
 * 
 * @property {Object} props - Properties
 * @property {string} props.storyName - story name
 * @property {string} props.storyAssigned - story assigned 
 * @property {string} props.storyEstimation - story estimation  
 * @property {string} props.storyStatus - story status
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
  name: "sprintOptions",
  props: [
    "sprintStatus",
    "sprintName",
    
  ],
  components: {
    ModalConfirmation,
    ModalNewSprint
  },
  data() {
    return {
      user: getLoggedUser(),
      showModal: false,
      showEditSprint: false
    }
  },
  methods: {
    goBack() {
       this.$router.go(-1);
    },

    /**
     * show modal delete story
     */
    showSprintForm() {
      this.showModal = true;
    },
    /**
     * show modal edit story
     */
    showEditModal() {
      this.showEditSprint = true;
    },
    /**
     * delete sprint
     */
    deleteSprint() {
      const apiUrl = import.meta.env.VITE_API_URL;
      const sprintId = this.$route.params.id;

      const options = {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      }


      fetch(`${apiUrl}/delSprint/${sprintId}`, options)
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