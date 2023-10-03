import axios from 'axios'
import Swal from 'sweetalert2'
import Const from '@/const'
const { VITE_SERVER_BASE_URL } = import.meta.env

const HttpModule = {
  install (Vue) {
    const http = axios.create({
      baseURL: VITE_SERVER_BASE_URL,
    })

    /*------------- request ------------- */
    http.interceptors.request.use((config) => {
      // window.$loading.show()

      config.headers = config.headers ?? {}
      config.headers['Content-Type'] = config.data instanceof FormData ? 'multipart/form-data' : 'application/json'

      // header 에 menuId및 토큰 저장.
      // config.headers ["MENU_ID"] = window.$store.getters['Menu/getCurMenuId']
      // config.headers.Authorization = `Bearer ${sessionStorage.getItem("userToken")}`
      return config;
    }, (error) => {
      // window.$loading.hide()

      Swal.fire({ icon: 'error', text: error })
    })

    /*------------ response ------------ */
    http.interceptors.response.use((response) => {
      // window.$loading.hide ()
      if (response.data.code !== Const.RESPONSE_TYPE.SUCCESS) {
        Swal.fire({ icon: 'error', text: response.data.code + ' : ' + response.data.message })
        // return new Promise(() => {})
        return response.data
      }

      if(response.headers.get("Authorization")) {
        window.$store.dispatch('User/setToken', response.headers.get("Authorization"))
      }
      return response.data

    }, (error) => {
      // window.$loading.hide()

      if (error.response) {
        if (error.response.status === 401) {
          window.$store.dispatch('User/setToken', '')
        }
        Swal.fire({ icon: 'error', text: error.response.status + ' : ' + error.response.statusText })
        console.log(error.response)
      } else {
        Swal.fire({ icon: 'error', text: error })
      }

      //return Promise.reject(error)
      return new Promise(() => {})

    })

    window.$http = http
    Vue.httpClient = http
    Vue.config.globalProperties.$http = http
  }
}
export default HttpModule