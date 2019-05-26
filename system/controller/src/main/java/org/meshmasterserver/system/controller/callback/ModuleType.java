package org.meshmasterserver.system.controller.callback;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ModuleType {
	Sensor("sensor"), Valve("valve");

	private final String string;
	ModuleType(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return this.string;
	}

	@JsonCreator
	public static ModuleType fromText(String value) {
		if(value == null) {
			throw new IllegalArgumentException();
		}
		for(ModuleType m : values()) {
			if(value.equals(m.toString())) {
				return m;
			}
		}
		throw new IllegalArgumentException();
	}
}
