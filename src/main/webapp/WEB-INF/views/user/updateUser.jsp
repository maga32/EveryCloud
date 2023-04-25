<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="${ pageContext.request.contextPath }/resources/js/user.js"></script>
<style>
	.important:before{
		color: red;
		content: "*";
	}
</style>

<form action="/updateUserProcess" method="post" id="updateUserForm">
	<input type="hidden" name="siteHtml" id="siteHtml" value="${ siteHtml }">
	<div class="p-5">
		<div class="row">
			<div class="col-12 important">
				아이디
			</div>
			<div class="col-12 col-md-6 mb-4">
				<c:choose>
					<c:when test="${ type eq null || user.userId eq null }">
						<script>
							alert("잘못된 접근방식입니다.");
							window.location.href = "/";
						</script>
					</c:when>
					<c:when test="${ type == 'admin'}">
						<input type="hidden" id="userOrigId" name="userOrigId" value="${ user.userId }">
						<input type="text" size="20" class="form-control" name="userId" id="userId" value="${ user.userId == 'admin' ? '' : user.userId }" onkeyup="$('#duplicateChecked').val('false')">
						<input type="hidden" id="duplicateChecked" value="true">
						<div class="col-12 btn btn-secondary btn-lg mt-2" onclick="checkOverlapId()">중복확인</div>
					</c:when>
					<c:when test="${ type == 'user'}">
						<input type="hidden" size="20" class="form-control" name="userId" id="userId" value="${ user.userId }">
						${ user.userId }
					</c:when>
				</c:choose>
			</div>

			<div class="col-12">
				새 비밀번호
			</div>
			<div class="col-12 col-md-6 mb-4">
				<input type="password" size="20" class="form-control" name="userPass" id="userPass" onkeyup="checkPassword()">
					${ user.userPass == "admin" ? "<span class='text-danger'>* 첫실행시 비밀번호 변경은 필수입니다.</span>" : "" }
				<div class="text-danger d-none" id="warningPassLength">
					* 비밀번호는 8자이상 설정해주세요.
				</div>
			</div>

			<div class="col-12">
				비밀번호 확인
			</div>
			<div class="col-12 col-md-6 mb-4">
				<input type="password" size="20" class="form-control" name="confirmUserPass" id="confirmUserPass" onkeyup="checkPassword()">
				<div class="text-danger d-none" id="warningPassCheck">
					* 비밀번호가 일치하지 않습니다.
				</div>
			</div>

			<div class="col-12 important">
				닉네임
			</div>
			<div class="col-12 col-md-6 mb-4">
				<input type="text" size="20" class="form-control" name="userNickname" id="userNickname" value="${ user.userNickname }">
			</div>

			<div class="col-12 important">
				이메일
				<span class="text-danger">(비밀번호 찾기시 필요)</span>
			</div>
			<div class="col-12 col-md-6 mb-4">
				<input type="text" size="20" class="form-control" name="userEmail" id="userEmail" value="${ user.userEmail }">
			</div>

			<div class="col-12 mb-4"></div>
			<div class="col-12 col-md-6 mb-4">
				<div class="col-12 btn btn-danger btn-lg mb-2" onclick="updateUserForm()">확인</div>
				<div class="col-12 btn btn-secondary btn-lg" onclick="location.href='${ siteHtml }'">취소</div>
			</div>
		</div>
	</div>
</form>