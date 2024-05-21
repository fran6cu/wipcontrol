<template>
  <section class="home" @reload="reload()">
    <article class="home__up">
      <Slider v-if="userLogged()" />
      <NextStories v-if="userLogged()"/>
    </article>

    <article class="home__center" v-if="!userLogged()">
      <h2 class="home__center__title">Lorem ipsum</h2>
      
    </article>

    <article class="home__down" v-if="userLogged()">
      <h2 class="home__down__title">Schedule</h2>
      <button class="comments__add" @click="showAddTask = true" v-if="user && user.rol === 'ADMIN'">
        <img src="../assets/icons/add.svg" alt="Add">
        Add Schedule
      </button>
      <Backlog />
    </article>
  </section>
  <AddSched v-if="showAddTask" @close="showAddTask = false"></AddSched>

</template>

<script>
/**
 
 * @module View/Index
 * 
 * @property {Object} components - Components
 * @property {Object} components.Header - Component header
 * @property {Object} components.Slider - Component slider
 * @property {Object} components.NextStories - Component next stories
 * @property {Object} components.Backlog - Componenent backlg
 * @property {Object} components.AddSession - Component form add task
 * @property {Object} data - component data
 * @property {boolean} data.showAddTask - show form add task
 * @property {Object} data.user - User logged
 */
import Slider from '../components/Main/Slider.vue';
import NextStories from '../components/Main/NextStories.vue';
import Header from '../components/Main/Header.vue'
import Backlog from '../components/Backlog/Backlog.vue';
import AddSched from '../components/Overlays/AddSched.vue';
import { getLoggedUser } from '../store/user';


export default {
  name: 'Index',
  components: {
    Header,
    Slider,
    NextStories,
    Backlog,
    AddSched
  },
  data() {
    return {
      showAddTask: false,
      user: getLoggedUser()
    }
  },
  methods: {
    /**
     * Check if logged user
     * @returns {Boolean} - True if yes
     */
    userLogged() {
      const userIsLogged = sessionStorage.getItem('loggedUser');
      if (userIsLogged) {
        return true;
      } else {
        return false;
      }
    },

     /**
     * reload form
     */
     reload() {
      this.forceUpdate();
      
    }
  }

}
</script>