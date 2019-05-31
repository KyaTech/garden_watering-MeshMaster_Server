package org.meshmasterserver.system.controller.meshcontroller;

@FunctionalInterface
public interface Continuation<ParameterType> {

	void run(ParameterType parameter);

}
