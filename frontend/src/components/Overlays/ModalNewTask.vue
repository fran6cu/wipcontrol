<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="modal" appear>
        <article class="formContainer newForm">
          <form class="formContainer__body__form" v-on:submit.prevent="checkNewTask()">
            <fieldset class="formContainer__body__userDataHandler">
              <label for="title">Title *</label>
              <input v-model="titleData" :class="{ 'input_error': !modTitleOk }" v-on:blur="checkIfHaveValue('title')"
                id="title" type="text" placeholder="Story title">
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
 
 * @module Component/Ovelays/ModalNewTask
 * 
 * @property {Object} data - Component
 * @property {String} data.errorMessage - error msg
 * @property {String} data.titleData - story title
 * @property {Boolean} data.modTitleOk - Is title ok
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
  name: "ModalNewTask",
  data() {
    return {

      users: [],
      sprints: [],
      options: ['TO DO', 'In progress', 'Done'],
            
      errorMessage: null,
      titleData: this.toEdit ? this.toEdit.name : null,
      modTitleOk: true,      
      statusData: this.toEdit ? this.toEdit.status : null,
      modStatusOk: true,
      descriptionData: this.toEdit ? this.toEdit.description : null,
      modDescriptionOk: true,
      criteriaData: this.toEdit ? this.toEdit.criteria : null,
      modCriteriaOk: true,
      count: 0,
      error: false,
      
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
  
   
  methods: {
   
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
      } else if (inputType === 'description') {
        this.modDescriptionOk = this.descriptionData === '' || this.descriptionData === null ? false : true;
      } else if (inputType === 'criteria') {
        this.modCriteriaOk = this.criteriaData === '' || this.criteriaData === null ? false : true;
      } else if (inputType === 'status') {
        this.modStatusOk = this.statusData === '' || this.statusData === null ? false : true;
      }

    },
    /**
     * create new task 
     * show error if us name already exist
     * if save ok, close modal and reload page
     */
    async checkNewTask() {
      
      this.checkIfHaveValue('title');      
      this.checkIfHaveValue('status');     
      this.checkIfHaveValue('description');
      this.checkIfHaveValue('criteria');
      if (this.modTitleOk && this.modDescriptionOk && this.modCriteriaOk && this.modStatusOk) {
        if (this.action === 'edit') {
          await this.checkUpdateTask();
          return;
        }

        const newTaskData = {
          id_story: this.$route.params.id,
          name: this.titleData,
          status: this.statusData,
          description: this.descriptionData,
          criteria: this.criteriaData,
        }
        const options = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(newTaskData)
        }
        const apiUrl = import.meta.env.VITE_API_URL;
        
        fetch(`${apiUrl}/tasks`, options)
          .then(response => response.json())
          .then(data => {
           
              this.$router.go();
           
                      })
                      .catch(error => {
    console.error('Error adding task:', error);
   
  });
      }
    },

   
    async checkUpdateTask() {
      const updatedTaskData = {
        name: this.titleData,
        status: this.statusData,
        description: this.descriptionData,
        criteria: this.criteriaData,
      }

      
      const options = {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(updatedTaskData)
        }

        const apiUrl = import.meta.env.VITE_API_URL;

        const idTask = this.toEdit.id;

       
        try {
        const response = await fetch(`${apiUrl}/tasks?idTask=${idTask}`, options);
        const data = await response.json();
        this.$router.go();
      } catch (error) {
        console.error('Error updating story:', error);
      }
      
        
      }
    
    }
  
  
}
</script>

