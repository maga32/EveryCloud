<template>
  <!-- wing button -->
  <span id="wingButton" class="d-block d-md-none pointer" @click="openWing">
    <span class="fa-stack fa-lg">
      <i class="fa fa-circle fa-stack-2x headerColor"></i>
      <i class="fa fa-bars fa-stack-1x fa-inverse"></i>
    </span>
  </span>

  <!-- wing mask -->
  <div id="wingMask" :style="{display: (minWing ? 'block' : 'none')}" @click="closeWing"></div>

  <!-- wing -->
  <div id="wing" class="border-end" :class="(!minWing || 'open')" style="overflow:visible;">

    <!-- theme select -->
    <div class="float-end pointer my-2">
      <!--
      <i v-if="!userDark" @click="userDark=true" class="fa-regular fa-sun text-warning"/>
      <i v-if="userDark" @click="userDark=false" class="fa-solid fa-moon text-warning"/>
      -->
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

    <div class="clearfix"></div>

    <c:if test="${ not empty user.id }">
      <div class="text-break">
        <span class="fs-1">${ user.nickname }</span> 님
        <div>( ${ user.id } )</div>
        <c:if test="${ user.auth eq 'Y'}">
          <div class="text-danger">관리자 계정</div>
        </c:if>
      </div>
      <div class="d-grid gap-2 my-4" id="wingUserInfo">
        <button class="btn btn-outline-secondary"
                onclick='goToPost({ url:"/updateUser", params:{ userId:"${ user.id }", type:"${ user.auth == "Y" ? "admin" : "user" }" } })'>
          Edit Profile
        </button>
        <button class="btn btn-secondary" onclick="location.href='/logout'">
          Logout
        </button>
      </div>
    </c:if>
    <c:if test="${ empty user.id }">
      <div class="d-grid gap-2 my-4">
        <button class="btn btn-secondary" onclick="location.href='/login'">
          Login
        </button>
      </div>
    </c:if>

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

    <form action="" method="post" id="goPostForm"></form>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useDark } from '@vueuse/core'

const userDark = useDark({
  selector: 'html',
  attribute: 'data-bs-theme',
  valueDark: 'dark',
  valueLight: 'light',
})

const minWing = ref(false)
const themeSelect = ref(false)
const test = ref('')

function openWing() {
  minWing.value = true
}

function closeWing() {
  minWing.value = false
}

function closeThemeSelect() {
  themeSelect.value = false
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
</style>