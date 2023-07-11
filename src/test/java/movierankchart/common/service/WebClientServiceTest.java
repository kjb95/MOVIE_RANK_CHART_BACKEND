package movierankchart.common.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebClientServiceTest {
    private WebClientService webClientService;

    @BeforeEach
    void setUp() {
        webClientService = new WebClientService();
    }

    @Test
    void get_NOT_FOUND_경로_예외처리() {
        // then
        Assertions.assertThatThrownBy(() -> webClientService.get("", "", null, Object.class))
                .isInstanceOf(RuntimeException.class);
    }
}