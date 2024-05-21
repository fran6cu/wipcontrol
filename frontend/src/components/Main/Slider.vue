<template>
  <section class="backlogToday">
    <h2 class="backlogToday__title">Today's Work</h2>
    <Loader msgLoad="Loading story" v-if="isLoading" />
    <Splide :options="slideOptions" class="backlogToday__splide" v-else-if="!isLoading && this.storys">
      <SplideSlide class="backlogToday__splide__slide" v-for="(story, index) in this.storysOnlys" :key="index"
        @click="redirectToStoryPage(story.storyBacklog.id)">
        <section class="storyInfo">
          <p class="storyInfo__p">Title: {{ story.storyBacklog.name }}</p>
          <p class="storyInfo__p">Description: {{ story.storyBacklog.description }}</p>
          <p class="storyInfo__p">Estimation: {{ story.storyBacklog.estimation }}</p>
          <p class="storyInfo__p">Points: {{ story.storyBacklog.points }}</p>
        </section>
      </SplideSlide>
    </Splide>
    <ErrorComp v-else msgError="No scheduled stories" />
  </section>
</template>

<script>
/**
 
 * @module Component/Slider
 * 
 * @property {Object} components - Components
 * @property {Object} components.Loader - Component loader
 * @property {Object} components.Splide - Component slider 
 * @property {Object} components.ErrorComp - Component error
 * @property {Object} data - Component data
 * @property {Array} data.storys - Array with today story
 * @property {boolean} data.isLoading - Bool is loading
 * @property {Object} data.slideOptions - slider options
 * @property {string} data.slideOptions.type - slider type
 * @property {boolean} data.slideOptions.arrows - show arrows
 * @property {boolean} data.slideOptions.cover - show all
 * @property {boolean} data.slideOptions.autoplay - slider autoplay
 * @property {number} data.slideOptions.interval - slider interval
 * @property {Array} data.storysOnlys - today task only
 * 
 */

import { Splide } from '@splidejs/vue-splide';
import Loader from './Loader.vue';
import ErrorComp from './Error.vue';

export default {
  name: "Slider",
  components: { Splide, Loader, ErrorComp },
  data() {
    return {
      storys: [],
      isLoading: false,
      slideOptions: {
        type: 'loop',
        arrows: false,
        cover: true,
        autoplay: true,
        interval: 3000
      },
      storysOnlys: []
    };
  },
  methods: {
    /**
     * get stories for the backlog
     * @returns {Promise} - Promise with the stories
     */
    async getTodayTasks() {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/scheds/today`)
        .then(response => response.json())
        .then(data => {
          this.storys = data;
          if (this.storys.code === 404) {
            this.storys = false;
            this.isLoading = false;
            return;
          }
          const storysArray = this.storys.map(proxy => proxy)
          this.storysOnlys = storysArray.filter((story, index, array) => {
            return array.findIndex(obj => obj.storyBacklog.id === story.storyBacklog.id) === index;
          });
          this.isLoading = false;
        });
    },
    /**
     * Redirect to story
     */
    redirectToStoryPage(storyId) {
      this.$router.push(`story/${storyId}`)
    }
  },
  // get stories for today
  mounted() {
    this.isLoading = true;
    this.getTodayTasks();
  }
}
</script>
