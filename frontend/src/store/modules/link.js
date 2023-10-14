const link = {
  namespaced: true,
  state: () => ({
    siteHtml: '',
    tempSiteHtml: '',
  }),
  getters: {
    siteHtml: (state) => {
      return state.siteHtml || localStorage.getItem('siteHtml') || '/'
    },
    tempSiteHtml: (state) => {
      return state.tempSiteHtml || state.siteHtml || localStorage.getItem('siteHtml') || '/'
    }
  },
  mutations: {
    addSiteHtml: (state, payload) => {
      state.siteHtml = payload
    },
    addTempSiteHtml: (state, payload) => {
      state.tempSiteHtml = payload
    }
  },
  actions: {
    addSiteHtml: ({commit}) => {
      const siteHtml = window.location.pathname + window.location.search
      localStorage.setItem('siteHtml', siteHtml)
      commit('addSiteHtml', siteHtml)
    },
    addTempSiteHtml: ({commit}) => {
      const tempSiteHtml = window.location.pathname + window.location.search
      commit('addTempSiteHtml', tempSiteHtml)
    }
  }
}

export default link