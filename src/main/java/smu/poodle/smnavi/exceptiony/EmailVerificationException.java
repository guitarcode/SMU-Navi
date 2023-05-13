package smu.poodle.smnavi.exceptiony;

public class EmailVerificationException extends RuntimeException {
    public EmailVerificationException(String message) {
        super(message);
    }
}
