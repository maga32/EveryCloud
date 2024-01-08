import { defineRule, configure } from 'vee-validate'
import { localize } from '@vee-validate/i18n'
import en from '@vee-validate/i18n/dist/locale/en.json'
import ko from '@vee-validate/i18n/dist/locale/ko.json'

import { required, email, min, max, is, is_not, confirmed, alpha_num, alpha_dash } from '@vee-validate/rules'

defineRule('required', required)
defineRule('email', email)
defineRule('min', min)
defineRule('max', max)
defineRule('is', is)
defineRule('is_not', is_not)
defineRule('confirmed', confirmed)
defineRule('alpha_num', alpha_num)
defineRule('alpha_dash', alpha_dash)

// custom made
defineRule('first_required', required)
defineRule('boolean', value => {
  if (!value) return false
  return true
})

const customLocalize = {
  en:{
    messages: {
      first_required: 'When setting up for the first time, the {field} value is required',
      boolean: 'Check the {field} value',
    },
  },
  ko:{
    messages: {
      first_required: '첫 설정시, {field} 항목은 필수 입력 항목입니다',
      boolean: '{field} 항목을 확인해주세요',
    }
  }
}

Object.assign(en.messages, customLocalize.en.messages)
Object.assign(ko.messages, customLocalize.ko.messages)

configure({
  validateOnInput: true,
  generateMessage: localize({
    en,
    ko,
  }),
})
