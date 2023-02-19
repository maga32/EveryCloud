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
	<div class="col-12 col-md-9 px-4 px-md-0" id="fileMenu">
		<div id="nowPath"></div>
	</div>
</div>
<div class="col-12" id="fileListContainer">
	<div id="fileList"></div>
	<div id="loadingList"><i class="fa-solid fa-circle-notch fa-spin"></i></div>
</div>

