<template>
  <Transition name="formBackground">
    <article class="formBG" v-if="isNeeded" @click.self="$emit('close-userForm')">
      <Transition name="formModal">
        <article class="formContainer">
          <section class="formContainer__selectors">
            <button type="button" @click="showForm = 'login'" :class="{ btnActivated: showForm === 'login' }">Sign in</button>
            <button type="button" @click="showForm = 'register'"
              :class="{ btnActivated: showForm === 'register' }">Sign Up</button>
          </section>
          <template v-if="showForm === 'login'">
            <section class="formContainer__body">
              <p class="formContainer__body--text">Join the team!!</p>
              <form class="formContainer__body__form" v-on:submit.prevent="checkLogin(userLog, passLog)">
                <label for="username">Email</label>
                <input v-model="userLog" id="username" type="text" placeholder="Write your email">
                <label for="password">Password</label>
                <input v-model="passLog" id="password" type="password" placeholder="Write your password">
                <Transition name="errorMessage">
                  <section v-show="messageError !== ''" class="formContainer__body__form--error">
                    <ul>
                      <li>{{ this.messageError }}</li>
                    </ul>
                  </section>
                </Transition>
                <button>Sign in</button>
              </form>
            </section>
          </template>
          <template v-else-if="showForm === 'register'">
            <section class="formContainer__body">
              <p class="formContainer__body--text">Join the team!!  </p>
              <form class="formContainer__body__form"
                v-on:submit.prevent="createNewUser(emailReg, usernameReg, passReg, repPassReg)">
                <label for="email">Email</label>
                <input v-model="emailReg" v-on:blur="validateEmail" :class="{ 'input_error': !regexEmailOk }" id="email"
                  type="email" placeholder="Write your email">
                <label for="username">User</label>
                <input v-model="usernameReg" v-on:blur="validateUser" :class="{ 'input_error': !regexUserOk }" id="username"
                  type="text" placeholder="Write your username">
                <label for="password">Password</label>
                <input v-model="passReg" v-on:blur="validatePass" :class="{ 'input_error': !regexPassOk }" id="password"
                  type="password" placeholder="Write your password">
                <label for="repPassword">Repeat password</label>
                <input v-model="repPassReg" v-on:blur="validateRepeatPass" :class="{ 'input_error': !repeatPassOk }"
                  id="repPassword" type="password" placeholder="Repeat password">
                <Transition name="errorMessage">
                  <section
                    v-show="!regexEmailOk || !regexPassOk || !regexUserOk || !repeatPassOk || !emailExistsOk || !usernameExistsOk || messageError !== ''"
                    class="formContainer__body__form--error">
                    <ul>
                      <li v-if="!regexEmailOk">Email error</li>
                      <li v-if="!regexUserOk">User error [5 to 20 characters, only letters and numbers]</li>
                      <li v-if="!regexPassOk">Password error [8 to 32 characters, uppercase, lowercase, special characters and numbers]</li>
                      <li v-if="!repeatPassOk">Password mismatch</li>
                      <li v-if="!emailExistsOk">Email already in use</li>
                      <li v-if="!usernameExistsOk">Username already in use </li>
                      <li v-if="messageError !== ''">{{ messageError }}</li>
                    </ul>
                  </section>
                </Transition>
                <button>Register</button>
              </form>
            </section>
          </template>
        </article>
      </Transition>
    </article>
  </Transition>
</template>
  
<script>
import {
  validateEmailRegex,
  validatePasswordRegex,
  validateSamePassRegex,
  validateUserRegex
} from "../../plugins/formValidations.js";
import { loginUser, registerNewUser } from "../../store/user";

/**
 
 * @property {Object} props - Component properties
 * @property {Boolean} props.isNeeded - component has to show
 * @property {Object} emits - Events from component
 * @property {Function} emits.close-userForm - Event close form
 * @property {Object} data - component data
 * @property {String} data.showForm - show form
 * @property {String} data.userLog - Email login
 * @property {String} data.passLog - Password login
 * @property {String} data.messageError - error message
 * @property {String} data.emailReg - Email to register
 * @property {String} data.usernameReg - username to register
 * @property {String} data.passReg - password to register
 * @property {String} data.repPassReg - Repeat password
 * @property {Boolean} data.regexEmailOk - email ok
 * @property {Boolean} data.regexUserOk - user ok
 * @property {Boolean} data.regexPassOk - password ok
 * @property {Boolean} data.repeatPassOk - repeat is ok
 * @property {Boolean} data.usernameExistsOk - username exist?
 * @property {Boolean} data.emailExistsOk - email exist?
 */

export default {
  name: "LRUser",
  props: {
    isNeeded: {
      type: Boolean,
      default: false
    }
  },
  emits: ['close-userForm'],
  data() {
    return {
      showForm: 'login',
      userLog: '',
      passLog: '',
      messageError: '',
      emailReg: '',
      usernameReg: '',
      passReg: '',
      repPassReg: '',
      regexEmailOk: true,
      regexUserOk: true,
      regexPassOk: true,
      repeatPassOk: true,
      usernameExistsOk: true,
      emailExistsOk: true
    }
  },
  /**
   * watch for changes in form
   */
  watch: {
    showForm() {
      this.messageError = "";
    }
  },
  methods: {
    /**
     * Check user login
     * @param {String} logUser 
     * @param {String} logPass 
     */
    async checkLogin(logUser, logPass) {
      const loginResult = await loginUser(logUser, logPass)

      if (!loginResult) {
        this.messageError = 'Email or password incorrect'
        return;
      } else {
        this.messageError = 'Login OK'
        this.$router.go();
      }
    },
    /**
     * Check username already in use
     * 
     * @param {String} username 
     */
    async usernameIsUsed() {
      const apiUrl = import.meta.env.VITE_API_URL;

      await fetch(`${apiUrl}/user/username?usernameSearched=${this.usernameReg}`)
        .then(response => response.json())
        .then(data => {
          if (data === true) {
            this.usernameExistsOk = false
          } else {
            this.usernameExistsOk = true
          }
        })
    },
    /**
     * check email already in use
     * 
     * @param {String} email 
     */
    async emailIsUsed() {
      const apiUrl = import.meta.env.VITE_API_URL;

      await fetch(`${apiUrl}/user/email?emailSearched=${this.emailReg}`)
        .then(response => response.json())
        .then(data => {
          if (data === true) {
            this.emailExistsOk = false
          } else {
            this.emailExistsOk = true
          }
        })
    },
    /**
     * Check email
     */
    validateEmail() {
      this.regexEmailOk = validateEmailRegex(this.emailReg)
      this.emailIsUsed()
    },
    /**
     * Check user
     */
    validateUser() {
      this.regexUserOk = validateUserRegex(this.usernameReg)
      this.usernameIsUsed()
    },
    /**
     * Check password
     */
    validatePass() {
      this.regexPassOk = validatePasswordRegex(this.passReg)
    },
    /**
     * check password repeated
     */
    validateRepeatPass() {
      this.repeatPassOk = validateSamePassRegex(this.repPassReg, this.passReg)
    },
    /**
     * check form
     * @returns {Boolean} 
     */
    lastCheckReg() {
      if (this.regexEmailOk && this.regexUserOk && this.regexPassOk && this.repeatPassOk && this.usernameExistsOk && this.emailExistsOk)
        if (this.emailReg !== '' && this.usernameReg !== '' && this.passReg !== '' && this.repPassReg !== '')
          return true
      return false
    },
    /**
     * new user calling API
     * @param {String} emailReg 
     * @param {String} userReg 
     * @param {String} passReg 
     */
    async createNewUser(emailReg, userReg, passReg) {
      // check fields
      if (this.lastCheckReg()) {
        this.messageError = ''
        // new user
        const newUser = {
          "username": userReg,
          "email": emailReg,
          "pass": passReg
        }
        const registerResult = await registerNewUser(newUser)

        if (!registerResult) {
          this.messageError = 'Error creating user'
          return;
        } else {
          this.messageError = 'Register Completed'
          this.$router.go();
        }

      } else {
        // Error if empty fields
        this.messageError = 'Form Error'
      }
    }
  }
}
</script>