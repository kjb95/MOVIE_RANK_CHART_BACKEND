package movierankchart.domain.users.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movies.constants.MoviesErrorMessage;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import movierankchart.domain.users.costants.UsersErrorMessage;
import movierankchart.domain.users.dto.request.FindUsersInChatRoomRequestDto;
import movierankchart.domain.users.dto.request.UpdateUserChatRoomRequestDto;
import movierankchart.domain.users.dto.response.CreateUserRequestDto;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDto;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDtos;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final MoviesRepository moviesRepository;

    public FindUsersInChatRoomResponseDtos findUsersInChatRoom(FindUsersInChatRoomRequestDto findUsersInChatRoomRequestDto) {
        Long moviesId = findUsersInChatRoomRequestDto.getMoviesId();
        boolean isExistMovie = moviesRepository.existsById(moviesId);
        if (!isExistMovie) {
            throw new NoSuchElementException(MoviesErrorMessage.NOT_FOUND_MOVIES);
        }
        List<Users> users = usersRepository.findUsersByMovies_MoviesId(moviesId);
        List<FindUsersInChatRoomResponseDto> findUsersInChatRoomResponseDtos = users.stream()
                .map(Users::toFindUsersInChatRoomResponseDto)
                .collect(Collectors.toList());
        return new FindUsersInChatRoomResponseDtos(findUsersInChatRoomResponseDtos);
    }

    @Transactional
    public void createUser(CreateUserRequestDto createUserRequestDto) {
        boolean alreadyExistsUsers = usersRepository.existsUsersByNickname(createUserRequestDto.getNickname());
        if (alreadyExistsUsers) {
            throw new DataIntegrityViolationException(UsersErrorMessage.EXISTS_USER);
        }
        Users users = Users.createUsers(createUserRequestDto.getNickname());
        usersRepository.save(users);
    }

    @Transactional
    public void updateUserChatRoom(long usersId, UpdateUserChatRoomRequestDto updateUserChatRoomRequestDto) {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new NoSuchElementException(UsersErrorMessage.NOT_FOUND_USER));
        if (updateUserChatRoomRequestDto.getMoviesId() == null) {
            users.setMovies(null);
            return;
        }
        Movies movies = moviesRepository.findById(updateUserChatRoomRequestDto.getMoviesId())
                .orElseThrow(() -> new NoSuchElementException(MoviesErrorMessage.NOT_FOUND_MOVIES));
        users.setMovies(movies);
    }
}
