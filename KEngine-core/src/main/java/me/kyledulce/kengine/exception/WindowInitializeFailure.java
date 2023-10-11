package me.kyledulce.kengine.exception;

public class WindowInitializeFailure extends RuntimeException {
    public WindowInitializeFailure() {}

    public WindowInitializeFailure(String message) {
        super(message);
    }

    public WindowInitializeFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
