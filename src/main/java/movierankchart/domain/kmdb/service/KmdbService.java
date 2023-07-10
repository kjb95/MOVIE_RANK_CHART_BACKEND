package movierankchart.domain.kmdb.service;

import lombok.RequiredArgsConstructor;
import movierankchart.common.exception.ErrorCode;
import movierankchart.common.service.WebClientService;
import movierankchart.domain.kmdb.constants.KmdbConstants;
import movierankchart.domain.kmdb.dto.KmdbMovieDetailResponseDto;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class KmdbService {

    @Value("${kmdb.api.key}")
    private String KMDB_API_KEY;

    private final WebClientService webClientService;

    public KmdbResultResponseDto findMovieDetail(String title, String movieCd, String openDt) {
        MultiValueMap<String, String> params = createParams(title);
        KmdbMovieDetailResponseDto kmdbMovieDetailResponseDto = webClientService.get(KmdbConstants.BASE_URL, "", params, KmdbMovieDetailResponseDto.class);
        List<KmdbResultResponseDto> results = kmdbMovieDetailResponseDto.getData()
                .get(0)
                .getResult();
        return findMovieDetail(results, movieCd, openDt);
    }

    private MultiValueMap<String, String> createParams(String title) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ServiceKey", KMDB_API_KEY);
        params.add("collection", KmdbConstants.COLLECTION_PARAM_VALUE);
        params.add("title", title);
        return params;
    }

    private KmdbResultResponseDto findMovieDetail(List<KmdbResultResponseDto> results, String movieCd, String openDt) {
        if (results == null) {
            throw new NoSuchElementException(ErrorCode.KMDB_MOVIE_DETAIL_NOT_FOUND.getMessage());
        }
        KmdbResultResponseDto kmdbResultResponseDto = results.stream()
                .filter(result -> filterByMovieCdOrOpenDt(result, movieCd, openDt))
                .findFirst()
                .orElse(results.get(0));
        kmdbResultResponseDto.setOpenDt(openDt);
        return kmdbResultResponseDto;
    }

    private boolean filterByMovieCdOrOpenDt(KmdbResultResponseDto result, String movieCd, String openDt) {
        boolean isMatchMovieCd = result.getCodes()
                .getCode()
                .equals(movieCd);
        boolean isMatchOpenDt = result.getRatings()
                .getRating()
                .get(0)
                .getReleaseDate()
                .contains(openDt);
        return isMatchMovieCd || isMatchOpenDt;
    }
}
