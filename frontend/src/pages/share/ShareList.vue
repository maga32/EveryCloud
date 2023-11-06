<template>
  <!-- filter -->
  <div class="row fixed-top" id="shareMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded-bottom border border-top-0 p-2 m-0 bg-light-subtle">
        <div class="col-5"></div>
        <div class="col-6 px-2">
          <input type="text" class="w-100 border border-secondary rounded-5 px-2" placeholder="Filter" id="keyword" v-model="form.keyword" @keyup.enter="loadShareList('','','','')">
        </div>
        <div class="col-1 pointer" @click="loadShareList('','','','')"><i class="fa-solid fa-magnifying-glass" /></div>
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
    <div id="shareList">

      <label v-for="li in shareList" class="w-100 pe-1 pe-md-5" @click="labelClick">
        <table class="w-100 rounded fileTable" :class="!setting.checkedFiles.includes(li.name) || 'checked'">
          <tr>
            <td v-show="!setting.search" class="text-center" style="width:35px;">
              <input v-if="setting.search" type="hidden" class="li_parent" :value="li.parent">
              <input v-else type="checkbox" class="form-check-input checkFile" v-model="setting.checkedFiles" :value="li.name">
            </td>
            <td class="text-center py-2" style="width:80px;">
              <img class="fileImg" :src="imgSelector(li.extension, li.isHidden, li.path)" :style="li.isHidden ? 'opacity:0.3;' : ''" style="max-width:64px" loading="lazy">
            </td>
            <td class="w-auto">
              <div>
                <span v-if="li.extension !== 'folder'" class="fileName" style="word-break:break-all">
                  {{ li.name }}
                </span>
                <span v-else class="fileName pointer" @click="loadShareList('', li.path.replace(/\\/g, '/'),'','','',true)" style="word-break:break-all">
                  {{ li.name }}
                </span>
              </div>
              <div class="text-gray d-flex align-items-center">
                <div class="flex-shrink-0" style="width:60px;">
                  {{ li.extension === 'folder' ? '-' : Utils.fileSize(li.size) }}
                </div>
                <div class="flex-grow-1" style="word-break:break-all; flex-basis: 110px; padding: 0 10px;">
                  <div v-if="setting.search" style="min-width:55px; font-size:10px;">
                    {{ (!!form.shareLink ? 'shareLink : ' + form.shareLink + ' / ' : '') + li.path }}
                  </div>
                  <div v-else style="min-width:55px; text-align: right;">
                    {{ extensions[li.extension] }}
                  </div>
                </div>
                <div style="word-break:keep-all; text-align:right">
                  {{ dayjs(li.date).format('YY/MM/DD HH:mm') }}
                </div>
              </div>
            </td>
          </tr>
        </table>
      </label>

    </div>

  </div>
</template>

<script setup>
import {computed, inject, onUpdated} from 'vue'
import Utils from '@/modules/utils'
import dayjs from 'dayjs'

const props = defineProps(['form', 'setting', 'shareList', 'imageThumbnail', 'extensions'])

const labelClick = (e) => {
  // block to show File Control Menu
  if(e.target.classList.contains('pointer') || props.setting.search) e.preventDefault()

  // if search file mode, go to path
  if(props.setting.search) loadShareList('', e.target.closest('table').querySelector('.li_parent').value, '', '', '', true)
}

// get from parent component
/**
 * @params shareLink<br> path<br> sort<br> order<br> keyword<br> resetKeyword = false
 */
const loadShareList = inject('loadShareList')
const imgSelector = inject('imgSelector')

</script>

<style>
</style>