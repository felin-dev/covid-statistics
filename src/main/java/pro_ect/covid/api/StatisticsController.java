package pro_ect.covid.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pro_ect.covid.domain.CountryStatistics;
import pro_ect.covid.service.StatisticsService;

import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Validated
@RestController
@RequiredArgsConstructor
class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("country/{code}")
    public ResponseEntity<CountryStatisticsResponse> getCountryStatistics(
            @Pattern(regexp = "[A-Z]{2}", message = "Country code should be a ISO 3166-1 alpha-2 code.") @PathVariable String code) {
        return statisticsService.findCountryStatisticsByCode(code)
                .map(CountryStatisticsResponse::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ErrorResponseStatusException(NOT_FOUND, "No data available for country with code '%s'.".formatted(code)));
    }

    private record CountryStatisticsResponse(
            UUID id,
            String country,
            String countryCode,
            String slug,
            long newConfirmed,
            long totalConfirmed,
            long newDeaths,
            long totalDeaths,
            long newRecovered,
            long totalRecovered,
            Instant date) {

        CountryStatisticsResponse(CountryStatistics statistics) {
            this(
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
                    statistics.date());
        }
    }
}
