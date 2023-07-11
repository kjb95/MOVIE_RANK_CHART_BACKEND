package movierankchart.domain.kmdb.service;

import movierankchart.common.service.WebClientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KmdbServiceFailTest {
    private KmdbService kmdbService;
    private WebClientService webClientService;

    @BeforeEach
    void setUp() {
        webClientService = new WebClientService();
        kmdbService = new KmdbService(webClientService);
    }

    @Test
    void KMDB_OPEN_API_키값을_주입해_주지_않아서_예외_발생() {
        // given
        String title = "귀공자";

        // then
        Assertions.assertThatThrownBy(() -> kmdbService.findMovieDetail(title, "", ""))
                .isInstanceOf(RuntimeException.class);
    }
}