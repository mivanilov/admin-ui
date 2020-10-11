package org.mi.adminui.security.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CryptoUtilsTest {

    private static final String TEXT = "text";
    private static final String SECRET = "TmRSZ1VqWG4ycjV1OHgvQT9EKEcrS2JQZVNoVm1ZcDM=";

    private final CryptoUtils cryptoUtils = new CryptoUtils();

    @Test
    void encryptText() {
        assertTrue(StringUtils.isNotBlank(cryptoUtils.encrypt(TEXT, SECRET)));
    }

    @Test
    void decryptText() {
        String encryptedText = cryptoUtils.encrypt(TEXT, SECRET);
        assertEquals(TEXT, cryptoUtils.decrypt(encryptedText, SECRET));
    }
}
