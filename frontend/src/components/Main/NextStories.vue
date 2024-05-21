<template>
  <section class="next">
    <h2 class="next__title"> US for {{ selectedSprint }}</h2>
    <section class="next__container">
      <Loader msgLoad="Loading next stories" v-if="isLoading" />
      <ul class="list" v-else-if="!isLoading && nextStorys.length > 0">
        <li class="list__item" v-for="(story, index) in this.nextStorys.slice(0, 5)">
          <p class="list__item__order">{{ index + 1 }}</p>
          <section class="list__item__info">
            <p class="list__item__info__name" @click="redirectToStoryPage(story.id)">{{ story.name }}</p>
            <p class="list__item__info__vote">{{ story.points }} points</p>
          </section>
        </li>
        <li class="list__item" v-if="this.nextStorys" v-for="emptyElement in 5 - this.nextStorys.length">
        </li>
      </ul>
      <ErrorComp v-else-if="!isLoading && nextStorys.length === 0" msgError="There are no stories" />
    </section>
  </section>
</template>

<script>
/**
 
 * @module Component/NextStories
 * 
 * @property {Object} components - Components
 * @property {Object} components.Loader - Component loader
 * @property {Object} components.ErrorComp - Component error
 * @property {Object} data - Data component
 * @property {Array} data.nextStorys - Next user stories
 * @property {boolean} data.isLoading - loader
 */
import Loader from './Loader.vue';
import ErrorComp from './Error.vue';

export default {
  name: "NextStories",
  components: { Loader, ErrorComp },
  data() {
    return {
      nextStorys: [],
      sprints: [],
      isLoading: false,
      selectedSprint:'',
     
    };
  },
 
  methods: {

    async fetchSprints() {
      try {
        const apiUrl = import.meta.env.VITE_API_URL;
        const response = await fetch(`${apiUrl}/sprints/names`);
        
        const data = await response.json();
        this.sprints = data;
        if (data.length > 0) {
          this.selectedSprint = data[0].name; 
        }
      } catch (error) {
        console.error('Error fetching sprint:', error);
      }
    },
    /**
     * get stories by points
     */
    async getNextStorys() {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/stories/next`)
        .then(response => response.json())
        .then(data => {
          this.nextStorys = data;
      
          if (this.nextStorys !== undefined && this.nextStorys !== null && this.nextStorys.length > 5) {
            this.nextStorys = this.nextStorys.slice(0, 5);
          }
          this.isLoading = false;
                
        });
    },
    /**
     * Redirect to the user story
     * @param {number} storyId - story id
     */
    redirectToStoryPage(storyId) {
      this.$router.push(`story/${storyId}`)
    }
  },

  
 mounted() {
    this.isLoading = true;
    this.getNextStorys();
    this.fetchSprints();
  }
}
</script>