package movierankchart.security.handler;

import lombok.RequiredArgsConstructor;
import movierankchart.common.utils.CookieUtils;
import movierankchart.domain.users.costants.UsersErrorMessage;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import movierankchart.security.constants.CookieType;
import movierankchart.security.constants.SecurityConstants;
import movierankchart.security.service.RefreshTokenService;
import movierankchart.security.service.TokenProviderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProviderService tokenProviderService;
    private final UsersRepository usersRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Users users = findUsers(authentication);
        // 액세스 토큰, 리프레시 토큰 생성
        String accessToken = tokenProviderService.createToken(users.getEmail(), Duration.ofSeconds(CookieType.ACCESS_TOKEN.getSeconds()));
        String refreshToken = tokenProviderService.createToken(users.getEmail(), Duration.ofSeconds(CookieType.REFRESH_TOKEN.getSeconds()));
        refreshTokenService.saveToken(users, refreshToken);
        // 응답쿠키에 액세스 토큰, 리프레시 토큰 담기
        CookieUtils.addCookie(response, CookieType.ACCESS_TOKEN.getName(), accessToken, CookieType.ACCESS_TOKEN.getSeconds());
        CookieUtils.addCookie(response, CookieType.REFRESH_TOKEN.getName(), refreshToken, CookieType.REFRESH_TOKEN.getSeconds());
        CookieUtils.addCookie(response, CookieType.AUTHENTICATION_DONE.getName(), CookieType.AUTHENTICATION_DONE.getName(), CookieType.AUTHENTICATION_DONE.getSeconds());
    }

    private Users findUsers(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get(SecurityConstants.OAUTH2_ATTRIBUTE_EMAIL);
        return usersRepository.findUsersByEmail(email).orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
    }
}
