<template>
  <div class="col-12" id="fileListContainer">

    <div id="loadingList" class="fixed-top row" :class="!setting.loadingList || 'act'">
      <div class="d-none d-md-block col-3"></div>
      <div class="col-12 col-md-9 px-4 text-center bg-secondary bg-opacity-10 d-flex align-items-center justify-content-center">
        <i class="fa-solid fa-circle-notch fa-spin fs-5" /><span class="ps-2 fs-4">Loading...</span>
      </div>
    </div>

    <div id="fileList" class="px-2">

      <label v-for="li in fileList" class="w-100 pe-2 pe-md-5">
        <span class="d-none">
          {{ li.extension = li.isDirectory ? 'folder' : !extensions.hasOwnProperty(li.extension) ? 'default' : li.extension }}
        </span>
        <table class="w-100 rounded fileTable">
          <tr>
            <td class="text-center" style="width:35px;">
              <input type="checkbox" class="form-check-input checkFile" name="checkedFile" :value="li.name">
            </td>
            <td class="text-center py-2" style="width:80px;">
              <img class="fileImg" v-if="imageThumbnail.hasOwnProperty(li.extension) && !li.isHidden" :src="'/api/v1/thumbnailMaker?shareLink=' + form.shareLink + '&name=' + li.path.replace(/\\/g, '/')" width="64px" loading="lazy">
              <img class="fileImg" v-else :src="'/img/fileicons/' + extensions[li.extension] + '.png'" :style="li.isHidden ? 'opacity:0.3;' : ''" loading="lazy">
            </td>
            <td class="w-auto">
              <div>
                <span v-if="li.extension !== 'folder'" class="fileName" style="word-break:break-all">
                  {{ li.name }}
                </span>
                <span v-else class="fileName pointer" @click="loadFileList(form.shareLink, li.path.replace(/\\/g, '/'),'','','',true)" style="word-break:break-all">
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

<!--    <button @click="childTest">child test</button>-->
  </div>
</template>

<script setup>
import {inject, onUpdated} from 'vue'
import Utils from '@/modules/utils'
import dayjs from 'dayjs'

const props = defineProps(['form', 'setting', 'fileList', 'imageThumbnail', 'extensions'])

function childTest() {
  console.log(props)
}

// get from parent component
const loadFileList = inject('loadFileList')

</script>

<style>
</style>