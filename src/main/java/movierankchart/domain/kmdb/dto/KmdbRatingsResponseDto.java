package movierankchart.domain.kmdb.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KmdbRatingsResponseDto {
    private List<KmdbRatingResponseDto> rating;
}
