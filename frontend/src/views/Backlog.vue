<template>
  <section class="wipContainer">
    <h2 class="wipContainer__title">Backlog</h2>
    <button class="comments__add" @click="showStoryForm" v-if="user && user.rol === 'ADMIN'">
      <img src="../assets/icons/add.svg" alt="Add">
      New User Story
    </button>
    <ul class="wipContainer__list" v-if="storiesList.stories">
      <li class="wipContainer__list__item" v-for="story in storiesList.stories">
        <div class="wipContainer__list__item__container" @click="seeStoryDetails(story.id)">
          <p class="wipContainer__list__item__container--img">  {{ story.name }}  </p>
          <p class="wipContainer__list__item__container--title"> {{ story.name }} </p>
        </div>

      </li>
    </ul>
    <v-pagination v-if="storiesList.stories" :length="storiesList.totalPages" :total-visible="8" v-model="currentPage"
      prev-icon="ai-triangle-left-fill" next-icon="ai-triangle-right-fill"></v-pagination>
    <ErrorComp v-else messageError="Empty backlog"></ErrorComp>
  </section>
  <ModalNewStory v-if="showAddStory" @close="hideForm" :action="add"></ModalNewStory>
</template>

<script>
import { getLoggedUser } from '../store/user';
import ModalNewStory from '../components/Overlays/ModalNewStory.vue';
import ErrorComp from '../components/Main/Error.vue';

/**
 
 * @module View/Backlog
 * 
 * @property {Object} data 
 * @property {Array} data.storiesList 
 * @property {Number} data.currentPage
 * @property {Object} data.user 
 * @property {Boolean} data.showAddStory 
 * @property {Object} components 
 * @property {Object} components.ModalNewStory
 * @property {Object} components.ErrorComp
 */
export default {
  name: "Backlog",
  data() {
    return {
      storiesList: [],
      currentPage: 1,
      user: getLoggedUser(),
      showAddStory: false,
    }
  },
  components: {
    ModalNewStory,
    ErrorComp
  },
  methods: {
    /**
     * @param {Number} page 
     * @returns {Promise} 
     */
    async getStoriesByPage(page) {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/stories/page?numberPage=${page}`)
        .then(response => response.json())
        .then(data => {
          this.storiesList = data;
        });
    },
    /**
     * @param {Number} storyId 
     */
    seeStoryDetails(storyId) {
      this.$router.push(`/story/${storyId}`);
    },
    /**
     * show form to add story
     */
    showStoryForm() {
      this.showAddStory = true;
    },
    /**
     * hide form to add story
     */
    hideForm() {
      this.showAddStory = false;
    }
  },
 
  mounted() {
    this.currentPage = parseInt(this.$route.query.page) || 1;
    this.getStoriesByPage(this.currentPage - 1);
  },

  watch: {
    currentPage: function (val) {
      this.$router.push('/backlog?page=' + val);
    },
    $route() {
      this.currentPage = parseInt(this.$route.query.page) || 1;
      this.getStoriesByPage(this.currentPage - 1);
    }
  }
}
</script>