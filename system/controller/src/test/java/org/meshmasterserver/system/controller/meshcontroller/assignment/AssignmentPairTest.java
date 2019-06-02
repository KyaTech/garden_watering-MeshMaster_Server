package org.meshmasterserver.system.controller.meshcontroller.assignment;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AssignmentPairTest {

	private AssignmentPair<Object> assignmentPair;

	@Mock
	private Assignment<Object> assignment;

	@Mock
	private Continuation<Object> continuation;

	@Before
	public void setUp() throws Exception {
		assignmentPair = new AssignmentPair<>(assignment, continuation);
	}

	@Test
	public void test_run() throws Exception {
		Object testObject = new Object();


		doReturn(testObject).when(assignment).execute();
		assignmentPair.run();

		verify(continuation).run(testObject);
	}
}