package movierankchart.domain.kmdb.service;

import lombok.RequiredArgsConstructor;
import movierankchart.common.service.WebClientService;
import movierankchart.domain.kmdb.constants.KmdbConstants;
import movierankchart.domain.kmdb.dto.KmdbMovieDetailResponseDto;
import movierankchart.domain.kobis.constants.KobisConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class KmdbService {

    @Value("${kmdb.api.key}")
    private static String KMDB_API_KEY;

    private final WebClientService webClientService;

    public KmdbMovieDetailResponseDto findMovieDetail(String openDt, String title) {
        MultiValueMap<String, String> params = createParams(openDt,title);
        return webClientService.get(KmdbConstants.BASE_URL, "", params, KmdbMovieDetailResponseDto.class);
    }

    private MultiValueMap<String, String> createParams(String openDt, String title) {
        String releaseDts = openDt.replace(KobisConstants.OPEN_DT_DELIMITER,"");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ServiceKey", KMDB_API_KEY);
        params.add("collection", KmdbConstants.COLLECTION_PARAM_VALUE);
        params.add("title", title);
        params.add("releaseDts", releaseDts);
        return params;
    }
}
