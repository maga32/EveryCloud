<template>
  <!-- wing button -->
  <span id="wingButton" class="d-block d-md-none pointer" @click="openWing">
    <span class="fa-stack fa-lg">
      <i class="fa fa-circle fa-stack-2x headerColor"></i>
      <i class="fa fa-bars fa-stack-1x fa-inverse"></i>
    </span>
  </span>

  <!-- wing mask -->
  <div id="wingMask" :style="{display: (minWingOpened ? 'block' : 'none')}" @click="closeWing"></div>

  <!-- wing -->
  <div id="wing" class="border-end" :class="(!minWingOpened || 'open')" style="overflow:visible;">
    <!-- theme select -->
    <div class="float-end" data-bs-toggle="dropdown" aria-expanded="false">
      <div class="pointer" @click="openThemeSelect">
        <i class="fa-solid fa-gear" /> 테마&nbsp;<i class="fa-solid fa-caret-right" /> <i class="fa-solid" id="theme-icon" />
      </div>
      <ul class="dropdown-menu show" v-if="themeSelectOpened" v-on-click-outside="closeThemeSelect">
        <li>
          <button type="button" class="dropdown-item d-flex align-items-center text-warning change-theme" data-bs-theme-value="light">
            <i class="fa-solid fa-sun"></i>&nbsp;Light
          </button>
        </li>
        <li>
          <button type="button" class="dropdown-item d-flex align-items-center change-theme" data-bs-theme-value="dark">
            <i class="fa-solid fa-moon"></i>&nbsp;&nbsp;Dark
          </button>
        </li>
        <li>
          <button type="button" class="dropdown-item d-flex align-items-center text-success change-theme" data-bs-theme-value="auto">
            <i class="fa-solid fa-wand-magic-sparkles"></i>&nbsp;Auto
          </button>
        </li>
      </ul>
    </div>

    <div class="clearfix"></div>

    <c:if test="${ not empty user.id }">
      <div>
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
          <router-link :to="{path:'/file', query:{path:'/'}}" class="list-group-item list-group-item-action" id="list_file"><i class="fa-solid fa-folder" /> 파일</router-link>
        </div>
      </div>
      <div class="col-12">
        <div class="list-group list-group-flush">
          <router-link to="/share" class="list-group-item list-group-item-action" id="list_share"><i class="fa-solid fa-share-nodes" /> 공유</router-link>
        </div>
      </div>
    </div>
    <!-- Menu end -->

    <form action="" method="post" id="goPostForm"></form>

  </div>
</template>

<script>
import { vOnClickOutside } from '@vueuse/components'

export default {
  name: 'WingMenu',
  data() {
    return {
      minWingOpened: false,
      themeSelectOpened: false,
    }
  },
  methods: {
    openWing() {
      this.minWingOpened = true
    },
    closeWing() {
      this.minWingOpened = false
    },
    openThemeSelect() {
      this.themeSelectOpened = true
    },
    closeThemeSelect() {
      this.themeSelectOpened = false
    }
  }
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