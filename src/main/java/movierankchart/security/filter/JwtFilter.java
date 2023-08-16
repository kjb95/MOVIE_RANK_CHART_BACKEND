package movierankchart.security.filter;

import lombok.RequiredArgsConstructor;
import movierankchart.security.constants.SecurityConstants;
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

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProviderService tokenProviderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // HTTP 요청 헤더에서 액세스 토큰 조회
        String accessTokenHeader = request.getHeader(SecurityConstants.JWT_TOKEN_HEADER);
        String accessToken = tokenProviderService.getTokenByRequestHeader(accessTokenHeader);
        // 액세스 토큰이 유효하지 않은경우, SecurityContext에 null이 저장됨
        // 액세스 토큰이 유효한 경우, SecurityContext에 인증객체가 저장됨
        Authentication authentication = tokenProviderService.createAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
