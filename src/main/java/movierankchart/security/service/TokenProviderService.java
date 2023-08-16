package movierankchart.security.service;

import io.jsonwebtoken.*;
import movierankchart.security.constants.SecurityConstants;
import movierankchart.security.constants.TokenStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;

@Service
public class TokenProviderService {
    @Value("${jwt.issuer}")
    private String JWT_ISSUER;
    @Value("${jwt.secret_key}")
    private String JWT_SECRET_KEY;

    public String createToken(String subject, Duration duration) {
        Date now = new Date();
        return Jwts.builder()
                // header: 토큰의 타입과 JWT를 서명하는데 사용한 알고리즘을 명시
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)  // 토큰타입
                // payload: Key-Value 형태의 데이터 조각(Claim)으로 구성됨
                .setIssuer(JWT_ISSUER)  // 토큰 발급자
                .setIssuedAt(now)  // 토큰 발급 시간
                .setExpiration(new Date(now.getTime() + duration.toMillis()))  // 토큰 만료 시간
                .setSubject(subject)  // 토큰 이름
                // signature: 특정 암호화 알고리즘을 사용하여, 인코딩된 헤더와 페이로드와 비밀키를 암호화
                .signWith(SignatureAlgorithm.HS256, // alg header 설정
                        JWT_SECRET_KEY  // 비밀키 설정
                ).compact();
    }

    public TokenStatus isValidToken(String token) {
        if (token == null) {
            return TokenStatus.INVALID;
        }
        try {
            findClaimsByJwt(token);
            return TokenStatus.VALID;
        }
        catch (Exception e) {
            return TokenStatus.INVALID;
        }
    }

    public Authentication createAuthentication(String token) {
        if (isValidToken(token) != TokenStatus.VALID) {
            return null;
        }
        Claims claims = findClaimsByJwt(token);
        User user = new User(claims.getSubject(), "", Collections.EMPTY_LIST);
        return new UsernamePasswordAuthenticationToken(user, token, Collections.EMPTY_LIST);
    }

    public Claims findClaimsByJwt(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String getTokenByRequestHeader(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }
        if (!authorizationHeader.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(SecurityConstants.JWT_TOKEN_PREFIX.length());
    }

}
