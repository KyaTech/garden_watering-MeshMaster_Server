package systemcontroller.config;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigTest {

	@BeforeClass
	public static void setUp() throws Exception {
		Config.changeConfigFile("src/test/resources/test_config.yaml");
	}

	@Test
	public void test_getApiHost() {
		Assertions.assertThat(Config.getApiHost()).isEqualTo("testhost.com");
	}

	@Test
	public void test_getApiPort() {
		Assertions.assertThat(Config.getApiPort()).isEqualTo(8080);
	}

	@Test
	public void test_getApiPrefix() {
		Assertions.assertThat(Config.getApiPrefix()).isEqualTo("/api/v1");
	}
}