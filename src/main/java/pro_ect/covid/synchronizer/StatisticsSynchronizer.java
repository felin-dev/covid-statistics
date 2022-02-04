package pro_ect.covid.synchronizer;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro_ect.covid.service.StatisticsService;

@Component
@RequiredArgsConstructor
public class StatisticsSynchronizer {

    private final StatisticsService service;

    @Scheduled(cron = "0 * * * * *") // every minute
    public void synchronize() {
        service.refreshCountryStatistics();
    }
}
