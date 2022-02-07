package pro_ect.covid.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pro_ect.covid.domain.CountryStatistics;
import pro_ect.covid.service.StatisticsService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatisticsService service;

    @Test
    void givenAvailableStatisticsForCountry_thenTheyAreReturned() throws Exception {
        when(service.findCountryStatisticsByCode("BG"))
                .thenReturn(Optional.of(new CountryStatistics(UUID.fromString("01fe8ab4-d8c8-4ba7-b8a4-8a039968f5a1"), "Bulgaria", "BG", "bulgaria", 0, 995436, 0, 33770, 0, 0, Instant.parse("2022-02-07T07:34:48.869Z"))));

        mockMvc
                .perform(get("/country/BG"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": "01fe8ab4-d8c8-4ba7-b8a4-8a039968f5a1",
                            "country": "Bulgaria",
                            "countryCode": "BG",
                            "slug": "bulgaria",
                            "newConfirmed": 0,
                            "totalConfirmed": 995436,
                            "newDeaths": 0,
                            "totalDeaths": 33770,
                            "newRecovered": 0,
                            "totalRecovered": 0,
                            "date": "2022-02-07T07:34:48.869Z"
                        }"""));
    }

    @Test
    void givenUnavailableStatisticsForCountry_thenNotFoundIsReturned() throws Exception {
        when(service.findCountryStatisticsByCode("BG")).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/country/BG"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No data available for country with code 'BG'.")));
    }

    @Test
    void givenInvalidCountryCode_thenBadRequestIsReturned() throws Exception {
        mockMvc
                .perform(get("/country/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Country code should be a ISO 3166-1 alpha-2 code.")));
    }

    @Test
    void givenServiceFailure_thenInternalServerErrorIsReturned() throws Exception {
        when(service.findCountryStatisticsByCode("BG")).thenThrow(new RuntimeException("Failed on purpose."));

        mockMvc
                .perform(get("/country/BG"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Unspecified error occurred.")));
    }
}
