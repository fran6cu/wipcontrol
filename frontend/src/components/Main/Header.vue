<template>
  <header class="header">
    <section class="header__container">
      <img src="../../assets/images/wiplogo.PNG" alt="WipControl Logo" class="header__container__logo"
        @click="redirectToIndex()">
        <a class="header__container--label" href="/sprint" v-if="user" v-for="sprint in sprints" :key="sprint.id" :value="sprint.name">Current Sprint: {{ sprint.name }}</a>
        <a class="header__container--label" v-if="user" >User: {{ user.username }}</a>
      <img src="../../assets/icons/menu.svg" alt="menu" class="header__container__menu" @click="menuOpen = true">
      
    </section>
  </header>
  <Menu :menuOpen="menuOpen" @close-menu="menuOpen = false" @open-user-form="openUserForm()"
    @open-contact="contactOpen = true" @open-edit-user="openEditForm()"></Menu>
  <LRUser :is-needed="formUserOpen" @close-user-form="formUserOpen = false">
  </LRUser>
  <FormModal v-if="contactOpen" :form-type="'contact'" @close="contactOpen = false">
  </FormModal>
  <EditUser v-if="editUserOpen" @close-profile="editUserOpen = false"></EditUser>
</template>

<script>
/**
 
 * @module Component/Header
 * 
 * @property {Object} components - Components
 * @property {Object} components.Menu - Component menu
 * @property {Object} components.LRUser - Component form login and registration
 * @property {Object} components.FormModal - Component form contact
 * @property {Object} components.EditUser - Component form user
 * @property {Object} data - Variables 
 * @property {boolean} data.menuOpen - Variables menu
 * @property {boolean} data.formUserOpen - Form user open
 * @property {boolean} data.contactOpen - Form contact open
 * @property {boolean} data.editUserOpen - Form edit user open
 */
import Menu from '../Overlays/Menu.vue';
import LRUser from '../Overlays/LRUser.vue';
import FormModal from '../Overlays/FormModal.vue';
import EditUser from '../Overlays/EditUser.vue';
import { getLoggedUser } from '../../store/user';

export default {
  name: 'Header',
  components: {
    Menu,
    LRUser,
    FormModal,
    EditUser
  },
  data() {
    return {
      menuOpen: false,
      formUserOpen: false,
      contactOpen: false,
      editUserOpen: false,
      user: getLoggedUser(),
      sprints: [],
    }
  },
  mounted() {
    // Fetch data from the server
     this.fetchSprints();
 
  },
  methods: {

    async fetchSprints() {
      try {
        
        const apiUrl = import.meta.env.VITE_API_URL;
        const response = await fetch(`${apiUrl}/sprints/names`);
        const data = await response.json();
        this.sprints = data;
      } catch (error) {
        console.error('Error fetching sprint:', error);
      }
    },
    /**
     * go to home
     */
    redirectToIndex() {
      this.$router.push('/')
    },
    /**
     * open LR form
     */
    openUserForm() {
      this.menuOpen = false;
      this.formUserOpen = true;
    },
    /**
     * open user form
     */
    openEditForm() {
      this.menuOpen = false;
      this.editUserOpen = true;
    }
  }
}
</script>