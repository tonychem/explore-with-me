package statistics.client;

import dto.EndPointHitDto;
import dto.ViewStats;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class StatisticsClient {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String statisticsUrl;
    private static final String MINIMUM_START_DATE = "1900-01-01 00:00:00";
    private static final String MAXIMUM_END_DATE = "2999-12-12 23:59:59";

    public StatisticsClient(String statisticsUrl) {
        this.statisticsUrl = statisticsUrl;
    }

    public void registerView(String app, String uri, String ip, LocalDateTime localDateTime) {
        EndPointHitDto endPointHitDto = new EndPointHitDto(app, uri, ip, localDateTime);
        HttpEntity<EndPointHitDto> requestEntity = new HttpEntity<>(endPointHitDto);
        restTemplate.postForObject(statisticsUrl + "/hit", requestEntity, Void.class);
    }

    public Integer getViewCountForEvent(long eventId) {
        String minStartDateEncoded =
                URLEncoder.encode(MINIMUM_START_DATE, StandardCharsets.UTF_8);

        String maxEndDateEncoded =
                URLEncoder.encode(MAXIMUM_END_DATE, StandardCharsets.UTF_8);

        String pathEncoded =
                URLEncoder.encode("/event/" + eventId, StandardCharsets.UTF_8);

        Map<String, Object> params = Map.of("start", minStartDateEncoded,
                "end", maxEndDateEncoded,
                "uris", pathEncoded);

        ResponseEntity<List<ViewStats>> response =
                restTemplate.exchange(statisticsUrl + "/stats?start={start}&end={end}&uris={uris}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        },
                        params);

        List<ViewStats> dtoList = response.getBody();

        if (dtoList == null || dtoList.size() == 0) {
            return 0;
        }

        return (int) dtoList.stream()
                .mapToLong(ViewStats::getHits)
                .sum();
    }
}
