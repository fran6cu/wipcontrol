<template>
  <Loader class="loader__details" v-if="isLoading" :msgLoad="'Loading story info'" />
  <section class="mainDetails" v-else @reload="reload()">
    
      <StoryOptions  :storyName="storyInfo.name" 
      :storyAssigned="storyInfo.assigned" :storyPoints="storyInfo.points" :storyStatus="storyInfo.status" :storySprint="storyInfo.sprint" :storyData="storyInfo" />
    <section class="buttons">
      <button class="buttons__item" :class="{ 'active': activeTab === 'info', 'inactive': activeTab !== 'info' }"
        @click="changeTab('info')">
        Information
      </button>
      <button class="buttons__item" :class="{ 'active': activeTab === 'comments', 'inactive': activeTab !== 'comments' }"
        @click="changeTab('comments')">
        Comments
      </button>
      <button class="buttons__item" :class="{ 'active': activeTab === 'tasks', 'inactive': activeTab !== 'tasks' }"
        @click="changeTab('tasks')">
        Tasks
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
import infoStory from '../Story/InfoStory.vue'
import commentStory from '../Comment/CommentStory.vue'
import StoryOptions from '../Story/StoryOptions.vue'
import taskStory from '../Task/TaskStory.vue'

/**
 
 * @module View/StoryDetails
 * 
 * @property {Object} components - Components
 * @property {Object} components.Header - Component header
 * @property {Object} components.StoryOptions - Component background
 * @property {Object} components.Loader - Component loader
 * @property {Object} components.infoStory - Component information
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
  name: "StoryDetails",
  components: {
    Header,
    StoryOptions,
    Loader,
    infoStory,
    commentStory,
    taskStory
  },
  data() {
    return {
      storyInfo: {},
      storyTasks: {},
      isLoading: true,
      infoIsOpen: true,
      commentsIsOpen: false,
      taskIsOpen: false,
      activeTab: 'info',
      tasks: [],
    };
  },
  methods: {
    /**
     * story info
     * 
     * @param {Number} storyId - story id
     * @returns {Object} - story object
     */
    async getStoryDetails(storyId) {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/stories?idStory=${storyId}`)
        .then(response => response.json())
        .then(data => {
          this.storyInfo = data;
          this.isLoading = false;
        });
    },
    /**
     * Order tasks by date  
     * 
     * @returns {Array} - tasks ordered by date
     */
   
    changeTab(tabName) {
      this.activeTab = tabName;
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
          return 'infoStory';
        case "comments":
          return 'commentStory';
        case "tasks":
            return 'taskStory';
       
        default:
          return 'taskStory';
      }
    }
  },
  mounted() {
    /**
     * get story details
     */
    this.getStoryDetails(this.$route.params.id);
  }
};

</script>