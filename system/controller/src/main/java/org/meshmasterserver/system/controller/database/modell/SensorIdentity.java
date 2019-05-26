package org.meshmasterserver.system.controller.database.modell;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class SensorIdentity implements Serializable {

	@ManyToOne
	@JoinColumn(name = "node_id")
	private Node node;

	@Column
	private int index = 0;

	public SensorIdentity() {

	}

	public SensorIdentity(Node node) {
		this.node = node;
	}

	public SensorIdentity(Node node, int index) {
		this.node = node;
		this.index = index;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
