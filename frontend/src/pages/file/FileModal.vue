<template>
  <div>
    <div class="modal-backdrop fade show"></div>
    <transition name="modal" appear>
      <div @click.self="$emit('close', reload, reloadCheckedFiles)" class="modal fade show" style="display: block" id="functionModal">
        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" :class="!fullSize || 'modal-fullscreen'">
          <div class="modal-content">

            <div class="modal-header">
              <h1 class="modal-title fs-5 text-break w-100 pe-3" id="functionModalLabel">
                {{ functionModalLabel[modalFunc] }}

                <template v-if="modalFunc === 'copyFiles' || modalFunc === 'moveFiles'" >
                  {{ moveTo.nowPath }}
                  <div class="d-flex pt-4 align-items-center">
                    <div v-if="isNewFolder" class="w-100 pe-4">
                      <input type="text" class="form-control" v-model="newName" placeholder="새 폴더명을 입력해주세요.">
                    </div>
                    <div class="flex-shrink-1 pointer" @click="moveFilesNewFolder">
                      <i class="fa-solid fa-folder-plus" :class="!isNewFolder || 'text-success'" />
                    </div>
                  </div>
                </template>
                <template v-else-if="modalFunc === 'showImg'">
                  <span>
                    <i class="btn btn-outline-secondary fa-solid fa-minus ms-2" @click="zoom -= (zoom < 0.2 ? 0 : (zoom < 3.1 ? 0.1 : (zoom < 8.1 ? 0.5 : 1)))"/>
                    <i class="btn btn-outline-secondary fa-solid fa-plus ms-2" @click="zoom += (zoom < 3 ? 0.1 : (zoom < 8 ? 0.5 : 1))"/>
                    <i class="btn btn-outline-secondary fa-solid fa-arrows-left-right ms-2" @click="zoom = 1"/>
                    <i class="btn btn-outline-secondary fa-solid fa-rotate-right ms-2" @click="degree += 90"/>
                    <i class="btn btn-outline-secondary fa-solid fa-rotate-left ms-2" @click="degree -= 90"/>
                    <span v-if="zoom != 1" class="ms-3">{{ Math.round(100 * zoom) }}%</span>
                  </span>
                </template>

              </h1>
              <button type="button" class="btn-close" @click="$emit('close', reload, reloadCheckedFiles)"></button>
            </div>

            <div class="modal-body" id="functionModalBody" @keyup.enter="submit">

              <div v-if="modalFunc === 'newFolder'">
                <input type="text" class="form-control" v-model="newName" placeholder="폴더명을 입력해주세요">
              </div>
              <div v-else-if="modalFunc === 'newFile'">
                <input type="text" class="form-control" v-model="newName" placeholder="파일명을 입력해주세요">
              </div>
              <div v-else-if="modalFunc === 'changeName'">
                <input type="text" class="form-control" v-model="newName" placeholder="바꿀 이름을 입력해주세요">
              </div>
              <div v-else-if="modalFunc === 'deleteFiles'">
                <div class="text-danger">아래 {{ setting.checkedFiles.length }} 개의 파일을 삭제하시겠습니까?</div>
                <div v-for="li in setting.checkedFiles"> {{ li }} </div>
              </div>
              <div v-else-if="modalFunc === 'copyFiles' || modalFunc === 'moveFiles'">
                <table>
                  <!-- upper directory -->
                  <tr v-if="moveTo.parentPath !== moveTo.nowPath" class="pointer" @click="loadFolders(moveTo.parentPath)">
                    <td class='text-center py-2' style='width:50px;'>
                      <img class="fileImg" :src="'/img/fileicons/' + extensions['folder'] + '.png'" loading="lazy">
                    </td>
                    <td class='w-auto'>..</td>
                  </tr>

                  <!-- directory list -->
                  <tr v-for="li in moveTo.folderList" class="pointer" @click="loadFolders(li.path.replace(/\\/g, '/'))">
                    <td class='text-center py-2' style='width:50px;'>
                      <img class='fileImg' :src="'/img/fileicons/' + extensions['folder'] + '.png'" loading="lazy">
                    </td>
                    <td class='w-auto'>
                      <div>{{ li.name }}</div>
                    </td>
                  </tr>

                  <!-- Error text -->
                  <tr v-if="moveTo.invalidText"><td>{{ moveTo.invalidText }}</td></tr>
                </table>
              </div>
              <div v-else-if="modalFunc === 'shareFile'">
                <input class="form-control text-break" id="newShareLink" v-model="modalBody.shareLink" @click="copyShareLink">
              </div>
              <div v-else-if="modalFunc === 'needPassword'">
                needPassword
              </div>
              <div v-else-if="modalFunc === 'showImg'" ref="imageBox">
                <div v-if="!imageSize.height" class="text-center mt-4"><i class="fa-solid fa-circle-notch fa-spin fs-5"/></div>
                <img
                  ref="image"
                  :srcset="
                              modalBody.image+'&size=360  360w,'
                            + modalBody.image+'&size=540  540w,'
                            + modalBody.image+'&size=720  720w,'
                            + modalBody.image+'&size=1080 1080w,'
                            + modalBody.image+'&size=2440 2440w,'
                            + modalBody.image+'&size=3200 3200w,'
                            + modalBody.image+'&size=3840 3840w,'
                            + modalBody.image+'&size=7680 7680w'
                          "
                  :src="modalBody.image+'&size=0'"
                  @click="fullSize=!fullSize"
                  style="transition: all 1s;"
                  :style="imgStyle + 'transform:rotate('+degree+'deg);'"
                >
              </div>

            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="$emit('close', reload, reloadCheckedFiles)">취소</button>
              <button type="button" class="btn btn-primary" @click="submit">확인</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, inject, computed } from 'vue'
import Const from '@/const'
import Swal from 'sweetalert2'
import Utils from '@/modules/utils'
import { useElementSize } from '@vueuse/core'

const props = defineProps(['modalFunc', 'form', 'setting', 'extensions', 'modalBody'])
const emit = defineEmits(['close'])

const functionModalLabel = {
  newFolder     : '폴더 만들기',
  newFile       : '파일 만들기',
  changeName    : '이름변경',
  deleteFiles   : '파일삭제',
  copyFiles     : '복사:',
  moveFiles     : '이동:',
  shareFile     : '공유',
  needPassword  : '비밀번호 입력',
  showImg       : '사진보기',
}

const reload = ref(false)
const reloadCheckedFiles = ref([])
const isNewFolder = ref(false)
const newName = ref('')
const origName = ref('')

const imageBox = ref(null)
const image = ref(null)
const imageSize = reactive(useElementSize(image, {width: 0, height: 0}, {box: 'border-box'}))
const imageBoxSize = reactive(useElementSize(imageBox, {width: 0, height: 0}, {box: 'border-box'}))
const zoom = ref(1)
const fullSize = ref(false)
const degree = ref(0)

const moveTo = reactive({
  parentPath: props.form.path,
  nowPath: '',
  invalidText: '',
})

onMounted(()=> {
  if(props.modalFunc === 'moveFiles' || props.modalFunc === 'copyFiles') {
    loadFolders(props.form.path)
  } else if(props.modalFunc === 'changeName') {
    origName.value = props.setting.checkedFiles[0]
    newName.value = origName.value
  }
})

const imgStyle = computed({
  get() {
    let result = 'width:100%;'
    if(degree.value % 180 === 0) {
      result = 'width:'+ 100*zoom.value +'%; height: auto;'
    } else {
      const ratio = image.value.width / image.value.height
      const imgW = imageBoxSize.width * zoom.value
      const imgH = imgW * ratio

      const marginX = (imgH - imgW) / 2
      const marginY = (imgW - imgH) / 2
      result = 'width:' + imgH + 'px;'
             + 'height:' + imgW + 'px;'
             + 'margin:' + marginX + 'px ' + marginY + 'px;'
    }
    return result
  }
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
    if (!newName.value.trim()) {
      Swal.fire({ icon: 'error', text: '폴더명은 필수입니다.' })
    } else {
      const params = {
        path: moveTo.nowPath,
        shareLink: props.form.shareLink,
        name: newName.value.trim()
      }
      $http.post('/file/newFolder', params, null)
        .then((response) => {
          loadFolders(params.path)
          if(Utils.addSlash(Utils.backSlashToSlash(params.path)) === Utils.addSlash(props.form.path)) {
            reload.value = true
            reloadCheckedFiles.value = props.setting.checkedFiles
          }
        })
    }
  }
}

const copyShareLink = () => {
  document.querySelector("#newShareLink").select()
  document.execCommand("copy");
  Swal.fire({ icon: 'success', text: '링크가 복사되었습니다.', timer: 1200, showConfirmButton: false })
  emit('close', false, props.setting.checkedFiles)
}


/*-------------------- modal submit --------------------*/
const submit = async () => {
  let result = false

  if(!newName.value.trim() &&
      (props.modalFunc === 'newFile' || props.modalFunc === 'newFolder' || props.modalFunc === 'changeName')) {
    Swal.fire({ icon: 'error', text: '이름은 필수입니다.' })
    return result
  }

  // new file
  if(props.modalFunc === 'newFile') {
    const params = {
      path: props.form.path,
      shareLink: props.form.shareLink,
      name: newName.value.trim()
    }
    await $http.post('/file/newFile', params, null)
      .then((response) => {
        result = (response.code === Const.RESPONSE_TYPE.SUCCESS)
      })
    reloadCheckedFiles.value = props.setting.checkedFiles

  // new folder
  } else if(props.modalFunc === 'newFolder') {
    const params = {
      path: props.form.path,
      shareLink: props.form.shareLink,
      name: newName.value.trim()
    }
    await $http.post('/file/newFolder', params, null)
        .then((response) => {
          result = (response.code === Const.RESPONSE_TYPE.SUCCESS)
        })
    reloadCheckedFiles.value = props.setting.checkedFiles

  // change name
  } else if(props.modalFunc === 'changeName') {
    const params = {
      path: props.form.path,
      shareLink: props.form.shareLink,
      name: newName.value.trim(),
      origName: origName.value
    }
    await $http.post('/file/changeName', params, null)
        .then((response) => {
          result = (response.code === Const.RESPONSE_TYPE.SUCCESS)
        })

  // copy files
  } else if(props.modalFunc === 'copyFiles') {
    const params = {
      path: props.form.path,
      newPath: moveTo.nowPath,
      shareLink: props.form.shareLink,
      fileList: props.setting.checkedFiles
    }
    await $http.post('/file/copyFiles', params, null)
        .then((response) => {
          result = (response.code === Const.RESPONSE_TYPE.SUCCESS)
        })

  // move files
  } else if(props.modalFunc === 'moveFiles') {
    const params = {
      path: props.form.path,
      newPath: moveTo.nowPath,
      shareLink: props.form.shareLink,
      fileList: props.setting.checkedFiles
    }
    await $http.post('/file/moveFiles', params, null)
        .then((response) => {
          result = (response.code === Const.RESPONSE_TYPE.SUCCESS)
        })

  // delete files
  } else if(props.modalFunc === 'deleteFiles') {
    const params = {
      path: props.form.path,
      shareLink: props.form.shareLink,
      fileList: props.setting.checkedFiles
    }
    await $http.post('/file/deleteFiles', params, null)
        .then((response) => {
          result = (response.code === Const.RESPONSE_TYPE.SUCCESS)
        })
  // share file
  } else if(props.modalFunc === 'shareFile') {
    copyShareLink()
    return false
  // show image
  } else if(props.modalFunc === 'showImg') {
    result = true
  }

  if(!result) return false

  emit('close', true, reloadCheckedFiles.value)
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