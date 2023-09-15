package com.project.everycloud.common.type;

public enum ResponseType {
    SUCCESS("200", "성공"),

    BAD_REQUEST("400", "잘못된 요청입니다."),
    WRONG_PATH("401", "삭제되었거나 잘못된 링크입니다."),
    NOT_LOGIN("403", "로그인이 필요한 서비스입니다."),
    NOT_ALLOWED("403", "접근권한이 없습니다."),
    NEED_PASSWORD("403", "패스워드가 필요한 서비스입니다."),
    WROING_LOGIN("403", "id와 password를 확인해주세요."),

    DUP_PK("702", "이미 등록된 데이터입니다."),
    NOT_EXIST_FILE("703", "업로드 파일이 없습니다."),
    NOT_ALLOW_FILE("704", "잘못된 파일 형식입니다.");

    String code;
    String message;

    ResponseType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
