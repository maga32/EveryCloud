<template>
  <!-- filter -->
  <div class="row fixed-top" id="shareMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded-bottom border border-top-0 p-2 m-0 bg-light-subtle">
        <div class="col-5"></div>
        <div class="col-6 px-2">
          <input type="text" class="w-100 border border-secondary rounded-5 px-2" placeholder="Filter" id="keyword" v-model="form.keyword" @keyup.enter="loadShareList">
        </div>
        <div class="col-1 pointer" @click="loadShareList"><i class="fa-solid fa-magnifying-glass" /></div>
      </div>
    </div>
  </div>

  <!-- Share List -->
  <div class="col-12" id="fileListContainer">

    <!-- Loading Motion -->
    <div id="loadingList" class="fixed-top row" :class="!setting.loadingList || 'act'">
      <div class="d-none d-md-block col-3"></div>
      <div class="col-12 col-md-9 px-4 text-center bg-secondary bg-opacity-10 d-flex align-items-center justify-content-center">
        <i class="fa-solid fa-circle-notch fa-spin fs-5" /><span class="ps-2 fs-4">Loading...</span>
      </div>
    </div>

    <!-- File List -->
    <div id="shareList" class="pe-0 pe-md-3">

      <table v-for="li in shareList" class="w-100 rounded border-bottom">
        <tr @click="editShareModal(li.link)">
          <td class="text-center text-secondary" style="width:40px">
            <i v-if="li.method === 0" class="fa-solid fa-globe" />
            <i v-else-if="li.method === 1" class="fa-solid fa-lock" />
            <i v-else-if="li.method === 2" class="fa-solid fa-user-group" />
          </td>
          <td class="w-auto py-2 pe-3">
            <div class="text-gray text-break-all" style="font-size:0.8rem">
              {{ form.shareLink + '/file?shareLink=' + li.link.replaceAll(' ', '&nbsp;') }}
            </div>
            <div class="d-flex">
              <div class="flex-grow-1 text-break-all">
                <span v-if="!li.exist" class="badge text-bg-danger">Invalid</span>
                {{ li.path }}
              </div>
            </div>
          </td>
        </tr>
      </table>

    </div>

  </div>
</template>

<script setup>
import { computed, inject, onMounted, onUpdated, reactive, ref } from 'vue'
import Utils from '@/modules/utils'
import dayjs from 'dayjs'
import Const from "@/const";
import router from "@/router";

const props = defineProps(['setting', 'modalBody'])

const shareList = ref([])

const form = reactive({
  shareLink: '',
  keyword: '',
})

onMounted(() => {
  loadShareList()
})

const loadShareList = () => {
  props.setting.loadingList = true

  $http.post('/share/shareList', form, null)
      .then((response) => {
        console.log(response)
        if(response.code === Const.RESPONSE_TYPE.NOT_ALLOWED) {
          router.go(-1)
        } else if(response.data) {
          shareList.value = response.data.lists
          form.shareLink = response.data.option
        }
        props.setting.loadingList = false
      })
}

const editShareModal = (shareLink) => {
  setModalBody({ shareLink: shareLink })
  shareModal('shareList')
}

const shareModal = inject('shareModal')
const setModalBody = inject('setModalBody')

</script>

<style>
</style>