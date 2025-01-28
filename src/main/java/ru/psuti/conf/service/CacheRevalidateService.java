package ru.psuti.conf.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CacheRevalidateService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${frontend.domain.local}/api/revalidateTag")
    private String url;

    @Async
    public void invalidateCache(List<String> tags) {
        try {
            restTemplate.delete(url, Map.of("tags", tags));
        } catch (RestClientException e) {
            log.error("Ошибка при запросе на ревалидацию кеша:", e);
        }
    }

    @Async
    public void invalidateCache(String tag) {
        try {
            restTemplate.delete(url, Map.of("tags", List.of(tag)));
        } catch (RestClientException e) {
            log.error("Ошибка при запросе на ревалидацию кеша:", e);
        }
    }
}
