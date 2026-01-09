package com.in.mzncvmc.common.system.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationResponse {
    private final boolean success;
    private final String message;
    private final boolean deleted;

    public VerificationResponse(boolean success, String message, boolean deleted) {
        this.success = success;
        this.message = message;
        this.deleted = deleted;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public boolean isDeleted() { return deleted; }
}
