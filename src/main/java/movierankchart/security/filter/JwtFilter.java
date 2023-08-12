package movierankchart.security.filter;

import lombok.RequiredArgsConstructor;
import movierankchart.common.utils.CookieUtils;
import movierankchart.domain.users.costants.UsersErrorMessage;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import movierankchart.security.constants.TokenType;
import movierankchart.security.constants.SecurityConstants;
import movierankchart.security.constants.TokenStatus;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // HTTP 요청 헤더에서 액세스 토큰, 리프레시 토큰 조회
        String accessTokenHeader = request.getHeader(SecurityConstants.ACCESS_TOKEN_HEADER);
        String refreshTokenHeader = request.getHeader(SecurityConstants.REFRESH_TOKEN_HEADER);
        String accessToken = tokenProviderService.getTokenByRequestHeader(accessTokenHeader);
        String refreshToken = tokenProviderService.getTokenByRequestHeader(refreshTokenHeader);
        TokenStatus accessTokenStatus = tokenProviderService.isValidToken(accessToken);
        TokenStatus refereshTokenStatus = tokenProviderService.isValidToken(refreshToken);
        // 액세스 토큰이 만료되고, 리프리시 토큰이 유효한 경우, 새로운 액세스 토큰 생성
        if (accessTokenStatus == TokenStatus.EXPIRED && refereshTokenStatus == TokenStatus.VALID) {
            String email = tokenProviderService.findClaimsByJwt(refreshToken).getSubject();
            Users users = usersRepository.findUsersByEmail(email).orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
            accessToken = tokenProviderService.createToken(users.getEmail(), Duration.ofSeconds(TokenType.ACCESS_TOKEN.getSeconds()));
            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.getName(), accessToken);
        }
        // 액세스 토큰이 유효하지 않은경우, SecurityContext에 null이 저장됨
        // 액세스 토큰이 유효한 경우, SecurityContext에 인증객체가 저장됨
        Authentication authentication = tokenProviderService.createAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
