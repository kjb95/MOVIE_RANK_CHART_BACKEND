package movierankchart.domain.movies.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.domain.movies.dto.request.FindMoviesByMovieTitleRequestDto;
import movierankchart.domain.movies.dto.response.FindMoviesByMovieTitleResponseDtos;
import movierankchart.domain.movies.service.MoviesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/movies")
public class MoviesController {
    private final MoviesService moviesService;

    @Operation(summary = "영화명으로 영화 조회")
    @GetMapping
    public ResponseEntity<FindMoviesByMovieTitleResponseDtos> findMoviesByMovieTitle(FindMoviesByMovieTitleRequestDto findMoviesByMovieTitleRequestDto) {
        FindMoviesByMovieTitleResponseDtos findMoviesByMovieTitleResponseDtos = moviesService.findMoviesByMovieTitle(findMoviesByMovieTitleRequestDto);
        return ResponseEntity.ok(findMoviesByMovieTitleResponseDtos);
    }

}
