<template>
  <!-- wing button -->
  <span id="wingButton" class="d-block d-md-none pointer">
    <span class="fa-stack fa-lg">
      <i class="fa fa-circle fa-stack-2x headerColor"></i>
      <i class="fa fa-bars fa-stack-1x fa-inverse"></i>
    </span>
  </span>

  <!-- wing mask -->
  <div id="wingMask" style="display:none;"></div>

  <!-- wing -->
  <div id="wing" class="border-end">
    <div class="float-end" data-bs-toggle="dropdown" aria-expanded="false">
      <div class="pointer" data-bs-auto-close="inside">
        <i class="fa-solid fa-gear"></i>
        테마&nbsp;
        <i class="fa-solid fa-caret-right"></i>
        &nbsp;
        <i class="fa-solid" id="theme-icon"></i>
      </div>
      <ul class="dropdown-menu">
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
    <div class="row my-3">
      <div class="col-12">
        <div class="list-group list-group-flush">
          <a href="/file?path=/" class="list-group-item list-group-item-action" id="list_file"><i class="fa-solid fa-folder"></i> 파일</a>
        </div>
      </div>
      <div class="col-12">
        <div class="list-group list-group-flush">
          <a href="/share" class="list-group-item list-group-item-action" id="list_share"><i class="fa-solid fa-share-nodes"></i> 공유</a>
        </div>
      </div>
    </div>
    <!-- Menu end -->

    <form action="" method="post" id="goPostForm"></form>

  </div>
</template>

<script>
export default {
  name: 'WingMenu',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
