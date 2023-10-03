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
    updateUser: (state, payload) => {
      state.user = payload
    }
  },
  actions: {
    updateUser: ({commit}) => {
      $http.post('/getSessionUser')
        .then((response) => {
          if (response.code === '402') {
            router.push({
              path: '/updateUserForm',
              state: {
                params : {
                  type: 'admin',
                }
              }
            })
          } else {
            commit('updateUser', response.data)
          }
        })
    }
  }
}

export default user