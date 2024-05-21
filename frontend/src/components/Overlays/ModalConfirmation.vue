<template>
  <Transition name="modalBG">
    <section class="modalContainer" @click.self="$emit('close')">
      <Transition name="modal" appear>
        <section class="modal">
          <p class="modal__msg">{{ msg }}</p>
          <div class="modal__buttons">
            <button class="modal__buttons--item" @click="$emit('delete')">Yes</button>
            <button class="modal__buttons--item" @click="$emit('close')">No</button>
          </div>
        </section>
      </Transition>
    </section>
  </Transition>
</template>

<script>

/**
 
 * @module Component/Overlays/ModalConfirmation
 * 
 * @property {Object} props - component properties
 * @property {String} props.msg - msg
 * @property {Object} emits - Events component
 * @property {Function} emits.close - Event close
 * @property {Function} emits.delete - Event delete
 */
export default {
  name: "ModalConfirmation",
  emits: ["close", "delete"],
  props: {
    msg: {
      type: String,
      required: true
    }
  }
}
</script>