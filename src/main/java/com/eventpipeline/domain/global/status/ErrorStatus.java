package com.eventpipeline.domain.global.status;

import org.springframework.http.HttpStatus;

public enum ErrorStatus {
    INTERNAL_ERROR("S001", "서버 내부 오류가 발생했습니다."),
    INVALID_TOKEN("TOKEN4003", "잘못된 토큰입니다."),
    EXPENSE_FORBIDDEN("EXP403", "해당 지출 내역에 대한 권한이 없습니다."),
    EXPENSE_NOT_FOUND("EXP404", "해당 지출 내역을 찾을 수 없습니다.");


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
