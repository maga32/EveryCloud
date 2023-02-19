<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="content-language" content="kr">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
	
	<!-- 부트스트램, 폰트어썸 -->
	<link href="${ pageContext.request.contextPath }/resources/css/bootstrap.min.css" rel="stylesheet">
	<script src="${ pageContext.request.contextPath }/resources/js/bootstrap.bundle.min.js"></script>
	<script src="${ pageContext.request.contextPath }/resources/js/jquery-3.6.3.min.js"></script>
	<script src="${ pageContext.request.contextPath }/resources/js/moment.min.js"></script>
	<script src="https://kit.fontawesome.com/2adeaa7ee7.js" crossorigin="anonymous"></script>
	
	<link href="${ pageContext.request.contextPath }/resources/css/index.css" rel="stylesheet">	
	<script src="${ pageContext.request.contextPath }/resources/js/index.js"></script>
</head>
<body>
	<div class="row">
		<div class="col-12" id="header">
			<%@ include file="frame/header.jsp" %>
		</div>
		
		<div class="col-d-none col-md-3"></div>
			<%@ include file="frame/wing.jsp" %>
		
		<div class="col-12 col-md-9" id="body">
			<jsp:include page="${ param.body }" />
		</div>
	</div>
</body>
</html>