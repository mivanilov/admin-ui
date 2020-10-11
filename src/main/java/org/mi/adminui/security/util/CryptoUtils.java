package org.mi.adminui.security.util;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;

@Component
public class CryptoUtils {

    private static final String SALT = "050f8d044ba51d74";

    public String encrypt(String text, String secret) {
        return Encryptors.text(secret, SALT).encrypt(text);
    }

    public String decrypt(String text, String secret) {
        return Encryptors.text(secret, SALT).decrypt(text);
    }
}
