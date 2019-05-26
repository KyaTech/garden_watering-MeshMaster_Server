package org.meshmasterserver.system.controller.callback;

public class RegistrationPayloadData {
	//{"registration":{"module":"sensor","node":1},"request_id":101}

	private RegistrationData registration;
	private int request_id;

	public static class RegistrationData {
		private ModuleType module;
		private int node;
		private int index = 0;
		private int pin = -1;

		public RegistrationData() {
		}

		public int getPin() {
			return pin;
		}

		public void setPin(int pin) {
			this.pin = pin;
		}

		public void setModule(ModuleType module) {
			this.module = module;
		}

		public void setNode(int node) {
			this.node = node;
		}

		public ModuleType getModule() {
			return module;
		}

		public int getNode() {
			return node;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public RegistrationPayloadData() {
	}

	public void setRegistration(RegistrationData registration) {
		this.registration = registration;
	}

	public void setRequest_id(int request_id) {
		this.request_id = request_id;
	}

	public RegistrationData getRegistration() {
		return registration;
	}

	public int getRequest_id() {
		return request_id;
	}
}
