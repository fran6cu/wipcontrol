<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="modal" appear>
        <article class="formContainer newForm">
          <form class="formContainer__body__form" v-on:submit.prevent="checkUpdateSprint()">
            <fieldset class="formContainer__body__userDataHandler">
             
              <label for="status">Status</label>
              <select v-model="statusData" id="status">
                <option v-for="(value, index) in options" :key="index" :value="value" >
                  {{ value }}
                </option>
              </select>
              
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
 
 * @module Component/Ovelays/ModalNewSprint
 * 
 * @property {Object} data - Component
 * @property {String} data.errorMessage - error msg
 * @property {String} data.titleData - sprint title
 * @property {Boolean} data.modTitleOk - Is title ok
 * @property {String} data.assignedData - sprint assigned
 * @property {Boolean} data.modAssignedOk - is asigned ok
 * @property {String} data.estimationData - sprint estimation 
 * @property {Boolean} data.modEstimationOk - is estimation ok
 * @property {String} data.statusData - sprint status
 * @property {String} data.descriptionData - sprint description
 * @property {Boolean} data.modDescriptionOk - Is description ok
 * @property {String} data.criteriaData - sprint criteria
 * @property {Boolean} data.modCriteriaOk - Is criteria ok
 * @property {String} data.statusData - status sprint
 * @property {Boolean} data.modStatusOk - Is status ok
 * @property {Number} data.count - characters count 
 * @property {Object} emits - Events component
 * @property {String} emits.close - modal close
 * @property {Object} props - component properties
 * @property {String} props.action - Action
 * @property {Object} props.toEdit - Sprint to edit
 */
export default {
  name: "ModalNewSprint",
  data() {
    return {

      users: [],
      sprints: [],
      options: ['OPEN', 'CLOSED'],
      errorMessage: null,
      titleData: this.toEdit ? this.toEdit.name : null,
      modTitleOk: true,
      statusData: this.toEdit ? this.toEdit.status : null,
      modStatusOk: true,
       
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
    },
  
  },
  
 
  methods: {

    goBack() {
       this.$router.go(-1);
    },

      
       
    async checkUpdateSprint() {
      const updatedSprintData = {
         status: this.statusData,
        
      }

      
      const options = {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(updatedSprintData)
        }

        const apiUrl = import.meta.env.VITE_API_URL;

        const idSprint = this.toEdit.id;

       
        try {
        const response = await fetch(`${apiUrl}/sprints?idSprint=${idSprint}`, options);
        const data = await response.json();
        this.$router.go();
      } catch (error) {
        console.error('Error updating sprint:', error);
      }
      
        
      },

      checkIfHaveValue(inputType) {
      if (inputType === 'title') {
        this.modTitleOk = this.titleData === '' || this.titleData === null ? false : true;
      } 

    },
    
    }
  
  
}
</script>

