package org.meshmasterserver.system.controller.meshcontroller.assignment;

@FunctionalInterface
public interface Assignment<ReturnType> {

	ReturnType execute() throws IllegalArgumentException;

}
