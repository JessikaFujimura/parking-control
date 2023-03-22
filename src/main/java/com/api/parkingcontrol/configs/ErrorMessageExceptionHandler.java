package com.api.parkingcontrol.configs;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ErrorMessageExceptionHandler {

    public final String KEY_CODE = "code";
    public final String KEY_REASON = "reason";
    public final String KEY_ERRORS = "errors";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, HttpStatus> exceptionResponseMap = new HashMap<>()
    {{
        put(IllegalArgumentException.class.getName(), HttpStatus.BAD_REQUEST);
        put(InvalidFormatException.class.getName(), HttpStatus.NOT_FOUND);
        put(NoHandlerFoundException.class.getName(), HttpStatus.NOT_FOUND);
        put(AccessDeniedException.class.getName(), HttpStatus.FORBIDDEN);
        put(HttpRequestMethodNotSupportedException.class.getName(), HttpStatus.METHOD_NOT_ALLOWED);
        put(HttpMessageNotReadableException.class.getName(), HttpStatus.BAD_REQUEST);
        put(MaxUploadSizeExceededException.class.getName(), HttpStatus.PAYLOAD_TOO_LARGE);
        put(NoSuchBeanDefinitionException.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
    }};

    private final Map<String, String> errorReasonMap = new HashMap<>() {{
        put(IllegalArgumentException.class.getName(), "Incorrect parameter type");
        put(InvalidFormatException.class.getName(), "Invalid format");
        put(NoHandlerFoundException.class.getName(), "The resource might not exists");
        put(AccessDeniedException.class.getName(), "You don't have permission");
        put(HttpRequestMethodNotSupportedException.class.getName(), "Http method is not support");
        put(HttpMessageNotReadableException.class.getName(), "Malformed JSON request");
        put(MaxUploadSizeExceededException.class.getName(), "The upload file is too large");
        put(NoSuchBeanDefinitionException.class.getName(), "There are some internal error");
    }};

    public Map<String, Object> createHttpResponseData(Exception ex) {
        Map<String, List<String>> errorMessages = (ex instanceof MultipleErrorMessage)
                ? ((MultipleErrorMessage) ex).getErrorMessages()
                : Collections.emptyMap();

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException exception = (ResponseStatusException) ex;
            return new HashMap<>() {{
                put(KEY_CODE, exception.getStatusCode().value());
                put(KEY_REASON, exception.getReason());
                put(KEY_ERRORS, exception.getMessage());
            }};
        }
        Class<? extends Exception> exception = ex.getClass();
        if(exception.isAnnotationPresent(ResponseStatus.class)) {
            ResponseStatus responseStatus = exception.getAnnotation(ResponseStatus.class);
            return new HashMap<>() {{
                put(KEY_CODE, responseStatus.code().value());
                put(KEY_REASON, responseStatus.reason());
                put(KEY_ERRORS, errorMessages);
            }};
        }

        HttpStatus httpStatus = exceptionResponseMap.get(ex.getClass().getName());
        if (httpStatus != null){
            return new HashMap<>() {{
                put(KEY_CODE, httpStatus.value());
                put(KEY_REASON, errorReasonMap.get(ex.getClass().getName()));
                put(KEY_ERRORS, Collections.emptyMap());
            }};
        }
        logger.error("Unexpect exception", ex);
        return new HashMap<>() {{
            put(KEY_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
            put(KEY_REASON, "Internal Server Error");
            put(KEY_ERRORS, Collections.emptyMap());
        }};
    }
}
