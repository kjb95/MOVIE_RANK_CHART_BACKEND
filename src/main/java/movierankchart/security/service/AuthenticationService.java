package movierankchart.security.service;

import movierankchart.security.constants.SecurityErrorMessage;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    public UserDetails findUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            throw new AuthenticationCredentialsNotFoundException(SecurityErrorMessage.NOT_FOUND_AUTHENTICATION);
        }
        return (UserDetails) authentication.getPrincipal();
    }
}
