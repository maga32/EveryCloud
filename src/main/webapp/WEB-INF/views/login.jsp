<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="${ pageContext.request.contextPath }/resources/js/login.js"></script>

<form action="loginProcess" method="post" id="loginForm">
	<div class="p-5">
		<div class="row border border-secondary bg-light-subtle rounded-3">
			<div class="col-12 text-center py-4 fs-2">
				Login
			</div>
			
			<div class="col-7 col-sm-9 col-lg-10">
				<div class="py-2">
					아이디
				</div>
				<div class="pb-2">
					<input type="text" size="20" class="form-control" name="userId" id="userId">
				</div>
				<div class="py-2">
					비밀번호
				</div>
				<div class="pb-2">
					<input type="password" size="20" class="form-control" name="userPass" id="userPass" onkeyup="pressEnter()">
				</div>
			</div>
			<div class="col-5 col-sm-3 col-lg-2 d-grid gap-2">
				<div class="btn btn-primary mt-4 mb-2 d-flex justify-content-center" onclick="loginCheck()">
					<div class="align-self-center" >로그인</div>
				</div>
			</div>
			
			
		</div>
	</div>
</form>