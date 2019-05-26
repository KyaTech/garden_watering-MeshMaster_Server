package org.meshmasterserver.system.controller.update;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorIdentity;
import org.meshmasterserver.system.controller.database.modell.SensorValue;
import org.meshmasterserver.system.controller.database.repository.NodeRepository;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.SensorValueRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.meshmasterserver.system.controller.meshcontroller.MeshController;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateMeshTest {

	private UpdateMesh updateMesh;

	@MockBean
	private NodeRepository nodeRepository;

	@MockBean
	private SensorRepository sensorRepository;

	@MockBean
	private ValveRepository valveRepository;

	@MockBean
	private MeshController meshController;

	@MockBean
	private SensorValueRepository sensorValueRepository;

	@Before
	public void setUp() throws Exception {
		updateMesh = new UpdateMesh(meshController, sensorRepository, valveRepository, sensorValueRepository, nodeRepository);
	}

	@Test
	public void run() {

	}

	@Test
	public void updateSensors() {

		Node node = new Node(1, "", "", "", 1);
		Sensor sensor = new Sensor(new SensorIdentity(node, 0), "", "", 3);
		Sensor sensor1 = new Sensor(new SensorIdentity(node, 1), "", "", 4);


		when(sensorRepository.findByIdentityNode(eq(node))).thenReturn(Arrays.asList(sensor, sensor1));
		when(meshController.requestSensor(eq(1), Mockito.anyInt())).thenReturn(24.0);

		updateMesh.updateSensors(node);

		verify(sensorRepository).findByIdentityNode(eq(node));
		verify(meshController, times(1)).requestSensor(eq(1), eq(0));
		verify(meshController, times(1)).requestSensor(eq(1), eq(1));
	}

	@Test
	public void saveSensorValue() {
		Node node = new Node(1, "", "", "", 1);
		Sensor sensor = new Sensor(new SensorIdentity(node, 0), "", "", 3);
		double value = 24.0;

		ArgumentCaptor<SensorValue> sensorValue = ArgumentCaptor.forClass(SensorValue.class);

		updateMesh.saveSensorValue(sensor, value);

		verify(sensorValueRepository).save(sensorValue.capture());

		SensorValue capturedValue = sensorValue.getValue();
		assertThat(capturedValue.getSensor()).isEqualTo(sensor);
		assertThat(capturedValue.getValue()).isEqualTo(value);
	}
}