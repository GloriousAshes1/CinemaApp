package com.example.finalprojectmobileapplication.event;

public class ResultQrCodeEvent {
    private String result;

    public ResultQrCodeEvent(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
