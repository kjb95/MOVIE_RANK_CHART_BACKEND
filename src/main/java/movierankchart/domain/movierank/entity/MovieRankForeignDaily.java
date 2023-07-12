package movierankchart.domain.movierank.entity;

import lombok.Getter;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;

import javax.persistence.Entity;

@Entity
@Getter
public class MovieRankForeignDaily extends MovieRankBase {
    @Override
    public MovieRankBase createMovieRank(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto, KmdbResultResponseDto kmdbResultResponseDto) {
        return null;
    }
}
