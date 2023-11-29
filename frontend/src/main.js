import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import HttpModule from './modules/http'
import { createHead } from '@unhead/vue'
import DropdownMenu from 'v-dropdown-menu'
import { TippyPlugin } from 'tippy.vue'
import { Form, Field, ErrorMessage } from 'vee-validate'
import './modules/vee-validation'
import { createI18n } from 'vue-i18n'
import localeMessage from './locales'

const languages = createI18n({
  locale: 'en',
  fallbackLocale: 'ko',
  localeMessage,
})
createApp(App)
  .use(HttpModule)                          // axios
  .use(router)                              // router
  .use(store)                               // store
  .use(createHead())                        // meta
  .use(languages)                           // languages
  .use(DropdownMenu)                        // dropdown
  .use(TippyPlugin)                         // tooltip
  .component("Form", Form)                  // validation form
  .component("Field", Field)                // validation field
  .component("ErrorMessage", ErrorMessage)  // validation error
  .mount('#app')
