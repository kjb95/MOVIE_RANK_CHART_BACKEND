package movierankchart.domain.kmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class KmdbMovieDetailResponseDto {
    @JsonProperty("Data")
    private ArrayList<KmdbDataResponseDto> Data;
}
