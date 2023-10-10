import router from '@/router'

const user = {
  namespaced: true,
  state: () => ({
    user: {
      id: '',
      nickname: '',
      auth: '',
    }
  }),
  getters: {
    getUser: (state) => {
      return state.user
    }
  },
  mutations: {
    getSession: (state, payload) => {
      state.user = payload
    }
  },
  actions: {
    getSession: ({commit}) => {
      return new Promise((resolve, reject) => {
        $http.post('/getSessionUser')
          .then((response) => {
            if (response.code === '402') {
              router.push({
                path: '/updateUserForm',
                state: {params: {type: 'admin'}}
              })
            } else {
              commit('getSession', response.data)
            }
          })
        resolve()
      })
    }
  }
}

export default user