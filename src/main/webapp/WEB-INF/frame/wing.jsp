<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- wing button -->
<span id="wingButton" class="d-block d-md-none pointer">
	<span class="fa-stack fa-lg">
		<i class="fa fa-circle fa-stack-2x headerColor"></i>
		<i class="fa fa-bars fa-stack-1x fa-inverse"></i>
	</span>
</span>

<!-- wing mask -->
<div id="wingMask" style="display:none;"></div>

<!-- wing -->
<div id="wing" class="border-end">
	<div class="float-end" data-bs-toggle="dropdown" aria-expanded="false">
		<div class="pointer" data-bs-auto-close="inside">
			<i class="fa-solid fa-gear"></i>
			테마&nbsp;
			<i class="fa-solid fa-caret-right"></i>
			&nbsp;
			<i class="fa-solid" id="theme-icon"></i>
		</div>
		<ul class="dropdown-menu">
			<li>
				<button type="button" class="dropdown-item d-flex align-items-center text-warning change-theme" data-bs-theme-value="light">
					<i class="fa-solid fa-sun"></i>&nbsp;Light
				</button>
			</li>
			<li>
				<button type="button" class="dropdown-item d-flex align-items-center change-theme" data-bs-theme-value="dark">
					<i class="fa-solid fa-moon"></i>&nbsp;&nbsp;Dark
				</button>
			</li>
			<li>
				<button type="button" class="dropdown-item d-flex align-items-center text-success change-theme" data-bs-theme-value="auto">
					<i class="fa-solid fa-wand-magic-sparkles"></i>&nbsp;Auto
				</button>
			</li>
		</ul>
	</div>
	
	<div class="clearfix"></div>
	
	<div class="row">
		<div class="col-12">
			<div class="list-group list-group-flush">
				<a href="/file?path=/" class="list-group-item list-group-item-action" id="list_file"><i class="fa-solid fa-folder"></i> 파일</a>
			</div>
		</div>
	</div>
</div>

