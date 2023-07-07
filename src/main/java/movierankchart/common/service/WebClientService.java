package movierankchart.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebClientService {
    public WebClient createWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public <T> T get(String baseUrl, String path, MultiValueMap<String, String> params, Class<T> responseDto) {
        String jsonData = createWebClient(baseUrl).get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParams(params)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(jsonData, responseDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}