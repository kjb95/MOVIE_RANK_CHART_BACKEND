package movierankchart.common.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import movierankchart.domain.kmdb.constants.KmdbConstants;
import movierankchart.domain.kobis.constants.KobisConstants;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebClientService {
    private WebClient kmdbWebCleint;
    private WebClient kobisWebClient;
    private Map<String, WebClient> baseUrlToWebclient = new HashMap<>();

    public WebClientService() {
        kmdbWebCleint = WebClient.builder()
                .baseUrl(KmdbConstants.BASE_URL)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs()
                        .maxInMemorySize(-1))
                .build();
        kobisWebClient = WebClient.builder()
                .baseUrl(KobisConstants.BASE_URL)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs()
                        .maxInMemorySize(-1))
                .build();
        baseUrlToWebclient.put(KmdbConstants.BASE_URL, kmdbWebCleint);
        baseUrlToWebclient.put(KobisConstants.BASE_URL, kobisWebClient);
    }

    public <T> T get(String baseUrl, String path, MultiValueMap<String, String> params, Class<T> responseDto) {
        WebClient webClient = baseUrlToWebclient.get(baseUrl);
        String jsonData = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParams(params)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(RuntimeException::new)
                .block();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            return objectMapper.readValue(jsonData, responseDto);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }
}