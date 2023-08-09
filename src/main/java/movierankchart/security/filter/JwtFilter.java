package movierankchart.security.filter;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProviderService tokenProviderService;
    private final UsersRepository usersRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // HTTP 요청 헤더에서 액세스 토큰, 리프레시 토큰 조회
        String accessTokenHeader = request.getHeader(SecurityConstants.ACCESS_TOKEN_HEADER);
        String refreshTokenHeader = request.getHeader(SecurityConstants.REFRESH_TOKEN_HEADER);
        String accessToken = getAccessToken(accessTokenHeader);
        String refreshToken = getAccessToken(refreshTokenHeader);
        // 액세스 토큰과 리프레시 토큰이 둘다 유효하지 않다면, 액세스 토큰과 리프레시 토큰 재생성 x
        if (!tokenProviderService.isValidToken(accessToken) && !tokenProviderService.isValidToken(refreshToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        Users users = findUsersByToken(accessToken, refreshToken);
        // 유효한 토큰이 존재한다면, 액세스 토큰, 리프레시 토큰 재생성
        createAccessToken(users, response);
        createRefreshToken(users, response);
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }
        if (!authorizationHeader.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(SecurityConstants.JWT_TOKEN_PREFIX.length());
    }

    private Users findUsersByToken(String accessToken, String refreshToken) {
        String validToken = tokenProviderService.isValidToken(accessToken) ? accessToken : refreshToken;
        String email = tokenProviderService.findClaimsByJwt(validToken).getSubject();
        return usersRepository.findUsersByEmail(email).orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
    }

    private void createAccessToken(Users users, HttpServletResponse response) {
        String accessToken = tokenProviderService.createToken(users.getEmail(), Duration.ofSeconds(CookieType.ACCESS_TOKEN.getSeconds()));
        Authentication authentication = tokenProviderService.createAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CookieUtils.addCookie(response, CookieType.ACCESS_TOKEN.getName(), accessToken, CookieType.ACCESS_TOKEN.getSeconds());
    }

    private void createRefreshToken(Users users, HttpServletResponse response) {
        String refreshToken = tokenProviderService.createToken(users.getEmail(), Duration.ofSeconds(CookieType.REFRESH_TOKEN.getSeconds()));
        refreshTokenService.saveToken(users, refreshToken);
        CookieUtils.addCookie(response, CookieType.REFRESH_TOKEN.getName(), refreshToken, CookieType.REFRESH_TOKEN.getSeconds());
    }
}
