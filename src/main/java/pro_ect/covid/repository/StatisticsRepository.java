package pro_ect.covid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatisticsRepository extends JpaRepository<CountryStatisticsEntity, UUID> {

    Optional<CountryStatisticsEntity> findByCountryCode(String countryCode);
}
