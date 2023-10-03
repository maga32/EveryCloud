const link = {
  namespaced: true,
  state: () => ({
    siteHtml: '/',
  }),
  getters: {
    siteHtml: (state) => {
      return state.siteHtml
    }
  },
  mutations: {
    addSiteHtml: (state, payload) => {
      state.siteHtml = payload
    }
  },
  actions: {
    addSiteHtml: ({commit}) => {
      commit('addSiteHtml', window.location.pathname + window.location.search)
    }
  }
}

export default link