<template>
<!--  <script src="${ pageContext.request.contextPath }/resources/setting/extensions.json"></script>-->
<!--  <script src="${ pageContext.request.contextPath }/resources/js/file.js"></script>-->
<!--  <link href="${ pageContext.request.contextPath }/resources/css/file.css" rel="stylesheet">-->

  <input type="hidden" id="shareLink" name="shareLink" value="${ shareLink }">
  <input type="hidden" id="path" name="path" value="${ fn:replace(path, '+', '%20') }">
  <input type="hidden" id="sort" name="sort" value="${ sort }">
  <input type="hidden" id="order" name="order" value="${ order }">
  <input type="hidden" id="viewHidden" name="viewHidden" value="${ viewHidden }">

  <div class="row fixed-top" id="fileMenuContainer">
    <div class="col-0 col-md-3"></div>
    <div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
      <div class="row rounded border p-2 m-0 bg-light-subtle">
        <div class="col-12 col-md-8">
          <div id="nowPath"></div>
        </div>
        <div class="col-12 col-md-4 row text-end">
          <div class="col-4 d-md-none"></div>
          <div class="col-6 col-md-8 px-2">
            <input type="text" class="w-100 border border-secondary rounded-5 px-2" id="keyword" name="keyword" value="${ keyword }" onkeypress="searchFileEnter(event)">
          </div>
          <div class="col-1 col-md-2 pointer" onClick="searchFileClick()"><i class="fa-solid fa-magnifying-glass"></i></div>
          <div class="col-1 col-md-2 ps-2">
					<span class="pointer" data-bs-toggle="dropdown" aria-expanded="false">
						<i class="fa-solid fa-wrench"></i>
					</span>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><div class="dropdown-item pointer" onClick="toggleHiddenCheck()">
                <span id="viewHiddenCheck" class="inactive"><i class="fa-solid fa-check"></i></span>
                <i class="fa-solid fa-file-shield pe-2"></i>숨김파일보기
              </div></li>
              <li><div class="dropdown-item pointer" onClick="newFolder()" data-bs-toggle="modal" data-bs-target="#functionModal">
                <i class="fa-solid fa-folder-plus pe-2"></i>새폴더
              </div></li>
              <li><div class="dropdown-item pointer" onClick="newFile()" data-bs-toggle="modal" data-bs-target="#functionModal">
                <i class="fa-solid fa-file-circle-plus pe-2"></i>새파일
              </div></li>
            </ul>
          </div>
        </div>

        <table class="w-100">
          <tr>
            <td class="text-center" style="width:35px;"><input id="checkAllFile" type="checkbox" class="form-check-input"></td>
            <td style="width:80px;"></td>
            <td class="w-auto d-flex">
              <span class="pointer" id="nameSort" onClick="loadFileList('', '', 'name', '')">이름${ sort eq 'name' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
              <span class="pointer px-3" id="sizeSort" onClick="loadFileList('', '', 'size','')">크기${ sort eq 'size' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
            </td>
            <td class="text-end" style="width:15%">
              <span class="pointer" id="typeSort" onClick="loadFileList('', '', 'type','')">종류${ sort eq 'type' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
              <span class="pointer d-none" id="pathSort" onClick="loadFileList('', '', 'path','')">경로${ sort eq 'path' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
            </td>
            <td class="text-center" style="width:20%">
              <span class="pointer" id="dateSort"  onClick="loadFileList('', '', 'date','')">날짜${ sort eq 'date' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
            </td>
          </tr>
        </table>
      </div>
    </div>
  </div>

  <div class="position-fixed shadow border border-secondary rounded bg-body-tertiary p-3 inactive" id="fileControlMenu">
    테스트
  </div>

  <div class="col-12" id="fileListContainer">
    <div id="fileList" class="px-2"></div>
    <div id="loadingList" class="text-center m-2"><i class="fa-solid fa-circle-notch fa-spin"></i> Loading... </div>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="functionModal" tabindex="-1" aria-labelledby="functionModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5 text-break w-100 pe-3" id="functionModalLabel"></h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body" id="functionModalBody">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
          <button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="functionModalAffect()">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FilePage',
  props: {
    msg: String
  },
  data() {
    return {
    }
  },
  mounted() {

  },
  methods : {

  }
}
</script>

<style>
@import '@/assets/css/file.css';
</style>
