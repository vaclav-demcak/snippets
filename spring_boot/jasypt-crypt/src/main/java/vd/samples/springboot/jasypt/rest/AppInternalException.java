package vd.samples.springboot.jasypt.rest;

public class AppInternalException extends RuntimeException {

    public AppInternalException(Throwable thr) {
        super(thr);
    }
}
