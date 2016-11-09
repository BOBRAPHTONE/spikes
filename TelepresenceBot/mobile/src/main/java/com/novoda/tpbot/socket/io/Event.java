package com.novoda.tpbot.socket.io;

public enum Event {

    CONNECTED("connected"),
    ERROR("error"),
    JOIN("join");

    private final String code;

    Event(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
