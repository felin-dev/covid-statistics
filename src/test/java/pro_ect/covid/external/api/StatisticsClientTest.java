package pro_ect.covid.external.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import pro_ect.covid.config.RestTemplateConfiguration;
import pro_ect.covid.domain.CountryStatistics;

import java.time.Instant;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.MockRestServiceServer.createServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@Import({RestTemplateConfiguration.class, StatisticsClient.class})
@TestPropertySource(properties = "statistics-api.url=https://api.covid19api.com")
class StatisticsClientTest {

    private static final CountryStatistics BULGARIAN_STATISTICS = new CountryStatistics(UUID.fromString("01fe8ab4-d8c8-4ba7-b8a4-8a039968f5a1"), "Bulgaria", "BG", "bulgaria", 0, 995436, 0, 33770, 0, 0, Instant.parse("2022-02-07T07:34:48.869Z"));
    private static final CountryStatistics UNITED_KINGDOM_STATISTICS = new CountryStatistics(UUID.fromString("8050a77c-fe27-42f3-9428-52730d1dd4ab"), "United Kingdom", "GB", "united-kingdom", 53426, 17923805, 75, 158856, 0, 0, Instant.parse("2022-02-07T07:34:48.869Z"));

    @Autowired
    private StatisticsClient client;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    void givenSuccessfulResponse_thenTheCountryStatisticsAreReturned() {
        MockRestServiceServer server = createServer(restTemplate);
        server.expect(once(), requestTo("https://api.covid19api.com/summary"))
                .andExpect(method(GET))
                .andRespond(withStatus(OK)
                        .contentType(APPLICATION_JSON)
                        .body(contentOf(requireNonNull(getClass().getResource("countryStatistics.json")), UTF_8)));

        assertThat(client.fetchCountryStatistics()).containsExactly(BULGARIAN_STATISTICS, UNITED_KINGDOM_STATISTICS);
    }

    @Test
    void givenErrorResponse_thenExceptionIsThrown() {
        MockRestServiceServer server = createServer(restTemplate);
        server.expect(once(), requestTo("https://api.covid19api.com/summary"))
                .andExpect(method(GET))
                .andRespond(withStatus(INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> client.fetchCountryStatistics()).isInstanceOf(HttpStatusCodeException.class);
    }
}