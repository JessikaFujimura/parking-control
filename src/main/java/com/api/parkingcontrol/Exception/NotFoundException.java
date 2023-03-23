package com.api.parkingcontrol.Exception;

import com.api.parkingcontrol.configs.Logging;
import com.api.parkingcontrol.configs.Logging.Level;
import com.api.parkingcontrol.configs.MultipleErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.util.List;
import java.util.Map;

@Logging(level = Level.ERROR)
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException implements MultipleErrorMessage {

    @Serial
    private static final long serialVersionUID = 6964670367101916282L;

    private final Map<String, List<String>> errorMessage;

    public NotFoundException(Map<String, List<String>> err) {
        this.errorMessage = Map.copyOf(err);
    }

    @Override
    public Map<String, List<String>> getErrorMessages() {
        return errorMessage;
    }
}
