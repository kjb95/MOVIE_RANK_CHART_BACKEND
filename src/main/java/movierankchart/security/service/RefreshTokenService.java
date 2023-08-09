package movierankchart.security.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.users.entity.Users;
import movierankchart.security.entity.RefreshToken;
import movierankchart.security.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveToken(Users users, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUsers(users).orElse(new RefreshToken(users, token));
        refreshTokenRepository.save(refreshToken);
    }
}
