const emailRegex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;

function checkPassword() {
    if($("#userPass").val() && $("#userPass").val().length < 8){
        $("#warningPassLength").removeClass("d-none");
    } else {
        $("#warningPassLength").addClass("d-none");
        if ($("#userPass").val() == $("#confirmUserPass").val()) {
            $("#warningPassCheck").addClass("d-none");
        } else {
            $("#warningPassCheck").removeClass("d-none");
        }
    }
}

function checkOverlapId() {
    if(!$("#userId").val()) return reject("userId", "아이디를 입력해주세요.");
    let data = "userId=" + $("#userId").val();

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
    if(!$("#userId").val()) return reject("userId", "아이디를 입력해주세요.");
    if($("#duplicateChecked").val() == "false") return reject("userId", "아이디 중복확인을 해주세요.");
    if($("#userPass").val() && $("#userPass").val().length < 8) return reject("userPass", "비밀번호는 8자이상 설정해주세요.");
    if($("#userPass").val() != $("#confirmUserPass").val()) return reject("confirmUserPass", "비밀번호 일치여부를 확인해주세요.");
    if(!$("#userNickname").val()) return reject("userNickname", "닉네임을 입력해주세요.");
    if(!$("#userEmail").val()) return reject("userEmail", "이메일을 입력해주세요.");
    if(!emailRegex.test($("#userEmail").val())) return reject("userEmail", "올바른 이메일을 입력해주세요.");
    $("#updateUserForm").submit();
}

function reject(pointer, reason) {
    alert(reason);
    $("#"+pointer).focus();
    return false;
}