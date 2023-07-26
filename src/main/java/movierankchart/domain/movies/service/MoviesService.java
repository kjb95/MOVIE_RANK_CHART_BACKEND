package movierankchart.domain.movies.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movies.dto.request.FindMoviesByMovieTitleRequestDto;
import movierankchart.domain.movies.dto.response.FindMoviesByMovieTitleResponseDto;
import movierankchart.domain.movies.dto.response.FindMoviesByMovieTitleResponseDtos;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import movierankchart.domain.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoviesService {
    private final MoviesRepository moviesRepository;
    private final UsersRepository usersRepository;

    public FindMoviesByMovieTitleResponseDtos findMoviesByMovieTitle(FindMoviesByMovieTitleRequestDto findMoviesByMovieTitleRequestDto) {
        boolean considerSomeoneChatroom = findMoviesByMovieTitleRequestDto.getIsConsiderSomeoneChatroom();
        String title = findMoviesByMovieTitleRequestDto.getTitle();
        List<Movies> moviess = considerSomeoneChatroom ? moviesRepository.findMoviesByTitleContainingSomeoneInChatRoom(title) : moviesRepository.findMoviesByTitleContaining(title);
        List<FindMoviesByMovieTitleResponseDto> findMoviesByMovieTitleResponseDtos = moviess.stream()
                .map(movies -> createFindMoviesByMovieTitleResponseDto(movies))
                .collect(Collectors.toList());
        return new FindMoviesByMovieTitleResponseDtos(findMoviesByMovieTitleResponseDtos);
    }

    private FindMoviesByMovieTitleResponseDto createFindMoviesByMovieTitleResponseDto(Movies movies) {
        Long chatRoomCount = usersRepository.countUsersByMovies_MoviesId(movies.getMoviesId());
        return movies.toFindMoviesByMovieTitleResponseDto(chatRoomCount.intValue());
    }
}
