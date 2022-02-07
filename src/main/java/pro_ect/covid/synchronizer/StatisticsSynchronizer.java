package pro_ect.covid.synchronizer;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro_ect.covid.service.StatisticsService;

@Component
@RequiredArgsConstructor
class StatisticsSynchronizer {

    private final StatisticsService service;

    @Scheduled(cron = "${synchronizer.schedule}")
    void synchronize() {
        service.refreshCountryStatistics();
    }
}
