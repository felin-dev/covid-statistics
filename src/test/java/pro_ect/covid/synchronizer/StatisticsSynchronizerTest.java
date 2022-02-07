package pro_ect.covid.synchronizer;

import pro_ect.covid.synchronizer.StatisticsSynchronizerTest.EnableSchedulingConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.TestPropertySource;
import pro_ect.covid.service.StatisticsService;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {EnableSchedulingConfiguration.class, StatisticsSynchronizer.class})
@TestPropertySource(properties = "synchronizer.schedule=* * * * * *") // every second
class StatisticsSynchronizerTest {

    @MockBean
    private StatisticsService service;

    @Test
    void assertCountryStatisticsRefreshIsTriggered() {
        await().untilAsserted(() -> verify(service, atLeast(1)).refreshCountryStatistics());
    }

    @Configuration
    @EnableScheduling
    static class EnableSchedulingConfiguration {
    }
}