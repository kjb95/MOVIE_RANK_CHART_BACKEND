package movierankchart.domain.movieopenapihistory.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movieopenapihistory.dto.FindMovieRankDataRangeResponseDto;
import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieOpenApiHistoryService {
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    public FindMovieRankDataRangeResponseDto findMovieRankDataRange() {
        List<MovieOpenApiHistory> movieOpenApiHistories = movieOpenApiHistoryRepository.findAll();
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistories.get(0);
        return movieOpenApiHistory.toFindMovieRankDataRangeResponseDto();
    }
}
