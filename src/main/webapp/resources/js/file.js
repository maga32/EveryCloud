let shareFileList = new Array() ;

$(document).ready(function() {
	loadFileList($("#shareLink").val(),$("#path").val(),$("#sort").val(),$("#order").val(),$("#keyword").val());

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
		loadFileList(event.state.shareLink, event.state.path, event.state.sort, event.state.order, event.state.keyword, false, false);
    }
});

// loading FileList
function loadFileList(shareLink, path, sort, order, keyword, resetKeyword = false, saveHistory = true) {
	$("#fileList").html("");
	$("#loadingList").addClass("act");
	$("#checkAllFile").prop("checked", false);
	viewFileControlMenu();

	if(!shareLink) shareLink = $("#shareLink").val();
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

	let data = "shareLink=" + shareLink + "&path=" + path + "&sort=" + sort + "&order=" + order + "&keyword=" + keyword;
	if(!$("#viewHiddenCheck").hasClass("inactive")) data += "&viewHidden=true	";

	$.ajax({
		url: "/fileList",
		type: "post",
		data: data,
		success: function(result) {
			if(result.invalidAuth) {
				alert(result.invalidAuth);
				return false;
			}

			if(result.validPath) {
				$("#nowPath").html(pathLink(shareLink, result.nowPath));

				const fileList = result.fileList;
				let listHtml = "";

				for(i in fileList) {
					listHtml += makeList(fileList[i].isDirectory, fileList[i].isHidden, shareLink, fileList[i].getPath, fileList[i].getName, fileList[i].getExtension, fileList[i].lastModified, fileList[i].length);
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
				history.pushState({"shareLink":shareLink , "path":path, "sort":sort, "order":order, "keyword":keyword}, null, "file?shareLink=" + shareLink + "&path=" + path + "&sort=" + sort + "&order=" + order + "&keyword=" + keyword);
			}
		}
	});
}

// define link of #nowPath
function pathLink(shareLink, path) {
	const parents = path.replace(/\\/g, "/").split("/");
	let parentsHtml = "";
	let parentHtml = "";
	let parentsLink = "";

	for(let i=0; i < parents.length; i++) {
		parentsLink += parents[i] + "/";
		if(!parents[0]) parents[0] = "root";
		if(parents[i+2]) {
			parentsHtml += "<div class='pointer dropdown-item' onclick=\"loadFileList('" + shareLink + "','" + encodeURIComponent(parentsLink) + "','','','',true)\" style='white-space:normal'>" + parents[i] + "</div>\n";
		} else if(parents[i+1]) {
			parentHtml += "<div class='px-2 text-truncate'><span class='pointer' onclick=\"loadFileList('" + shareLink + "','" + encodeURIComponent(parentsLink) + "','','','',true)\">" + parents[i] + "</span></div>\n<div><i class='fa-solid fa-angle-right'></i> </div>\n";
		} else if(parents[i]) {
			parentHtml += "<div class='px-2 text-truncate'><span class='pointer' onclick=\"loadFileList('" + shareLink + "','" + encodeURIComponent(parentsLink) + "','','','',true)\" id='parent'>" + parents[i] + "</span></div>\n";
		}
	}

	let resultHtml = "<div class='d-flex'>";

	if(parents.length >2) {
		resultHtml += 	"<div class='btn-group'>"
					+		"<span class='dropdown-toggle pointer' data-bs-toggle='dropdown' aria-expanded='false'>"
					+			"<i class='fa-solid fa-house'></i>"
					+		"</span>"
					+		"<span class='dropdown-menu dropdown-menu-start' style='max-width:300px; word-break:break-all;'>"
					+			parentsHtml
					+		"</span>"
					+	"</div>";
	}

	resultHtml += parentHtml + "</div>";

	return resultHtml;
}

// make a file list each line
function makeList(isDirectory, isHidden, shareLink, path, name, extension, date, size) {
	if(isDirectory) {
		extension = "folder";
	} else if(!extensions.hasOwnProperty(extension)) {
		extension = "default";
	}

	let fileHtml = "";
	fileHtml += "<label class='w-100 pe-2 pe-md-5'>\n";
	fileHtml += "<table class='w-100 rounded fileTable'>\n";
	fileHtml += 	"<tr>\n";
	fileHtml += 		"<td class='text-center' style='width:35px;'><input type='checkbox' class='form-check-input checkFile' name='checkedFile' value='" + name + "'></td>\n";
	fileHtml += 		"<td class='text-center py-2' style='width:80px;'>\n";
	if(imageThumbnail.hasOwnProperty(extension) && !isHidden) {
		fileHtml +=			"<img class='fileImg' src='/api/thumbnailmaker?shareLink=" + shareLink + "&name=" + encodeURIComponent(path.replace(/\\/g, "/")) + "' width='64px'>\n"
	} else {
		fileHtml +=			"<img class='fileImg' src='/resources/img/fileicons/" + extensions[extension] + ".png' " + (isHidden ? "style='opacity:0.3;'" : "") + ">" + "\n";
	}
	fileHtml += 		"</td>\n";
	fileHtml += 		"<td class='w-auto'>\n";
	fileHtml += 			"<div>\n";
	fileHtml += 				"<span class='fileName " + ( extension == "folder" ? "pointer' onClick=\"loadFileList('" + shareLink + "','"+ encodeURIComponent(path.replace(/\\/g, "/")) + "','','','',true)\" " : "'" ) + " style='word-break:break-all'>"+ name + "</span>\n";
	fileHtml += 			"</div>\n";
	fileHtml += 			"<div class='text-gray d-flex align-items-center'>\n";
	fileHtml += 				"<div class='flex-shrink-0' style='width:60px;'>" + ( extension == "folder" ? "-" : fileSize(size) ) + "</div>\n";
	fileHtml += 				"<div class='flex-grow-1' style='word-break:break-all; flex-basis: 110px; padding: 0 10px;'>\n";
	fileHtml += 					"<div style='min-width:55px; " + ( $("#keyword").val() ? "font-size:10px;'>" + (shareLink ? "shareLink : " + shareLink + " / " : "") + path : "text-align: right;'>" + extensions[extension] )  + "</div>\n";
	fileHtml += 				"</div>\n";
	fileHtml += 				"<div style='word-break:keep-all; text-align:right'>" + moment(date, "x").format("YY/MM/DD HH:mm") + "</div>\n";
	fileHtml += 			"</div>\n";
	fileHtml += 		"</td>\n";
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

	$("#"+sort+"Sort").attr("onClick","loadFileList('', '', '" + sort + "', '" + (order=='asc'?'desc':'asc') + "', '')");
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

	if(cntSelected > 1) {
		fileControlHtml += "<div class='px-1'>" + cntSelected + "개의 파일 선택</div>\n";
		fileControlHtml += "<div class='text-center px-2 py-3'><img src='/resources/img/fileicons/files.png'></div>\n";
		fileControlHtml += "<table>\n";
		fileControlHtml += 		"<tr class='pointer'>\n";
		fileControlHtml += 			"<td class='p-1'><i class='fa-solid fa-cloud-arrow-down'></i></td>\n";
		fileControlHtml += 			"<td class='p-1' onclick=\"downloadFiles()\">다운로드</td>\n";
		fileControlHtml += 		"</tr>\n"
		fileControlHtml += 		"<tr class='pointer'>\n";
		fileControlHtml += 			"<td class='p-1'><i class='fa-solid fa-share'></i></td>\n";
		fileControlHtml += 			"<td class='p-1' onclick=\"moveFiles('','moveFiles')\" data-bs-toggle='modal' data-bs-target='#functionModal'>이동</td>\n";
		fileControlHtml += 		"</tr>\n";
		fileControlHtml += 		"<tr class='pointer'>\n";
		fileControlHtml += 			"<td class='p-1'><i class='fa-solid fa-clipboard'></i></td>\n"
		fileControlHtml += 			"<td class='p-1' onclick=\"moveFiles('','copyFiles')\" data-bs-toggle='modal' data-bs-target='#functionModal'>복사</td>\n";
		fileControlHtml += 		"</tr>\n";
		fileControlHtml += 		"<tr class='pointer'>\n";
		fileControlHtml += 			"<td class='p-1'><i class='fa-solid fa-trash'></i></td>\n";
		fileControlHtml += 			"<td class='p-1 pointer' onclick=\"deleteFiles()\" data-bs-toggle='modal' data-bs-target='#functionModal'>삭제</td>\n";
		fileControlHtml += 		"</tr>\n";
		fileControlHtml += "</table>\n";
	} else if(cntSelected == 1) {
		fileControlHtml += "<div class='px-1' style='word-break:break-all;'>" + $("table.checked .fileName").text() + "</div>\n";
		fileControlHtml += "<div class='text-center px-2 py-3'><img src='" + $("table.checked img").attr("src") + "' style='min-width:64px;'></div>\n";
		fileControlHtml += "<table>\n";
		fileControlHtml += 		"<tr>\n";
		fileControlHtml += 			"<td class='p-1'><i class='fa-solid fa-star'></i></td>\n";
		fileControlHtml += 			"<td class='p-1'>즐겨찾기</td>\n";
		fileControlHtml += 		"</tr>\n"
		fileControlHtml += 		"<tr>\n";
		fileControlHtml += 			"<td class='p-1'><i class='fa-solid fa-pen-to-square'></i></td>\n";
		fileControlHtml += 			"<td class='p-1 pointer' onclick=\"changeFileName()\" data-bs-toggle='modal' data-bs-target='#functionModal'>이름바꾸기</td>\n";
		fileControlHtml += 		"</tr>\n";
		fileControlHtml += 		"<tr>\n";
		fileControlHtml += 			"<td class='p-1'><i class='fa-solid fa-share-nodes'></i></td>\n"
		fileControlHtml += 			"<td class='p-1 pointer' onclick=\"shareFile()\" data-bs-toggle='modal' data-bs-target='#functionModal'>공유</td>\n";
		fileControlHtml += 		"</tr>\n";
		fileControlHtml += 		"<tr>\n";
		fileControlHtml += 			"<td colspan='2' class='p-2'>\n";
		fileControlHtml += 				"<div class='btn-group'>\n";
  		fileControlHtml += 					"<button type='button' class='btn btn-outline-secondary' onclick=\"downloadFiles()\"><i class='fa-solid fa-cloud-arrow-down'></i></button>\n";
  		fileControlHtml += 					"<button type='button' class='btn btn-outline-secondary' onclick=\"moveFiles('','moveFiles')\" data-bs-toggle='modal' data-bs-target='#functionModal'><i class='fa-solid fa-share'></i></button>\n";
  		fileControlHtml += 					"<button type='button' class='btn btn-outline-secondary' onclick=\"moveFiles('','copyFiles')\" data-bs-toggle='modal' data-bs-target='#functionModal'><i class='fa-solid fa-clipboard'></i></button>\n";
  		fileControlHtml += 					"<button type='button' class='btn btn-outline-secondary' onclick=\"deleteFiles()\" data-bs-toggle='modal' data-bs-target='#functionModal'><i class='fa-solid fa-trash'></i></button>\n";
		fileControlHtml += 				"</div>\n";
		fileControlHtml += 			"</td>\n";
		fileControlHtml += 		"</tr>\n";
		fileControlHtml += "</table>\n";
	}

	$("#fileControlMenu").html(fileControlHtml);
}

// create new directory
function newFolder() {
	let htmlText = "<input type='text' class='form-control' id='newFolderName' placeholder='폴더명을 입력해주세요'>"
				+ "<input type='hidden' id='functionModalAct' value='newFolder'>";
	$("#functionModalLabel").text("폴더 만들기");
	$("#functionModalBody").html(htmlText);
	focusById("newFolderName");
}

// create new file
function newFile() {
	let htmlText = "<input type='text' class='form-control' id='newFileName' placeholder='파일명을 입력해주세요'>"
				+ "<input type='hidden' id='functionModalAct' value='newFile'>";
	$("#functionModalLabel").text("파일 만들기");
	$("#functionModalBody").html(htmlText);
	focusById("newFileName");
}

// change the file name
function changeFileName() {
	let htmlText = "<input type='text' class='form-control' id='newFileName' value='" + $("input:checkbox[name=checkedFile]:checked").val() + "'>"
				+ "<input type='hidden' id='functionModalAct' value='changeName'>";
	$("#functionModalLabel").text("이름변경");
	$("#functionModalBody").html(htmlText);
	focusById("newFileName");
}

// share file
function shareFile() {
	let htmlText = "";
	let sharedFullLink = "";

	$.ajax({
		url: "/shareNewFile",
		type: "post",
		async: false,
		data: "path=" + $("#path").val() + "&fileName=" + $("input:checkbox[name=checkedFile]:checked").val(),
		success: function(result) {
			if(result.result != "ok") {
				alert(result.result);
				return false;
			} else {
				sharedFullLink = result.sharedFullLink;
				shareFileList[0] = result.sharedFile;
				console.log(shareFileList);
			}
		},
		error: function() {
			console.log("error");
		}
	});

	htmlText = "<input type='text' class='form-control' id='sharedLink' value='" + (sharedFullLink || "링크생성오류") + "'>"
		+ "<input type='hidden' id='functionModalAct' value='shareFile'>";
	$("#functionModalLabel").text("공유");
	$("#functionModalBody").html(htmlText);
}

// cursor on input box
function focusById(id) {
	setTimeout(()=>{
		$("#"+id).focus();
		$("#"+id)[0].setSelectionRange($("#"+id).val().length, $("#"+id).val().length);
	},500)
}

/* move(or copy) files
* type - moveFiles / copyFiles
*/
function moveFiles(path="", type="moveFiles") {
	if(!path) path = decodeURIComponent($("#path").val());
	let htmlText = loadFolderList(path, type) 
				+ "<input type='hidden' id='moveToPath' value='" + path + "'>"
				+ "<input type='hidden' id='functionModalAct' value='" + type + "'>";
	let modalLabelHtml = (type == "moveFiles" ? "이동" : "복사") + " : " + path
					+	"<div class='d-flex pt-4 align-items-center'>"
					+		"<div class='d-none w-100 pe-4' id='moveFilesNewFolderOpen'>"
					+			"<input type='text' class='form-control' id='" + type + "NewFolderName' name='" + type + "NewFolderName'>"
					+		"</div>"
					+		"<div class='flex-shrink-1 pointer' onclick=\"moveFilesNewFolder('" + type + "')\"><i class='fa-solid fa-folder-plus'></i></div>"
					+	"</div>";
	$("#functionModalLabel").html(modalLabelHtml);
	$("#functionModalBody").html(htmlText);
}

// folder lists for move(or copy) files
function loadFolderList(path, type) {
	let data = "path=" + encodeURIComponent(path);
	let listHtml = "<table>";

	$.ajax({
		url: "/folderList",
		type: "post",
		async: false,
		data: data,
		success: function(result) {
			if(result.validPath) {
				const folderList = result.folderList;

				listHtml += "<tr class='pointer' onclick=\"moveFiles('" + path.replace(/\b\/[^/]*$|[^:/]*$/, "") + "', '" + type + "')\">\n"
						+ 		"<td class='text-center py-2' style='width:50px;'>\n"
						+ 			"<img class='fileImg' src='/resources/img/fileicons/" + extensions["folder"] + ".png' >\n"
						+ 		"</td>\n"
						+		"<td class='w-auto'>..</td>\n"
						+ 	"</tr>\n";

				for(i in folderList) {
					listHtml += makeFolderList(folderList[i].getPath, folderList[i].getName, type);
				}

			} else {
				listHtml += "<tr><td>잘못된 경로입니다.</td></tr>";
			}
		},
		error: function() {
			listHtml += "<tr><td>잘못된 접근입니다.</td></tr>";
		},
		complete: function() {
			listHtml += "</table>";
		}
	});

	return listHtml;
}

/** create new folder when changing files path
*	type - moveFiles / copyFiles
*/
function moveFilesNewFolder(type) {
	if($("#moveFilesNewFolderOpen").hasClass("d-none")) {
		$("#moveFilesNewFolderOpen").removeClass("d-none");
	} else {
		$.ajax({
			url: "/newFolder",
			type: "post",
			data: "path=" + $("#moveToPath").val() + "&newFolderName=" + $("#moveFilesNewFolderName").val(),
			success: function(result) {
				if(result.result != "ok") {
					alert(result.result);
					return false;
				}
			},
			error: function() {
				console.log("error");
			},
			complete: function() {
				moveFiles($("#moveToPath").val()+"/"+$("#moveFilesNewFolderName").val(), type);
			}
		});
	}
}

// make a folder list each line for move files
function makeFolderList(path, name, type) {
	let makeFolderListHtml = "<tr class='pointer' onclick=\"moveFiles('" + path.replace(/\\/g, "/") + "', '" + type + "')\">\n";
	makeFolderListHtml += 		"<td class='text-center py-2' style='width:50px;'>\n";
    makeFolderListHtml += 			"<img class='fileImg' src='/resources/img/fileicons/" + extensions["folder"] + ".png' >\n";
    makeFolderListHtml += 		"</td>\n";
    makeFolderListHtml += 		"<td class='w-auto'>\n";
    makeFolderListHtml += 			"<div>" + name + "</div>\n";
    makeFolderListHtml += 		"</td>\n";
	makeFolderListHtml += "</tr>\n";

	return makeFolderListHtml;
}

// delete files
function deleteFiles() {
	let fileNames = "";
	$("input:checkbox[name=checkedFile]:checked").each(function() {fileNames += $(this).val() + "<br>";});
	let htmlText = "<div class='text-danger'>아래 " + $("input:checkbox[name=checkedFile]:checked").length + "개의 파일을 삭제하시겠습니까?</div><br>"
				+ fileNames
				+ "<input type='hidden' id='functionModalAct' value='deleteFiles'>";
	$("#functionModalLabel").text("파일삭제");
	$("#functionModalBody").html(htmlText);
}

// download files
function downloadFiles() {
	let fileNames = "";
	$("input:checkbox[name=checkedFile]:checked").each(function() {fileNames += encodeURIComponent($(this).val()) + ",";});
	fileNames = fileNames.slice(0, -1);
	window.open("/fileDownload?path=" + $("#path").val() + "&fileNames=" + fileNames);
}

// execute function
function functionModalAffect() {
	let action = $("#functionModalAct").val();
	let data = "";
	let url = "";
	let selectedFiles = "";
	let errorMsg = "";

	$("input:checkbox[name=checkedFile]:checked").each(function() {selectedFiles += encodeURIComponent($(this).val()) + ",";});
	selectedFiles = selectedFiles.slice(0, -1);

	if(action == "newFolder") {
		let newFolderName = encodeURIComponent($("#newFolderName").val());
		if(!newFolderName) errorMsg+= "폴더명을 적어주세요.";

		data = "path=" + $("#path").val() + "&newFolderName=" + newFolderName;
		url = "/newFolder";
	} else if(action == "newFile") {
		let newFileName = encodeURIComponent($("#newFileName").val());
		if(!newFileName) errorMsg+= "파일명을 적어주세요.";

		data = "path=" + $("#path").val() + "&newFileName=" + newFileName;
		url = "/newFile";
	} else if(action == "changeName") {
		let newFileName = encodeURIComponent($("#newFileName").val());
		if(!newFileName) errorMsg+= "파일명을 적어주세요.";

		data = "path=" + $("#path").val() + "&origFileName=" + selectedFiles + "&newFileName=" + newFileName;
		url = "/chageName";
	} else if(action == "deleteFiles") {
		data = "path=" + $("#path").val() + "&fileNames=" + selectedFiles;
		url = "/deleteFiles";
	} else if(action == "moveFiles" || action == "copyFiles") {
		let moveToPath = encodeURIComponent($("#moveToPath").val());
		let path = $("#path").val();
		if(moveToPath == path && action == "moveFiles") errorMsg+= "현재 위치와 동일합니다.";

		data = "path=" + path + "&fileNames=" + selectedFiles + "&moveToPath=" + moveToPath + "&type=" + action;
		url = "/moveFiles";
	} else if(action == "shareFile") {
		$("#sharedLink").select();
		document.execCommand("copy");
		alert("링크가 복사되었습니다");
		return false;
	}

	if(errorMsg) {
		alert(errorMsg);
		return false;
	}

	$("#fileList").addClass("d-none");
	$("#loadingList").addClass("act");

	$.ajax({
		url: url,
		type: "post",
		data: data,
		success: function(result) {
			if(result.result != "ok") alert(result.result);
		},
		error: function() {
			console.log("error");
		},
		complete: function() {
			loadFileList('','','','');
			$("#fileList").removeClass("d-none");
		}
	});
}