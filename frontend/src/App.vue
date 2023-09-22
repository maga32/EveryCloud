<template>
  <div class="row">
    <div class="col-12" id="header">
      <header-menu/>
    </div>

    <div class="col-d-none col-md-3"></div>
    <wing-menu/>

    <div class="col-12 col-md-9 px-4 px-md-0" id="body">
      <router-view/>
    </div>

  </div>
</template>

<script>
import HeaderMenu from './components/HeaderMenu.vue'
import WingMenu from './components/WingMenu.vue'

export default {
  name: 'App',
  components: {
    HeaderMenu,
    WingMenu,
  }
}
</script>

<style>
@import "@/assets/css/common.css";
@import "@/assets/lib/bootstrap/css/bootstrap.min.css";
@import "@/assets/lib/fontawesome/css/fontawesome.min.css";
@import "@/assets/lib/fontawesome/css/brands.min.css";
@import "@/assets/lib/fontawesome/css/solid.min.css";
</style>
