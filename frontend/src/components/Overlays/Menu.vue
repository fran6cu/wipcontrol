<template>
  <Transition name="menuBackground">
    <article v-if="menuOpen" class="menuContainer" @click.self="$emit('close-menu')">
      <Transition class="menuModal">
        <nav class="menuContainer__overlay">
          <header class="yesUser" v-if="user">
            <img class="yesUser__img" alt="User Avatar" v-if="user.avatar !== null && user.avatar !== ''"
              :src="user.avatar">
            <img class="yesUser__img" alt="User Avatar" v-else src="../../assets/images/default.png">
            <p class="yesUser__username">@{{ user.username }}</p>
            <a class="yesUser__edit" @click="$emit('open-edit-user')">Edit Profile</a>
          </header>
          <header class="noUser" v-else @click="$emit('open-user-form')">
            <p class="text">
              <span class="underline">Sign in</span>
              <br>or<br>
              <span class="underline">Sign Up</span>
            </p>
          </header>
          <ul class="listMenu" v-if="userLogged()">
            <li @click="goToHome()" class="listMenu__item">
              <img class="listMenu__item--img" src="../../assets/icons/home.svg" alt="Home">
              <p class="listMenu__item--text">Home</p>
            </li>
            <li @click="goToSprint()" class="listMenu__item">
              <img class="listMenu__item--img" src="../../assets/icons/backlog.svg" alt="Sprint">
              <p class="listMenu__item--text">Sprint</p>
            </li>
            <li @click="goToBacklog()" class="listMenu__item">
              <img class="listMenu__item--img" src="../../assets/icons/backlog.svg" alt="Backlog">
              <p class="listMenu__item--text">Backlog</p>
            </li>
            <li @click="goToContact()" class="listMenu__item">
              <img class="listMenu__item--img" src="../../assets/icons/mail.svg" alt="Contact">
              <p class="listMenu__item--text">Contact</p>
            </li>
            <li @click="logOut()" class="listMenu__item logout" v-if="userLogged()">
              <img class="listMenu__item--img" src="../../assets/icons/logout.svg" alt="Close sesion">
              <p class="listMenu__item--text">Close sesion</p>
            </li>
          </ul>
        </nav>
      </Transition>
    </article>
  </Transition>
</template>

<script>
import { getLoggedUser, logoutUser } from '../../store/user';

/**

 * 
 * @property {Object} data.user - Logged user
 * @property {Object} props - Component properties
 * @property {Boolean} props.menuOpen - Open menu or not
 * @property {Object} data - Component data
 * @property {Function} emits.open-user-form - Open form de Sign in/Sign up
 * @property {Function} emits.open-edit-user - Open form user edition
 * @property {Function} emits.open-contact - Open contact form
 * @property {Function} emits - Component Events
 * @property {Function} emits.close-menu - Close menu
 
 */
export default {
  name: 'Menu',
  props: {
    menuOpen: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      user: getLoggedUser(),
    }
  },
  emits: ['close-menu', 'open-user-form', 'open-edit-user', 'open-contact', 'reload'],
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
     * Back to home
     */
    goToHome() {
      this.$emit('close-menu');
      this.$router.push('/');
    },
    /**
     * Back to Sprint
     */
     goToSprint() {
      this.$emit('close-menu');
      this.$router.push('/sprint'); 
    },
    /**
     * Back to Backlog
     */
    goToBacklog() {
      this.$emit('close-menu');
      this.$router.push('/backlog'); 
    },
     /**
     * show form contact
     */
     goToContact() {
      this.$emit('close-menu');
      this.$emit('open-contact');
    },
    /**
     * Close sesion
     */
    logOut() {
      logoutUser();
      this.user = null;
      this.$emit('close-menu');
      this.$router.push('/');
      this.$router.go();
      
      
    }
   
  }
}
</script>