package org.mi.adminui.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.mi.adminui.web.core.configuration.property.AppAuthProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    private final AppAuthProperties appAuthProperties;

    public TokenProvider(AppAuthProperties appAuthProperties) {
        this.appAuthProperties = appAuthProperties;
    }

    public String createToken(String userEmail) {
        return JWT.create()
                  .withSubject(userEmail)
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(System.currentTimeMillis() + appAuthProperties.getToken().getExpireMs()))
                  .sign(getAlgorithm());
    }

    public String getUserEmailFromToken(String jwt) {
        return JWT.require(getAlgorithm())
                  .build()
                  .verify(jwt)
                  .getSubject();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(appAuthProperties.getToken().getSecret());
    }
}
