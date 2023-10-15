import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import HttpModule from './modules/http'
import { Form, Field, ErrorMessage } from 'vee-validate'
import DropdownMenu from 'v-dropdown-menu'
import './modules/vee-validation'

createApp(App)
  .use(HttpModule)                          // axios
  .use(router)                              // router
  .use(store)                               // store
  .use(DropdownMenu)                        // dropdown
  .component("Form", Form)                  // validation form
  .component("Field", Field)                // validation field
  .component("ErrorMessage", ErrorMessage)  // validation error
  .mount('#app')