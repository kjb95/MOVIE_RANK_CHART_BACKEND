package movierankchart.domain.users.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movies.constants.MoviesErrorMessage;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import movierankchart.domain.users.costants.UsersErrorMessage;
import movierankchart.domain.users.dto.request.UpdateUserChatRoomRequestDto;
import movierankchart.domain.users.dto.response.FindUsersResponseDto;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final MoviesRepository moviesRepository;

    @Transactional
    public void updateUserChatRoom(long usersId, UpdateUserChatRoomRequestDto updateUserChatRoomRequestDto) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_ID));
        if (updateUserChatRoomRequestDto.getChatRoomId() == null) {
            users.setMovies(null);
            return;
        }
        Movies movies = moviesRepository.findById(updateUserChatRoomRequestDto.getChatRoomId())
                .orElseThrow(() -> new NoSuchElementException(MoviesErrorMessage.NOT_FOUND_MOVIES));
        users.setMovies(movies);
    }

    public FindUsersResponseDto findUsers(String email) {
        Users users = usersRepository.findUsersByEmail(email).orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
        return users.toFindUsersResponseDto();
    }

    public Users findUsers(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        return usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
    }
}
