package org.meshmasterserver.system.controller.callback;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;

import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorIdentity;
import org.meshmasterserver.system.controller.database.modell.Valve;
import org.meshmasterserver.system.controller.database.modell.ValveIdentity;
import org.meshmasterserver.system.controller.database.repository.NodeRepository;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationEndpoint {
	private Logger log = LoggerFactory.getLogger(RegistrationEndpoint.class);
	private final NodeRepository nodeRepository;
	private final SensorRepository sensorRepository;
	private final ValveRepository valveRepository;


	@Autowired
	public RegistrationEndpoint(NodeRepository nodeRepository, SensorRepository sensorRepository, ValveRepository valveRepository) {
		this.nodeRepository = nodeRepository;
		this.sensorRepository = sensorRepository;
		this.valveRepository = valveRepository;
	}


	@RequestMapping(value = "/callbacks/submit", method = POST)
	public void submit(@RequestBody RegistrationPayloadData registrationPayloadData) {
		RegistrationPayloadData.RegistrationData registration = registrationPayloadData.getRegistration();

		Node node = findNodeOrCreate(registration);

		if (registration.getModule() == ModuleType.Sensor) {
			Sensor sensor = findSensorOrCreate(node, registration.getIndex(),registration.getPin());
		} else if (registration.getModule() == ModuleType.Valve) {
			Valve valve = findValveOrCreate(node,registration.getIndex(),registration.getPin());
		}
	}

	Valve findValveOrCreate(Node node, int index, int pin) {
		log.info("Valve registration");

		Valve valve = valveRepository.findByIdentityNodeAndIdentityIndex(node, index);
		if (valve == null) {
			valve = new Valve();
			valve.setIdentity(new ValveIdentity(node, index));
			valve.setPin(pin);

			valveRepository.save(valve);
		} else {
			if (valve.getPin() != pin) {
				valve.setPin(pin);

				valveRepository.save(valve);
			}
		}
		return valve;
	}

	Sensor findSensorOrCreate(Node node, int index, int pin) {
		log.info("Sensor registration");

		Sensor sensor = sensorRepository.findByIdentityNodeAndIdentityIndex(node, index);
		if (sensor == null) {
			sensor = new Sensor();
			sensor.setIdentity(new SensorIdentity(node,index));
			sensor.setPin(pin);

			sensorRepository.save(sensor);
		} else {
			if (sensor.getPin() != pin) {
				sensor.setPin(pin);
				sensorRepository.save(sensor);
			}
		}

		return sensor;
	}

	Node findNodeOrCreate(RegistrationPayloadData.RegistrationData registration) {
		List<Node> nodes = nodeRepository.findByMeshNodeId(registration.getNode());
		Node node;
		if (nodes.size() > 0) {
			node = nodes.get(0);
		} else {
			node = new Node();
			node.setMeshNodeId(registration.getNode());

			nodeRepository.save(node);
		}
		return node;
	}
}
