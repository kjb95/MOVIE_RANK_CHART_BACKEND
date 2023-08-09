package movierankchart.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    public static void addCookie(HttpServletResponse httpServletResponse, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        httpServletResponse.addCookie(cookie);
    }
}
