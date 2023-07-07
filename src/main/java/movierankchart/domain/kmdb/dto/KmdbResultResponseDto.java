package movierankchart.domain.kmdb.dto;

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
    private String repRlsDate;
    private String posters;
}
