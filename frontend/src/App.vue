<template>
  <div class="row mx-0">
    <div class="col-12" id="header">
      <header-menu/>
    </div>

    <wing-menu/>

    <div class="col-d-none col-md-3"></div>
    <div class="col-12 col-md-9 px-4 px-md-0" id="body">
      <router-view/>
    </div>

  </div>
</template>

<script setup>
import { useHead } from '@unhead/vue'
import HeaderMenu from './components/HeaderMenu.vue'
import WingMenu from './components/WingMenu.vue'
import { onMounted, ref } from "vue";

const metaTitle       = ref('Every Cloud')
const metaAuthor      = ref('EveryCloud')
const metaDescription = ref('EveryCloud Share personal cloud for everyone.')
const metaKeywords    = ref('EveryCloud, Share, Personal, Cloud')
const metaUrl         = ref('')
const metaImage       = ref('')

useHead({
  title: metaTitle,
  meta: [
      { name: 'title',                      content: metaTitle },
      { name: 'author',                     content: metaAuthor },
      { name: 'description',                content: metaDescription },
      { name: 'keywords',                   content: metaKeywords },
      { property: 'og:type',                content: 'website' },
      { property: 'og:rich_attachment',     content: 'true' },
      { property: 'og:site_name',           content: metaTitle },
      { property: 'og:title',               content: metaTitle },
      { property: 'og:description',         content: metaDescription },
      { property: 'og:keywords',            content: metaKeywords },
      { property: 'og:image',               content: metaImage },
      { property: 'og:url',                 content: metaUrl },
      { name: 'twitter:card',               content: 'summary_large_image' },
      { name: 'twitter:site',               content: metaTitle },
      { name: 'twitter:title',              content: metaTitle },
      { name: 'twitter:description',        content: metaDescription },
      { name: 'twitter:keywords',           content: metaKeywords },
      { name: 'twitter:image',              content: metaImage },
      { name: 'twitter:creator',            content: metaAuthor },
      { itemProp: 'name',                   content: metaTitle },
      { itemProp: 'description',            content: metaDescription },
      { itemProp: 'keywords',               content: metaKeywords },
      { itemProp: 'image',                  content: metaImage },
      { name: 'apple-mobile-web-app-title', content: metaTitle },
  ],
})

onMounted(() => {
  $http.get('/settings/getMeta')
    .then((response) => {
      if(response.data) {
        metaTitle.value       = response.data.metaTitle
        metaAuthor.value      = response.data.metaAuthor
        metaDescription.value = response.data.metaDescription
        metaKeywords.value    = response.data.metaKeywords
        metaUrl.value         = response.data.externalUrl
        metaImage.value       = response.data.externalUrl + '/api/v1/file/getMetaImage'
      }
    })
})

</script>

<style>
@import "@/assets/css/common.css";
@import "@/assets/lib/bootstrap/css/bootstrap.min.css";
@import "@/assets/lib/fontawesome/css/fontawesome.min.css";
@import "@/assets/lib/fontawesome/css/brands.min.css";
@import "@/assets/lib/fontawesome/css/solid.min.css";
@import "@/assets/lib/fontawesome/css/regular.min.css";
@import "tippy.js/dist/tippy.css";
</style>
