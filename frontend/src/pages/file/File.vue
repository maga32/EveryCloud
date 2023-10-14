<template>
  <div class="row fixed-top" id="fileMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded border p-2 m-0 bg-light-subtle">

        <div class="col-12 col-md-8">
          <div id="nowPath">
            <div class='d-flex'>
              <dropdown-menu withDropdownCloser direction="right">
                <template #trigger>
                  <i class="pointer fa-solid fa-wrench"></i>
                </template>

                <template #body>
                  <ul class="dropdown-menu show" dropdown-closer>
                    <li><div class="dropdown-item pointer" @click="toggleHiddenCheck">
                      <span id="viewHiddenCheck" :class="form.viewHidden || 'inactive'"><i class="fa-solid fa-check"></i></span>
                      <i class="fa-solid fa-file-shield pe-2"></i>숨김파일보기
                    </div></li>
                    <li><div class="dropdown-item pointer" onClick="newFolder()" data-bs-toggle="modal" data-bs-target="#functionModal">
                      <i class="fa-solid fa-folder-plus pe-2"></i>새폴더
                    </div></li>
                    <li><div class="dropdown-item pointer" onClick="newFile()" data-bs-toggle="modal" data-bs-target="#functionModal">
                      <i class="fa-solid fa-file-circle-plus pe-2"></i>새파일
                    </div></li>
                  </ul>
                </template>
              </dropdown-menu>
            </div>
          </div>
        </div>

        <div class="col-12 col-md-4 row text-end">
          <div class="col-4 d-md-none"></div>
          <div class="col-6 col-md-8 px-2">
            <input type="text" class="w-100 border border-secondary rounded-5 px-2" id="keyword" v-model="form.keyword">
          </div>
          <div class="col-1 col-md-2 pointer" onClick="searchFileClick()"><i class="fa-solid fa-magnifying-glass"></i></div>
          <div class="col-1 col-md-2 ps-2">
            <dropdown-menu withDropdownCloser direction="right">
              <template #trigger>
                <i class="pointer fa-solid fa-wrench"></i>
              </template>

              <template #body>
                <ul class="dropdown-menu show" dropdown-closer>
                  <li><div class="dropdown-item pointer" @click="toggleHiddenCheck">
                    <span id="viewHiddenCheck" :class="form.viewHidden || 'inactive'"><i class="fa-solid fa-check"></i></span>
                    <i class="fa-solid fa-file-shield pe-2"></i>숨김파일보기
                  </div></li>
                  <li><div class="dropdown-item pointer" onClick="newFolder()" data-bs-toggle="modal" data-bs-target="#functionModal">
                    <i class="fa-solid fa-folder-plus pe-2"></i>새폴더
                  </div></li>
                  <li><div class="dropdown-item pointer" onClick="newFile()" data-bs-toggle="modal" data-bs-target="#functionModal">
                    <i class="fa-solid fa-file-circle-plus pe-2"></i>새파일
                  </div></li>
                </ul>
              </template>
            </dropdown-menu>
          </div>
        </div>

        <table class="w-100">
          <tr>
            <td class="text-center" style="width:35px;"><input type="checkbox" class="form-check-input" v-model="setting.checkAllFile"></td>
            <td style="width:80px;"></td>
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

  <div class="position-fixed shadow border border-secondary rounded bg-body-tertiary p-3 inactive" id="fileControlMenu">
    테스트
  </div>

  <FileList
      :form="form"
      :setting="setting"
      :fileList="fileList"
      :imageThumbnail="imageThumbnail"
      :extensions="extensions"
  />

<!--  <button @click="parentTest">parent test</button>-->

  <!-- Modal -->
  <div class="modal fade" id="functionModal" tabindex="-1" aria-labelledby="functionModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5 text-break w-100 pe-3" id="functionModalLabel"></h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body" id="functionModalBody">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
          <button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="functionModalAffect()">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, provide, watch } from 'vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import { imageThumbnail, extensions } from '@/assets/extensions'
import FileList from './FileList.vue'

const route = useRoute()

const form = reactive({
    shareLink: '',
    path: '',
    sort: 'name',
    order: 'asc',
    keyword: '',
    viewHidden: false,
})

const setting = reactive({
  loadingList: false,
  nowPath: '',
  search: false,
  checkAllFile: false,
  dropdownSetting: false,
})

const homePath = reactive({
  parents: []
})

const fileList = ref([])

onMounted(() => {
  makeFileList()
})

watch(() => route.fullPath, (to, from)=>{
  if(from !== to) {
    makeFileList()
  }
})

function sortArrow(sort) {
  return (sort==form.sort) ? ((form.order == 'asc') ? '↑' : '↓') : ''
}

function loadFileList(shareLink, path, sort, order, keyword, resetKeyword = false) {
  setting.checkAllFile = false
  viewFileControlMenu()

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

async function makeFileList() {
  setting.loadingList = true

  const urlParams = new URLSearchParams(window.location.search)

  form.shareLink  = urlParams.get('shareLink')  || form.shareLink
  form.path       = urlParams.get('path')       || form.path
  form.sort       = urlParams.get('sort')       || form.sort
  form.order      = urlParams.get('order')      || form.order
  form.keyword    = urlParams.get('keyword')    || form.keyword

  setting.search = !!form.keyword

  await $http.post('/fileList', form, null)
        .then((response) => {
          pathLink(form.shareLink, response.data.option.nowPath)
          fileList.value = response.data.lists
        })
  $store.dispatch('link/addSiteHtml')

  setting.loadingList = false
}

function viewFileControlMenu() {

}

function pathLink(shareLink, nowPath) {
  const parents = nowPath.replace(/\\/g, '/').split('/')
  let link = ''

  for(let i=0; i < parents.length; i++) {
    link += parents[i] + '/'
    homePath.parents.push({link: link, name: parents[i]})
  }
}

function toggleHiddenCheck() {
  form.viewHidden = !form.viewHidden
  makeFileList()
}

// pass to child component
provide('loadFileList', loadFileList)

</script>

<style>
@import '@/assets/css/file.css';
</style>
