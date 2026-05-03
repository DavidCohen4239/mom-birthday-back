package com.example.mom_birthday_site.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongGuessResponse {
    private boolean success;
    private String message;
    private long foundCount;
    private String code;

    // בנאי עבור ניחוש שגוי או שיר שכבר נמצא
    public SongGuessResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // בנאי עבור ניחוש נכון
    public SongGuessResponse(boolean success, String message, long foundCount) {
        this.success = success;
        this.message = message;
        this.foundCount = foundCount;
    }

    // בנאי עבור ניחוש אחרון שמשלים את כל הארבעה
    public SongGuessResponse(boolean success, String message, long foundCount, String code) {
        this.success = success;
        this.message = message;
        this.foundCount = foundCount;
        this.code = code;
    }
}
