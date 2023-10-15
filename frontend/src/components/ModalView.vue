<template>
  <div>
    <div class="modal-backdrop fade show"></div>
    <transition name="modal" appear>
      <div @click.self="$emit('close')" class="modal fade show" style="display: block" id="functionModal">
        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
          <div class="modal-content">
            <div class="modal-header">
              <h1 class="modal-title fs-5 text-break w-100 pe-3" id="functionModalLabel">
                <slot name="functionModalLabel">

                </slot>
              </h1>
              <button type="button" class="btn-close" @click="$emit('close')"></button>
            </div>
            <div class="modal-body" id="functionModalBody">
              <slot name="functionModalBody">

              </slot>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="$emit('close')">취소</button>
              <button type="button" class="btn btn-primary" @click="">확인</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>

</script>

<style scoped>
.modal-enter-active,
.modal-leave-active {
  transition: all 0.5s ease;
}

.modal-enter-from,
.modal-leave-to {
  transform: translateY(-20px);
  opacity: 0;
}
</style>