<template>
  
  <section>
    <Loader v-if="isLoading"></Loader>
    <section class="comments" v-else-if="!isLoading && comments.length > 0">
      <button class="comments__add" @click="showForm" v-if="user">
        <img src="../../assets/icons/add.svg" alt="Add">
        Add a comment
      </button>
      <section class="comments__container">
        <article class="comments__container--item" v-for="comment in comments">
          <section class="user">
            <img class="user__img" alt="Avatar" v-if="comment.user.avatar" :src="comment.user.avatar">
            <img class="user__img" aria="Default" v-else src="../../assets/images/default.png"
              alt="User">
            <button class="user__remove" @click="showConfirmation(comment.id)"
              v-if="user !== null && (comment.user.id === user.id || user.rol === 'ADMIN')">
              <img class="user__remove--icon" src="../../assets/icons/trash.svg" alt="Delete">
              Delete
            </button>
          </section>
          <section class="comment">
            <p class="comment__info">@{{ comment.user.username }} &nbsp;&nbsp;·&nbsp;&nbsp;
              {{ dataFormat(comment.date) }}</p>
            <p class="comment__text">{{ comment.text }}</p> 
          </section>
        </article>
      </section>
    </section>
    <section v-else-if="!isLoading && comments.length === 0" class="noComment">
    
      <button class="noComment__button" @click="showForm" v-if="user && !user.msg">
        <img src="../../assets/icons/add.svg" alt="Add">
        Add a comment
      </button>
    </section>
    <ModalConfirmation v-if="showModal" @close="hideConfirmation" @delete="deleteComment"
      :msg="'Are you sure?'"></ModalConfirmation>
    <FormModal v-if="showAddComment" @close="hideForm" :formType="'addComment'">
    </FormModal>
  </section>
</template>

<script>
import Loader from '../Main/Loader.vue'
import ModalConfirmation from '../Overlays/ModalConfirmation.vue'
import FormModal from '../Overlays/FormModal.vue'
import { getLoggedUser } from '../../store/user'

/**
 
 * @module Component/StoryDetails/CommentStory
 * 
 * @property {Object} components - Component
 * @property {Object} components.Loader - Component Loader
 * @property {Object} components.ModalConfirmacion - Component ModalConfirmacion
 * @property {Object} components.FormModal - Component FormModal
 * @property {boolean} isLoading - check if is load
 * @property {Array} comments - Comments list 
 * @property {boolean} showModal - confirmation modal
 * @property {number} idCommentToDel - Id comment to del
 * @property {boolean} showAddComment - show id comment
 * @property {Object} user - logged user
 */
export default {
  name: "commentStory",
  components: {
    Loader,
    ModalConfirmation,
    FormModal
  },
  data() {
    return {
      isLoading: true,
      comments: [],
      showModal: false,
      idCommentToDel: null,
      showAddComment: false,
      user: getLoggedUser()
    }
  },
  methods: {
    /**
     * get comments
     */
    async getComments() {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/comments/story/${this.$route.params.id}/page/0`)
        .then(response => response.json())
        .then(data => {
          this.comments = data.comments;
          this.isLoading = false;
        });
    },
    /**
     * Format date YYYY-MM-DD HH:MM:SS to DD/MM/YYYY
     * @returns {string} - Date format
     */
    dataFormat(datef) {
      const date = new Date(datef);
      const day = date.getDate();
      const month = date.getMonth() + 1;
      const year = date.getFullYear();

      const formattedDay = day < 10 ? `0${day}` : day;
      const formattedMonth = month < 10 ? `0${month}` : month;

      return `${formattedDay}/${formattedMonth}/${year}`;
    },
    /**
     * Comment delete
     */
    deleteComment() {
      const apiUrl = import.meta.env.VITE_API_URL;
      const options = {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      }
      fetch(`${apiUrl}/comments/${this.idCommentToDel}`, options)
        .then(response => response.json())
        .then(data => {
          if (data.id) {
            this.getComments();
            this.hideConfirmation();
          }
        })
    },
    /**
     * confirmation delete
     * @param {number} idComment - Id comment
     */
    showConfirmation(idComment) {
      this.idCommentToDel = idComment;
      this.showModal = true
    },
    /**
     * hide confirmation
     */
    hideConfirmation() {
      this.idCommentToDel = null;
      this.showModal = false
    },
    /**
     * show form add comment
     */
    showForm() {
      this.showAddComment = true;
    },
    /**
     * hide form add comment
     */
    hideForm() {
      this.showAddComment = false;
    }
  },
  mounted() {
    this.isLoading = true;
    this.getComments();
  }
}
</script>