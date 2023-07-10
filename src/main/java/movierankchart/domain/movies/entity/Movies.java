package movierankchart.domain.movies.entity;

import lombok.*;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.entity.AuditEntity;
import movierankchart.common.utils.DateUtils;
import movierankchart.common.utils.StringUtils;
import movierankchart.domain.kmdb.constants.KmdbConstants;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kobis.constants.KobisConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movies extends AuditEntity {
    @Id
    private Long moviesId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalDate openingDate;
    @Column(nullable = false)
    private String poster;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String nation;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private String runtime;
    @Column(nullable = false)
    private String ratingGrade;

    public static Movies fromDto(KmdbResultResponseDto kmdbResultResponseDto) {
        String openDt = kmdbResultResponseDto.getOpenDt()
                .replaceAll(KobisConstants.OPEN_DT_DELIMITER, "");
        LocalDate date = DateUtils.stringToLocalDate(openDt, BatchConstants.YYYYMMDD);
        String title = parseKmdbResultTitle(kmdbResultResponseDto.getTitle());
        String poster = StringUtils.subStringUntil(kmdbResultResponseDto.getPosters(), KmdbConstants.STRING_DELIMITER);
        return Movies.builder()
                .moviesId(Long.parseLong(kmdbResultResponseDto.getMovieSeq()))
                .title(title)
                .openingDate(date)
                .poster(poster)
                .genre(kmdbResultResponseDto.getGenre())
                .nation(kmdbResultResponseDto.getNation())
                .company(kmdbResultResponseDto.getCompany())
                .runtime(kmdbResultResponseDto.getRuntime())
                .ratingGrade(kmdbResultResponseDto.getRating())
                .build();
    }

    static String parseKmdbResultTitle(String title) {
        return title.replace("!HS", "")
                .replace("!HE", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
