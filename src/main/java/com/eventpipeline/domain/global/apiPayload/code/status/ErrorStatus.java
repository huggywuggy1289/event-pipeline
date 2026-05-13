package com.eventpipeline.domain.global.apiPayload.code.status;

import org.springframework.http.HttpStatus;

public enum ErrorStatus {
    INTERNAL_ERROR("5000", "서버 내부 오류가 발생했습니다.");


    private final String code;
    private final String message;

    ErrorStatus(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
}
