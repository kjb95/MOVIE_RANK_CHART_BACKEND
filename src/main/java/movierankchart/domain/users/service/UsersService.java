package movierankchart.domain.users.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movies.constants.MoviesConstants;
import movierankchart.domain.movies.repository.MoviesRepository;
import movierankchart.domain.users.dto.request.FindUsersInChatRoomRequestDto;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDto;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDtos;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final MoviesRepository moviesRepository;

    public FindUsersInChatRoomResponseDtos findUsersInChatRoom(FindUsersInChatRoomRequestDto findUsersInChatRoomRequestDto) {
        Long moviesId = findUsersInChatRoomRequestDto.getMoviesId();
        boolean isExistMovie = moviesRepository.existsById(moviesId);
        if (!isExistMovie) {
            throw new NoSuchElementException(MoviesConstants.NOT_FOUND_MOVIES_ID);
        }
        List<Users> users = usersRepository.findUsersByMovies_MoviesId(moviesId);
        List<FindUsersInChatRoomResponseDto> findUsersInChatRoomResponseDtos = users.stream()
                .map(Users::toFindUsersInChatRoomResponseDto)
                .collect(Collectors.toList());
        return new FindUsersInChatRoomResponseDtos(findUsersInChatRoomResponseDtos);
    }
}
