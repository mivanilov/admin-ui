package org.mi.adminui.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "app.auth")
public class AppAuthProperties {

    private final Session session;
    private final Oauth2 oauth2;

    public AppAuthProperties(Session session, Oauth2 oauth2) {
        this.session = session;
        this.oauth2 = oauth2;
    }

    public Session getSession() {
        return session;
    }

    public Oauth2 getOauth2() {
        return oauth2;
    }

    public static class Session {

        private final String secret;
        private final String cookieName;
        private final int cookieExpireSec;

        public Session(String secret, String cookieName, int cookieExpireSec) {
            this.secret = secret;
            this.cookieName = cookieName;
            this.cookieExpireSec = cookieExpireSec;
        }

        public String getSecret() {
            return secret;
        }

        public String getCookieName() {
            return cookieName;
        }

        public int getCookieExpireSec() {
            return cookieExpireSec;
        }
    }

    public static class Oauth2 {

        private final String authRequestCookieName;
        private final int authRequestCookieExpireSec;

        public Oauth2(String authRequestCookieName, int authRequestCookieExpireSec) {
            this.authRequestCookieName = authRequestCookieName;
            this.authRequestCookieExpireSec = authRequestCookieExpireSec;
        }

        public String getAuthRequestCookieName() {
            return authRequestCookieName;
        }

        public int getAuthRequestCookieExpireSec() {
            return authRequestCookieExpireSec;
        }
    }
}
