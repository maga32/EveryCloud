$(document).ready(function(){
	loadFileList($("#path").val(),$("#sort").val(),$("#order").val(),$("#keyword").val());

	$("#checkAllFile").click(function(){
		if(this.checked) {
			$(".fileTable").addClass("checked");
		} else {
			$(".fileTable").removeClass("checked");
		}
		$(".checkFile").prop("checked", this.checked);
		viewFileControlMenu();
	});
});

// when history back clicked
window.addEventListener('popstate', function(event) {
	if (event.state != null) {
		loadFileList(event.state.path, event.state.sort, event.state.order, event.state.keyword, false, false);
    }
});

// loading FileList
function loadFileList(path, sort, order, keyword, resetKeyword = false, saveHistory = true) {
	$("#fileList").html("");
	$("#loadingList").addClass("act");
	$("#checkAllFile").prop("checked", false);
	viewFileControlMenu();

	if(!path) path = $("#path").val();
	if(!sort) sort = $("#sort").val();
	if(!order) order = $("#order").val();
	if(resetKeyword) $("#keyword").val("");
	if(!keyword) keyword = $("#keyword").val();

	if(keyword) {
		$("#pathSort").removeClass("d-none");
		$("#typeSort").addClass("d-none");
	} else {		
		$("#typeSort").removeClass("d-none");
		$("#pathSort").addClass("d-none");
	}

	let data = "path=" + path + "&sort=" + sort + "&order=" + order + "&keyword=" + keyword;
	if(!$("#viewHiddenCheck").hasClass("inactive")) data += "&viewHidden=true	";

	$.ajax({
		url: "fileList",
		type: "post",
		data: data,
		success: function(result) {
			if(result.realPath) {
				loadFileList(result.realPath,'','','',true,false);
				return false;
			}

			if(result.validPath) {
				$("#nowPath").html(pathLink(result.nowPath));

				const fileList = result.fileList;
				let listHtml = "";

				for(i in fileList) {
					listHtml += makeList(fileList[i].isDirectory, fileList[i].isHidden, fileList[i].getPath, fileList[i].getName, fileList[i].getExtension, fileList[i].lastModified, fileList[i].length);
				}

				listHtml += "</table>";

				$("#fileList").html(listHtml);

				setFileMenu(path, sort, order);

			} else {
				setTimeout(function() { $("#fileList").html("잘못된 경로입니다."); }, 300);
			}
		},
		error: function() {
			console.log("error");
		},
		complete: function() {
			$("#loadingList").removeClass("act");
			if(saveHistory) {
				history.pushState({"path":path, "sort":sort, "order":order, "keyword":keyword}, null, "file?path=" + path + "&sort=" + sort + "&order=" + order + "&keyword=" + keyword);
			}
		}
	});
}

// define link of #nowPath
function pathLink(path) {
	const parents = path.replace(/\\/g, "/").split("/");
	let parentsHtml = "";
	let parentHtml = "";
	let parentsLink = "";

	for(let i=0; i < parents.length; i++) {
		parentsLink += parents[i] + "/";
		if(!parents[0]) parents[0] = "root";
		if(parents[i+2]) {
			parentsHtml += "<div class='pointer dropdown-item' onclick=\"loadFileList('" + encodeURIComponent(parentsLink) + "','','','',true)\">" + parents[i] + "</div>\n";
		} else if(parents[i+1]) {
			parentHtml += "<span class='mx-2'><span class='pointer' onclick=\"loadFileList('" + encodeURIComponent(parentsLink) + "','','','',true)\" id='parent'>" + parents[i] + "</span></span>\n<span><i class='fa-solid fa-angle-right'></i> </span>\n";
		} else if(parents[i]) {
			parentHtml += "<span class='mx-2'><span class='pointer' onclick=\"loadFileList('" + encodeURIComponent(parentsLink) + "','','','',true)\" id='parent'>" + parents[i] + "</span></span>\n";
		}
	}

	let resultHtml = "";

	if(parents.length >2) {
		resultHtml += 	"<div class='btn-group'>"
					+		"<span class='dropdown-toggle pointer' data-bs-toggle='dropdown' aria-expanded='false'>"
					+			"<i class='fa-solid fa-house'></i>"
					+		"</span>"
					+		"<span class='dropdown-menu dropdown-menu-start'>"
					+			parentsHtml
					+		"</span>"
					+	"</div>";
	}

	resultHtml += parentHtml;

	return resultHtml;
}

// make a file list each line
function makeList(isDirectory, isHidden, path, name, extension, date, size) {
	if(isDirectory) {
		extension = "folder";
	} else if(!extensions.hasOwnProperty(extension)) {
		extension = "default";
	}

	let fileHtml = "";
	fileHtml += "<label class='w-100 pe-2 pe-md-5'>\n";
	fileHtml += "<table class='w-100 rounded fileTable'>\n";
	fileHtml += 	"<tr>\n";
	fileHtml += 		"<td rowspan='2' class='text-center' style='width:35px;'><input type='checkbox' class='form-check-input checkFile' name='checkedFile' value='" + name + "'></td>\n";
	fileHtml += 		"<td rowspan='2' class='text-center py-2' style='width:80px;'>\n";
	if(imageThumbnail.hasOwnProperty(extension) && !isHidden) {
		fileHtml +=			"<img src='/api/thumbnailmaker?name=" + encodeURIComponent(path.replace(/\\/g, "/")) + "'>\n"
	} else {
		fileHtml +=			"<img src='/resources/img/fileicons/" + extensions[extension] + ".png' " + (isHidden ? "style='opacity:0.3;'" : "") + ">" + "\n";
	}
	fileHtml += 		"</td>\n";
	fileHtml += 		"<td colspan='3' class='text-break w-auto'>\n";
	fileHtml += 			"<span " + ( extension == "folder" ? "class='pointer' onClick=\"loadFileList('" + encodeURIComponent(path.replace(/\\/g, "/")) + "','','','',true)\" " : "" ) + ">"+ name + "</span>\n";
	fileHtml += 		"</td>\n";
	fileHtml += 	"</tr>\n";
	fileHtml += 	"<tr class='text-gray'>\n";
	fileHtml += 		"<td class='pe-2' style='width:80px;'>" + ( extension == "folder" ? "-" : fileSize(size) ) + "</td>\n";
	fileHtml += 		"<td class='text-break'><div style='" + ($("#keyword").val() ? "font-size:10px;'>" + path : "text-align: right;'>" + extensions[extension])  + "</div></td>\n";
	fileHtml += 		"<td class='text-center w-25'>" + moment(date, "x").format("YY/MM/DD HH:mm") + "</td>\n";
	fileHtml += 	"</tr>\n";
	fileHtml += "</table>\n";
	fileHtml += "</label>\n";

	return fileHtml;
}

// transrate filesize from byte
function fileSize(size) {
	if(size > 1073741824) {
		return (size/1073741824).toFixed(2) + "gb";
	} else if(size > 1048576) {
		return (size/1048576).toFixed(1) + "mb";
	} else if(size > 1024) {
		return (size/1024).toFixed(1) + "kb";
	} else {
		return size + "byte";
	}
}

// set file sort
function setFileMenu(path, sort, order) {
	$("#path").val(path);
	$("#sort").val(sort);
	$("#order").val(order);
	$("#nameSort").text("이름");
	$("#pathSort").text("경로");
	$("#typeSort").text("종류");
	$("#dateSort").text("날짜");
	$("#sizeSort").text("크기");

	$("#"+sort+"Sort").attr("onClick","loadFileList('', '" + sort + "', '" + (order=='asc'?'desc':'asc') + "', '')");
	(order=='asc') ? $("#"+sort+"Sort").append("↑") : $("#"+sort+"Sort").append("↓");
}

// search file with Enter key
function searchFileEnter(e) {
	if(e.keyCode == 13) loadFileList('','','','');
}

// search file with click search button
function searchFileClick() {
	loadFileList('','','','');
}

function toggleHiddenCheck() {
	$("#viewHiddenCheck").toggleClass("inactive");
	loadFileList('','','','');
}

// when select or unselect file
$(document).on("change", ".checkFile", function(){
	if($(this).is(":checked")) {
		$(this).closest("table").addClass("checked");
	} else {
		$(this).closest("table").removeClass("checked");
	}
	viewFileControlMenu();	
});

// view file control menu
function viewFileControlMenu() {
	if($(".fileTable").hasClass("checked")) {
		$("#fileControlMenu").removeClass("inactive");
	} else {
		$("#fileControlMenu").addClass("inactive");
	}
	makeFileControlMenu();
}

// make file control menu
function makeFileControlMenu() {
	let fileControlHtml = "";
	const cntSelected = $("input:checkbox[name=checkedFile]:checked").length;
	$("#fileControlMenu").html(cntSelected);
}