package com.project.everycloud.common.config;

import com.project.everycloud.common.exception.*;
import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public AppResponse<String> handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {

        ResponseType apiResponseType = null;
        String apiResponseMessage = "";

        // NoAuth
        if(e instanceof HttpClientErrorException.Unauthorized || e instanceof NotAllowedException) {
            apiResponseType = ResponseType.NOT_ALLOWED;
            apiResponseMessage = ResponseType.NOT_ALLOWED.message();
        // DupPk
        } else if(e instanceof DuplicateKeyException) {
            apiResponseType = ResponseType.DUP_PK;
            apiResponseMessage = ResponseType.DUP_PK.message();
        // ExistName
        } else if(e instanceof ExistNameException) {
            apiResponseType = ResponseType.EXIST_NAME;
            apiResponseMessage = e.getMessage() + " - " + ResponseType.EXIST_NAME.message();
        // NotExistFile
        } else if(e instanceof NotExistFileException) {
            apiResponseType = ResponseType.NOT_EXIST_FILE;
            apiResponseMessage = e.getMessage() + " - " + ResponseType.NOT_EXIST_FILE.message();
        // NotVaildArgument
        } else if(e instanceof MethodArgumentNotValidException) {
            apiResponseType = ResponseType.NOT_VALID_AGUMENT;
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<String> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.toList());
            apiResponseMessage = errors.toString();
        // NeedAdmin
        } else if(e instanceof NeedAdminException) {
            apiResponseType = ResponseType.NEED_ADMIN;
            apiResponseMessage = ResponseType.NEED_ADMIN.message();
        // NeedLogin
        } else if(e instanceof NeedLoginException) {
            apiResponseType = ResponseType.NEED_LOGIN;
            apiResponseMessage = ResponseType.NEED_LOGIN.message();
        // NeedPassword
        } else if(e instanceof NeedPasswordException) {
            apiResponseType = ResponseType.NEED_PASSWORD;
            apiResponseMessage = ResponseType.NEED_PASSWORD.message();
        // InvalidLogin
        } else if(e instanceof InvalidLoginException) {
            apiResponseType = ResponseType.INVALID_LOGIN;
            apiResponseMessage = ResponseType.INVALID_LOGIN.message();
        // InvalidPath
        } else if(e instanceof InvalidLinkException) {
            apiResponseType = ResponseType.INVALID_LINK;
            apiResponseMessage = ResponseType.INVALID_LINK.message();
        // InvalidPath
        } else if(e instanceof InvalidPathException) {
            apiResponseType = ResponseType.INVALID_PATH;
            apiResponseMessage = ResponseType.INVALID_PATH.message();
        // CertSiteError
        } else if(e instanceof CertSiteErrorException) {
            apiResponseType = ResponseType.CERT_SITE_ERROR;
            apiResponseMessage = ResponseType.CERT_SITE_ERROR.message();
        // 화면에 경고창 띄워주기(인증서버오류)
        // CertError
        } else if(e instanceof CertErrorException) {
            apiResponseType = ResponseType.CERT_ERROR;
            apiResponseMessage = ResponseType.CERT_ERROR.message();
        // BadRequest
        } else if(e instanceof BadRequestException) {
            apiResponseType = ResponseType.BAD_REQUEST;
            apiResponseMessage = ResponseType.BAD_REQUEST.message();
        // ETC
        } else {
            e.printStackTrace();
            apiResponseType = ResponseType.ERROR;
            apiResponseMessage = ResponseType.ERROR.message();
            // apiResponseMessage = e.toString();
        }

        AppResponse<String> apiResponse = new AppResponse<String>()
            .setCode(apiResponseType.code())
            .setMessage(apiResponseMessage);

        return apiResponse;
    }
}
