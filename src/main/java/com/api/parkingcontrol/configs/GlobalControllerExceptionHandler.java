package com.api.parkingcontrol.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorMessageLoggingHandler loggingHandler = new ErrorMessageLoggingHandler();
    private final ErrorMessageExceptionHandler exceptionHandler = new ErrorMessageExceptionHandler();

    @ExceptionHandler
    public void handleGeneralException(Exception ex, HttpServletResponse response) throws IOException {
        loggingHandler.perform(ex);
        Map<String, Object> responseData = exceptionHandler.createHttpResponseData(ex);
        Integer responseCode = (Integer) responseData.get(exceptionHandler.KEY_CODE);

        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(responseCode);

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}
