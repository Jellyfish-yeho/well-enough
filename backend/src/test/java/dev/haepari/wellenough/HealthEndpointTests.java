package dev.haepari.wellenough;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class HealthEndpointTests {

	@Autowired
	private MockMvcTester mvc;

	@Test
	void healthReturnsUpWithoutDetails() {
		assertThat(mvc.get().uri("/actuator/health"))
			.hasStatusOk()
			.bodyJson()
			.extractingPath("$.status")
			.isEqualTo("UP");
		assertThat(mvc.get().uri("/actuator/health"))
			.bodyJson()
			.doesNotHavePath("$.components");
	}

	@Test
	void otherActuatorEndpointsAreNotExposed() {
		assertThat(mvc.get().uri("/actuator/env")).hasStatus(HttpStatus.NOT_FOUND);
	}

}
