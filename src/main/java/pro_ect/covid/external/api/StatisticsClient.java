package pro_ect.covid.external.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro_ect.covid.domain.CountryStatistics;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public class StatisticsClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    StatisticsClient(@Value("${statistics-api.url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public Iterable<CountryStatistics> fetchCountryStatistics() {
        return requireNonNull(restTemplate.getForObject(baseUrl + "/summary", Summary.class))
                .countries().stream()
                .map(statistics -> new CountryStatistics(
                        statistics.id(),
                        statistics.country(),
                        statistics.countryCode(),
                        statistics.slug(),
                        statistics.newConfirmed(),
                        statistics.totalConfirmed(),
                        statistics.newDeaths(),
                        statistics.totalDeaths(),
                        statistics.newRecovered(),
                        statistics.totalRecovered(),
                        statistics.date()))
                .toList();
    }

    private record Summary(@JsonProperty("Countries") List<CountryStatistics> countries) {

        private record CountryStatistics(
                @JsonProperty("ID") UUID id,
                @JsonProperty("Country") String country,
                @JsonProperty("CountryCode") String countryCode,
                @JsonProperty("Slug") String slug,
                @JsonProperty("NewConfirmed") long newConfirmed,
                @JsonProperty("TotalConfirmed") long totalConfirmed,
                @JsonProperty("NewDeaths") long newDeaths,
                @JsonProperty("TotalDeaths") long totalDeaths,
                @JsonProperty("NewRecovered") long newRecovered,
                @JsonProperty("TotalRecovered") long totalRecovered,
                @JsonProperty("Date") Instant date) {
        }
    }
}
