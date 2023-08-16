package movierankchart.domain.movies.entity;

import lombok.*;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.entity.AuditEntity;
import movierankchart.common.utils.DateUtils;
import movierankchart.common.utils.StringUtils;
import movierankchart.domain.kmdb.constants.KmdbConstants;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.movies.dto.response.FindMoviesResponseDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movies extends AuditEntity {
    @Id
    private Long moviesId;
    @Column(nullable = false)
    private String title;
    @Column
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
        if (kmdbResultResponseDto == null) {
            return null;
        }
        Movies movies = new Movies();
        movies.moviesId = Long.parseLong(kmdbResultResponseDto.getMovieSeq());
        movies.title = parseKmdbResultTitle(kmdbResultResponseDto.getTitle());
        String openDt = kmdbResultResponseDto.getOpenDt()
                .replaceAll(KobisConstants.OPEN_DT_DELIMITER, "");
        movies.openingDate = DateUtils.stringToLocalDate(openDt, BatchConstants.YYYYMMDD);
        movies.poster = StringUtils.subStringUntil(kmdbResultResponseDto.getPosters(), KmdbConstants.STRING_DELIMITER);
        movies.genre = kmdbResultResponseDto.getGenre();
        movies.nation = kmdbResultResponseDto.getNation();
        movies.company = kmdbResultResponseDto.getCompany();
        movies.runtime = kmdbResultResponseDto.getRuntime();
        movies.ratingGrade = kmdbResultResponseDto.getRating();
        return movies;
    }

    static String parseKmdbResultTitle(String title) {
        return title.replace("!HS", "")
                .replace("!HE", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public FindMoviesResponseDto toFindMoviesResponseDto(int chatRoomCount) {
        return new FindMoviesResponseDto(moviesId, title, openingDate, poster, genre, nation, company, runtime, ratingGrade, chatRoomCount);
    }
}
