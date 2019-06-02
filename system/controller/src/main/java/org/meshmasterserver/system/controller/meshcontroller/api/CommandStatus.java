package org.meshmasterserver.system.controller.meshcontroller.api;

public enum CommandStatus {
	ERROR,
	OK;

	public static CommandStatus from(String status) {
		if ("OK".equals(status)) {
			return OK;
		} else if ("ERROR".equals(status)) {
			return ERROR;
		}
		return null;
	}
}
