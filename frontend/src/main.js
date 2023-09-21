import './assets/css/common.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import HttpModule from './modules/http'

/* vuex */
window.$store = store

createApp(App)
  .use(HttpModule)  // axios
  .use(router)      // router
  .mount('#app')
