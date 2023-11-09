<template>
  <div>
    <div class="modal-backdrop fade show"></div>
    <transition name="modal" appear>
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

              <div v-if="modalFunc === 'shareList'">
                <div class="row">
                  <div class="col-12">
                    <div class="form-check me-3" style="display:inline-block">
                      <input class="form-check-input" type="radio" v-model="useExternalUrl" :value="true" name="useUrl" id="externalUrl">
                      <label class="form-check-label" for="externalUrl">
                        기본주소
                      </label>
                    </div>
                    <div class="form-check" style="display:inline-block">
                      <input class="form-check-input" type="radio" v-model="useExternalUrl" :value="false" name="useUrl" id="nowUrl">
                      <label class="form-check-label" for="nowUrl">
                        현재주소
                      </label>
                    </div>
                  </div>
                  <div class="col-12 mb-1">
                    Link :
                    <div style="word-break:break-all">{{ fullLink }}</div>
                    <div>
                      <button class="btn btn-sm btn-secondary me-2" @click="copyShareLink">복사</button>
                      <button class="btn btn-sm btn-secondary me-2" @click="QRShow = !QRShow">QR code</button>
                      <img v-show="QRShow" :src="QRCodeSrc" class="pt-2">
                    </div>
                  </div>
                  <div class="col-12 my-2 border rounded py-2">
                    <table class="w-100">
                      <tr>
                        <td style="min-width: 90px">링크 수정 : </td>
                        <td colspan="2"><input class="form-control-sm" v-model="share[0].link"></td>
                      </tr>
                      <tr>
                        <td>경로 수정 : </td>
                        <td>{{ share[0].path }}</td>
                        <td><button class="btn btn-sm btn-outline-secondary">select</button></td>
                      </tr>
                    </table>
                  </div>
                </div>
                <input type="text" class="float-end" :value="fullLink" id="fullLink" style="opacity:0; width:10px; height:0px;">
              </div>

            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="$emit('close', false)">취소</button>
              <button type="button" class="btn btn-primary" @click="submit">확인</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import Const from '@/const'
import Swal from 'sweetalert2'
import Utils from '@/modules/utils'
import { useQRCode } from '@vueuse/integrations/useQRCode'

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

const shareGroup = ref([{
  shareLink: '',
  groupNo: '',
  groupName: '',
  auth: '',
}])

const externalUrl = ref('')
const nowUrl = ref('')
const useExternalUrl = ref(true)

const origLink = ref('')

const QRLink = ref('')
const QRCodeSrc = useQRCode(QRLink)
const QRShow = ref(false)

onMounted(()=> {
  nowUrl.value = window.location.origin

  if(props.modalFunc === 'shareList') {
    console.log('body', props.modalBody.link)
    $http.post('/share/shareInfo', props.modalBody, null)
      .then((response) => {
        console.log('res : ', response)
        shareGroup.value  = response.data.lists
        share.value[0]    = response.data.option.share
        origLink.value    = response.data.option.share.link
        externalUrl.value = response.data.option.externalUrl
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


/*-------------------- modal submit --------------------*/
const submit = async () => {
  let result = false

  // example
  if(props.modalFunc === 'newFile') {

    result = true
  }

  if(!result) return false

  emit('close', true)
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