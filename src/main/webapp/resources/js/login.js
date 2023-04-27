function pressEnter() {
	if (window.event.keyCode == 13) loginCheck();
}

function loginCheck() {
	const id = "" + $("#id").val();
	const pass = "" + $("#pass").val();
	let loginFail = "";

	if(!id) loginFail += "아이디를 입력해주세요\n";
	if(!pass) loginFail += "비밀번호를 입력해주세요\n";

	if(loginFail) {
		alert(loginFail);
	} else {
		$("#loginForm").submit();
	}
}