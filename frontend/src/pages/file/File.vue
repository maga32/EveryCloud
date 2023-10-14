<template>
  <div class="row fixed-top" id="fileMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded border p-2 m-0 bg-light-subtle">

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

        <div class="col-12 col-md-4 row text-end">
          <div class="col-4 d-md-none"></div>
          <div class="col-6 col-md-8 px-2">
            <input type="text" class="w-100 border border-secondary rounded-5 px-2" id="keyword" v-model="form.keyword" @keyup.enter="loadFileList('','','','')">
          </div>
          <div class="col-1 col-md-2 pointer" @click="loadFileList('','','','')"><i class="fa-solid fa-magnifying-glass" /></div>
          <div class="col-1 col-md-2 ps-2">
            <dropdown-menu withDropdownCloser direction="right">
              <template #trigger>
                <i class="pointer fa-solid fa-wrench" />
              </template>
              <template #body>
                <ul class="dropdown-menu show" dropdown-closer style="right:50px">
                  <li><div class="dropdown-item pointer" @click="toggleHiddenCheck">
                    <span id="viewHiddenCheck" :class="form.viewHidden || 'inactive'"><i class="fa-solid fa-check" /></span>
                    <i class="fa-solid fa-file-shield pe-2" />숨김파일보기
                  </div></li>
                  <li><div class="dropdown-item pointer" onClick="newFolder()">
                    <i class="fa-solid fa-folder-plus pe-2" />새폴더
                  </div></li>
                  <li><div class="dropdown-item pointer" onClick="newFile()">
                    <i class="fa-solid fa-file-circle-plus pe-2" />새파일
                  </div></li>
                </ul>
              </template>
            </dropdown-menu>
          </div>
        </div>

        <table class="w-100">
          <tr>
            <td class="text-center" style="width:35px;"><input type="checkbox" class="form-check-input" v-model="setting.checkAllFile"></td>
            <td style="width:20px;"></td>
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
  index: 0,
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

  form.shareLink  = urlParams.get('shareLink')  || ''
  form.path       = urlParams.get('path')       || ''
  form.sort       = urlParams.get('sort')       || 'name'
  form.order      = urlParams.get('order')      || 'asc'
  form.keyword    = urlParams.get('keyword')    || ''

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
