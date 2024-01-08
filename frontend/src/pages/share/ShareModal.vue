<template>
  <div>
    <div class="modal-backdrop fade show"></div>
    <transition name="modal" appear>
      <Form>
        <div @click.self="$emit('close', false)" class="modal fade show" style="display: block" id="functionModal">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
            <div class="modal-content">

              <div class="modal-header">
                <h1 class="modal-title fs-5 text-break w-100 pe-3" id="functionModalLabel">
                  {{ functionModalLabel[modalFunc] }}

                  <template if="modalFunc === 'showImg'">
                    <span>
                      <i class="btn btn-outline-secondary fa-solid fa-minus ms-2" />
                    </span>
                  </template>

                </h1>
                <button type="button" class="btn-close" @click="$emit('close', false)"></button>
              </div>

              <div class="modal-body" id="functionModalBody" @keyup.enter="submit">

                <!-- shareList START -->
                <div v-if="modalFunc === 'shareList'">
                  <div class="row">
                    <div class="col-12">
                      <div class="form-check me-3" style="display:inline-block">
                        <input class="form-check-input" type="radio" v-model="useExternalUrl" :value="true" name="useUrl" id="externalUrl">
                        <label class="form-check-label pointer" for="externalUrl">
                          기본주소
                        </label>
                      </div>
                      <div class="form-check" style="display:inline-block">
                        <input class="form-check-input" type="radio" v-model="useExternalUrl" :value="false" name="useUrl" id="nowUrl">
                        <label class="form-check-label pointer" for="nowUrl">
                          현재주소
                        </label>
                      </div>
                    </div>
                    <div class="col-12 mb-1">
                      Link :
                      <div class="text-break-all">{{ fullLink }}</div>
                      <div>
                        <input type="text" class="visually-hidden-focusable" :value="fullLink" id="fullLink">
                        <button class="btn btn-sm btn-secondary me-2" @click="copyShareLink">복사</button>
                        <button class="btn btn-sm btn-secondary me-2" @click="QRShow = !QRShow">QR code</button>
                        <img v-show="QRShow" :src="QRCodeSrc" class="pt-2">
                      </div>
                    </div>

                    <div class="col-12 col-lg-6 pt-2 px-1">
                      <div class="border rounded p-2">
                        <table class="w-100">
                          <tr>
                            <td style="width: 105px">링크 수정 : </td>
                            <td colspan="2" class="py-1">
                              <Field type="text" class="form-control-sm" name="changeLink" label="링크" rules="alpha_dash|required" v-model="share[0].link"/>
                              <ErrorMessage name="changeLink" as="p" class="text-danger" />
                            </td>
                          </tr>
                          <tr>
                            <td>경로 수정 : </td>
                            <td class="py-1 text-break-all">{{ share[0].path }}</td>
                            <td><button class="btn btn-sm btn-outline-secondary ms-1" @click="console.log(share[0].date)">select</button></td>
                          </tr>
                          <tr>
                            <td>만료 기한 : </td>
                            <td class="py-1" colspan="2">
                              <div class="form-check me-3" style="display:inline-block">
                                <input class="form-check-input" type="radio" v-model="useExpire" :value="false" name="useExpire" id="notUseExpire">
                                <label class="form-check-label pointer" for="notUseExpire">
                                  미사용
                                </label>
                              </div>
                              <div class="form-check" style="display:inline-block">
                                <input class="form-check-input" type="radio" v-model="useExpire" :value="true" name="useExpire" id="useExpire">
                                <label class="form-check-label pointer" for="useExpire">
                                  사용
                                </label>
                              </div>
                            </td>
                          </tr>
                          <tr v-if="useExpire">
                            <td></td>
                            <td class="py-1" colspan="2">
                              <VueDatePicker
                                  v-model="share[0].date"
                                  auto-apply
                                  :enable-time-picker="false"
                                  :month-change-on-scroll="false"
                                  :format="timeFormat"
                                  six-weeks="append"
                                  :auto-position="false"
                                  position="right"
                                  :locale="$i18n.locale"
                                  year-first
                              />
                            </td>
                          </tr>
                        </table>
                      </div>
                    </div>
                    <div class="col-12 col-lg-6 pt-2 px-1">
                      <div class="border rounded p-2">
                        <table class="w-100">

                          <tr>
                            <td style="min-width: 90px">공유 방식 : </td>
                            <td class="py-1">
                              <div class="form-check me-3" style="display:inline-block">
                                <input class="form-check-input" type="radio" v-model="share[0].method" :value="0" name="shareMethod" id="shareByLink">
                                <label class="form-check-label pointer" for="shareByLink">
                                  링크
                                  <i class="fa-solid fa-circle-question">
                                    <tippy target="_parent">링크를 알고 있는 모든 사용자</tippy>
                                  </i>
                                </label>
                              </div>
                              <div class="form-check me-3" style="display:inline-block">
                                <input class="form-check-input" type="radio" v-model="share[0].method" :value="1" name="shareMethod" id="shareByPass">
                                <label class="form-check-label pointer" for="shareByPass">
                                  암호
                                  <i class="fa-solid fa-circle-question">
                                    <tippy target="_parent">비밀번호 입력 필요</tippy>
                                  </i>
                                </label>
                              </div>
                              <div class="form-check" style="display:inline-block">
                                <input class="form-check-input" type="radio" v-model="share[0].method" :value="2" name="shareMethod" id="shareByGroup">
                                <label class="form-check-label pointer" for="shareByGroup">
                                  그룹
                                  <i class="fa-solid fa-circle-question">
                                    <tippy target="_parent">그룹별 공유 지정</tippy>
                                  </i>
                                </label>
                              </div>
                            </td>
                          </tr>

                          <tr v-if="share[0].method !== 2">
                            <td style="width: 105px">유저 권한 : </td>
                            <td class="py-1" colspan="2">
                              <div class="form-check me-3" style="display:inline-block">
                                <input class="form-check-input" type="radio" v-model="share[0].auth" :value="0" name="shareAuth" id="shareAuthDown">
                                <label class="form-check-label pointer" for="shareAuthDown">
                                  다운로드
                                </label>
                              </div>
                              <div class="form-check" style="display:inline-block">
                                <input class="form-check-input" type="radio" v-model="share[0].auth" :value="1" name="shareAuth" id="shareAuthUpdate">
                                <label class="form-check-label pointer" for="shareAuthUpdate">
                                  수정
                                </label>
                              </div>
                            </td>
                          </tr>

                          <!-- password share -->
                          <tr v-if="share[0].method === 1">
                            <td class="pt-3">비밀번호 : </td>
                            <td class="pt-3">
                              <input type="password" class="form-control-sm" v-model="sharePass">
                            </td>
                          </tr>

                          <!-- group share -->
                          <tr v-if="share[0].method === 2">
                            <td colspan="2">
                              <div class="rounded border p-2 mt-3">
                                <table class="w-100">

                                  <tr>
                                    <td class="py-2" colspan="4">
                                      <div class="hstack gap-2 d-flex justify-content-end align-items-center">
                                        <input type="text" class="border border-secondary rounded-5 px-2" placeholder="Filter" v-model="searchFilter">
                                      </div>
                                    </td>
                                  </tr>
                                  <tr class="border-bottom text-center fw-bold">
                                    <td class="pt-2 pb-3">그룹명</td>
                                    <td class="pt-2 pb-3">권한없음</td>
                                    <td class="pt-2 pb-3">다운로드</td>
                                    <td class="pt-2 pb-3">수정</td>
                                  </tr>

                                  <!-- group list -->
                                  <tr v-for="(li, i) in searchGroup.slice((paging.page-1) * paging.size, paging.page * paging.size)" class="border-bottom">
                                    <td class="ps-1 py-2 text-break-all">
                                      {{ shareGroup[li.no].groupName }}
                                    </td>
                                    <td class="text-center">
                                      <div class="form-check" style="display:inline-block">
                                        <input class="form-check-input" type="radio" v-model="shareGroup[li.no].auth" :value="null" :name="'groupAuth'+i">
                                      </div>
                                    </td>
                                    <td class="text-center">
                                      <div class="form-check" style="display:inline-block">
                                        <input class="form-check-input" type="radio" v-model="shareGroup[li.no].auth" :value="0" :name="'groupAuth'+i">
                                      </div>
                                    </td>
                                    <td class="text-center">
                                      <div class="form-check" style="display:inline-block">
                                        <input class="form-check-input" type="radio" v-model="shareGroup[li.no].auth" :value="1" :name="'groupAuth'+i">
                                      </div>
                                    </td>
                                  </tr>

                                  <!-- paging -->
                                  <tr>
                                    <td colspan="4">
                                      <ul class="pagination pagination-sm d-flex justify-content-center mt-3 mb-2">

                                        <li class="page-item">
                                          <div class="page-link pointer" @click="paging.page=pagingNav.first">
                                            <i class="fa-solid fa-angles-left" />
                                          </div>
                                        </li>
                                        <li class="page-item">
                                          <div class="page-link pointer" @click="paging.page=pagingNav.before">
                                            <i class="fa-solid fa-angle-left" />
                                          </div>
                                        </li>

                                        <li class="page-item" v-for="li in pageList">
                                          <div class="page-link pointer" @click="paging.page=li" :class="paging.page!==li || 'bg-secondary-subtle'">{{ li }}</div>
                                        </li>

                                        <li class="page-item">
                                          <div class="page-link pointer" @click="paging.page=pagingNav.after">
                                            <i class="fa-solid fa-angle-right" />
                                          </div>
                                        </li>
                                        <li class="page-item">
                                          <div class="page-link pointer" @click="paging.page=pagingNav.last">
                                            <i class="fa-solid fa-angles-right" />
                                          </div>
                                        </li>

                                      </ul>
                                    </td>
                                  </tr>

                                </table>
                              </div>
                            </td>
                          </tr>
                          <!-- group share -->

                        </table>
                      </div>
                    </div>

                  </div>
                </div>
                <!-- shareList END -->


                <!-- shareGroup START -->
                <!-- shareGroup END -->

                <!-- shareLog START -->
                <!-- shareLog END -->

              </div>

              <div class="modal-footer">
                <button type="button" class="btn btn-danger me-4" @click="deleteFunction">삭제</button>
                <button type="button" class="btn btn-secondary" @click="$emit('close', false)">취소</button>
                <button type="button" class="btn btn-primary" @click="submit">확인</button>
              </div>
            </div>
          </div>
        </div>
      </Form>
    </transition>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import Swal from 'sweetalert2'
import { useQRCode } from '@vueuse/integrations/useQRCode'
import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import dayjs from 'dayjs'
import { Tippy } from 'tippy.vue'
import Const from "@/const";

const props = defineProps(['tab', 'modalFunc', 'modalBody', 'setting'])
const emit = defineEmits(['close'])

const functionModalLabel = {
  shareList: '파일공유',
}

const share = ref([{
  link: '',
  path: '',
  date:'',
  method: '',
  pass: '',
  auth: '',
  exist: '',
}])

const sharePass = ref('')

const shareGroup = ref([{
  shareLink: '',
  groupNo: '',
  groupName: '',
  auth: '',
}])

const paging = ref({
  page: 1,
  size: 5,
  total: 0,
  pageSize: 5,
})

const searchFilter = ref('')
const searchGroup = computed({
  get() {
    let list = []
    for(let i = 0; i < shareGroup.value.length; i++) {
      if(shareGroup.value[i].groupName.includes(searchFilter.value)) {
          list.push({
            no: i,
            shareLink: shareGroup.value[i].shareLink,
            groupNo: shareGroup.value[i].groupNo,
            groupName: shareGroup.value[i].groupName,
            auth: shareGroup.value[i].auth,
          })
        }
    }
    paging.value.total = list.length

    return list
  }
})

// temp for test.. it will be deleted
const testGroup = () => {
  let group = []
  for(let i=1; i <= 54; i++) {
    group.push({
      shareLink: null,
      groupNo: i,
      groupName: i + '_name',
      auth: i % 3 === 0 ? 0 : i % 3 === 1 ? 1 : null
    })
  }
  return group
}

// paging component
const pagingNav = ref({
  first: 1,
  last: 1,
  before: 1,
  after: 1,
})

// paging component
const pageList = computed({
  get() {
    const last  = Math.ceil(paging.value.total / paging.value.size) || 1
    const start = Math.floor((paging.value.page-1) / paging.value.pageSize) * paging.value.pageSize + 1
    const end   = last < (start + paging.value.pageSize) ? last : start + paging.value.pageSize - 1

    pagingNav.value.last    = last
    pagingNav.value.before  = (start !== 1) ? (start - 1) : 1
    pagingNav.value.after   = (end < last) ? (end + 1) : last

    let list = []
    for (let i = start; i <= end; i++) { list.push(i) }

    return list
  }
})

const origLink = ref('')

const externalUrl = ref('')
const nowUrl = ref('')
const useExternalUrl = ref(true)
const useExpire = ref(false)

const QRLink = ref('')
const QRCodeSrc = useQRCode(QRLink)
const QRShow = ref(false)

onMounted(()=> {
  nowUrl.value = window.location.origin

  if(props.modalFunc === 'shareList') {
    $http.post('/share/shareInfo', props.modalBody, null)
      .then((response) => {
        if(response.data) {
          console.log('res : ', response)
          shareGroup.value = response.data.lists
          paging.value.total = response.data.lists.length
shareGroup.value = testGroup()
paging.value.total = shareGroup.value.length
          share.value[0] = response.data.option.share
          origLink.value = response.data.option.share.link
          useExpire.value = !!response.data.option.share.date
          externalUrl.value = response.data.option.externalUrl
        }
      })
  }
})

const fullLink = computed({
  get() {
    const result = (useExternalUrl.value ? externalUrl.value : nowUrl.value) + '/file?shareLink=' + share.value[0].link
    QRLink.value = result
    return result
  }
})

/*------------------------ functions ------------------------*/

const copyShareLink = async() => {
  document.querySelector('#fullLink').select()
  document.execCommand('copy')
  Swal.fire({ icon: 'success', text: '링크가 복사되었습니다.', timer: 1200, showConfirmButton: false })
}

const timeFormat = (time) => {
  return dayjs(time).format('YYYY/MM/DD')
}

const shareGroupList = () => {
  let list = []
  for(let i = 0; i < shareGroup.value.length; i++) {
    if(shareGroup.value[i].auth != null) {
      list.push({
        shareLink: share.value[0].link,
        groupNo: shareGroup.value[i].groupNo,
        auth: shareGroup.value[i].auth,
      })
    }
  }
  paging.value.total = list.length

  return list
}
/*-------------------- modal submit --------------------*/
const submit = async () => {
  let result = false

  if(props.modalFunc === 'shareList') {
    const params = {
      origLink        : origLink.value,
      link            : share.value[0].link,
      path            : share.value[0].path,
      method          : share.value[0].method,
      date            : (useExpire.value ? share.value[0].date : null),
      auth            : share.value[0].auth,
      pass            : ((share.value[0].method === 1) ? sharePass.value : null),
      shareGroupList  : ((share.value[0].method === 2) ? shareGroupList() : null),
    }

    await $http.post('/share/shareUpdate', params, null)
      .then((response) => {
        if(response) result = true
      }
    )
  }

  if(!result) return false

  emit('close', result)
}

const deleteFunction = () => {
  Swal.fire({
    icon: 'error',
    text: '삭제하시겠습니까?',
    showConfirmButton: false,
    showCancelButton: true,
    showDenyButton: true,
    denyButtonText: '삭제',
    cancelButtonText: '취소',
  }).then((result) => {
    if (result.isDenied) {
      Swal.fire({ icon: 'success', text: '삭제되었습니다.', timer: 1200, showConfirmButton: false })
      emit('close', true)
    }
  })
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

.page-link {
  color: unset
}
</style>