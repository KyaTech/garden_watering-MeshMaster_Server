package org.meshmasterserver.system.controller.database.modell;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nodes")
public class Node implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "node_id")
	private int id;

	@Column(columnDefinition = "text")
	private String name;
	@Column(columnDefinition = "text")
	private String description;
	@Column(columnDefinition = "text")
	private String place;

	@Column(name = "mesh_node_id")
	private int meshNodeId;

	public Node() {}

	public Node(int id,String name, String description, String place, int meshNodeId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.place = place;
		this.meshNodeId = meshNodeId;
	}

	public Node(String name, String description, String place, int meshNodeId) {
		this.name = name;
		this.description = description;
		this.place = place;
		this.meshNodeId = meshNodeId;
	}

	public int getId() {
		return id;
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

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getMeshNodeId() {
		return meshNodeId;
	}

	public void setMeshNodeId(int meshNodeId) {
		this.meshNodeId = meshNodeId;
	}
}
