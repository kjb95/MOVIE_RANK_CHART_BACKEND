package movierankchart.domain.movieopenapihistory.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.domain.movieopenapihistory.dto.FindMovieRankDataRangeResponseDto;
import movierankchart.domain.movieopenapihistory.service.MovieOpenApiHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/movie-open-api-history")
public class MovieOpenApiHistoryController {
    private final MovieOpenApiHistoryService movieOpenApiHistoryService;

    @Operation(summary = "영화 순위 데이터 범위 조회")
    @GetMapping
    public ResponseEntity<FindMovieRankDataRangeResponseDto> findMovieRankDataRange() {
        FindMovieRankDataRangeResponseDto movieRankDataRange = movieOpenApiHistoryService.findMovieRankDataRange();
        return ResponseEntity.ok(movieRankDataRange);
    }
}
