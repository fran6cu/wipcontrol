<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="modal" appear>
        <article class="formContainer newForm">
          <form class="formContainer__body__form" v-on:submit.prevent="checkUpdateStory()">
            <fieldset class="formContainer__body__userDataHandler">
            
              <label for="points">Points</label>
              <input v-model="pointsData" id="points" maxlength="3" placeholder="Points" readonly>

              <label for="status">Rate US</label>
                <select v-model="addedPoints" id="selectField">
                  <option v-for="(value, index) in options" :key="index" :value="value">
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
 
 * @module Component/Ovelays/ModalAddPoints
 * 
 * @property {Object} data - Component
 * @property {String} data.errorMessage - error msg
 * @property {Number} data.count - characters count 
 * @property {Object} emits - Events component
 * @property {String} emits.close - modal close
 * @property {Object} props - component properties
 * @property {String} props.action - Action
 * @property {Object} props.toEdit - Story to edit
 */
export default {
  name: "ModalAddPoints",
  data() {
    return {
      options: [1,2,3,5,8,13,21],
      errorMessage: null,
      pointsData: this.toEdit ? this.toEdit.points : null,
      modPointsOk: true,
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
   
  methods: {
     async checkUpdateStory() {
      const updatedStoryData = {
        points: this.pointsData + this.addedPoints,
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

