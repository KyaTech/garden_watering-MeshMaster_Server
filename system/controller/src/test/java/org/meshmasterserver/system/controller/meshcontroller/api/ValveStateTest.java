package org.meshmasterserver.system.controller.meshcontroller.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ValveStateTest {

	@Test
	public void fromString_ON() {
		Assertions.assertThat(ValveState.from("ON")).isEqualTo(ValveState.ON);
	}

	@Test
	public void fromString_OFF() {
		Assertions.assertThat(ValveState.from("OFF")).isEqualTo(ValveState.OFF);
	}
}