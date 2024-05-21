<template>
  <Transition name="formBackground">
    <article ref="Profile" class="formBG" @click.self="$emit('close-profile')" v-intersect>
      <article class="formContainer">
        
        <form @submit.prevent="sendInfoUser" class="formContainer__body" enctype="multipart/form-data">
          <fieldset class="formContainer__body__imageHandler">
            <img class="formContainer__body__imageHandler--image" v-if="!showDefaultAvatar" :src="avatarToShow"
              alt="user image">
            <img class="formContainer__body__imageHandler--image" v-else src="../../assets/images/default.png"
              alt="user image">
            <label for="profilePicture" class="formContainer__body__imageHandler--label">
              Image
            </label>
            <input id="profilePicture" class="formContainer__body__imageHandler--input" type="file" accept="image/*"
              @change="handleFileInput" :multiple="false" ref="imageFile">
          </fieldset>
          <fieldset class="formContainer__body__userDataHandler">
            <label for="email">Email</label>
            <input v-model="emailData" v-on:blur="validateEmail" :class="{ 'input_error': !modEmailOk }" id="email"
              type="email" placeholder="New email...">
            <label for="username">User</label>
            <input v-model="usernameData" v-on:blur="validateUser" :class="{ 'input_error': !modUserOk }" id="username"
              type="text" placeholder="New user...">
            <label for="password">Password</label>
            <input v-model="passData" v-on:blur="validatePass" :class="{ 'input_error': !modPassOk }" id="password"
              type="password" placeholder="New password...">
          </fieldset>
          <Transition name="errorMessage">
            <section v-show="!modEmailOk || !modPassOk || !modUserOk || errorMessage !== null"
              class="formContainer__body__form--error">
              <ul>
                <li v-if="!modEmailOk">Email error</li>
                <li v-if="!modUserOk">User error [5 to 20 characters, only letters and numbers]</li>
                <li v-if="!modPassOk">Password error [8 to 32 characters, uppercase, lowercase, special characters and numbers]</li>
                <li v-if="errorMessage !== null">{{ errorMessage }}</li>
              </ul>
            </section>
          </Transition>
          <button class="formContainer__body__userDataHandler--submit">User Update</button>
        </form>
      </article>
    </article>
  </Transition>
</template>

<script>
import { validateEmailRegex, validatePasswordRegex, validateUserRegex } from "../../plugins/formValidations.js";
import { getLoggedUser } from "../../store/user";

/**
 
 * @module Component/Overlays/EditUser
 * 
 * @property {Object} data - component data
 * @property {String} data.imageUrl - URL profile
 * @property {String} data.errorMessage - Error msg
 * @property {String} data.imageFile - image file
 * @property {String} data.emailData - user email
 * @property {String} data.usernameData - user Username 
 * @property {String} data.passData - user pass
 * @property {Boolean} data.modEmailOk - email validation
 * @property {Boolean} data.modUserOk - user validation
 * @property {Boolean} data.modPassOk - pass validación
 * @property {Object} data.loggedUser - user logged
 * @property {Boolean} data.showDefaultAvatar - default image
 * @property {String} data.avatarToShow - profile image
 * @property {Object} emits - Events component
 * @property {Function} emits.close-profile - component close
 */
export default {
  name: "EditUser",
  data() {
    return {
      imageUrl: "",
      errorMessage: null,
      imageFile: "",
      emailData: "",
      usernameData: "",
      passData: "",
      modEmailOk: true,
      modUserOk: true,
      modPassOk: true,
      loggedUser: getLoggedUser(),
      avatarToShow: null,
      showDefaultAvatar: true,
    }
  },
  emits: ['close-profile'],
  methods: {
    /**
     * Hnadle image profile
     * @param event get event profile
     */
    handleFileInput(event) {
      // check if is an image
      if (event.target.files.length === 1 && event.target.files[0].type.startsWith('image/')) {
        const selectedFile = event.target.files[0];
        this.imageUrl = URL.createObjectURL(selectedFile);
        this.errorMessage = null;
        this.showedAvatar('newUserAvatar', this.imageUrl)
      } else {
        this.errorMessage = "Select an image";
      }
    },
    /**
     * send user data
     */
    async sendInfoUser() {
      if (this.emailData === "" && this.usernameData === "" && this.passData === "" && this.$refs.imageFile.files[0] === undefined) {
        this.errorMessage = "Fill up the fields";
        return;
      }

       const newUserData = {
       
        'email': this.emailData === '' ? '' : this.emailData,
        'username': this.usernameData === '' ? '' : this.usernameData,
        'pass': this.passData === '' ? '' : this.passData,
        
          }

      const formData = new FormData();

      
      const jsonUserData = JSON.stringify(newUserData);

      const blobUser = new Blob([jsonUserData], {
        type: 'application/json'
      });

      formData.append('userToUpdate', blobUser);
      formData.append('newAvatar', this.$refs.imageFile.files[0] ? this.$refs.imageFile.files[0] : '');

      const apiUrl = import.meta.env.VITE_API_URL;

      const response = await fetch(`${apiUrl}/user?idUser=${this.loggedUser.id}`, {
        method: 'PUT',
        body: formData
      });
      const newUserDataUpdated = await response.json();
      this.$router.go();
    },
    /**
     * check email
     */
    validateEmail() {
      if (this.emailData === "") {
        this.modEmailOk = true
        return;
      }
      this.modEmailOk = validateEmailRegex(this.emailData)
    },
    /**
     * check user
     */
    validateUser() {
      if (this.usernameData === "") {
        this.modUserOk = true
        return;
      }
      this.modUserOk = validateUserRegex(this.usernameData)
    },
    /**
     * Check pass
     */
    validatePass() {
      // validate pass
      if (this.passData === "") {
        this.modPassOk = true
      } else
        this.modPassOk = validatePasswordRegex(this.passData)
    },
    /**
     * check image
     * @param {String} whatINeed - 
     * @param {String} newAvatar - New image
     */
    showedAvatar(whatINeed, newAvatar = null) {
      const defaultAvatar = "../../assets/images/default.png";
      if (whatINeed === "initialAvatar" && this.loggedUser) {
        this.avatarToShow = (this.loggedUser.avatar !== null && this.loggedUser.avatar !== '') ? this.loggedUser.avatar : defaultAvatar;
        if (this.loggedUser.avatar === null) {
          this.showDefaultAvatar = true;
        } else {
          this.showDefaultAvatar = false;
          this.avatarToShow = this.loggedUser.avatar;
        }
      }
      if (whatINeed === 'newUserAvatar') {
        this.showDefaultAvatar = false;
        this.avatarToShow = newAvatar;
      }
    }
  },
  mounted() {
    this.showedAvatar("initialAvatar");
  }
}
</script>