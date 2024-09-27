<template>
  <!-- filter -->
  <div class="row fixed-top" id="shareMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded-bottom border border-top-0 p-2 m-0 bg-light-subtle">
        <div class="col-4">
          <select class="form-select form-select-sm" v-model="form.condition">
            <option value="all">전체</option>
            <option value="id">ID</option>
            <option value="nickname">닉네임</option>
            <option value="email">Email</option>
            <option value="groupName">그룹명</option>
          </select>
        </div>
        <div class="col-6 px-2">
          <input type="text" class="w-100 border border-secondary rounded-5 px-2" placeholder="Filter" v-model="form.keyword" @keyup.enter="loadShareUser">
        </div>
        <div class="col-1 text-center pointer" @click="loadShareUser"><i class="fa-solid fa-magnifying-glass" /></div>
        <div class="col-1 text-center pointer" @click="editShareModal(0)"><i class="fa-solid fa-user-plus" /></div>
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

      <table v-for="li in groupList" class="w-100 rounded border-bottom pointer">
        <tr>
          <td class="w-auto py-2" @click="editShareModal(li.id)">
            <div class="flex-grow-1 text-break-all">
              <div>{{ li.nickname }} ( {{ li.id }} )</div>
              <div class="text-gray">{{ li.groupName }} / {{ li.email }}</div>
            </div>
          </td>
          <td class="text-center text-secondary" style="width:40px">
            <i v-if="li.auth !== 'Y'" class="fa-solid fa-trash pointer" @click="deleteUser(li.id)"/>
          </td>
        </tr>
      </table>

    </div>

  </div>
</template>

<script setup>
import { computed, inject, onMounted, onUpdated, reactive, ref } from 'vue'
import Const from '@/const'
import router from '@/router'
import Swal from 'sweetalert2'

const props = defineProps(['setting', 'modalBody'])

const groupList = ref([])

const form = reactive({
  condition: 'all',
  keyword: '',
})

onMounted(() => {
  loadShareUser()
})

const loadShareUser = () => {
  props.setting.loadingList = true

  $http.post('/user/userList', form, null)
    .then((response) => {
      if(response.code === Const.RESPONSE_TYPE.NOT_ALLOWED) {
        router.go(-1)
      } else if(response.data) {
        groupList.value = response.data.lists
      }
      props.setting.loadingList = false
    })
}

const editShareModal = (userId) => {
  setModalBody({ userId: userId })
  shareModal('userSetting')
}

const deleteUser = (userId) => {
  Swal.fire({
    icon: 'error',
    input: 'checkbox',
    inputPlaceholder: '확인',
    customClass: {
      input: 'bg-transparent',
      confirmButton: 'bg-danger',
    },
    inputValidator: (result) => {
      return !result && '확인버튼을 체크해주세요';
    },
    html: `${userId} <span class="text-danger">사용자를 삭제하시겠습니까?<br>
    이 행위는 사용자 계정을 삭제하며 복구할 수 없습니다.</span>`,
    showCancelButton: true,
    confirmButtonText: '삭제',
    cancelButtonText: '취소',
  }).then(
      async(result) => {
        if(result.isConfirmed) {
          let resultOk = false

          await $http.post('/user/deleteUser', null, {params: {userId: userId}})
            .then((response) => {
              resultOk = (response?.code === Const.RESPONSE_TYPE.SUCCESS)
            })

          if(!resultOk) return false
          Swal.fire({icon: 'success', text: '삭제되었습니다.', timer: 1200, showConfirmButton: false})
          loadShareUser()
        }

      }
  )
}

const shareModal = inject('shareModal')
const setModalBody = inject('setModalBody')

</script>

<style>
</style>