package com.project.everycloud.common.config;

import com.project.everycloud.common.exception.BadRequestException;
import com.project.everycloud.common.exception.NeedAdminException;
import com.project.everycloud.common.exception.NeedLoginException;
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
import java.util.stream.Collectors;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public AppResponse<String> handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {

        ResponseType apiResponseType = null;
        String apiResponseMessage = "";

        // NoAuth
        if(e instanceof HttpClientErrorException.Unauthorized) {
            apiResponseType = ResponseType.NOT_ALLOWED;
            apiResponseMessage = ResponseType.NOT_ALLOWED.message();
        // DupPk
        } else if(e instanceof DuplicateKeyException) {
            apiResponseType = ResponseType.DUP_PK;
            apiResponseMessage = ResponseType.DUP_PK.message();
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
        // BadRequest
        } else if(e instanceof BadRequestException) {
            apiResponseType = ResponseType.BAD_REQUEST;
            apiResponseMessage = ResponseType.BAD_REQUEST.message();
        // ETC
        } else {
            apiResponseType = ResponseType.ERROR;
            // apiResponseMessage = ResponseType.ERROR.message();
            apiResponseMessage = e.toString();
        }

        AppResponse<String> apiResponse = new AppResponse<String>()
            .setCode(apiResponseType.code())
            .setMessage(apiResponseMessage);

        return apiResponse;
    }
}
