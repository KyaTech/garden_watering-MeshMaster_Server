package org.meshmasterserver.system.controller.meshcontroller;


import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MeshAssignmentPairHandlerTest {

	@Mock
	private Assignment<Object> assignment;
	@Mock
	private Continuation<Object> continuation;

	@Mock
	private AssignmentPair assignmentPair;
	@Mock
	private AssignmentPair exception_assignmentPair;


	private MeshAssignmentPairHandler meshAssignmentPairHandler = new MeshAssignmentPairHandler();


	@Test
	public void test_add_and_run() throws Exception {

		meshAssignmentPairHandler.add(assignmentPair);
		meshAssignmentPairHandler.add(exception_assignmentPair);

		doThrow(new Exception()).doNothing().when(exception_assignmentPair).run();

		meshAssignmentPairHandler.run();

		verify(assignmentPair).run();
		verify(exception_assignmentPair,times(2)).run();

	}
}