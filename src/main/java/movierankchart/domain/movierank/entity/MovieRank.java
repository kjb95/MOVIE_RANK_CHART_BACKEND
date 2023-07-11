package movierankchart.domain.movierank.entity;

import lombok.*;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class MovieRank {
    @Column(nullable = false)
    private Integer rankIncrement;
    @Column(columnDefinition = "tinyint(1)", nullable = false)
    private boolean isNewRank;
    @Column(nullable = false)
    private Long audienceCount;
    @Column(nullable = false)
    private Long sales;
    @Column(nullable = false)
    private Long screeningsCount;
    @Column(nullable = false)
    private Long theatersCount;

    public static MovieRank fromDto(KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto) {
        MovieRank movieRank = new MovieRank();
        movieRank.rankIncrement = Integer.parseInt(kobisBoxOfficeResponseDto.getRankInten());
        movieRank.isNewRank = kobisBoxOfficeResponseDto.getRankOldAndNew()
                .equals(KobisConstants.RANK_NEW) ? true : false;
        movieRank.audienceCount = Long.parseLong(kobisBoxOfficeResponseDto.getAudiCnt());
        movieRank.sales = Long.parseLong(kobisBoxOfficeResponseDto.getSalesAmt());
        movieRank.screeningsCount = Long.parseLong(kobisBoxOfficeResponseDto.getScrnCnt());
        movieRank.theatersCount = Long.parseLong(kobisBoxOfficeResponseDto.getShowCnt());
        return movieRank;
    }
}
