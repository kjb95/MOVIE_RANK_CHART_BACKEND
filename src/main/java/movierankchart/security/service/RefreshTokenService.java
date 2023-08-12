package movierankchart.security.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.users.costants.UsersErrorMessage;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import movierankchart.security.constants.RefreshTokenErrorMessage;
import movierankchart.security.entity.RefreshToken;
import movierankchart.security.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void saveToken(Users users, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUsers(users).orElse(new RefreshToken(users, token));
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        Users users = usersRepository.findUsersByEmail(email).orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUsers(users).orElseThrow(() -> new NoSuchElementException(RefreshTokenErrorMessage.NOT_FOUND_REFRESH_TOKEN_USER));
        refreshTokenRepository.delete(refreshToken);
    }
}
