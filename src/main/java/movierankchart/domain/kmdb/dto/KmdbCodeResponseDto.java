package movierankchart.domain.kmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KmdbCodeResponseDto {
    @JsonProperty("CodeNo")
    private String CodeNo;
}
