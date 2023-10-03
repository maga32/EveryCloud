<template>
  <!-- wing button -->
  <span id="wingButton" class="d-block d-md-none pointer" @click="openWing">
    <span class="fa-stack fa-lg">
      <i class="fa fa-circle fa-stack-2x text-secondary"></i>
      <i class="fa fa-bars fa-stack-1x fa-inverse"></i>
    </span>
  </span>

  <!-- wing mask -->
  <div id="wingMask" :style="{display: (minWing ? 'block' : 'none')}" @click="closeWing"></div>

  <!-- wing -->
  <div id="wing" class="border-end" :class="(!minWing || 'open')" style="overflow:visible;">

    <!-- language select -->
    <div class="d-flex flex-wrap pt-2">
      <select v-model="state.language" class="form-select-sm flex-grow-1" @change="languageSelect(this.value)">
        <option disabled value="">Select</option>
        <option value="en">English</option>
        <option value="ko">Korean</option>
      </select>
      <i class="fa-solid fa-language ps-1" style="font-size: 2rem"/>
    </div>
    <div class="clearfix"/>

    <!-- theme select -->
    <div class="float-end pointer my-2">
      <span v-if="userDark" @click="userDark=false" class="fa-stack fa-2x" style="font-size: 1rem">
        <i class="fa-solid fa-toggle-on fa-stack-2x text-black fa-rotate-180"></i>
        <i class="fa-solid fa-circle fa-stack-1x pe-2 text-black" style="font-size: 1rem"></i>
        <i class="fa-solid fa-moon fa-stack-1x pe-2 text-warning" style="font-size: 1rem"></i>
      </span>
      <span v-if="!userDark" @click="userDark=true" class="fa-stack fa-2x" style="font-size: 1rem">
        <i class="fa-solid fa-toggle-on fa-stack-2x text-gray"></i>
        <i class="fa-solid fa-circle fa-stack-1x ps-2 text-gray" style="font-size: 1rem"></i>
        <i class="fa-solid fa-sun fa-stack-1x ps-2 text-white" style="font-size: 1rem"></i>
      </span>
    </div>

    <div class="clearfix"/>

    <div v-if="!$store.state.user.user">
      <div class="d-grid gap-2 my-4">
        <router-link to="/loginForm" class="btn btn-secondary">Login</router-link>
      </div>
    </div>

    <div v-else>
      <div class="text-break">
        <span class="fs-1">{{ $store.state.user.user.nickname }}</span> 님
        <div>( {{ $store.state.user.user.id }} )</div>
        <div v-if="$store.state.user.user.auth=='Y'" class="text-danger">관리자 계정</div>
      </div>
      <div class="d-grid gap-2 my-4">
        <router-link :to="{path:'/updateUserForm', state:{params:{type: $store.state.user.user.auth == 'Y' ? 'admin' : 'user' }}}"
            class="btn btn-outline-secondary">Edit Profile</router-link>
        <router-link class="btn btn-secondary" to="/" @click="logout">
          Logout
        </router-link>
      </div>
    </div>

    <!-- Menu start -->
    <div class="row my-3" @click="closeWing">
      <div class="col-12">
        <div class="list-group list-group-flush">
          <router-link :to="{path:'/file', query:{path:'/'}}" class="list-group-item list-group-item-action" active-class="bg-body-secondary">
            <i class="fa-solid fa-folder" /> 파일
          </router-link>
        </div>
      </div>
      <div class="col-12">
        <div class="list-group list-group-flush">
          <router-link to="/share" class="list-group-item list-group-item-action" active-class="bg-body-secondary">
            <i class="fa-solid fa-share-nodes" /> 공유
          </router-link>
        </div>
      </div>
    </div>
    <!-- Menu end -->

  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useDark, useStorage, usePreferredLanguages } from '@vueuse/core'
import { setLocale } from '@vee-validate/i18n'

const form = reactive({
  languages: ['en', 'ko'],
})

onMounted(()=> {
  languageSelect()
  $store.dispatch('user/getSession')
})

function logout() {
  $http.post('/logout').then(()=>
    $store.dispatch('user/getSession')
  )
}

/*------- theme & wing -------*/
const state = useStorage('my-state', {language: ''})

const userDark = useDark({
  selector: 'html',
  attribute: 'data-bs-theme',
  valueDark: 'dark',
  valueLight: 'light',
})

const minWing = ref(false)
const themeSelect = ref(false)

function openWing() {
  minWing.value = true
}

function closeWing() {
  minWing.value = false
}

function closeThemeSelect() {
  themeSelect.value = false
}

function languageSelect(language='') {
  language = language || (state.value.language || (form.languages.includes(usePreferredLanguages()) || 'en'))
  state.value.language = language
  setLocale(language)
}
</script>

<style>
#wing {
  position: fixed;
  z-index: 100;
  top: 100px;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100vh;
  display:block;
  transform: translateX(0px);
  transition: 0.3s all ease;
  transition: 0.2s all;
  overflow-y: scroll;
  -ms-overflow-style: none;
}

#wing::-webkit-scrollbar {
  display: none;
  width: 0 !important;
}

@media (max-width: 767px){
  #wing {
    width: 300px;
    transform: translateX(-300px);
  }

  #wing.open {
    top: 0;
    background: var(--bs-body-bg);
    transform: translateX(0px);
  }

  #wingMask {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    width: 100%;
    background: black;
    height: 100vh;
    opacity: 0.3;
    z-index: 90;
  }
}

@media (min-width: 767px){
  #wing {
    width: 23%;
  }
}

#wingButton {
  position: fixed;
  z-index: 80;
  top: 50px;
  left: -32px;
  width: 70px;
}

.swal2-container, .swal2-popup {
   color: var(--bs-body-color);
   background-color: var(--bs-body-bg);
 }
</style>