package com.sdk.rh.networking;

public class ApiResponse {

    private String status;
    private Subscriber data;
    private String message;
    private String code;
    private int callsLeft;
    private long timestamp;

    public String getStatus() {
        return status != null ? status : "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return status.equalsIgnoreCase("ok");
    }

    public Subscriber getData() {
        return data != null ? data : null;
    }

    public void setData(Subscriber data) {
        this.data = data;
    }

    public int getCallsLeft() {
        return callsLeft != 0 ? callsLeft : 0;
    }

    public void setCallsLeft(int callsLeft) {
        this.callsLeft = callsLeft;
    }

    public long getTimestamp() {
        return timestamp != 0 ? timestamp : 0;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
