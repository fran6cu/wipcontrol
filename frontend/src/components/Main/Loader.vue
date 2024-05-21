<template>
  <section class="loader">
    <p class="loader__text">
      {{ msgLoad }}
    </p>
    <img alt="Spinner" class="loader__img" src="../assets/images/tail-spin.svg">
  </section>
</template>

<script>
/**
 
 * @module Component/Loader
 * 
 * @property {Object} props - component properties
 * @property {string} props.msgLoad - load message 
 */

export default {
  name: "Loader",
  props: ["msgLoad"]
}
</script>