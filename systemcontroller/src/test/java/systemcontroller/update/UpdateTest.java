package systemcontroller.update;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import systemcontroller.config.Config;

public class UpdateTest {

	private static int hits = 0;

	@BeforeClass
	public static void beforeAll() throws Exception {
		Config.changeConfigFile("src/test/java/resources/test_config.yaml");
	}

	@Before
	public void setUp() throws Exception {
		hits = 0;
	}

	@After
	public void tearDown() throws Exception {
		Update.stop();
	}

	@Test
	public void test_start() throws InterruptedException {
		Update.changeRunnable(UpdateTest::incrementHits);
		Update.start();

		Thread.sleep((long) (Config.getTimerPeriod() / 2));


		assertThat(hits).isEqualTo(1);
	}

	@Test
	public void test_stop() throws InterruptedException {
		Update.changeRunnable(UpdateTest::incrementHits);
		Update.start();

		Thread.sleep((long) (Config.getTimerPeriod() * 1.5));

		Update.stop();

		Thread.sleep(Config.getTimerPeriod());

		assertThat(hits).isEqualTo(2);
	}

	public static void incrementHits() {
		hits++;
	}

}