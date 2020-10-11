package org.mi.adminui.security.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({MockitoExtension.class})
class CookieUtilsTest {

    private static final String COOKIE_NAME = "cookieName";
    private static final String COOKIE_VALUE = "cookieValue";
    private static final int COOKIE_MAX_AGE = 60;

    private final CookieUtils cookieUtils = new CookieUtils();

    @Test
    void getCookie() {
        Cookie cookie = new Cookie(COOKIE_NAME, COOKIE_VALUE);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(cookie);

        Optional<Cookie> foundCookie = cookieUtils.getCookie(request, COOKIE_NAME);

        assertTrue(foundCookie.isPresent());
        assertSame(cookie, foundCookie.get());
    }

    @Test
    void addCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        cookieUtils.addCookie(response, COOKIE_NAME, COOKIE_VALUE, COOKIE_MAX_AGE);

        Cookie addedCookie = response.getCookie(COOKIE_NAME);
        assertNotNull(addedCookie);
        assertAll(
                () -> assertSame(COOKIE_VALUE, addedCookie.getValue()),
                () -> assertEquals("/", addedCookie.getPath()),
                () -> assertTrue(addedCookie.isHttpOnly()),
                () -> assertEquals(COOKIE_MAX_AGE, addedCookie.getMaxAge())
        );
    }

    @Test
    void deleteCookie() {
        Cookie cookie = new Cookie(COOKIE_NAME, COOKIE_VALUE);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(cookie);
        MockHttpServletResponse response = new MockHttpServletResponse();

        cookieUtils.deleteCookie(request, response, COOKIE_NAME);

        Cookie addedCookie = response.getCookie(COOKIE_NAME);
        assertNotNull(addedCookie);
        assertAll(
                () -> assertEquals("", addedCookie.getValue()),
                () -> assertEquals("/", addedCookie.getPath()),
                () -> assertEquals(0, addedCookie.getMaxAge())
        );
    }

    @Test
    void getObjectFromCookie() {
        assertEquals(COOKIE_VALUE, cookieUtils.deserialize(new Cookie(COOKIE_NAME, cookieUtils.serialize(COOKIE_VALUE)), String.class));
    }
}
