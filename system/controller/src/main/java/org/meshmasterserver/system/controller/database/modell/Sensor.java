package org.meshmasterserver.system.controller.database.modell;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sensors")
public class Sensor implements Serializable {

	@EmbeddedId
	private SensorIdentity identity;

	@Column(columnDefinition = "text")
	private String name;
	@Column(columnDefinition = "text")
	private String description;
	private int pin;

	public Sensor() {}

	public Sensor(SensorIdentity identity, String name, String description, int pin) {
		this.identity = identity;
		this.name = name;
		this.description = description;
		this.pin = pin;
	}

	public SensorIdentity getIdentity() {
		return identity;
	}

	public void setIdentity(SensorIdentity identity) {
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
