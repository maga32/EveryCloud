import axios from 'axios'
// import Utils from '@/modules/utils'
import Swal from 'sweetalert2'
import Const from '@/const'
const { VITE_SERVER_BASE_URL } = import.meta.env

const HttpModule = {
  install (Vue) {
    const http = axios.create({
      baseURL: VITE_SERVER_BASE_URL,
      // crossDomain: true,
      // withCredentials: true
    })

    /*------------- request ------------- */
    http.interceptors.request.use((config) => {
      // window.$loading.show()

      config.headers.Authorization = window.$store.getters['User/getToken']
      config.headers.Authorization = localStorage.getItem('token')

      // header 에 menuId를 저장한다.
      config.headers ["MENU_ID"] = window.$store.getters['Menu/getCurMenuId']

      return config
    }, (error) => {
      // window.$loading.hide()

      Swal.fire({ icon: 'error', text: error })
    })

    /*------------ response ------------ */
    http.interceptors.response.use((response) => {
      // window.$loading.hide ()

      if (response.status != Const.RESPONSE_TYPE.SUCCESS) {
        // throw new Error (response)
        Swal.fire({ icon: 'error', text: response })
        return new Promise(() => {})
      }

      if(response.headers.get("Authorization")) {
        window.$store.dispatch('User/setToken', response.headers.get("Authorization"))
      }
      return response.data

    }, (error) => {
      // window.$loading.hide()

      if (error.response) {
        if (error.response.status == 401) {
          window.$store.dispatch('User/setToken', '')
        }
        Swal.fire({ icon: 'error', text: error.response.data.message })
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