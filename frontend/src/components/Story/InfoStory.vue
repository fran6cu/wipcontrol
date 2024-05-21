<template>
  <section class="infoContainer">
    <h4 class="infoContainer__title">Description: As a ...</h4>
    <span class="infoContainer__divider"></span>
    <p class="infoContainer__description">
      {{ storyData.description }}
    </p>
    <h4 class="infoContainer__title">Acceptance criteria: Given ...</h4>
    <span class="infoContainer__divider"></span>
    <p class="infoContainer__description">
      {{ storyData.criteria }}
    </p>
   
  </section>
</template>

<script>
import ErrorComp from "../Main/Error.vue";

/**
 
 * @module Component/StoryDetails/InfoStory
 * 
 * @property {Object} components - Components
 * @property {Object} components.ErrorComp - Error Component
 * @property {Object} data - component data
 * @property {Object} data.storyData - story data
 */
export default {
  name: "infoStory",
  components: {
    ErrorComp
  },
  data() {
    return {
      
      storyData: {}
    };
  },
  methods: {
    /**
     * get data calling api
     * 
     * @param {number} storyId - story id
     * @returns {Object} - story data
     */
    async getStoryDetails(storyId) {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/stories?idStory=${storyId}`)
        .then(response => response.json())
        .then(data => {
          this.storyData = data;
          this.isLoading = false;
        });
    },
    
  },
  async mounted() {
    
    await this.getStoryDetails(this.$route.params.id);
  
  }
}
</script>