package dev.haepari.wellenough;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/** 테스트할 때만 잠깐 켜지는 PostgreSQL. 로컬 개발 DB는 건드리지 않는다. */
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer postgresContainer() {
		// docker-compose.yml과 같은 메이저 버전을 쓴다.
		return new PostgreSQLContainer(DockerImageName.parse("postgres:18"));
	}

}
