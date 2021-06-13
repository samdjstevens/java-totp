package dev.samstevens.totp.recovery;

import java.security.InvalidParameterException;

public class InsecureCodeException extends InvalidParameterException {
    public InsecureCodeException(){}
    public InsecureCodeException(String s){
        super(s);
    }
}
