$(document).ready(function(){
	loadFileList();
});

// loading FileList
function loadFileList() {
	let data = "path=" + $("#path").val();
	$.ajax({
		url: "fileList",
		type: "post",
		data: data,
		success: function(result) {
			if(result.realPath) window.location.replace("/file?path=" + result.realPath);

			if(result.validPath) {
				$("#nowPath").html(pathLink(result.nowPath));

				const fileList = result.fileList;
				let listHtml = "";
				let extension;

				for(i in fileList) {
					listHtml += makeList(fileList[i].isDirectory, fileList[i].isHidden, fileList[i].getPath, fileList[i].getName, fileList[i].getExtension, fileList[i].lastModified, fileList[i].length);
				}

				$("#fileList").html(listHtml);
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
	const parents = path.split("/");
	let parentsHtml = "";
	let parentsLink = "";

	// for mac,linux
	if(!parents[0]) parentsLink += "/";
	
	for(let i=0; i < parents.length; i++) {
		parentsLink += parents[i] + "/";
		if(i==0 && !parents[i]) parents[0] = "root";
		if(parents[i+1]) {
			parentsHtml += "<a href='/file?path=" + parentsLink + "'>" + parents[i] + "</a> <i class='fa-solid fa-angle-right'></i> ";
		} else {
			parentsHtml += "<a href='/file?path=" + parentsLink + "' id='parent'>" + parents[i] + "</a>";
		}
	}

	return parentsHtml;
}


function makeList(isDirectory, isHidden, path, name, extension, date, size) {
	if(isDirectory) {
		extension = "folder";
	} else if(!extensions.hasOwnProperty(extension)) {
		extension = "default";
	}

	let fileHtml = "";

	fileHtml += "<span class='fileList'>\n";
	fileHtml += "	<span " + ( extension == "folder" ? "class='pointer' onClick=\"location.href='/file?path=" + path + "'\" " : "" ) + ">\n";
	if(imageThumbnail.hasOwnProperty(extension) && !isHidden) {
		fileHtml += "		" + "<img src='/api/thumbnailmaker?name=" + path + "'>\n"
	} else {
		fileHtml += "		" + "<img src='/resources/img/fileicons/" + extensions[extension] + ".png'>" + "\n";
	}
	fileHtml += "		" + name + "<br>\n";
	fileHtml += "	</span>\n";
	fileHtml += "	<span>\n";
	fileHtml += "		날짜 : " + moment(date, "x").format("YY/MM/DD HH:mm") + "\n";
	fileHtml += "	</span><br>\n";
	fileHtml += "	<span>\n";
	fileHtml += "		크기 : " + ( extension == "folder" ? "-" : fileSize(size) ) + "\n";
	fileHtml += "	</span><br>\n";
	fileHtml += "</span><br>\n";

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