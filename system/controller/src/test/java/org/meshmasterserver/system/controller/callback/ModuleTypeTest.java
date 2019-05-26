package org.meshmasterserver.system.controller.callback;


import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ModuleTypeTest {

	@Test
	public void test_fromText_valve() {
		assertThat(ModuleType.fromText("valve")).isEqualTo(ModuleType.Valve);
	}

	@Test
	public void test_fromText_sensor() {
		assertThat(ModuleType.fromText("sensor")).isEqualTo(ModuleType.Sensor);
	}
	@Test
	public void test_fromText_null() {
		try {
			ModuleType.fromText(null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertThat(true).isTrue();
		}

	}
	@Test
	public void test_fromText_notFound() {
		try {
			ModuleType.fromText("trash");
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertThat(true).isTrue();
		}
	}
}