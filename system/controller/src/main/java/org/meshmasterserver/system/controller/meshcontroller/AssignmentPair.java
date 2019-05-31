package org.meshmasterserver.system.controller.meshcontroller;

public class AssignmentPair<Type> {

	private Assignment<Type> assignment;
	private Continuation<Type> continuation;

	public AssignmentPair(Assignment<Type> assignment, Continuation<Type> continuation) {
		this.assignment = assignment;
		this.continuation = continuation;
	}

	public void run() throws Exception{
		Type result = assignment.execute();
		continuation.run(result);
	}

}
