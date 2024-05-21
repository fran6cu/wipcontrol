<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="modal" appear>
        <article class="formContainer newForm">
          <form class="formContainer__body__form" v-on:submit.prevent="checkUpdateStory()">
            <fieldset class="formContainer__body__userDataHandler">
              <label for="sprint">Add to Sprint</label>
                <select v-model="sprintData" >
                  <option value="" disabled>Select a Sprint</option>
                  <option v-for="sprint in sprints" :key="sprint.id" :value="sprint.name">
                    {{ sprint.name }}
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
  name: "ModalAddSprint",
  data() {
    return {
      sprints: [],
      errorMessage: null,
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
    
    async checkUpdateStory() {
      const updatedStoryData = {
        sprint: this.sprintData,
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

