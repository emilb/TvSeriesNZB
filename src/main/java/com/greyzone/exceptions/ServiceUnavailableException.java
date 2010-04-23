package com.greyzone.exceptions;

@SuppressWarnings("serial")
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String msg) {
        super(msg);
    }

}
