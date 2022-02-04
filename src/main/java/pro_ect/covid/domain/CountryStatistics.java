package pro_ect.covid.domain;

import java.time.Instant;
import java.util.UUID;

public record CountryStatistics(
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
}