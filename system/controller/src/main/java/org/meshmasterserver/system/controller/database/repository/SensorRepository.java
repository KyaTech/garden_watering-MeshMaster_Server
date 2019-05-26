package org.meshmasterserver.system.controller.database.repository;

import java.util.List;

import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, SensorIdentity> {
	List<Sensor> findAll();
	List<Sensor> findByIdentityNode(Node identity_node);
	Sensor findByIdentityNodeAndIdentityIndex(Node identity_node, int identity_index);
}
