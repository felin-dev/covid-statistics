package pro_ect.covid.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import pro_ect.covid.domain.CountryStatistics;
import pro_ect.covid.external.api.StatisticsClient;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(StatisticsService.class)
class StatisticsServiceTest {

    private static final CountryStatistics BULGARIAN_STATISTICS = new CountryStatistics(UUID.fromString("56c43148-555a-4056-9f33-1e9e61d8a786"), "Bulgaria", "BG", "bulgaria", 0, 993255, 0, 33728, 0, 0, Instant.parse("2022-02-06T19:30:09.322Z"));
    private static final CountryStatistics UPDATED_BULGARIAN_STATISTICS = new CountryStatistics(UUID.fromString("56c43148-555a-4056-9f33-1e9e61d8a786"), "Bulgaria", "BG", "bulgaria", 2, 993266, 1, 33748, 3, 4, Instant.parse("2022-02-07T19:30:09.322Z"));
    private static final CountryStatistics GERMAN_STATISTICS = new CountryStatistics(UUID.fromString("60bb64aa-8bba-4e39-bbda-c801eee10709"), "Germany", "DE", "germany", 127116, 11065146, 41, 118722, 0, 0, Instant.parse("2022-02-06T19:30:09.322Z"));

    @Autowired
    private StatisticsClient client;
    @Autowired
    private StatisticsService service;

    @Test
    void assertCountryStatisticsAreRefreshed() {
        service.refreshCountryStatistics();

        assertThat(service.findCountryStatisticsByCode("BG")).contains(BULGARIAN_STATISTICS);
        assertThat(service.findCountryStatisticsByCode("DE")).contains(GERMAN_STATISTICS);
        assertThat(service.findCountryStatisticsByCode("GR")).isEmpty();

        // update
        when(client.fetchCountryStatistics())
                .thenReturn(List.of(UPDATED_BULGARIAN_STATISTICS, GERMAN_STATISTICS));

        service.refreshCountryStatistics();

        assertThat(service.findCountryStatisticsByCode("BG")).contains(UPDATED_BULGARIAN_STATISTICS);
        assertThat(service.findCountryStatisticsByCode("DE")).contains(GERMAN_STATISTICS);
        assertThat(service.findCountryStatisticsByCode("GR")).isEmpty();
    }

    @TestConfiguration
    static class StatisticsClientConfiguration {

        @Bean
        StatisticsClient client() {
            StatisticsClient client = mock(StatisticsClient.class);
            when(client.fetchCountryStatistics())
                    .thenReturn(List.of(BULGARIAN_STATISTICS, GERMAN_STATISTICS));

            return client;
        }
    }
}