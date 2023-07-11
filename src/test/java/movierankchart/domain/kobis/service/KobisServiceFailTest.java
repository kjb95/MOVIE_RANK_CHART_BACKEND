package movierankchart.domain.kobis.service;

import movierankchart.common.service.WebClientService;
import movierankchart.domain.kobis.constants.KobisConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class KobisServiceFailTest {
    private KobisService kobisService;
    private WebClientService webClientService;

    @BeforeEach
    void setUp() {
        webClientService = new WebClientService();
        kobisService = new KobisService(webClientService);
    }

    @Test
    void KOBIS_OPEN_API_키값을_주입해_주지_않아서_예외_발생() {
        // given
        LocalDate date = LocalDate.of(2023, 7, 2);
        String repNationCd = null;
        String apiPath = KobisConstants.DAILY_BOX_OFFICE_PATH;

        // then
        Assertions.assertThatThrownBy(() -> kobisService.findMovieRank(date, repNationCd, apiPath))
                .isInstanceOf(IllegalStateException.class);
    }
}
