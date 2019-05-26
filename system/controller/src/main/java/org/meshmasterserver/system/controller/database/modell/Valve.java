package org.meshmasterserver.system.controller.database.modell;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "valves")
public class Valve implements Serializable {

	@EmbeddedId
	private ValveIdentity identity;

	@Column(columnDefinition = "text")
	private String name;
	@Column(columnDefinition = "text")
	private String description;

	private int pin;

	public Valve() {

	}

	public Valve(ValveIdentity identity, String name, String description, int pin) {
		this.identity = identity;
		this.name = name;
		this.description = description;
		this.pin = pin;
	}


	public ValveIdentity getIdentity() {
		return identity;
	}

	public void setIdentity(ValveIdentity identity) {
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}
}
