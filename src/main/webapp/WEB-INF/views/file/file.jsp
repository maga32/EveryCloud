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
			
			<table class="w-100 text-center">
				<colgroup>
					<col>
					<col style="width:65%">
					<col>
					<col>
 				</colgroup>
				<tr>
					<td><input type="checkbox"  class="form-check-input"></td>
					<td>
						<span class="pointer" id="nameSort" onClick="loadFileList('', 'name', '')">이름${ sort eq 'name' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
					</td>
					<td>
						<div class="row">
							<div class="col-12 col-xl-6">
								<span class="pointer" id="typeSort" onClick="loadFileList('', 'type' ,'')">종류${ sort eq 'type' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
							</div>
							<div class="col-12 col-xl-6">
								<span class="pointer" id="sizeSort" onClick="loadFileList('', 'size' ,'')">크기${ sort eq 'size' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
							</div>
						</div>
					</td>
					<td>
						<span class="pointer" id="dateSort"  onClick="loadFileList('', 'date' ,'')">날짜${ sort eq 'date' ? (order eq 'asc' ? '↑' : '↓' ) : '' }</span>
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

