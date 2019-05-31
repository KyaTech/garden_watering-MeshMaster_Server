package org.meshmasterserver.system.controller.meshcontroller;

public enum ValveState {
	ON(true),
	OFF(false),
	;

	private boolean binary;

	ValveState(boolean binary) {
		this.binary = binary;
	}

	public static ValveState from(String state) {
		if (state.equals("ON")) {
			return ON;
		} else if (state.equals("OFF")) {
			return OFF;
		} else {
			return null;
		}
	}

	public static ValveState from(boolean binary) {
		if (binary) {
			return ON;
		} else {
			return OFF;
		}
	}

	public boolean asBinary() {
		return binary;
	}
}
