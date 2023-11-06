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

  <!-- Share Control Menu -->
  <div class="position-fixed shadow border border-secondary rounded bg-body-tertiary p-3" :class="setting.checkedFiles.length > 0 ? 'active': 'inactive'" id="fileControlMenu">
    <template v-if="setting.checkedFiles.length > 1">
      <div class="px-1"> {{ setting.checkedFiles.length }} 개의 파일 선택</div>
      <div class="text-center px-2 py-3"><img src="/img/fileicons/files.png"></div>
    </template>
    <template v-else-if="setting.checkedFiles.length === 1">
      <div class="px-1" style="word-break:break-all;">{{ setting.checkedFiles[0] }}</div>
      <div class="text-center px-2 py-3"><img :src="tempImg" style="min-width:64px;" @click="showImg"></div>
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
        <td class="p-1" @click="shareModal('moveFiles')">이동</td>
      </tr>
      <tr class="pointer">
        <td class="p-1"><i class="fa-solid fa-clipboard" /></td>
        <td class="p-1" @click="shareModal('copyFiles')">복사</td>
      </tr>
      <tr class="pointer">
        <td class="p-1"><i class="fa-solid fa-trash" /></td>
        <td class="p-1 pointer" @click="shareModal('deleteFiles')">삭제</td>
      </tr>
    </table>

    <table v-else-if="setting.checkedFiles.length === 1">
      <tr>
        <td class="p-1"><i class="fa-solid fa-star" /></td>
        <td class="p-1">즐겨찾기</td>
      </tr>
      <tr>
        <td class="p-1"><i class="fa-solid fa-pen-to-square" /></td>
        <td class="p-1 pointer" @click="shareModal('changeName')">이름바꾸기</td>
      </tr>
      <tr>
        <td class="p-1"><i class="fa-solid fa-share-nodes" /></td>
        <td class="p-1 pointer" @click="shareFile">공유</td>
      </tr>
      <tr>
        <td colspan="2" class="p-2">
          <div class="btn-group">
            <button type="button" class="btn btn-outline-secondary" @click="downloadFiles()"><i class="fa-solid fa-cloud-arrow-down" /></button>
            <button type="button" class="btn btn-outline-secondary" @click="shareModal('moveFiles')"><i class="fa-solid fa-share" /></button>
            <button type="button" class="btn btn-outline-secondary" @click="shareModal('copyFiles')"><i class="fa-solid fa-clipboard" /></button>
            <button type="button" class="btn btn-outline-secondary" @click="shareModal('deleteFiles')"><i class="fa-solid fa-trash" /></button>
          </div>
        </td>
      </tr>
    </table>
  </div>


  <!-- tabs -->
  <ShareList
      v-if="tab==='shareList'"
      :form="form"
      :setting="setting"
      :shareList="shareList"
      :imageThumbnail="imageThumbnail"
      :extensions="extensions"
  />

  <!-- Modal -->
  <ShareModal
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
import ShareList from './ShareList.vue'
import ShareModal from './ShareModal.vue'
import Const from "@/const";

const route = useRoute()

const tab = ref('shareList')

const form = reactive({
    shareLink: '',
    path: '',
    sort: 'name',
    order: 'asc',
    keyword: '',
    viewHidden: false,
})

const shareAuth = ref(0);

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

const shareList = ref([])

const modalOn = ref(false)
const modalFunc = ref('')
const modalBody = ref({})

/*------------------------ compute ------------------------*/

const checkAllFile = computed({
  get() {
    return setting.checkedFiles.length === shareList.value.length
  },
  set(value) {
    setting.checkedFiles = []
    value ? shareList.value.forEach((li)=>setting.checkedFiles.push(li.name)) : []
  }
})

const tempImg = computed({
  get() {
    const tempMap = shareList.value.filter(obj => obj.name === setting.checkedFiles[0])[0]
    return tempMap ? imgSelector(tempMap.extension, tempMap.isHidden, tempMap.path) : ''
  }
})

onMounted(() => {
  getShareList()
})

watch(() => route.fullPath, (to, from)=>{
  if(from !== to) {
    getShareList()
  }
})

/*------------------------ functions ------------------------*/

const openModal = () => { modalOn.value = true }
const closeModal = (reload, checkedFiles) => {
  modalOn.value = false
  if(reload) getShareList(checkedFiles)
}

const sortArrow = (sort) => { return (sort==form.sort) ? ((form.order == 'asc') ? '↑' : '↓') : '' }

const loadShareList = (shareLink, path, sort, order, keyword, resetKeyword = false) => {
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

const getShareList = async (checkedFiles = []) =>{
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
            shareList.value = response.data.lists.map(li => {
                                return {
                                  ...li,
                                  extension: li.isDirectory ? 'folder' : extensions.hasOwnProperty(li.extension) ? li.extension : 'default'
                                }
                              })
            $store.dispatch('link/addSiteHtml')
          } else if(response.code === Const.RESPONSE_TYPE.INVALID_PATH) {
            setTimeout(() => router.go(-1), 2000)
          } else if(response.code === Const.RESPONSE_TYPE.NEED_PASSWORD) {
            shareModal('needPassword')
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
  getShareList()
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
  window.open((import.meta.env.VITE_SERVER_BASE_URL) + '/file/fileDownload?path=' + encodeURIComponent(form.path) + '&fileNames=' + encodeURIComponent(setting.checkedFiles.join(':/:')) + '&shareLink=' + form.shareLink);
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
        shareModal('shareFile')
      }
    })
}

const showImg = (e) => {
  if(e.target.src.includes('thumbnailMaker')) {
    modalBody.value = { image: e.target.src }
    shareModal('showImg')
  }
}

const shareModal = (type) => {
  modalFunc.value = type
  openModal()
}

// pass to child component
provide('loadShareList', loadShareList)
provide('imgSelector', imgSelector)
provide('getShareList', getShareList)

</script>

<style>
@import '@/assets/css/file.css';
</style>
