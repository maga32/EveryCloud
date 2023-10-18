<template>
  <div>
    <div class="modal-backdrop fade show"></div>
    <transition name="modal" appear>
      <div @click.self="$emit('close')" class="modal fade show" style="display: block" id="functionModal">
        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
          <div class="modal-content">
            <div class="modal-header">
              <h1 class="modal-title fs-5 text-break w-100 pe-3" id="functionModalLabel">
                {{ functionModalLabel[modalFunc] }}
                <template v-if="modalFunc === 'copyFiles' || modalFunc === 'moveFiles'" >
                  {{ moveTo.nowPath }}
                  <div class="d-flex pt-4 align-items-center">
                    <div v-if="isNewFolder" class="w-100 pe-4">
                      <input type="text" class="form-control" v-model="newFolderName" placeholder="새 폴더명을 입력해주세요.">
                    </div>
                    <div class="flex-shrink-1 pointer" @click="moveFilesNewFolder">
                      <i class="fa-solid fa-folder-plus" :class="!isNewFolder || 'text-success'" />
                    </div>
                  </div>
                </template>
              </h1>
              <button type="button" class="btn-close" @click="$emit('close')"></button>
            </div>
            <div class="modal-body" id="functionModalBody">
              <div v-if="modalFunc === 'newFolder'">
                newFolder
              </div>
              <div v-else-if="modalFunc === 'newFile'">
                newFile
              </div>
              <div v-else-if="modalFunc === 'changeName'">
                changeName
              </div>
              <div v-else-if="modalFunc === 'deleteFiles'">
                deleteFiles
              </div>
              <div v-else-if="modalFunc === 'copyFiles' || modalFunc === 'moveFiles'">
                <table>
                  <tr v-if="moveTo.parentPath !== moveTo.nowPath" class="pointer" @click="loadFolders(moveTo.parentPath)">
                    <td class='text-center py-2' style='width:50px;'>
                      <img class="fileImg" :src="'/img/fileicons/' + extensions['folder'] + '.png'" loading="lazy">
                    </td>
                    <td class='w-auto'>..</td>
                  </tr>

                  <tr v-for="li in moveTo.folderList" class="pointer" @click="loadFolders(li.path.replace(/\\/g, '/'))">
                    <td class='text-center py-2' style='width:50px;'>
                      <img class='fileImg' :src="'/img/fileicons/' + extensions['folder'] + '.png'" loading="lazy">
                    </td>
                    <td class='w-auto'>
                      <div>{{ li.name }}</div>
                    </td>
                  </tr>

                  <tr v-if="moveTo.invalidText"><td>{{ moveTo.invalidText }}</td></tr>
                </table>

              </div>
              <div v-else-if="modalFunc === 'shareFile'">
                shareFile
              </div>
              <div v-else-if="modalFunc === 'needPassword'">
                needPassword
              </div>
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
import { onMounted, reactive, ref } from 'vue'
import Const from "@/const";
import Swal from "sweetalert2";

const props = defineProps(['modalFunc', 'form', 'extensions'])

const functionModalLabel = {
  newFolder     : '폴더 만들기',
  newFile       : '파일 만들기',
  changeName    : '이름변경',
  deleteFiles   : '파일삭제',
  copyFiles     : '복사:',
  moveFiles     : '이동:',
  shareFile     : '공유',
  needPassword  : '비밀번호 입력',
}

const isNewFolder = ref(false)
const newFolderName = ref('New Folder')

const moveTo = reactive({
  parentPath: props.form.path,
  nowPath: '',
  invalidText: '',
})

onMounted(()=> {
  if(props.modalFunc === 'moveFiles' || props.modalFunc === 'copyFiles') loadFolders(props.form.path)
})

/*------------------------ functions ------------------------*/
const loadFolders = (path) => {
  moveTo.invalidText = ''

  $http.post('/file/folderList', {path: path, shareLink: props.form.shareLink}, null)
    .then((response) => {
      if(response.data) {
        moveTo.parentPath = response.data.option.parentPath
        moveTo.nowPath    = response.data.option.nowPath
        moveTo.folderList = response.data.lists
        if(response.data.lists === null) moveTo.invalidText  = '잘못된 접근입니다.'
      }
    })
}

const moveFilesNewFolder = () => {
  if(!isNewFolder.value) {
    isNewFolder.value = true
  } else {
    if (!newFolderName.value.trim()) {
      Swal.fire({ icon: 'error', text: '폴더명은 필수입니다.' })
    } else {
      const params = {
        path: moveTo.nowPath,
        shareLink: props.form.shareLink,
        newName: newFolderName.value.trim()
      }
      $http.post('/file/newFolder', params, null)
        .then((response) => {
          loadFolders(params.path)
        })
    }
  }
}

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