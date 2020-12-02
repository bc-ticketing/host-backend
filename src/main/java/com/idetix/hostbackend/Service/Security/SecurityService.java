package com.idetix.hostbackend.Service.Security;

public interface SecurityService {
    boolean verifyAddressFromSignature(String address, String signature, String message);

    String getAlphaNumericString(int n);
}
