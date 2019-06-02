package org.meshmasterserver.system.controller.update;

import static org.meshmasterserver.system.controller.meshcontroller.api.MeshControllerApi.*;

import java.util.List;

import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorValue;
import org.meshmasterserver.system.controller.database.modell.Valve;
import org.meshmasterserver.system.controller.database.repository.NodeRepository;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.SensorValueRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.meshmasterserver.system.controller.meshcontroller.api.MeshController;
import org.meshmasterserver.system.controller.meshcontroller.assignment.AssignmentPair;
import org.meshmasterserver.system.controller.meshcontroller.assignment.AssignmentPairHandler;
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
	private final AssignmentPairHandler assignmentPairHandler;


	@Autowired
	public UpdateMesh(MeshController meshController, SensorRepository sensorRepository, ValveRepository valveRepository, SensorValueRepository sensorValueRepository, NodeRepository nodeRepository, AssignmentPairHandler assignmentPairHandler) {
		this.nodeRepository = nodeRepository;
		this.sensorRepository = sensorRepository;
		this.valveRepository = valveRepository;
		this.meshController = meshController;
		this.sensorValueRepository = sensorValueRepository;
		this.assignmentPairHandler = assignmentPairHandler;
	}

	@Override
	public void run() {

		log.info("Updating mesh nodes");

		for (Node node : nodeRepository.findAll()) {

			updateSensors(node);

			updateValves(node);

			assignmentPairHandler.run();

		}

	}

	void updateValves(Node node) {

		List<Valve> valves = valveRepository.findByIdentityNode(node);
		log.info("Updating valves");


		for (Valve valve : valves) {

			assignmentPairHandler.add(new AssignmentPair<>(() -> meshController.requestState(node.getMeshNodeId(), valve.getIdentity().getIndex()), valveState -> {
				valve.setLastState(valveState);
				valveRepository.save(valve);
				log.debug("Got new valve state for {} {}", valve.getIdentity(), valveState);
			}));

		}


	}


	void updateSensors(Node node) {
		List<Sensor> sensors = sensorRepository.findByIdentityNode(node);

		log.info("Updating sensors");

		for (Sensor sensor : sensors) {
			assignmentPairHandler.add(new AssignmentPair<>(() -> meshController.requestSensor(node.getMeshNodeId(), sensor.getIdentity().getIndex()), value -> saveSensorValue(sensor, value)));
		}
	}

	void saveSensorValue(Sensor sensor, Double value) {

		log.debug("Saving sensor values");

		if (value != INVALID_VALUE) {
			SensorValue sensorValue = new SensorValue(sensor, value);

			log.debug("Got new sensor state for {} {}", sensor.getIdentity(), value);

			sensorValueRepository.save(sensorValue);
		}

	}


}
