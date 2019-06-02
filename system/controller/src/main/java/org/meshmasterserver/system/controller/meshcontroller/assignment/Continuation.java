package org.meshmasterserver.system.controller.meshcontroller.assignment;

@FunctionalInterface
public interface Continuation<ParameterType> {

	void run(ParameterType parameter);

}
