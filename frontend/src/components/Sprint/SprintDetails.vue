<template>
  <Loader class="loader__details" v-if="isLoading" :msgLoad="'Loading sprint info'" />
  <section class="mainDetails" v-else @reload="reload()">
    
      <SprintOptions  :sprintName="sprintInfo.name"  :sprintStatus="sprintInfo.status" :sprintData="sprintInfo" />
    <section class="buttons">
      <button class="buttons__item" :class="{ 'active': activeTab === 'info', 'inactive': activeTab !== 'info' }"
        @click="changeTab('info')">
        Information
      </button>
   
    </section>
    <Transition name="fade" mode="out-in">
      <component :is="activeTabComponent"></component>
    </Transition>
  </section>
</template>

<script>
import Header from '../Main/Header.vue'
import Loader from '../Main/Loader.vue'
import SprintOptions from './SprintOptions.vue'


/**
 
 * @module View/StoryDetails
 * 
 * @property {Object} components - Components
 * @property {Object} components.Header - Component header
 * @property {Object} components.StoryOptions - Component background
 * @property {Object} components.Loader - Component loader
 * @property {Object} components.commentStory - Component comments
 * @property {Object} components.taskStory - Component task
 * @property {Object} data - component data
 * @property {Object} data.storyInfo - story info
 * @property {Object} data.storyTasks - story story
 * @property {Boolean} data.isLoading - check if loading
 * @property {Boolean} data.infoIsOpen - check if open
 * @property {Boolean} data.commentsIsOpen - check if comment open

 */
export default {
  name: "SprintDetails",
  components: {
    Header,
    SprintOptions,
    Loader,
        
  },
  data() {
    return {
      sprintInfo: {},
      isLoading: true,
      infoIsOpen: true,
      commentsIsOpen: false,
      activeTab: 'info',
      
    };
  },
  methods: {
    /**
     * sprint info
     * 
     * @param {Number} sprintId - sprint id
     * @returns {Object} - sprint object
     */
    async getSprintDetails(sprintId) {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/sprints?idSprint=${sprintId}`)
        .then(response => response.json())
        .then(data => {
          this.sprintInfo = data;
          this.isLoading = false;
        });
    },
   
      /**
     * reload form
     */
     reload() {
      this.forceUpdate();
      
    }
  },
  computed: {
    /**
     * check active tab
     */
    activeTabComponent() {
      switch (this.activeTab) {
        case "info":
          return 'infoSprint';
        
        default:
          return 'infoSprint';
      }
    }
  },
  mounted() {
    /**
     * get sprint details
     */
    this.getSprintDetails(this.$route.params.id);
  }
};

</script>