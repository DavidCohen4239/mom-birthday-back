package com.example.mom_birthday_site.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TasteGuessResponse {
    private boolean success;
    private String message;
    private long secondsLeft;
    private String code;

    public TasteGuessResponse(long secondsLeft,String message) {
        this.success = false;
        this.secondsLeft = secondsLeft;
        this.message = message;
    }

    public TasteGuessResponse(String code, String message) {
        this.success = true;
        this.message = message;
        this.secondsLeft = 0;
        this.code = code;
    }
    public TasteGuessResponse(String message) {
        this.success = false;
        this.message = message;
    }

    public TasteGuessResponse(boolean success, long secondsLeft) {
        this.success = success;
        this.secondsLeft = secondsLeft;
    }

}
