<template>
  <div>
    <div class="text-success pt-5">$http - api받아오기</div>
    <button @click="getApi">
      Execute
    </button>
    <div>Loading: {{ isLoading.toString() }}</div>
    <div>Finished: {{ isFinished.toString() }}</div>
  </div>

  <div>
    <div class="text-success pt-5">qr코드 생성</div>
    <input v-model="qrCodeText" @keyup="qrImgChange" />
    <div>
      <img :src="qrCodeLink"/>
      <br>qrCodeLink : {{ qrCodeLink }}
    </div>
  </div>
</template>

<script>
import { useQRCode } from '@vueuse/integrations/useQRCode'

export default {
  name: 'SampleMenu',
  data() {
    return {
      isLoading: '',
      isFinished: '',

      qrCodeText: 'test',
      qrCodeLink: '',
    }
  },
  mounted() {
    this.qrImgChange()
  },
  methods: {
    getApi() {
      this.$http.get('/fileList')
        .then((response) => {
          console.log("$http response : ")
          alert(JSON.stringify(response))
          console.log(response)
        })
    },
    qrImgChange() {
      this.qrCodeLink = useQRCode(this.qrCodeText)
    }
  }
}

</script>