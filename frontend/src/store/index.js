import { createStore } from 'vuex'
import link from './modules/link'
import user from './modules/user'

const store = createStore({
  modules: {
    link,
    user
  }
})

window.$store = store

export default store