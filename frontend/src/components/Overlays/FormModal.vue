<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="modal" appear>
        <article class="modalForm">
          <section v-if="formType === 'addComment'">
            <p class="modalForm__comment">Write your comment</p>
            <textarea class="modalForm__textarea" placeholder="Write your comment here..." v-model.trim="comment"
              @keyup="countCharacters">
                        </textarea>
            <p class="modalForm__count" :class="{ 'limit': count === 500 }">{{ count }}/500</p>
            <Transition name="errorMessage">
              <section v-show="error" class="formContainer__body__form--error">
                <ul>
                  <li>Comment has to be longer. At least 10 characters</li>
                </ul>
              </section>
            </Transition>
            <section class="modalForm__error" v-if="error">
              <p class="modalForm__error__title">Please, review this error:</p>
              <ul class="modalForm__error__list">
                <li class="modalForm__error__list--item">Comment has to be longer. At least 10 characters.</li>
              </ul>
            </section>
            <section class="modalForm__buttons">
              <button class="modalForm__buttons--item" @click="addComment">Confirm</button>
              <button class="modalForm__buttons--item" @click="$emit('close')">Cancel</button>
            </section>
          </section>
          <section v-if="formType === 'addCommentTask'">
            <p class="modalForm__comment">Write your comment</p>
            <textarea class="modalForm__textarea" placeholder="Write your comment here..." v-model.trim="comment"
              @keyup="countCharacters">
                        </textarea>
            <p class="modalForm__count" :class="{ 'limit': count === 500 }">{{ count }}/500</p>
            <Transition name="errorMessage">
              <section v-show="error" class="formContainer__body__form--error">
                <ul>
                  <li>Comment has to be longer. At least 10 characters</li>
                </ul>
              </section>
            </Transition>
            <section class="modalForm__error" v-if="error">
              <p class="modalForm__error__title">Please, review this error:</p>
              <ul class="modalForm__error__list">
                <li class="modalForm__error__list--item">Comment has to be longer. At least 10 characters.</li>
              </ul>
            </section>
            <section class="modalForm__buttons">
              <button class="modalForm__buttons--item" @click="addCommentTask">Confirm</button>
              <button class="modalForm__buttons--item" @click="$emit('close')">Cancel</button>
            </section>
          </section>
          <section v-if="formType === 'contact'">
            <p class="contact__text">Send us a message</p>
            <form ref="contactForm" class="formContainer__body__form" v-on:submit.prevent="sendNewEmail()">
              <label for="email">Name</label>
              <input v-model="nameContact" id="email" type="text" min="3" placeholder="Write your name here">
              <label for="message">Message</label>
              <textarea v-model="messageContact" id="message" type="text" min="10"
                placeholder="Write your message here"></textarea>
              <Transition name="errorMessage">
                <section v-show="error" class="formContainer__body__form--error">
                  <ul>
                    <li>Please fill the entire form</li>
                  </ul>
                </section>
              </Transition>
              <button>Send</button>
            </form>
          </section>
        </article>
      </Transition>
    </section>
  </Transition>
</template>

<script>
import emailjs from '@emailjs/browser';
import { getLoggedUser } from '../../store/user';

/**
 * 
 * @property {Object} data.loggedUser - User logged
 * @property {Function} emits - Component events
 * @property {Object} props - Component properties
 * @property {String} props.formType - Form type to show
 * @property {Object} data - Component data
 * @property {String} data.nameContact - Email User name
 * @property {String} data.messageContact - Email message
 * @property {String} data.comment - New Comment
 * @property {Number} data.count - Number of characters
 * @property {Boolean} data.error - Check error
 * @property {String} emits.close - Close modal
 */
export default {
  name: "FormModal",
  props: {
    formType: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      comment: '',
      count: 0,
      error: false,
      nameContact: '',
      messageContact: '',
      loggedUser: getLoggedUser()
    }
  },
  emits: ['close'],
  methods: {
    /**
     * Textarea characters count
     */
    countCharacters() {
      this.count = this.comment.length;

      if (this.count > 500) {
        this.comment = this.comment.substring(0, 500);
      }
    },
    /**
     * Add a comment
     */
    addComment() {
      if (this.count < 10) {
        this.error = true;
        return;
      } else {
        this.error = false;
        const commentPost = {
          id_user: this.loggedUser.id,
          id_story: this.$route.params.id,
          text: this.comment
        }
        
        const options = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(commentPost)
        }
        const apiUrl = import.meta.env.VITE_API_URL;
        fetch(`${apiUrl}/comments`, options)
          .then(response => response.json())
          .then(data => {
            this.$router.go();
          })
      }
    },

    addCommentTask() {
      if (this.count < 10) {
        this.error = true;
        return;
      } else {
        this.error = false;
        const commentPost = {
          id_user: this.loggedUser.id,
          id_story: this.$route.params.id,
          text: this.comment
        }
        const options = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(commentPost)
        }
        const apiUrl = import.meta.env.VITE_API_URL;
        fetch(`${apiUrl}/taskComments`, options)
          .then(response => response.json())
          .then(data => {
            this.$router.go();
          })
      }
    },
    /**
     * Send email to admin with name and message
     * https://www.npmjs.com/package/@emailjs/browser
     * registry user : ffuentesp@uoc.edu
     */
     sendNewEmail() {
       if (this.nameContact === '' || this.messageContact === '') {
         this.error = true;
         return;
       }
       this.error = false;
       const templateParams = {
         name: this.nameContact,
         message: this.messageContact
       }
       emailjs.send('service_wipcontrol', 'template_wipcontrol', templateParams, 'Nm3I3YY6GBCKw1C7H')
         .then((response) => {
           this.$emit('close');
         }
         );
     }
  }
}
</script>