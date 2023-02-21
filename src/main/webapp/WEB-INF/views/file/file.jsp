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
<input type="hidden" id="sort" name="sort" value="${ sort }">
<input type="hidden" id="order" name="order" value="${ order }">

<div class="row fixed-top" id="fileMenuContainer">
	<div class="col-0 col-md-3"></div>
	<div class="col-12 col-md-9 px-4 ps-md-0" id="fileMenu">
		<div class="row rounded border p-2">
			<table class="w-100">
				<tr>
					<td class="w-auto">
						<div class="row">
							<div class="col-12 col-md-6">
								<span id="nowPath"></span>
							</div>
							<div class="col-12 col-md-6 text-end">
								<input type="text" class="rounded-5 px-2 mx-1" id="keyword" name="keyword" size="10" value="${ keyword }" onkeypress="searchFileEnter(event)">
								<span class="pointer" onClick="searchFileClick()"><i class="fa-solid fa-magnifying-glass"></i></span>
								<span class="btn-group">
									<span class="pointer" data-bs-toggle="dropdown" aria-expanded="false">
										<i class="fa-solid fa-wrench"></i>
									</span>
									<ul class="dropdown-menu dropdown-menu-end">
										<li><button class="dropdown-item" type="button">Action</button></li>
										<li><button class="dropdown-item" type="button">Another action</button></li>
										<li><button class="dropdown-item" type="button">Something else here</button></li>
									</ul>
								</span>
							</div>
						</div>
					</td>
				</tr>
			</table>
			
			<table class="w-100">
				<tr>
					<td class="text-center" style="width:35px;"><input id="checkAllFile" type="checkbox" class="form-check-input"></td>
					<td style="max-width:85px;"></td>
					<td class="w-auto d-flex justify-content-evenly">
						<span class="pointer" id="nameSort" onClick="loadFileList('', 'name', '')">이름${ sort eq 'name' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
						<span class="pointer" id="sizeSort" onClick="loadFileList('', 'size','')">크기${ sort eq 'size' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
					</td>
					<td class="w-25 text-center">
						<span class="pointer" id="typeSort" onClick="loadFileList('', 'type','')">종류${ sort eq 'type' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
						<span class="pointer d-none" id="pathSort" onClick="loadFileList('', 'path','')">경로${ sort eq 'path' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
					</td>
					<td class="w-25 text-center">
						<span class="pointer" id="dateSort"  onClick="loadFileList('', 'date','')">날짜${ sort eq 'date' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<div class="col-12" id="fileListContainer">
	<div id="fileList" class="px-2"></div>
	<div id="loadingList" class="text-center m-2"><i class="fa-solid fa-circle-notch fa-spin"></i> Loading... </div>
</div>

