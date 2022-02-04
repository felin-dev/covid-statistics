package pro_ect.covid.repository;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryStaticsEntity {

    @Id
    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String countryCode;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private long newConfirmed;
    @Column(nullable = false)
    private long totalConfirmed;
    @Column(nullable = false)
    private long newDeaths;
    @Column(nullable = false)
    private long totalDeaths;
    @Column(nullable = false)
    private long newRecovered;
    @Column(nullable = false)
    private long totalRecovered;
    @Column(nullable = false)
    private Instant date;
}
