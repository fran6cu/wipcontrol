<template>
  <section class="wipContainer">
    <h2 class="wipContainer__title">Sprint</h2>
    <button class="comments__add" @click="newSprint" v-if="user && user.rol === 'ADMIN'">
      <img class="add" src="../assets/icons/add.svg" alt="Add">
      New Sprint
    </button>
    <ul class="wipContainer__list" v-if="sprintsList.sprints">
      <li class="wipContainer__list__item" v-for="sprint in sprintsList.sprints">
        <div class="wipContainer__list__item__container" @click="seeSprintDetails(sprint.id)">
          <p class="wipContainer__list__item__container--img">  {{ sprint.name }}  </p>
          <p class="wipContainer__list__item__container--img">  {{ sprint.status }}  </p>
          <p class="wipContainer__list__item__container--title"> {{ sprint.name }} </p>
          <p class="wipContainer__list__item__container--title"> {{ sprint.status }} </p>
        </div>
      </li>
    </ul>
    <v-pagination v-if="sprintsList.sprints" :length="sprintsList.totalPages" :total-visible="10" v-model="currentPage"
      prev-icon="ai-triangle-left-fill" next-icon="ai-triangle-right-fill"></v-pagination>
    <ErrorComp v-else msgError="Empty List"></ErrorComp>
  </section>
  
</template>

<script>
import { getLoggedUser } from '../store/user';

import ErrorComp from '../components/Main/Error.vue';

/**
 
 * @module View/Sprint
 * 
 * @property {Object} data - component
 * @property {Array} data.sprintsList - sprints list
 * @property {Number} data.currentPage - current page
 * @property {Object} data.user - User logged
 * @property {Boolean} data.showAddSprint - show add sprint
 * @property {Object} components - Component view
 * @property {Object} components.ModalNewSprint - Component form
 * @property {Object} components.ErrorComp - Component empty backlog
 */
export default {
  name: "Sprint",
  data() {
    return {
      sprintsList: [],
      sprints:[],
      currentPage: 1,
      user: getLoggedUser(),
      showAddSprint: false,
      sprintNumber: '',
      
     
    }
  },
  components: {
    ErrorComp
  },
  methods: {

    async newSprint() {

      // first update old sprint to closed 
      this.updateSprintStatus(`Sprint${this.sprintNumber}`, 'CLOSED')

      // create new one
      const newSprintData = {
          name: `Sprint${this.sprintNumber + 1}`,
          status: 'OPEN',
        }

        const options = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(newSprintData)
        }
        const apiUrl = import.meta.env.VITE_API_URL;
        try {
          const response = await fetch(`${apiUrl}/sprints`, options);
          if (response.ok) {
            await this.getNumSprints();
            this.$router.go();
          } else {
            console.error('Failed to add sprint:', response.status, response.statusText);
          }
         } catch (error) {
           console.error('Error adding sprint:', error);
        }

    // }
      
    },

    /**
     * get sprint number
     * 
     * @returns {Promise} - Promise with data
     */
    
    async getNumSprints() {
  const apiUrl = import.meta.env.VITE_API_URL;
  try {
    const response = await fetch(`${apiUrl}/sprints/counter`);
    if (response.ok) {
      const data = await response.json();
     
      if (typeof data === 'number') {
        this.sprintNumber = data;
        
      } else {
        console.error('error:', data);
      }
    } else {
      console.error('Error obtaining sprints:', response.status, response.statusText);
    }
  } catch (error) {
    console.error('Error retrieving sprints:', error);
  }
},

    /**
     * get sprints data by page
     * 
     * @param {Number} page - Page
     * @returns {Promise} - Promise with data
     */
    async getSprintsByPage(page) {
      const apiUrl = import.meta.env.VITE_API_URL;
      return await fetch(`${apiUrl}/sprints/page?numberPage=${page}`)
        .then(response => response.json())
        .then(data => {
          this.sprintsList = data;
        });
    },
    /**
     * get sprint details
     * 
     * @param {Number} sprintId - sprint id
     */
    seeSprintDetails(sprintId) {
      this.$router.push(`/sprint/${sprintId}`);
    },

    /**
     * hide sprint form
     */
    hideForm() {
      this.showAddSprint = false;
    },

    
    async updateSprintStatus(sprintName, newStatus) {

              const apiUrl = import.meta.env.VITE_API_URL;
                const options = {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ status: newStatus })
                };

                const response = await fetch(`${apiUrl}/sprints?sprintName=${sprintName}`, options);

                if (response.ok) {
                    // Sprint status updated successfully
                    console.log(`Sprint ${sprintName} status updated to ${newStatus}`);
                    return true;
                } else {
                    console.error('Failed to update sprint status:', response.status, response.statusText);
                    return false;
                }
            }, 
  },
         
  mounted() {
    this.currentPage = parseInt(this.$route.query.page) || 1;
    this.getSprintsByPage(this.currentPage - 1);
    this.sprintNumber = this.getNumSprints();
    },
  /**
   * check route changes and call api
   */
  watch: {
    currentPage: function (val) {
      this.$router.push('/sprint?page=' + val);
    },
    $route() {
      this.currentPage = parseInt(this.$route.query.page) || 1;
      this.getSprintsByPage(this.currentPage - 1);
      this.sprintNumber = this.getNumSprints();
    }
  }
}
</script>