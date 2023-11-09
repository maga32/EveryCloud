<template>
  <!-- Top Menu -->
  <div class="row fixed-top" id="fileMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded border p-2 m-0 bg-light-subtle">

        <!-- Navigation -->
        <div class="col-12 col-md-8">
          <div id="nowPath">
            <div class='d-flex'>

                <div v-if="homePath.index > 2" >
                  <dropdown-menu withDropdownCloser>
                    <template #trigger>
                      <i class="pointer fa-solid fa-house" />
                    </template>
                    <template #body>
                      <ul class="dropdown-menu show" dropdown-closer>
                        <li v-for="(li,i) in homePath.parents">
                          <div v-if="i < homePath.index-1" class="dropdown-item pointer" @click="loadFileList(form.shareLink, li.link, '','','',true)">
                            {{ li.name }}
                          </div>
                        </li>
                      </ul>
                    </template>
                  </dropdown-menu>
                </div>
                <div v-else class='pointer' @click="loadFileList(form.shareLink, homePath.parents[0].link, '','','',true)">
                  <i class="pointer fa-solid fa-house" />
                </div>

                <div v-if="homePath.index > 0" class="px-2 text-truncate">
                  <span class='pointer' @click="loadFileList(form.shareLink, (homePath.index === 1 ? homePath.parents[1].link : homePath.parents[homePath.index-1].link), '','','',true)">
                    {{ homePath.index === 1 ? homePath.parents[1].name : homePath.parents[homePath.index-1].name }}
                  </span>
                </div>
                <div v-if="homePath.index > 1">
                  <i class='fa-solid fa-angle-right' />
                </div>
                <div v-if="homePath.index > 1" class="px-2 text-truncate">
                  <span class='pointer' @click="loadFileList(form.shareLink, homePath.parents[homePath.index].link, '','','',true)">
                    {{ homePath.parents[homePath.index].name }}
                  </span>
                </div>

            </div>
          </div>
        </div>

        <!-- Tools -->
        <div class="col-12 col-md-4 px-0 row text-end">
          <div class="col-4 col-sm-6 d-md-none"></div>
          <div class="col-6 col-sm-4 col-md-8 px-2">
            <input type="text" class="w-100 border border-secondary rounded-5 px-2" placeholder="Filter" id="keyword" v-model="form.keyword" @keyup.enter="loadFileList('','','','')">
          </div>
          <div class="col-1 col-md-2 pointer" @click="loadFileList('','','','')"><i class="fa-solid fa-magnifying-glass" /></div>
          <div class="col-1 col-md-2">
            <dropdown-menu withDropdownCloser direction="right">
              <template #trigger>
                <i class="pointer fa-solid fa-wrench" />
              </template>
              <template #body>
                <ul class="dropdown-menu show" dropdown-closer style="right:50px">
                  <li><div class="dropdown-item pointer" @click="toggleHiddenCheck">
                    <span id="viewHiddenCheck" :class="form.viewHidden || 'inactive'"><i class="fa-solid fa-check" />&nbsp;</span>
                    <i class="fa-solid fa-file-shield pe-2" />숨김파일보기
                  </div></li>
                  <li v-if="!setting.search && shareAuth === 1"><div class="dropdown-item pointer" @click="fileModal('newFolder')">
                    <i class="fa-solid fa-folder-plus pe-2" />새폴더
                  </div></li>
                  <li v-if="!setting.search && shareAuth === 1"><div class="dropdown-item pointer" @click="fileModal('newFile')">
                    <i class="fa-solid fa-file-circle-plus pe-2" />새파일
                  </div></li>
                </ul>
              </template>
            </dropdown-menu>
          </div>
        </div>

        <!-- Filter -->
        <table class="w-100">
          <tr>
            <td class="text-center" style="width:35px"><input v-if="!setting.search" type="checkbox" class="form-check-input" v-model="checkAllFile"></td>
            <td style="width:20px"></td>
            <td class="w-auto d-flex">
              <span class="pointer" @click="loadFileList('', '', 'name', (form.order == 'asc' ? 'desc' : 'asc'))">이름{{sortArrow('name')}}</span>
              <span class="pointer px-3" @click="loadFileList('', '', 'size', (form.order == 'asc' ? 'desc' : 'asc'))">크기{{sortArrow('size')}}</span>
            </td>
            <td class="text-end" style="width:15%">
              <span v-if="!!setting.search" class="pointer" @click="loadFileList('', '', 'path', (form.order == 'asc' ? 'desc' : 'asc'))">경로{{sortArrow('path')}}</span>
              <span v-else class="pointer" @click="loadFileList('', '', 'type', (form.order == 'asc' ? 'desc' : 'asc'))">종류{{sortArrow('type')}}</span>
            </td>
            <td class="text-center" style="width:20%">
              <span class="pointer" @click="loadFileList('', '', 'date', (form.order == 'asc' ? 'desc' : 'asc'))">날짜{{sortArrow('date')}}</span>
            </td>
          </tr>
        </table>

      </div>
    </div>
  </div>

  <!-- File Control Menu -->
  <div class="position-fixed shadow border border-secondary rounded bg-body-tertiary p-3" :class="setting.checkedFiles.length > 0 ? 'active': 'inactive'" id="fileControlMenu">
    <template v-if="setting.checkedFiles.length > 1">
      <div class="px-1"> {{ setting.checkedFiles.length }} 개의 파일 선택</div>
      <div class="text-center px-2 py-3"><img src="/img/fileicons/files.png"></div>
    </template>
    <template v-else-if="setting.checkedFiles.length === 1">
      <div class="px-1" style="word-break:break-all">{{ setting.checkedFiles[0] }}</div>
      <div class="text-center px-2 py-3"><img :src="tempImg" style="min-width:64px" @click="showImg"></div>
    </template>

    <table v-if="shareAuth !== 1">
      <tr class="pointer">
        <td class="p-1"><i class="fa-solid fa-cloud-arrow-down" /></td>
        <td class="p-1" @click="downloadFiles()">다운로드</td>
      </tr>
    </table>

    <table v-else-if="setting.checkedFiles.length > 1">
      <tr class="pointer">
        <td class="p-1"><i class="fa-solid fa-cloud-arrow-down" /></td>
        <td class="p-1" @click="downloadFiles()">다운로드</td>
      </tr>
      <tr class="pointer">
        <td class="p-1"><i class="fa-solid fa-share" /></td>
        <td class="p-1" @click="fileModal('moveFiles')">이동</td>
      </tr>
      <tr class="pointer">
        <td class="p-1"><i class="fa-solid fa-clipboard" /></td>
        <td class="p-1" @click="fileModal('copyFiles')">복사</td>
      </tr>
      <tr class="pointer">
        <td class="p-1"><i class="fa-solid fa-trash" /></td>
        <td class="p-1 pointer" @click="fileModal('deleteFiles')">삭제</td>
      </tr>
    </table>

    <table v-else-if="setting.checkedFiles.length === 1">
      <tr>
        <td class="p-1"><i class="fa-solid fa-star" /></td>
        <td class="p-1">즐겨찾기</td>
      </tr>
      <tr>
        <td class="p-1"><i class="fa-solid fa-pen-to-square" /></td>
        <td class="p-1 pointer" @click="fileModal('changeName')">이름바꾸기</td>
      </tr>
      <tr>
        <td class="p-1"><i class="fa-solid fa-share-nodes" /></td>
        <td class="p-1 pointer" @click="shareFile">공유</td>
      </tr>
      <tr>
        <td colspan="2" class="p-2">
          <div class="btn-group">
            <button type="button" class="btn btn-outline-secondary" @click="downloadFiles()"><i class="fa-solid fa-cloud-arrow-down" /></button>
            <button type="button" class="btn btn-outline-secondary" @click="fileModal('moveFiles')"><i class="fa-solid fa-share" /></button>
            <button type="button" class="btn btn-outline-secondary" @click="fileModal('copyFiles')"><i class="fa-solid fa-clipboard" /></button>
            <button type="button" class="btn btn-outline-secondary" @click="fileModal('deleteFiles')"><i class="fa-solid fa-trash" /></button>
          </div>
        </td>
      </tr>
    </table>
  </div>

  <!-- File List -->
  <FileList
    :form="form"
    :setting="setting"
    :fileList="fileList"
    :imageThumbnail="imageThumbnail"
    :extensions="extensions"
  />

  <!-- Modal -->
  <FileModal
    v-if="modalOn"
    @close="closeModal"
    :form="form"
    :setting="setting"
    :modalFunc="modalFunc"
    :modalBody="modalBody"
    :extensions="extensions"
  />

</template>

<script setup>
import { onMounted, reactive, ref, provide, watch, computed } from 'vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import { imageThumbnail, extensions } from '@/assets/extensions'
import FileList from './FileList.vue'
import FileModal from './FileModal.vue'
import Const from '@/const'

const route = useRoute()

const form = reactive({
    shareLink: '',
    path: '',
    sort: 'name',
    order: 'asc',
    keyword: '',
    viewHidden: false,
})

const shareAuth = ref(0)

const setting = reactive({
  loadingList: false,
  nowPath: '',
  search: false,
  checkedFiles: [],
})

const homePath = reactive({
  index: 0,
  parents: []
})

const fileList = ref([])

const modalOn = ref(false)
const modalFunc = ref('')
const modalBody = ref({})

/*------------------------ compute ------------------------*/

const checkAllFile = computed({
  get() {
    return setting.checkedFiles.length === fileList.value.length
  },
  set(value) {
    setting.checkedFiles = []
    value ? fileList.value.forEach((li)=>setting.checkedFiles.push(li.name)) : []
  }
})

const tempImg = computed({
  get() {
    const tempMap = fileList.value.filter(obj => obj.name === setting.checkedFiles[0])[0]
    return tempMap ? imgSelector(tempMap.extension, tempMap.isHidden, tempMap.path) : ''
  }
})

onMounted(() => {
  getFileList()
})

watch(() => route.fullPath, (to, from)=>{
  if(from !== to) {
    getFileList()
  }
})

/*------------------------ functions ------------------------*/

const openModal = () => { modalOn.value = true }
const closeModal = (reload, checkedFiles) => {
  modalOn.value = false
  if(reload) getFileList(checkedFiles)
}

const sortArrow = (sort) => { return (sort==form.sort) ? ((form.order == 'asc') ? '↑' : '↓') : '' }

const loadFileList = (shareLink, path, sort, order, keyword, resetKeyword = false) => {
  form.shareLink  = shareLink || form.shareLink
  form.path       = path      || form.path
  form.sort       = sort      || form.sort
  form.order      = order     || form.order
  form.keyword    = keyword   || resetKeyword ? '' : form.keyword

  router.push({
    path: '/file',
    query: {
      shareLink: form.shareLink,
      path: form.path,
      sort: form.sort,
      order: form.order,
      keyword: form.keyword
    }
  })
}

const getFileList = async (checkedFiles = []) =>{
  setting.loadingList = true

  const urlParams = new URLSearchParams(window.location.search)

  form.shareLink  = urlParams.get('shareLink')  || ''
  form.path       = urlParams.get('path')       || ''
  form.sort       = urlParams.get('sort')       || 'name'
  form.order      = urlParams.get('order')      || 'asc'
  form.keyword    = urlParams.get('keyword')    || ''

  setting.search = !!form.keyword

  await $http.post('/file/fileList', form, null)
        .then((response) => {
          // if you are admin, remove shareLink
          if(!response.data?.option?.shareLink) {
            form.shareLink = ''
            form.path = response.data?.option.path
          }
          if(response.data) {
            homeLink(form.shareLink, response.data.option.nowPath)
            shareAuth.value = response.data.option.shareAuth
            fileList.value = response.data.lists.map(li => {
                                return {
                                  ...li,
                                  extension: li.isDirectory ? 'folder' : extensions.hasOwnProperty(li.extension) ? li.extension : 'default'
                                }
                              })
            $store.dispatch('link/addSiteHtml')
          } else if(response.code === Const.RESPONSE_TYPE.INVALID_PATH) {
            setTimeout(() => router.go(-1), 2000)
          } else if(response.code === Const.RESPONSE_TYPE.NEED_PASSWORD) {
            fileModal('needPassword')
          }
          setting.loadingList = false
        })

  setting.checkedFiles = checkedFiles
}

const homeLink = (shareLink, nowPath) => {
  homePath.parents = []
  const parents = nowPath.replace(/\\/g, '/').split('/')
  let link = ''

  for(let i=0; i < parents.length; i++) {
    link += parents[i] + '/'
    if(!parents[0]) parents[0] = 'HOME'
    if(parents[i]) homePath.parents.push({link: link, name: parents[i]})
  }

  homePath.index = homePath.parents.length -1
}

const toggleHiddenCheck = () => {
  form.viewHidden = !form.viewHidden
  getFileList()
}

const imgSelector = (extension, isHidden, path) => {
  if(imageThumbnail.hasOwnProperty(extension) && !isHidden) {
    return (import.meta.env.VITE_SERVER_BASE_URL) + '/file/thumbnailMaker?shareLink=' + form.shareLink + '&name=' + encodeURIComponent(path.replace(/\\/g, '/'))
  } else {
    return '/img/fileicons/' + extensions[extension] + '.png'
  }
}

/*------------------------ File Control Menu Functions ------------------------*/

const downloadFiles = () => {
  window.open((import.meta.env.VITE_SERVER_BASE_URL) + '/file/fileDownload?path=' + encodeURIComponent(form.path) + '&fileNames=' + encodeURIComponent(setting.checkedFiles.join(':/:')) + '&shareLink=' + form.shareLink)
}

const shareFile = () => {
  const params = {
    path: form.path,
    shareLink: form.shareLink,
    name: setting.checkedFiles[0]
  }

  $http.post('/share/shareNewFile', params, null)
    .then((response) => {
      if(response.data) {
        modalBody.value = { shareLink: response.data }
        fileModal('shareFile')
      }
    })
}

const showImg = (e) => {
  if(e.target.src.includes('thumbnailMaker')) {
    modalBody.value = { image: e.target.src }
    fileModal('showImg')
  }
}

const fileModal = (type) => {
  modalFunc.value = type
  openModal()
}

// pass to child component
provide('loadFileList', loadFileList)
provide('imgSelector', imgSelector)
provide('getFileList', getFileList)

</script>

<style>
@import '@/assets/css/file.css';
</style>
