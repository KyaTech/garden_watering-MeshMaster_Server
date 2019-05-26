package org.meshmasterserver.system.controller.database.repository;

import java.util.List;

import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorValueRepository extends JpaRepository<SensorValue, Long> {
	@Override
	List<SensorValue> findAll();
	List<SensorValue> findBySensor(Sensor sensor);
}
