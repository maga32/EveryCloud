const link = {
  namespaced: true,
  state: () => ({
    siteHtml: '/',
  }),
  getters: {
    siteHtml: (state) => {
      return (state.siteHtml == '/') ? localStorage.getItem('siteHtml') : state.siteHtml
    }
  },
  mutations: {
    addSiteHtml: (state, payload) => {
      state.siteHtml = payload
    }
  },
  actions: {
    addSiteHtml: ({commit}) => {
      const siteHtml = window.location.pathname + window.location.search
      localStorage.setItem('siteHtml', siteHtml)

      commit('addSiteHtml', siteHtml)
    }
  }
}

export default link