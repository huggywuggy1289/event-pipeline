package com.eventpipeline.domain.global.apiPayload.exception;

import com.eventpipeline.domain.global.apiPayload.code.status.ErrorStatus;

public class GeneralException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public GeneralException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
