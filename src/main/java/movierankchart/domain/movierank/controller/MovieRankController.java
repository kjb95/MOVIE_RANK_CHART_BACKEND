package movierankchart.domain.movierank.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.domain.movierank.dto.request.FindMovieRankLineChartRequestDto;
import movierankchart.domain.movierank.dto.request.FindMovieRankTopTenRequestDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDtos;
import movierankchart.domain.movierank.dto.response.FindMovieRankTopTenResponseDtos;
import movierankchart.domain.movierank.service.MovieRankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/movie-rank")
public class MovieRankController {
    private final MovieRankService movieRankService;

    @Operation(summary = "영화 순위 TOP 10에 필요한 데이터 조회")
    @GetMapping("/top-ten")
    public ResponseEntity<FindMovieRankTopTenResponseDtos> findMovieRankTopTen(@Valid FindMovieRankTopTenRequestDto findMovieRankTopTenRequestDto) {
        FindMovieRankTopTenResponseDtos findMovieRankTopTenResponseDtos = movieRankService.findMovieRankTopTen(findMovieRankTopTenRequestDto);
        return ResponseEntity.ok(findMovieRankTopTenResponseDtos);
    }

    @Operation(summary = "라인 차트에 필요한 데이터 조회")
    @GetMapping("/line-chart")
    public ResponseEntity<FindMovieRankLineChartResponseDtos> findMovieRankLineChart(@Valid FindMovieRankLineChartRequestDto findMovieRankLineChartRequestDto) {
        FindMovieRankLineChartResponseDtos findMovieRankLineChartResponseDtos = movieRankService.findMovieRankLineChart(findMovieRankLineChartRequestDto);
        return ResponseEntity.ok(findMovieRankLineChartResponseDtos);
    }

    @Operation(summary = "영화 상세 페이지의 라인차트에 필요한 데이터 조회")
    @GetMapping("/line-chart/{moviesId}")
    public ResponseEntity<FindMovieRankLineChartResponseDtos> findMovieRankLineChartByMoviesId(@PathVariable long moviesId) {
        FindMovieRankLineChartResponseDtos movieRankLineChartByMoviesId = movieRankService.findMovieRankLineChartByMoviesId(moviesId);
        return ResponseEntity.ok(movieRankLineChartByMoviesId);
    }
}
