package movierankchart.domain.kmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KmdbResultResponseDto {
    private String movieSeq;
    private String title;
    private String nation;
    private String company;
    private String runtime;
    private String rating;
    private String genre;
    private KmdbRatingsResponseDto ratings;
    private String posters;
    @JsonProperty("Codes")
    private KmdbCodesResponseDto Codes;
    private String openDt;
}
