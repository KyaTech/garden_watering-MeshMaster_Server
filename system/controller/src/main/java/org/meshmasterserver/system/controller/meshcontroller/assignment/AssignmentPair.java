package org.meshmasterserver.system.controller.meshcontroller.assignment;

public class AssignmentPair<Type> {

	private Assignment<Type> assignment;
	private Continuation<Type> continuation;

	public AssignmentPair(Assignment<Type> assignment, Continuation<Type> continuation) {
		this.assignment = assignment;
		this.continuation = continuation;
	}

	public void run() throws IllegalArgumentException {
		Type result = null;
		try {
			result = assignment.execute();
		} catch (IllegalArgumentException e) {
			throw e;
		}
		continuation.run(result);
	}

}
