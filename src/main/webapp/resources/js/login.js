function pressEnter() {
	if (window.event.keyCode == 13) loginCheck();
}

function loginCheck() {
	const userId = "" + $("#userId").val();
	const userPass = "" + $("#userPass").val();
	let loginFail = "";

	if(!userId) loginFail += "아이디를 입력해주세요\n";
	if(!userPass) loginFail += "비밀번호를 입력해주세요\n";

	if(loginFail) {
		alert(loginFail);
	} else {
		$("#loginForm").submit();
	}
}