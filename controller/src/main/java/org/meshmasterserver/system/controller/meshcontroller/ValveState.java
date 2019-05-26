package org.meshmasterserver.system.controller.meshcontroller;

public enum ValveState {
	ON,
	OFF,
	;

	public static ValveState from(String state) {
		if (state.equals("ON")) {
			return ON;
		} else if (state.equals("OFF")) {
			return OFF;
		} else {
			return null;
		}
	}
}
