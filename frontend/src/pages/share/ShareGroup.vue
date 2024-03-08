<template>
  <!-- filter -->
  <div class="row fixed-top" id="shareMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded-bottom border border-top-0 p-2 m-0 bg-light-subtle">
        <div class="col-4"></div>
        <div class="col-6 px-2">
          <input type="text" class="w-100 border border-secondary rounded-5 px-2" placeholder="Filter" id="keyword" v-model="form.keyword" @keyup.enter="loadShareGroup">
        </div>
        <div class="col-1 pointer" @click="loadShareGroup"><i class="fa-solid fa-magnifying-glass" /></div>
        <div class="col-1 pointer" @click="editShareModal(0)"><i class="fa-solid fa-user-plus" /></div>
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

    <!-- Group List -->
    <div id="groupList" class="pe-0 pe-md-3">

      <table v-for="li in groupList" class="w-100 rounded border-bottom">
        <tr>
          <td class="w-auto py-2" @click="editShareModal(li.groupNo)">
            <div class="flex-grow-1 text-break-all">
              {{ li.groupName }}
            </div>
          </td>
          <td class="text-center text-secondary" style="width:40px">
            <i v-if="li.groupNo !== 1" class="fa-solid fa-trash" @click="deleteGroup(li.groupNo)"/>
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

const groupList = ref([])

const form = reactive({
  shareLink: '',
  keyword: '',
})

onMounted(() => {
  loadShareGroup()
})

const loadShareGroup = () => {
  props.setting.loadingList = true

  $http.post('/share/groupList', form, null)
    .then((response) => {
      if(response.code === Const.RESPONSE_TYPE.NOT_ALLOWED) {
        router.go(-1)
      } else if(response.data) {
        groupList.value = response.data.lists
      }
      props.setting.loadingList = false
    })
}

const editShareModal = (groupNo) => {
  setModalBody({ groupNo: groupNo })
  shareModal('shareGroup')
}

const shareModal = inject('shareModal')
const setModalBody = inject('setModalBody')

</script>

<style>
</style>