package movierankchart.domain.kobis.service;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.service.WebClientService;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class KobisService {

    @Value("${kobis.api.key}")
    private String KOBIS_API_KEY;

    private final WebClientService webClientService;

    public KobisMovieRankResponseDto findMovieRank(LocalDate date, String repNationCd, String apiPath) {
        MultiValueMap<String, String> params = createParams(date, repNationCd, apiPath);
        return webClientService.get(KobisConstants.BASE_URL, apiPath, params, KobisMovieRankResponseDto.class);
    }

    private MultiValueMap<String, String> createParams(LocalDate date, String repNationCd, String apiPath) {
        String dateString = DateUtils.localDateToString(date, BatchConstants.YYYYMMDD);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("key", KOBIS_API_KEY);
        params.add("targetDt", dateString);
        if (repNationCd != null && (repNationCd.equals(KobisConstants.KOREAN_MOVIE) || repNationCd.equals(KobisConstants.FOREIGN_MOVIE))) {
            params.add("repNationCd", repNationCd);
        }
        if (apiPath.equals(KobisConstants.WEEKLY_BOX_OFFICE_PATH)) {
            params.add("weekGb", KobisConstants.WEEKLY_TOTAL);
        }
        return params;
    }
}
