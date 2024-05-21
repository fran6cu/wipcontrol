<script>
import Header from './components/Main/Header.vue'
export default {
  name: 'App',
  components: {
    Header
  }
}
</script>

<template>
  <Header></Header>
  <RouterView></RouterView>
</template>
