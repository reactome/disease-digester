package org.reactome.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Byron
 */
@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class FailedAnalyzeDiseaseException extends Exception {
    public FailedAnalyzeDiseaseException(String message) {
        super(message);
    }
}
