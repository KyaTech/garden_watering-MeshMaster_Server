package org.meshmasterserver.system.controller.meshcontroller.api;


import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CommandStatusTest {

	@Test
	public void test_from_OK() {
		Assertions.assertThat(CommandStatus.from("OK")).isEqualTo(CommandStatus.OK);
	}

	@Test
	public void test_from_ERROR() {
		Assertions.assertThat(CommandStatus.from("ERROR")).isEqualTo(CommandStatus.ERROR);
	}
}