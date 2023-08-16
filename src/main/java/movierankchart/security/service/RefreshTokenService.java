package movierankchart.security.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.users.costants.UsersErrorMessage;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import movierankchart.security.constants.RefreshTokenErrorMessage;
import movierankchart.security.constants.TokenType;
import movierankchart.security.dto.CreateAccessTokenResponseDto;
import movierankchart.security.entity.RefreshToken;
import movierankchart.security.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsersRepository usersRepository;
    private final TokenProviderService tokenProviderService;

    @Transactional
    public void saveToken(Users users, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUsers(users)
                .orElse(new RefreshToken(users, token));
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshTokenStr) {
        String email = tokenProviderService.findClaimsByJwt(refreshTokenStr)
                .getSubject();
        Users users = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUsers(users)
                .orElseThrow(() -> new NoSuchElementException(RefreshTokenErrorMessage.NOT_FOUND_REFRESH_TOKEN_USER));
        refreshTokenRepository.delete(refreshToken);
    }

    public CreateAccessTokenResponseDto createAccessToken(String refreshToken) {
        String email = tokenProviderService.findClaimsByJwt(refreshToken)
                .getSubject();
        Users users = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
        String accessToken = tokenProviderService.createToken(users.getEmail(), Duration.ofSeconds(TokenType.ACCESS_TOKEN.getSeconds()));
        return new CreateAccessTokenResponseDto(accessToken);
    }
}
