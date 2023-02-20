$(document).ready(function(){
	loadFileList($("#path").val(),"name","asc");
});

// loading FileList
function loadFileList(path, sort, order) {
	if(!path) path = $("#path").val();
	if(!sort) sort = $("#sort").val();
	if(!order) order = $("#order").val();
	let data = "path=" + path + "&sort=" + sort + "&order=" + order;
	console.log(data);
	$.ajax({
		url: "fileList",
		type: "post",
		data: data,
		success: function(result) {
			if(result.realPath) window.location.replace("/file?path=" + result.realPath);

			if(result.validPath) {
				$("#nowPath").html(pathLink(result.nowPath));

				const fileList = result.fileList;
				let listHtml = "<table class='w-100'><colgroup><col><col><col style='width:60%'><col style='width:10%'><col></colgroup>";

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
			$("#loadingList").css("display","none");
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
		parentsLink += encodeURIComponent(parents[i]) + "/";
		if(!parents[0]) parents[0] = "root";
		if(parents[i+2]) {
			parentsHtml += "<a href='/file?path=" + parentsLink + "' class='dropdown-item'>" + parents[i] + "</a>\n";
		} else if(parents[i+1]) {
			parentHtml += "<div class='mx-2'><a href='/file?path=" + parentsLink + "' id='parent'>" + parents[i] + "</a></div>\n<div><i class='fa-solid fa-angle-right'></i> </div>\n";
		} else if(parents[i]) {
			parentHtml += "<div class='mx-2'>" + parents[i] + "</div>\n";
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


function makeList(isDirectory, isHidden, path, name, extension, date, size) {
	if(isDirectory) {
		extension = "folder";
	} else if(!extensions.hasOwnProperty(extension)) {
		extension = "default";
	}

	let fileHtml = "";

	fileHtml += "<tr>\n";
	fileHtml += 	"<td ><input type='checkbox' class='form-check-input' name='checkedFile' value='" + name + "'></td>\n";
	fileHtml += 	"<td class='text-center p-2'>\n";
	if(imageThumbnail.hasOwnProperty(extension) && !isHidden) {
		fileHtml +=		"<img src='/api/thumbnailmaker?name=" + encodeURIComponent(path.replace(/\\/g, "/")) + "'>\n"
	} else {
		fileHtml +=		"<img src='/resources/img/fileicons/" + extensions[extension] + ".png'>" + "\n";
	}
	fileHtml += 	"</td>\n";
	fileHtml += 	"<td class='text-break'>\n";
	fileHtml += 		"<span " + ( extension == "folder" ? "class='pointer' onClick=\"loadFileList('" + encodeURIComponent(path.replace(/\\/g, "/")) + "','','')\" " : "" ) + ">"+ name + "</span>\n";
	fileHtml += 	"</td>\n";
	fileHtml += 	"<td>\n";
	fileHtml += 		extensions[extension] + "\n";
	fileHtml += 	"</td>\n";
	fileHtml += 	"<td>\n";
	fileHtml += 		"<div class='row'>\n";
	fileHtml += 			"<div class='col-12 col-xl-4'>" + ( extension == "folder" ? "-" : fileSize(size) ) + "</div>\n";
	fileHtml += 			"<div class='col-12 col-xl-8'>" + moment(date, "x").format("YY/MM/DD HH:mm") + "</div>\n";
	fileHtml += 		"</div>\n";
	fileHtml += 	"</td>\n";
	fileHtml += "</tr>";

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

function setFileMenu(path, sort, order) {	
	$("#path").val(path);
	$("#sort").val(sort);
	$("#order").val(order);
	$("#nameSort").text("이름");
	$("#typeSort").text("종류");
	$("#dateSort").text("날짜");
	$("#sizeSort").text("크기");

	$("#"+sort+"Sort").attr("onClick","loadFileList('', '" + sort + "', '" + (order=='asc'?'desc':'asc') + "')");
	(order=='asc') ? $("#"+sort+"Sort").append("↑") : $("#"+sort+"Sort").append("↓");
}