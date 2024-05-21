<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="modal" appear>
        <article class="formContainer newForm">
          <form class="formContainer__body__form" v-on:submit.prevent="checkNewStory()">
            <fieldset class="formContainer__body__userDataHandler">
              <label for="title">Title *</label>
              <input v-model="titleData" :class="{ 'input_error': !modTitleOk }" v-on:blur="checkIfHaveValue('title')"
                id="title" type="text" placeholder="Story title">
              <label for="assigned">Assigned *</label>
                <select v-model="assignedData">
                  <option value="" disabled>Select a username</option>
                  <option v-for="user in users" :key="user.id" :value="user.username">
                    {{ user.username }}
                  </option>
                </select>
              <label for="estimation">Estimation *</label>
              <input v-model="estimationData" :class="{ 'input_error': !modEstimationOk }"
                v-on:blur="checkIfHaveValue('estimation')" id="estimation" maxlength="3" v-on:input="checkIsANumber"
                placeholder="Estimation">
              <label for="status">Status *</label>
                <select v-model="statusData" id="selectField">
                  <option v-for="(value, index) in options" :key="index" :value="value">
                    {{ value }}
                  </option>
                </select>
              <label for="description">Description *</label>
              <textarea v-model="descriptionData" @keyup="countCharacters" :class="{ 'input_error': !modDescriptionOk }"
                v-on:blur="checkIfHaveValue('description')" id="description"
                placeholder="Description"> </textarea>
              <label for="criteria">Criteria *</label>
              <textarea v-model="criteriaData" @keyup="countCharactersCrit" :class="{ 'input_error': !modCriteriaOk }"
                v-on:blur="checkIfHaveValue('criteria')" id="criteria"
                placeholder="Criteria"> </textarea>
              <p class="modalForm__count" :class="{ 'limit': count === 750 }">{{ count }}/750</p>
              <!-- <label for="sprint">Add to Sprint</label>
                <select v-model="sprintData" >
                  <option value="" disabled>Select a Sprint</option>
                  <option v-for="sprint in sprints" :key="sprint.id" :value="sprint.name">
                    {{ sprint.name }}
                  </option>
                </select> -->
            </fieldset>
            <button>Add</button>
          </form>
        </article>
      </Transition>
    </section>
  </Transition>
</template>

<script>

/**
 
 * @module Component/Ovelays/ModalNewStory
 * 
 * @property {Object} data - Component
 * @property {String} data.errorMessage - error msg
 * @property {String} data.titleData - story title
 * @property {Boolean} data.modTitleOk - Is title ok
 * @property {String} data.assignedData - story assigned
 * @property {Boolean} data.modAssignedOk - is asigned ok
 * @property {String} data.estimationData - story estimation 
 * @property {Boolean} data.modEstimationOk - is estimation ok
 * @property {String} data.statusData - story status
 * @property {String} data.descriptionData - story description
 * @property {Boolean} data.modDescriptionOk - Is description ok
 * @property {String} data.criteriaData - story criteria
 * @property {Boolean} data.modCriteriaOk - Is criteria ok
 * @property {String} data.statusData - status story
 * @property {Boolean} data.modStatusOk - Is status ok
 * @property {Number} data.count - characters count 
 * @property {Object} emits - Events component
 * @property {String} emits.close - modal close
 * @property {Object} props - component properties
 * @property {String} props.action - Action
 * @property {Object} props.toEdit - Story to edit
 */
export default {
  name: "ModalNewStory",
  data() {
    return {

      users: [],
      sprints: [],
      options: ['TO DO', 'In progress', 'Done'],
            
      errorMessage: null,
      titleData: this.toEdit ? this.toEdit.name : null,
      modTitleOk: true,
      assignedData: this.toEdit ? this.toEdit.assigned : null,
      modAssignedOk: true,
      selectedUsername: this.toEdit ? this.toEdit.assigned : null,
      modAssignedOk: true,
      estimationData: this.toEdit ? this.toEdit.estimation : null,
      modEstimationOk: true,
      statusData: this.toEdit ? this.toEdit.status : null,
      modStatusOk: true,
      descriptionData: this.toEdit ? this.toEdit.description : null,
      modDescriptionOk: true,
      criteriaData: this.toEdit ? this.toEdit.criteria : null,
      modCriteriaOk: true,
      sprintData: this.toEdit ? this.toEdit.sprint : null,
      count: 0,
    }
  },
  emits: ['close'],
  props: {
    action: {
      type: String,
      required: true
    },
    toEdit: {
      type: Object,
      required: false
    }
  },
  
  mounted() {
    // Fetch data from the server
    this.fetchUsers();
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
    
    async fetchUsers() {
      try {
        const apiUrl = import.meta.env.VITE_API_URL;
        const response = await fetch(`${apiUrl}/users`);
        
        const data = await response.json();
        this.users = data;
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    },



    /**
     * check value is a number
     * @param {Event} event 
     */
    checkIsANumber(event) {
      this.estimationData = event.target.value.replace(/\D/g, '');
    },
    /**
     * Check value is not empty
     * @param {String} value 
     */
    checkIsNotEmpty(value) {
      if (value.length > 0) {
        return true;
      } else {
        return false;
      }
    },
    /**
     * check description characters
     */
    countCharacters() {
      if (this.descriptionData && this.descriptionData.length > 750) {
        this.descriptionData = this.descriptionData.substring(0, 750);
      } else if (this.descriptionData && this.descriptionData.length > 0 && this.descriptionData.length <= 750) {
        this.count = this.descriptionData.length;
      } else {
        this.count = 0;
      }
    },
      /**
     * check criteria characters
     */
     countCharactersCrit() {
      if (this.criteriaData && this.criteriaData.length > 750) {
        this.criteriaData = this.criteriaData.substring(0, 750);
      } else if (this.criteriaData && this.criteriaData.length > 0 && this.criteriaData.length <= 750) {
        this.count = this.criteriaData.length;
      } else {
        this.count = 0;
      }
    },
    /**
     * check mandatory fields
     * @param {String} inputType - 
     */
    checkIfHaveValue(inputType) {
      if (inputType === 'title') {
        this.modTitleOk = this.titleData === '' || this.titleData === null ? false : true;
      } else if (inputType === 'assigned') {
        this.modAssignedOk = this.assignedData === '' || this.assignedData === null ? false : true;
      } else if (inputType === 'estimation') {
        this.modEstimationOk = this.estimationData === '' || this.estimationData === null ? false : true;
      } else if (inputType === 'description') {
        this.modDescriptionOk = this.descriptionData === '' || this.descriptionData === null ? false : true;
      } else if (inputType === 'criteria') {
        this.modCriteriaOk = this.criteriaData === '' || this.criteriaData === null ? false : true;
      } else if (inputType === 'status') {
        this.modStatusOk = this.statusData === '' || this.statusData === null ? false : true;
      }

    },
    /**
     * create new story 
     * show error if us name already exist
     * if save ok, close modal and reload page
     */
    async checkNewStory() {
      this.checkIfHaveValue('title');
      this.checkIfHaveValue('assigned');
      this.checkIfHaveValue('status');
      this.checkIfHaveValue('estimation');
      this.checkIfHaveValue('description');
      this.checkIfHaveValue('criteria');
      if (this.modTitleOk && this.modAssignedOk && this.modEstimationOk && this.modDescriptionOk && this.modCriteriaOk && this.modStatusOk) {
        if (this.action === 'edit') {
          await this.checkUpdateStory();
          return;
        }

        const newStoryData = {
          name: this.titleData,
          assigned: this.assignedData,
          estimation: this.estimationData,
          status: this.statusData,
          description: this.descriptionData,
          criteria: this.criteriaData,
          sprint: this.sprintData
        }
        const options = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(newStoryData)
        }
        const apiUrl = import.meta.env.VITE_API_URL;
        fetch(`${apiUrl}/stories`, options)
          .then(response => response.json())
          .then(data => {
            this.$router.go();
          })
      }
    },

   
    async checkUpdateStory() {
      const updatedStoryData = {
        name: this.titleData,
        assigned: this.assignedData,
        estimation: this.estimationData,
        status: this.statusData,
        description: this.descriptionData,
        criteria: this.criteriaData,
        sprint: this.sprintData
      }

      
      const options = {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(updatedStoryData)
        }

        const apiUrl = import.meta.env.VITE_API_URL;

        const idStory = this.toEdit.id;

       
        try {
        const response = await fetch(`${apiUrl}/stories?idStory=${idStory}`, options);
        const data = await response.json();
        this.$router.go();
      } catch (error) {
        console.error('Error updating story:', error);
      }
      
        
      }
    
    }
  
  
}
</script>

