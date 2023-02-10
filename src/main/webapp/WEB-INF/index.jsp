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
</head>
<body>
	<%--@ include file="template/header.jsp" --%>
	<jsp:include page="${ param.body }" />
	<%--@ include file="template/footer.jsp" --%>
</body>
</html>