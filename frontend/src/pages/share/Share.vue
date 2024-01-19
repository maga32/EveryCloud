<template>
  <!-- Top Menu -->
  <div class="row fixed-top" id="fileMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">

      <!-- tabs -->
      <div class="row rounded-top border border-bottom-0 p-2 m-0 bg-light-subtle" style="height: var(--file-share-height)">
        <table class="col-12 text-center">
          <div class="btn-group btn-group-sm" role="group" aria-label="Basic radio toggle button group">
            <input type="radio" class="btn-check" name="btnradio" id="btnradio1" checked>
            <label class="btn btn-outline-secondary px-3" for="btnradio1" @click="tab='shareList'">공유파일</label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio2">
            <label class="btn btn-outline-secondary px-3" for="btnradio2" @click="tab='shareGroup'">공유그룹</label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio3">
            <label class="btn btn-outline-secondary px-3" for="btnradio3" @click="tab='shareLog'">공유기록</label>
          </div>
        </table>
      </div>

    </div>
  </div>

  <!-- tabs -->
  <ShareList
      v-if="tab==='shareList'"
      :key="reloadTab"
      :setting="setting"
      :modalBody="modalBody"
  />

  <!-- Modal -->
  <ShareModal
    v-if="modalOn"
    @close="closeModal"
    :setting="setting"
    :modalFunc="modalFunc"
    :modalBody="modalBody"
  />

</template>

<script setup>
import { onMounted, reactive, ref, provide, watch, computed } from 'vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import ShareList from './ShareList.vue'
import ShareModal from './ShareModal.vue'
import Const from '@/const'

const route = useRoute()

const tab = ref('shareList')
const reloadTab = ref(0)

const setting = reactive({
  loadingList: false,
  selected: '',
})

const modalOn = ref(false)
const modalFunc = ref('')
const modalBody = ref({})

/*------------------------ functions ------------------------*/

const openModal = () => { modalOn.value = true }
const closeModal = (reload) => {
  modalOn.value = false
  setModalBody(null)
  if(reload) reloadTab.value++
}

const shareModal = (type) => {
  modalFunc.value = type
  openModal()
}

const setModalBody = (body) => {
  modalBody.value = body
}

provide('shareModal', shareModal)
provide('setModalBody', setModalBody)

</script>

<style>
@import '@/assets/css/file.css';
</style>
