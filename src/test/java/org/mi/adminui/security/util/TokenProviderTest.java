package org.mi.adminui.security.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.web.core.configuration.property.AppAuthProperties;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    private static final String EMAIL = "EMAIL";
    private static final String TOKEN_SECRET = "TmRSZ1VqWG4ycjV1OHgvQT9EKEcrS2JQZVNoVm1ZcDM=";
    private static final long TOKEN_EXPIRE_MS = 10000L;

    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        AppAuthProperties appAuthProperties = new AppAuthProperties(new AppAuthProperties.Token(TOKEN_SECRET, TOKEN_EXPIRE_MS),
                                                                    new AppAuthProperties.Cookie(0),
                                                                    new AppAuthProperties.Oauth2(0));
        tokenProvider = new TokenProvider(appAuthProperties);
    }

    @Test
    void returnCreatedToken() {
        assertTrue(StringUtils.isNotBlank(tokenProvider.createToken(EMAIL)));
    }

    @Test
    void returnUserEmailFromToken() {
        assertEquals(EMAIL, tokenProvider.getUserEmailFromToken(tokenProvider.createToken(EMAIL)));
    }
}
