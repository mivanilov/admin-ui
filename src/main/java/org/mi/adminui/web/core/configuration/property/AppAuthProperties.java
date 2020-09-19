package org.mi.adminui.web.core.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "app.auth")
public class AppAuthProperties {

    private final Token token;
    private final Cookie cookie;
    private final Oauth2 oauth2;

    public AppAuthProperties(final Token token, final Cookie cookie, final Oauth2 oauth2) {
        this.token = token;
        this.cookie = cookie;
        this.oauth2 = oauth2;
    }

    public Token getToken() {
        return token;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public Oauth2 getOauth2() {
        return oauth2;
    }

    public static class Token {

        private final String secret;
        private final long expireMs;

        public Token(final String secret, final long expireMs) {
            this.secret = secret;
            this.expireMs = expireMs;
        }

        public String getSecret() {
            return secret;
        }

        public long getExpireMs() {
            return expireMs;
        }
    }

    public static class Cookie {

        private final int expireSec;

        public Cookie(final int expireSec) {
            this.expireSec = expireSec;
        }

        public int getExpireSec() {
            return expireSec;
        }
    }

    public static class Oauth2 {

        private final int authRequestCookieExpireSec;

        public Oauth2(final int authRequestCookieExpireSec) {
            this.authRequestCookieExpireSec = authRequestCookieExpireSec;
        }

        public int getAuthRequestCookieExpireSec() {
            return authRequestCookieExpireSec;
        }
    }
}
