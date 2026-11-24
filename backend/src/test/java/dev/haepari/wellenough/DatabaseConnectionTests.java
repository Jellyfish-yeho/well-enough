package dev.haepari.wellenough;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class DatabaseConnectionTests {

	@Autowired
	private JdbcClient jdbcClient;

	@Test
	void connectsToPostgres18() {
		int versionNumber = jdbcClient.sql("select current_setting('server_version_num')::int")
			.query(Integer.class)
			.single();

		assertThat(versionNumber / 10000).isEqualTo(18);
	}

}
