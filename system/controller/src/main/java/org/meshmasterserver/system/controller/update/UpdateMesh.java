package org.meshmasterserver.system.controller.update;

import static org.meshmasterserver.system.controller.meshcontroller.MeshControllerApi.*;

import java.util.List;

import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorValue;
import org.meshmasterserver.system.controller.database.modell.Valve;
import org.meshmasterserver.system.controller.database.repository.NodeRepository;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.SensorValueRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.meshmasterserver.system.controller.meshcontroller.MeshController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UpdateMesh implements UpdateStep {

	private final Logger log = LoggerFactory.getLogger(UpdateMesh.class);


	private final NodeRepository nodeRepository;
	private final SensorRepository sensorRepository;
	private final ValveRepository valveRepository;
	private final MeshController meshController;
	private final SensorValueRepository sensorValueRepository;



	@Autowired
	public UpdateMesh(MeshController meshController, SensorRepository sensorRepository, ValveRepository valveRepository, SensorValueRepository sensorValueRepository, NodeRepository nodeRepository) {
		this.nodeRepository = nodeRepository;
		this.sensorRepository = sensorRepository;
		this.valveRepository = valveRepository;
		this.meshController = meshController;
		this.sensorValueRepository = sensorValueRepository;
	}

	@Override
	public void run() {

		log.info("Updating mesh nodes");

		for (Node node : nodeRepository.findAll()) {

			updateSensors(node);

		}

	}

	void updateSensors(Node node) {
		List<Sensor> sensors = sensorRepository.findByIdentityNode(node);

		log.debug("Updating sensors");

		for (Sensor sensor : sensors) {
			double value = meshController.requestSensor(node.getId(), sensor.getIdentity().getIndex());

			saveSensorValue(sensor,value);
		}
	}

	void saveSensorValue(Sensor sensor, double value) {

		log.debug("Saving sensor values");

		if (value != INVALID_VALUE) {
			SensorValue sensorValue = new SensorValue(sensor, value);

			log.debug("Found value {}", value);

			sensorValueRepository.save(sensorValue);
		}

	};


}
