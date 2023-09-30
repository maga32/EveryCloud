<!--<template>-->
<!--  <button @click="test">test1</button>-->
<!--</template>-->

<!--<script setup>-->
<!--import { defineRule, configure } from 'vee-validate'-->
<!--import { localize } from '@vee-validate/i18n'-->
<!--import en from '@vee-validate/i18n/dist/locale/en.json'-->
<!--import ko from '@vee-validate/i18n/dist/locale/ko.json'-->

<!--import { required, email, min, max, is_not } from '@vee-validate/rules'-->

<!--defineRule('required', required)-->
<!--defineRule('first_required', required)-->
<!--defineRule('email', email)-->
<!--defineRule('min', min)-->
<!--defineRule('max', max)-->
<!--defineRule('is_not', is_not)-->

<!--const customLocalize = {-->
<!--  en:{-->
<!--    messages: {-->
<!--      first_required: 'When setting up for the first time, the {field} value is required',-->
<!--    },-->
<!--  },-->
<!--  ko:{-->
<!--    messages: {-->
<!--      first_required: '첫 설정시, {field} 항목은 필수 입력 항목입니다',-->
<!--    }-->
<!--  }-->
<!--}-->
<!--//-->
<!--// configure({-->
<!--//   validateOnInput: true,-->
<!--//   generateMessage: localize({-->
<!--//     en:Object.assign(en, customLocalize.en),-->
<!--//     ko:Object.assign(ko, customLocalize.ko)-->
<!--//   }),-->
<!--// })-->

<!--function test() {-->
<!--  console.log("ko : ")-->
<!--  console.log(Object.assign(ko))-->
<!--  console.log("customLocalize.ko : ")-->
<!--  console.log(Object.assign(customLocalize.ko))-->
<!--  console.log("sum : ")-->
<!--  Object.assign(ko.messages, customLocalize.ko.messages)-->
<!--  console.log(ko)-->
<!--}-->

<!--</script>-->
