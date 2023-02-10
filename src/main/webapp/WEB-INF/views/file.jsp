<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${ validPath }">
	<span id="nowPath">${ nowPath.getPath() }</span>
	<br><br>
	<c:forEach items="${ fileList }" var="li">
		<c:set var="extension" value="${ li.isDirectory() ? 'folder' : FilenameUtils.getExtension(li.getName()).toString().toLowerCase() }" />
		<span>
			<span class="fileList">
				<span ${ extension eq 'folder' ? "style=cursor:pointer onClick=\"location.href='" += pageContext.request.contextPath += "?path=" += li.getPath() += "'" += "\"" : '' }>
					<c:choose>
						<c:when test="${ extension eq 'png' || extension eq 'jpg' || extension eq 'jpeg' || extension eq 'bmp' || extension eq 'gif' || extension eq 'exif' || extension eq 'heif' }">
							<img src="/api/thumbnailmaker?name=${ li.getPath() }" onerror="this.onerror=null; this.src='/resources/img/fileicons/default.png';">
						</c:when>
						<c:otherwise>
							<img src="/resources/img/fileicons/${ extension }.png" onerror="this.onerror=null; this.src='/resources/img/fileicons/default.png';">
						</c:otherwise>
					</c:choose>
					${ li.getName() }<br>
				</span>
				수정날짜 : <span class="date">${ li.lastModified() }</span><br>
				크기 : <span class="size">${ li.isDirectory() ? "-" : li.length() }</span><br><br>
			</span>
		</span>
	</c:forEach>
</c:if>
<c:if test="${ not validPath }">
	path : 없는경로입니당
</c:if>

<script>
	const parents = $("#nowPath").text().replace("/", "").split("/");
	if(parents[0]){
		let parentsText = "";
		let parentsLink = "/";
		for(let i=0; i < parents.length; i++){
			parentsLink += parents[i] + "/";
			if(i != parents.length-1){
				parentsText += "<a href='/file?path=" + parentsLink + "'>" + parents[i] + "</a> <i class='fa-solid fa-angle-right'></i> ";
			} else {
				parentsText += "<a href='/file?path=" + parentsLink + "' id='parent'>" + parents[i] + "</a>";
			}
		}
		console.log(parentsLink);
		console.log(parentsText);
		$("#nowPath").html(parentsText);
	}
	
	$(".fileList").each(function(){
		// size 
		if($(this).children(".size").text() == "-") {
			
		} else if($(this).children(".size").text() == 0) {
			$(this).children(".size").text("-");
		} else if($(this).children(".size").text() > 1073741824) {
			$(this).children(".size").text(($(this).children(".size").text()/1073741824).toFixed(2) + "gb");
		} else if($(this).children(".size").text() > 1048576) {
			$(this).children(".size").text(($(this).children(".size").text()/1048576).toFixed(1) + "mb");
		} else if($(this).children(".size").text() > 1024) {
			$(this).children(".size").text(($(this).children(".size").text()/1024).toFixed(1) + "kb");
		} else {
			$(this).children(".size").text($(this).children(".size").text() + "byte");
		}
		
		// date
		$(this).children(".date").text(moment($(this).children(".date").text(), "x").format("YY/MM/DD HH:mm"));
	});
	
</script>

