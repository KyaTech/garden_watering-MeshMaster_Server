package org.meshmasterserver.system.controller.database.modell;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "sensor_values")
public class SensorValue implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "value_id")
	private long valueID;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "sensor_index"),
		@JoinColumn(name = "node_id")
	})
	private Sensor sensor;

	private double value;

	@CreationTimestamp
	@Column(name = "created_at",updatable = false)
	private Date createdAt;

	public SensorValue() {}

	public SensorValue(Sensor sensor, double value) {
		this.sensor = sensor;
		this.value = value;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getValueID() {
		return valueID;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public double getValue() {
		return value;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
}
