package movierankchart.domain.movies.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movies.constants.MoviesErrorMessage;
import movierankchart.domain.movies.dto.request.FindMoviesRequestDto;
import movierankchart.domain.movies.dto.response.FindMoviesResponseDto;
import movierankchart.domain.movies.dto.response.FindMoviesResponseDtos;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import movierankchart.domain.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoviesService {
    private final MoviesRepository moviesRepository;
    private final UsersRepository usersRepository;

    public FindMoviesResponseDtos findMoviesById(FindMoviesRequestDto findMoviesRequestDto) {
        Movies movies = moviesRepository.findById(findMoviesRequestDto.getId()).orElseThrow(() -> new IllegalArgumentException(MoviesErrorMessage.NOT_FOUND_MOVIES));
        FindMoviesResponseDto findMoviesResponseDto = createFindMoviesResponseDto(movies);
        return new FindMoviesResponseDtos(Arrays.asList(findMoviesResponseDto));
    }

    public FindMoviesResponseDtos findMoviesByTitle(FindMoviesRequestDto findMoviesRequestDto) {
        String title = findMoviesRequestDto.getTitle();
        boolean considerSomeoneChatRoom = findMoviesRequestDto.getIsConsiderSomeoneChatroom();
        List<Movies> moviess = considerSomeoneChatRoom ? moviesRepository.findMoviesByTitleContainingSomeoneInChatRoom(title) : moviesRepository.findMoviesByTitleContaining(title);
        List<FindMoviesResponseDto> findMoviesResponseDtos = moviess.stream()
                .map(movies -> createFindMoviesResponseDto(movies))
                .collect(Collectors.toList());
        return new FindMoviesResponseDtos(findMoviesResponseDtos);
    }

    private FindMoviesResponseDto createFindMoviesResponseDto(Movies movies) {
        Long chatRoomCount = usersRepository.countUsersByMovies_MoviesId(movies.getMoviesId());
        return movies.toFindMoviesResponseDto(chatRoomCount.intValue());
    }
}
