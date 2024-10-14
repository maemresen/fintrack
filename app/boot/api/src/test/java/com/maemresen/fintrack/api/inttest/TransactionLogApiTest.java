package com.maemresen.fintrack.api.inttest;

import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.business.logic.dto.TransactionLogCreateRequestDto;
import com.maemresen.fintrack.business.logic.dto.TransactionLogDto;
import com.maemresen.fintrack.persistence.repository.TransactionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.maemresen.fintrack.util.test.ResponseEntityAssert.assertResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integrationTest")
@SpringBootTest(
        classes = FintrackApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:tc:postgresql:17:///inttest_db"
)
class TransactionLogApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TransactionLogRepository repository;

    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new TestRestTemplate(new RestTemplateBuilder().rootUri("http://localhost:" + port));
        repository.deleteAll();
    }

    private ResponseEntity<TransactionLogDto> create(TransactionLogCreateRequestDto createRequestDto) {
        return restTemplate.postForEntity("/transaction-log", createRequestDto, TransactionLogDto.class);
    }

    private ResponseEntity<TransactionLogDto[]> findAll() {
        return restTemplate.getForEntity("/transaction-log", TransactionLogDto[].class);
    }

    @Test
    void shouldRespondAllTransactionLogs() {
        var name = "name";
        var createRequest = TransactionLogCreateRequestDto.builder()
                .name(name)
                .build();

        var createResponse = create(createRequest);
        assertResponseEntity(createResponse)
                .isStatus2xx()
                .containsBody();

        var createResponseBody = createResponse.getBody();
        assertThat(createResponseBody)
                .extracting(TransactionLogDto::getId)
                .isNotNull();

        var findAllResponse = findAll();
        assertResponseEntity(findAllResponse)
                .isStatus2xx()
                .containsBody();

        var findAllResponseBody = findAllResponse.getBody();
        assertThat(findAllResponseBody).isNotNull();
        assertThat(findAllResponseBody[0])
                .extracting(TransactionLogDto::getId)
                .isEqualTo(Objects.requireNonNull(createResponse.getBody()).getId());

    }
}
