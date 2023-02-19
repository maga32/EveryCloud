<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="${ pageContext.request.contextPath }/resources/setting/extensions.json"></script>
<script src="${ pageContext.request.contextPath }/resources/js/file.js"></script>

<input type="hidden" id="path" name="path" value="${ path }">

<div id="nowPath"></div>
<div id="loadingList"><i class="fa-solid fa-circle-notch fa-spin"></i></div>
<div id="fileList"></div>

