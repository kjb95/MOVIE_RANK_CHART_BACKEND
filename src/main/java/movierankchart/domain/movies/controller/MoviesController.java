package movierankchart.domain.movies.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.domain.movies.constants.MoviesErrorMessage;
import movierankchart.domain.movies.dto.request.FindMoviesRequestDto;
import movierankchart.domain.movies.dto.response.FindMoviesResponseDtos;
import movierankchart.domain.movies.service.MoviesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/movies")
public class MoviesController {
    private final MoviesService moviesService;

    @Operation(summary = "영화 조회")
    @GetMapping
    public ResponseEntity<FindMoviesResponseDtos> findMovies(@Valid FindMoviesRequestDto findMoviesRequestDto) {
        if (findMoviesRequestDto.getId() == null && (findMoviesRequestDto.getTitle() == null || findMoviesRequestDto.getIsConsiderSomeoneChatroom() == null)) {
            throw new IllegalArgumentException(MoviesErrorMessage.ONE_OF_ID_OR_TITLE_REQUIRED);
        }
        FindMoviesResponseDtos findMoviesResponseDtos = findMoviesRequestDto.getId() == null ? moviesService.findMoviesByTitle(findMoviesRequestDto) : moviesService.findMoviesById(findMoviesRequestDto);
        return ResponseEntity.ok(findMoviesResponseDtos);
    }
}
