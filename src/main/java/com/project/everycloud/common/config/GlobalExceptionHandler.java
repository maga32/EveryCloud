package com.project.everycloud.common.config;

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

        //Auth Exception
        if(e instanceof HttpClientErrorException.Unauthorized) {
            apiResponseType = ResponseType.NOT_ALLOWED;
            apiResponseMessage = ResponseType.NOT_ALLOWED.message();
        } else if(e instanceof DuplicateKeyException) {
            apiResponseType = ResponseType.DUP_PK;
            apiResponseMessage = ResponseType.DUP_PK.message();
        } else if(e instanceof MethodArgumentNotValidException) {
            apiResponseType = ResponseType.NOT_VALID_AGUMENT;
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<String> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.toList());
            apiResponseMessage = errors.toString():
        } else {
            apiResponseType = ResponseType.ERROR;
            apiResponseMessage = ResponseType.ERROR.message();
        }

        AppResponse<String> apiResponse = new AppResponse<String>()
            .setCode(apiResponseType.code())
            .setMessage(apiResponseMessage);

        return apiResponse;
    }
}
