package pro_ect.covid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
class CovidStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CovidStatisticsApplication.class, args);
    }
}
