package movierankchart.domain.kobis.service;

import movierankchart.common.service.WebClientService;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * 해당 테스트를 활성화 하려면
 * 1. @Disabled 제거
 * 2. setUp() 안에
 *  ReflectionTestUtils.setField(kobisService, "KOBIS_API_KEY", 키값);
 *  코드 추가
 */
@Disabled
class KobisServiceTest {
    private KobisService kobisService;
    private WebClientService webClientService;

    @BeforeEach
    void setUp() {
        webClientService = new WebClientService();
        kobisService = new KobisService(webClientService);
    }

    @Test
    void KOBIS_OPEN_API_일간_박스오피스_전체영화_호출_성공() {
        // given
        LocalDate date = LocalDate.of(2023, 7, 2);
        String repNationCd = null;
        String apiPath = KobisConstants.DAILY_BOX_OFFICE_PATH;

        // when
        KobisMovieRankResponseDto movieRank = kobisService.findMovieRank(date, repNationCd, apiPath);

        // then
        int dailyBoxOfficeSize = movieRank.getBoxOfficeResult()
                .getDailyBoxOfficeList()
                .size();
        Assertions.assertThat(dailyBoxOfficeSize)
                .isEqualTo(10);
    }

    @Test
    void KOBIS_OPEN_API_일간_박스오피스_한국영화_호출_성공() {
        // given
        LocalDate date = LocalDate.of(2023, 7, 2);
        String repNationCd = KobisConstants.KOREAN_MOVIE;
        String apiPath = KobisConstants.DAILY_BOX_OFFICE_PATH;

        // when
        KobisMovieRankResponseDto movieRank = kobisService.findMovieRank(date, repNationCd, apiPath);

        // then
        int dailyBoxOfficeSize = movieRank.getBoxOfficeResult()
                .getDailyBoxOfficeList()
                .size();
        Assertions.assertThat(dailyBoxOfficeSize)
                .isEqualTo(10);
    }

    @Test
    void KOBIS_OPEN_API_일간_박스오피스_외국영화_호출_성공() {
        // given
        LocalDate date = LocalDate.of(2023, 7, 2);
        String repNationCd = KobisConstants.FOREIGN_MOVIE;
        String apiPath = KobisConstants.DAILY_BOX_OFFICE_PATH;

        // when
        KobisMovieRankResponseDto movieRank = kobisService.findMovieRank(date, repNationCd, apiPath);

        // then
        int dailyBoxOfficeSize = movieRank.getBoxOfficeResult()
                .getDailyBoxOfficeList()
                .size();
        Assertions.assertThat(dailyBoxOfficeSize)
                .isEqualTo(10);
    }

    @Test
    void KOBIS_OPEN_API_주간_박스오피스_전체영화_호출_성공() {
        // given
        LocalDate date = LocalDate.of(2023, 7, 2);
        String repNationCd = null;
        String apiPath = KobisConstants.WEEKLY_BOX_OFFICE_PATH;

        // when
        KobisMovieRankResponseDto movieRank = kobisService.findMovieRank(date, repNationCd, apiPath);

        // then
        int weeklyBoxOfficeSize = movieRank.getBoxOfficeResult()
                .getWeeklyBoxOfficeList()
                .size();
        Assertions.assertThat(weeklyBoxOfficeSize)
                .isEqualTo(10);
    }

    @Test
    void KOBIS_OPEN_API_주간_박스오피스_한국영화_호출_성공() {
        // given
        LocalDate date = LocalDate.of(2023, 7, 2);
        String repNationCd = KobisConstants.KOREAN_MOVIE;
        String apiPath = KobisConstants.WEEKLY_BOX_OFFICE_PATH;

        // when
        KobisMovieRankResponseDto movieRank = kobisService.findMovieRank(date, repNationCd, apiPath);

        // then
        int weeklyBoxOfficeSize = movieRank.getBoxOfficeResult()
                .getWeeklyBoxOfficeList()
                .size();
        Assertions.assertThat(weeklyBoxOfficeSize)
                .isEqualTo(10);
    }

    @Test
    void KOBIS_OPEN_API_주간_박스오피스_외국영화_호출_성공() {
        // given
        LocalDate date = LocalDate.of(2023, 7, 2);
        String repNationCd = KobisConstants.FOREIGN_MOVIE;
        String apiPath = KobisConstants.WEEKLY_BOX_OFFICE_PATH;

        // when
        KobisMovieRankResponseDto movieRank = kobisService.findMovieRank(date, repNationCd, apiPath);

        // then
        int weeklyBoxOfficeSize = movieRank.getBoxOfficeResult()
                .getWeeklyBoxOfficeList()
                .size();
        Assertions.assertThat(weeklyBoxOfficeSize)
                .isEqualTo(10);
    }

    @Test
    public void KOBIS_OPEN_API_일간_박스오피스_14일간_전체영화_호출_성공() {
        // given
        LocalDate startDate = LocalDate.of(2023, 6, 19);
        List<LocalDate> dates = DateUtils.getDatesRange(startDate, 14, 1);
        String repNationCd = null;
        String apiPath = KobisConstants.DAILY_BOX_OFFICE_PATH;

        // when
        List<KobisMovieRankResponseDto> movieRanks = kobisService.findMovieRank(dates, repNationCd, apiPath);

        // then
        Assertions.assertThat(movieRanks.size())
                .isEqualTo(14);
        movieRanks.forEach(movieRank -> {
            int boxOfficeSize = movieRank.getBoxOfficeResult()
                    .getDailyBoxOfficeList()
                    .size();
            Assertions.assertThat(boxOfficeSize)
                    .isEqualTo(10);
        });
    }

    @Test
    public void KOBIS_OPEN_API_일간_박스오피스_14일간_한국영화_호출_성공() {
        // given
        LocalDate startDate = LocalDate.of(2023, 6, 19);
        List<LocalDate> dates = DateUtils.getDatesRange(startDate, 14, 1);
        String repNationCd = KobisConstants.KOREAN_MOVIE;
        String apiPath = KobisConstants.DAILY_BOX_OFFICE_PATH;

        // when
        List<KobisMovieRankResponseDto> movieRanks = kobisService.findMovieRank(dates, repNationCd, apiPath);

        // then
        Assertions.assertThat(movieRanks.size())
                .isEqualTo(14);
        movieRanks.forEach(movieRank -> {
            int boxOfficeSize = movieRank.getBoxOfficeResult()
                    .getDailyBoxOfficeList()
                    .size();
            Assertions.assertThat(boxOfficeSize)
                    .isEqualTo(10);
        });
    }

    @Test
    public void KOBIS_OPEN_API_일간_박스오피스_14일간_외국영화_호출_성공() {
        // given
        LocalDate startDate = LocalDate.of(2023, 6, 19);
        List<LocalDate> dates = DateUtils.getDatesRange(startDate, 14, 1);
        String repNationCd = KobisConstants.FOREIGN_MOVIE;
        String apiPath = KobisConstants.DAILY_BOX_OFFICE_PATH;

        // when
        List<KobisMovieRankResponseDto> movieRanks = kobisService.findMovieRank(dates, repNationCd, apiPath);

        // then
        Assertions.assertThat(movieRanks.size())
                .isEqualTo(14);
        movieRanks.forEach(movieRank -> {
            int boxOfficeSize = movieRank.getBoxOfficeResult()
                    .getDailyBoxOfficeList()
                    .size();
            Assertions.assertThat(boxOfficeSize)
                    .isEqualTo(10);
        });
    }

    @Test
    public void KOBIS_OPEN_API_주간_박스오피스_2주간_전체영화_호출_성공() {
        // given
        LocalDate startDate = LocalDate.of(2023, 6, 19);
        List<LocalDate> dates = DateUtils.getDatesRange(startDate, 2, 7);
        String repNationCd = null;
        String apiPath = KobisConstants.WEEKLY_BOX_OFFICE_PATH;

        // when
        List<KobisMovieRankResponseDto> movieRanks = kobisService.findMovieRank(dates, repNationCd, apiPath);

        // then
        Assertions.assertThat(movieRanks.size())
                .isEqualTo(2);
        movieRanks.forEach(movieRank -> {
            int boxOfficeSize = movieRank.getBoxOfficeResult()
                    .getWeeklyBoxOfficeList()
                    .size();
            Assertions.assertThat(boxOfficeSize)
                    .isEqualTo(10);
        });
    }

    @Test
    public void KOBIS_OPEN_API_주간_박스오피스_2주간_한국영화_호출_성공() {
        // given
        LocalDate startDate = LocalDate.of(2023, 6, 19);
        List<LocalDate> dates = DateUtils.getDatesRange(startDate, 2, 7);
        String repNationCd = KobisConstants.KOREAN_MOVIE;
        String apiPath = KobisConstants.WEEKLY_BOX_OFFICE_PATH;

        // when
        List<KobisMovieRankResponseDto> movieRanks = kobisService.findMovieRank(dates, repNationCd, apiPath);

        // then
        Assertions.assertThat(movieRanks.size())
                .isEqualTo(2);
        movieRanks.forEach(movieRank -> {
            int boxOfficeSize = movieRank.getBoxOfficeResult()
                    .getWeeklyBoxOfficeList()
                    .size();
            Assertions.assertThat(boxOfficeSize)
                    .isEqualTo(10);
        });
    }

    @Test
    public void KOBIS_OPEN_API_주간_박스오피스_2주간_외국영화_호출_성공() {
        // given
        LocalDate startDate = LocalDate.of(2023, 6, 19);
        List<LocalDate> dates = DateUtils.getDatesRange(startDate, 2, 7);
        String repNationCd = KobisConstants.FOREIGN_MOVIE;
        String apiPath = KobisConstants.WEEKLY_BOX_OFFICE_PATH;

        // when
        List<KobisMovieRankResponseDto> movieRanks = kobisService.findMovieRank(dates, repNationCd, apiPath);

        // then
        Assertions.assertThat(movieRanks.size())
                .isEqualTo(2);
        movieRanks.forEach(movieRank -> {
            int boxOfficeSize = movieRank.getBoxOfficeResult()
                    .getWeeklyBoxOfficeList()
                    .size();
            Assertions.assertThat(boxOfficeSize)
                    .isEqualTo(10);
        });
    }
}