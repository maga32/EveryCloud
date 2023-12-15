import { createI18n } from 'vue-i18n'
import localeMessage from '@/locales'

const i18nModule = createI18n({
  locale: 'en',
  fallbackLocale: 'ko',
  messages: localeMessage,
})

window.$i18n = i18nModule

export default i18nModule