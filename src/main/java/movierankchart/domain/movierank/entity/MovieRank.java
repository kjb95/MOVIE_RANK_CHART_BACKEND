package movierankchart.domain.movierank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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
        boolean isNewRank = kobisBoxOfficeResponseDto.getRankOldAndNew()
                .equals(KobisConstants.RANK_NEW) ? true : false;
        return MovieRank.builder()
                .rankIncrement(Integer.parseInt(kobisBoxOfficeResponseDto.getRankInten()))
                .isNewRank(isNewRank)
                .audienceCount(Long.parseLong(kobisBoxOfficeResponseDto.getAudiCnt()))
                .sales(Long.parseLong(kobisBoxOfficeResponseDto.getSalesAmt()))
                .screeningsCount(Long.parseLong(kobisBoxOfficeResponseDto.getScrnCnt()))
                .theatersCount(Long.parseLong(kobisBoxOfficeResponseDto.getShowCnt()))
                .build();
    }
}
