<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="${ pageContext.request.contextPath }/resources/setting/extensions.json"></script>
<script src="${ pageContext.request.contextPath }/resources/js/file.js"></script>
<link href="${ pageContext.request.contextPath }/resources/css/file.css" rel="stylesheet">

<input type="hidden" id="path" name="path" value="${ path }">

<div class="row fixed-top" id="fileMenuContainer">
	<div class="col-0 col-md-3"></div>
	<div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
		<div class="row rounded border">
			<div class="d-flex">
				<div class="flex-grow-1 d-flex" id="nowPath"></div>
				<div class="btn-group">
					<span class="pointer" data-bs-toggle="dropdown" aria-expanded="false">
						<i class="fa-solid fa-wrench"></i>
					</span>
					<ul class="dropdown-menu dropdown-menu-end">
						<li><button class="dropdown-item" type="button">Action</button></li>
						<li><button class="dropdown-item" type="button">Another action</button></li>
						<li><button class="dropdown-item" type="button">Something else here</button></li>
					</ul>
				</div>
			</div>
			
			<div class="text-center align-items-center row">
				<div class="col-1"><input type="checkbox" class="form-check-input"></div>
				<div class="col-7">이름</div>
				<div class="col-1">종류</div>
				<div class="col-3 row">
					<div class="col-12 col-xl-8">날짜</div>
					<div class="col-12 col-xl-4">크기</div>
				</div>
			</div>
			
		</div>
	</div>
</div>
<div class="col-12" id="fileListContainer">
	<div id="fileList" class="ps-3 px-md-4"></div>
	<div id="loadingList" class="text-center m-2"><i class="fa-solid fa-circle-notch fa-spin"></i> Loading... </div>
</div>

