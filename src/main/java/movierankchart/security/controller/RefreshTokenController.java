package movierankchart.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.security.service.AuthenticationService;
import movierankchart.security.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/refresh-token")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "리프레시 토큰 삭제")
    @DeleteMapping
    public ResponseEntity<Void> deleteRefreshToken() {
        UserDetails userDetails = authenticationService.findUserDetails();
        refreshTokenService.deleteRefreshToken(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
