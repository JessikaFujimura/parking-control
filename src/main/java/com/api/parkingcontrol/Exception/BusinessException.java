package com.api.parkingcontrol.Exception;

import com.api.parkingcontrol.configs.MultipleErrorMessage;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class BusinessException extends RuntimeException  implements MultipleErrorMessage {

    @Serial
    private static final long serialVersionUID = 6964670367101916282L;

    private final Map<String, List<String>> errorMessage;

    public BusinessException(String message, Map<String, List<String>> err) {
        super(message);
        this.errorMessage = Map.copyOf(err);
    }

    @Override
    public Map<String, List<String>> getErrorMessages() {
        return errorMessage;
    }
}
