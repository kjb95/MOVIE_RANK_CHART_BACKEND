package movierankchart.domain.movierank.controller;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movierank.dto.FindMovieRankResponseDtos;
import movierankchart.domain.movierank.dto.FindMovieRequestDto;
import movierankchart.domain.movierank.service.MovieRankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/movie-rank")
public class MovieRankController {
    private final MovieRankService movieRankService;

    @GetMapping
    public ResponseEntity<FindMovieRankResponseDtos> findMovieRank(@Valid @ModelAttribute FindMovieRequestDto findMovieRequestDto) {
        FindMovieRankResponseDtos findMovieRankResponseDtos = movieRankService.findMovieRank(findMovieRequestDto);
        return ResponseEntity.ok(findMovieRankResponseDtos);
    }

}
