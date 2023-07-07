package movierankchart.domain.kmdb.service;

import movierankchart.common.service.WebClientService;
import movierankchart.domain.kmdb.dto.KmdbMovieDetailResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 해당 테스트를 활성화 하려면
 * 1. @Disabled 제거
 * 2. setUp() 안에
 *  ReflectionTestUtils.setField(kmdbService, "KMDB_API_KEY", 키값);
 *  코드 추가
 */
@Disabled
class KmdbServiceTest {
    private KmdbService kmdbService;
    private WebClientService webClientService;

    @BeforeEach
    void setUp() {
        webClientService = new WebClientService();
        kmdbService = new KmdbService(webClientService);
    }

    @Test
    void KMDB_OPEN_API_영화상세정보_호출_성공() {
        // given
        String openDt = "2017-12-27";
        String title = "1987";

        // when
        KmdbMovieDetailResponseDto movieDetail = kmdbService.findMovieDetail(openDt, title);

        // then
        int dataSize = movieDetail.getData()
                .get(0)
                .getResult()
                .size();
        Assertions.assertThat(dataSize)
                .isGreaterThanOrEqualTo(1);
    }
}