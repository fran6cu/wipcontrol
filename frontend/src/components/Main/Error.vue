<template>
  <section class="errorContainer">
    <p class="errorContainer__text">{{ msgError }}</p>
  </section>
</template>

<script>

/**

 * @module Component/ErrorComp
 * 
 * @property {Object} props - component properties
 * @property {string} props.msgError - msg error
 */
export default {
  name: "ErrorComp",
  props: ["msgError"]
}
</script>