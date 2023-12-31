package movierankchart.domain.kmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KmdbDataResponseDto {
    @JsonProperty("Result")
    public List<KmdbResultResponseDto> Result;
}
