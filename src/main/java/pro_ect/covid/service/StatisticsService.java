package pro_ect.covid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro_ect.covid.domain.CountryStatistics;
import pro_ect.covid.repository.CountryStatisticsEntity;
import pro_ect.covid.repository.StatisticsRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static java.util.stream.StreamSupport.stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsClient client;
    private final StatisticsRepository repository;

    @PostConstruct
    private void tryRefreshCountryStatistics() {
        try {
            refreshCountryStatistics();
        } catch (RuntimeException e) {
            log.error("Failed to refresh the countries statistics: ", e);
        }
    }

    public Optional<CountryStatistics> findCountryStatisticsByCode(String countryCode) {
        return repository.findByCountryCode(countryCode)
                .map(statistics -> new CountryStatistics(
                        statistics.getId(),
                        statistics.getCountry(),
                        statistics.getCountryCode(),
                        statistics.getSlug(),
                        statistics.getNewConfirmed(),
                        statistics.getTotalConfirmed(),
                        statistics.getNewDeaths(),
                        statistics.getTotalDeaths(),
                        statistics.getNewRecovered(),
                        statistics.getTotalRecovered(),
                        statistics.getDate()));
    }

    public void refreshCountryStatistics() {
        repository.saveAll(stream(client.fetchCountryStatistics().spliterator(), false)
                .map(statistics -> new CountryStatisticsEntity(
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
                .toList());
        log.info("Country statistics updated.");
    }
}

