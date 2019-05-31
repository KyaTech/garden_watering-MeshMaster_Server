package org.meshmasterserver.system.controller.update;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UpdateTaskTest {

	private UpdateTask updateTask;

	@MockBean
	private UpdateMesh updateMesh;

	@Before
	public void setUp() {
		updateTask = new UpdateTask(updateMesh);
	}

	@Test
	public void test_coming() {

	}
}