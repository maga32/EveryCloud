import axios from 'axios'
import Swal from 'sweetalert2'
import Const from '@/const'
import router from "@/router";
const { VITE_SERVER_BASE_URL } = import.meta.env

const HttpModule = {
  install (Vue) {
    const http = axios.create({
      baseURL: VITE_SERVER_BASE_URL,
    })

    /*------------- request ------------- */
    http.interceptors.request.use((config) => {
      // config.headers = config.headers ?? {}
      // config.headers['Content-Type'] = config.data instanceof FormData ? 'multipart/form-data' : 'application/json'

      return config;
    }, (error) => {
      Swal.fire({ icon: 'error', text: error })
    })

    /*------------ response ------------ */
    http.interceptors.response.use((response) => {
      if(response.data.code !== Const.RESPONSE_TYPE.SUCCESS) {
        Swal.fire({ icon: 'error', text: response.data.code + ' : ' + response.data.message })
      }
      if(response.data.code === Const.RESPONSE_TYPE.NEED_LOGIN) {
        $store.dispatch('link/addTempSiteHtml')
        router.replace('/loginForm')
      }

      return response.data

    }, (error) => {
      if(error.response) {
        if(error.response.status === Const.RESPONSE_TYPE.INTERNAL_SERVER_ERROR) {
          Swal.fire({icon: 'error', text: '서버의 재실행이 필요합니다.'})
        } else {
          Swal.fire({icon: 'error', text: error.response.status + ' : ' + error.response.statusText})
        }
      } else {
        Swal.fire({ icon: 'error', text: error })
      }

      return new Promise(() => {})
    })

    window.$http = http
    Vue.httpClient = http
    Vue.config.globalProperties.$http = http
  }
}
export default HttpModule