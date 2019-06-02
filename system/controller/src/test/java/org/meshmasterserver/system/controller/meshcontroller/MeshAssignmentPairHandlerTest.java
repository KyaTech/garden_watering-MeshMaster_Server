package org.meshmasterserver.system.controller.meshcontroller;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.function.BiFunction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meshmasterserver.system.controller.config.Config;
import org.meshmasterserver.system.controller.config.MeshControllerConfig;
import org.meshmasterserver.system.controller.meshcontroller.api.InvalidNodeException;
import org.meshmasterserver.system.controller.meshcontroller.assignment.AssignmentPair;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MeshAssignmentPairHandlerTest {


	private Config config = new Config();
	private MeshControllerConfig meshControllerConfig = new MeshControllerConfig();
	@Mock
	private AssignmentPair assignmentPair;
	@Mock
	private AssignmentPair exception_assignmentPair;

	@Mock
	private Map<AssignmentPair, Integer> failedAssignments;

	private MeshAssignmentPairHandler meshAssignmentPairHandler;

	@Before
	public void setUp() throws Exception {
		config.setMeshcontroller(meshControllerConfig);
		meshAssignmentPairHandler = new MeshAssignmentPairHandler(config);
	}

	@Test
	public void test_add_and_run() throws Exception {

		meshControllerConfig.setRetries(3);

		meshAssignmentPairHandler.add(assignmentPair);
		meshAssignmentPairHandler.add(exception_assignmentPair);

		doThrow(new InvalidNodeException()).doNothing().when(exception_assignmentPair).run();

		meshAssignmentPairHandler.run();

		verify(assignmentPair).run();
		verify(exception_assignmentPair,times(2)).run();

	}

	@Test
	public void test_add_and_run_retries_fail() throws Exception {

		meshControllerConfig.setRetries(3);

		meshAssignmentPairHandler.add(exception_assignmentPair);

		doThrow(new InvalidNodeException()).doThrow(new InvalidNodeException()).doThrow(new InvalidNodeException()).doNothing().when(exception_assignmentPair).run();

		meshAssignmentPairHandler.run();

		verify(exception_assignmentPair, times(3)).run();

	}

	@Test
	public void test_add_and_run_retries_success() throws Exception {

		meshControllerConfig.setRetries(3);

		meshAssignmentPairHandler.add(exception_assignmentPair);

		doThrow(new InvalidNodeException()).doThrow(new InvalidNodeException()).doNothing().when(exception_assignmentPair).run();

		meshAssignmentPairHandler.run();

		verify(exception_assignmentPair, times(3)).run();

	}

	@Test
	public void test_handleRetries_no_more() {
		meshControllerConfig.setRetries(3);


		when(failedAssignments.getOrDefault(any(AssignmentPair.class), anyInt())).thenReturn(2);

		meshAssignmentPairHandler.handleRetries(failedAssignments, assignmentPair, new IllegalArgumentException());

		verify(failedAssignments, times(0)).compute(any(), any());

	}

	@Test
	public void test_handleRetries_one_more() {
		meshControllerConfig.setRetries(3);


		when(failedAssignments.getOrDefault(any(AssignmentPair.class), anyInt())).thenReturn(1);

		meshAssignmentPairHandler.handleRetries(failedAssignments, assignmentPair, new IllegalArgumentException());


		ArgumentCaptor<BiFunction<AssignmentPair, Integer, Integer>> argumentCaptor = ArgumentCaptor.forClass(BiFunction.class);
		verify(failedAssignments, times(1)).compute(eq(assignmentPair), argumentCaptor.capture());

		BiFunction<AssignmentPair, Integer, Integer> value = argumentCaptor.getValue();
		assertThat(value.apply(assignmentPair, 2)).isEqualTo(3);

	}


}