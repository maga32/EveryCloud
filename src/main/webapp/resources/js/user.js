const emailRegex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;

function checkPassword() {
    if($("#pass").val() && $("#pass").val().length < 8){
        $("#warningPassLength").removeClass("d-none");
    } else {
        $("#warningPassLength").addClass("d-none");
        if ($("#pass").val() == $("#confirmPass").val()) {
            $("#warningPassCheck").addClass("d-none");
        } else {
            $("#warningPassCheck").removeClass("d-none");
        }
    }
}

function checkOverlapId() {
    if(!$("#id").val()) return reject("id", "아이디를 입력해주세요.");
    let data = "id=" + $("#id").val();

    $.ajax({
        url: "/checkOverlapId",
        type: "post",
        async: false,
        data: data,
        success: function(result) {
            if(result.result == "ok") {
                alert("중복체크 완료.");
                $("#duplicateChecked").val("true");
            } else {
                alert("이미 존재하는 아이디입니다.");
            }
        },
        error: function() {
            alert("잘못된 접근입니다");
        }
    });
}

function updateUserForm() {
    if(!$("#id").val()) return reject("id", "아이디를 입력해주세요.");
    if($("#duplicateChecked").val() == "false") return reject("id", "아이디 중복확인을 해주세요.");
    if($("#pass").val() && $("#pass").val().length < 8) return reject("pass", "비밀번호는 8자이상 설정해주세요.");
    if($("#pass").val() != $("#confirmPass").val()) return reject("confirmPass", "비밀번호 일치여부를 확인해주세요.");
    if(!$("#nickname").val()) return reject("nickname", "닉네임을 입력해주세요.");
    if(!$("#email").val()) return reject("email", "이메일을 입력해주세요.");
    if(!emailRegex.test($("#email").val())) return reject("email", "올바른 이메일을 입력해주세요.");
    $("#updateUserForm").submit();
}

function reject(pointer, reason) {
    alert(reason);
    $("#"+pointer).focus();
    return false;
}