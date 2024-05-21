<template>
  <Loader class="loader__details" v-if="isLoading" :msgLoad="'Loading sprint info'" />
  <section class="mainDetails" v-else @reload="reload()">
    
      <taskOptions  :taskName="taskInfo.name"  :taskStatus="taskInfo.status" :taskDescription="taskInfo.description" :taskData="taskInfo" />
    <section class="buttons">
      
      <button class="buttons__item" :class="{ 'active': activeTab === 'comments', 'inactive': activeTab !== 'comments' }"
        @click="changeTab('comments')">
        Comments
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
import commentTask from '../Comment/CommentTask.vue'
import taskOptions from '../Task/TaskOptions.vue'
import taskStory from '../Task/TaskStory.vue'

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
  name: "TaskDetails",
  components: {
    Header,
    taskOptions,
    Loader,
    commentTask,
    taskStory
    
    
  },
  data() {
    return {
      taskInfo: {},
      isLoading: true,
      infoIsOpen: true,
      commentsIsOpen: false,
      activeTab: 'task',
   };
  },
  methods: {
    /**
     * task info
     * 
     * @param {Number} sprintId - story id
     * @returns {Object} - story object
     */
    async getTaskDetails(taskId) {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/tasks?idTask=${taskId}`)
        .then(response => response.json())
        .then(data => {
          this.taskInfo = data;
          this.isLoading = false;
        });
    },

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
          return 'infoTask';
        case "comments":
          return 'commentTask';
        case "tasks":
            return 'taskStory';
        
        default:
          return 'commentTask';
      }
    }
  },
  mounted() {
    /**
     * get sprint details
     */
    this.getTaskDetails(this.$route.params.id);
  }
};

</script>