package movierankchart.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.common.utils.CookieUtils;
import movierankchart.security.constants.TokenStatus;
import movierankchart.security.constants.TokenType;
import movierankchart.security.dto.CreateAccessTokenResponseDto;
import movierankchart.security.service.RefreshTokenService;
import movierankchart.security.service.TokenProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/refresh-token")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;
    private final TokenProviderService tokenProviderService;

    @Operation(summary = "리프레시 토큰 삭제")
    @DeleteMapping
    public ResponseEntity<Void> deleteRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookie(request, TokenType.REFRESH_TOKEN.getName());
        refreshTokenService.deleteRefreshToken(refreshToken);
        CookieUtils.deleteCookie(request, response, TokenType.REFRESH_TOKEN.getName());
        return ResponseEntity.ok()
                .build();
    }

    @Operation(summary = "액세스 토큰 발급")
    @PostMapping("/access-token")
    public ResponseEntity<CreateAccessTokenResponseDto> createAccessToken(HttpServletRequest request) {
        String refreshToken = CookieUtils.getCookie(request, TokenType.REFRESH_TOKEN.getName());
        TokenStatus refreshTokenStatus = tokenProviderService.isValidToken(refreshToken);
        if (refreshTokenStatus == TokenStatus.INVALID) {  // 리프레시 토큰이 유효하지 않은 경우, 액세스 토큰을 발급하지 않음
            return ResponseEntity.ok()
                    .build();
        }
        CreateAccessTokenResponseDto createAccessTokenResponseDto = refreshTokenService.createAccessToken(refreshToken);
        return ResponseEntity.ok(createAccessTokenResponseDto);
    }
}
