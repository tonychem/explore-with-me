package statistics.client;

import dto.EndPointHitDto;
import dto.ViewStats;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.yandex.tonychem.utils.GlobalConstantConfig;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatisticsClient {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String statisticsURL = "http://localhost:9090";

    //Метод для регистрации просмотра события
    public void registerView(String app, String uri, String ip, LocalDateTime localDateTime) {
        EndPointHitDto endPointHitDto = new EndPointHitDto(app, uri, ip, localDateTime);
        HttpEntity<EndPointHitDto> requestEntity = new HttpEntity<>(endPointHitDto);
        restTemplate.postForObject(statisticsURL + "/hit", requestEntity, Void.class);
    }

    //Метод считает количество просмотров для заданного события
    public Long getViewCountForEvent(long eventId) {
        String minStartDateEncoded =
                URLEncoder.encode(GlobalConstantConfig.MINIMUM_DATE.format(GlobalConstantConfig.DEFAULT_FORMATTER),
                        StandardCharsets.UTF_8);

        String maxEndDateEncoded =
                URLEncoder.encode(GlobalConstantConfig.MAXIMUM_DATE.format(GlobalConstantConfig.DEFAULT_FORMATTER),
                        StandardCharsets.UTF_8);

        String pathEncoded =
                URLEncoder.encode("/event/" + eventId, StandardCharsets.UTF_8);

        Map<String, Object> params = Map.of("start", minStartDateEncoded,
                "end", maxEndDateEncoded,
                "uris", pathEncoded);

        ResponseEntity<List<ViewStats>> response =
                restTemplate.exchange(statisticsURL + "/stats?start={start}&end={end}&uris={uris}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        },
                        params);

        List<ViewStats> dtoList = response.getBody();

        if (dtoList == null || dtoList.size() == 0) {
            return 0L;
        }

        return dtoList.stream()
                .mapToLong(ViewStats::getHits)
                .sum();
    }
}
