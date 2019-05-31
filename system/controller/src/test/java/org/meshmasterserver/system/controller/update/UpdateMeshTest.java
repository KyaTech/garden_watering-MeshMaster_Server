package org.meshmasterserver.system.controller.update;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorIdentity;
import org.meshmasterserver.system.controller.database.modell.SensorValue;
import org.meshmasterserver.system.controller.database.modell.Valve;
import org.meshmasterserver.system.controller.database.modell.ValveIdentity;
import org.meshmasterserver.system.controller.database.repository.NodeRepository;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.SensorValueRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.meshmasterserver.system.controller.meshcontroller.MeshAssignmentPairHandler;
import org.meshmasterserver.system.controller.meshcontroller.MeshController;
import org.meshmasterserver.system.controller.meshcontroller.ValveState;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private MeshAssignmentPairHandler assignmentPairHandler;

	@Before
	public void setUp() {
		updateMesh = new UpdateMesh(meshController, sensorRepository, valveRepository, sensorValueRepository, nodeRepository, assignmentPairHandler);
	}

	@Test
	public void test_updateSensors() {

		Node node = new Node(1, "", "", "", 1);
		Sensor sensor = new Sensor(new SensorIdentity(node, 0), "", "", 3);
		Sensor sensor1 = new Sensor(new SensorIdentity(node, 1), "", "", 4);


		when(sensorRepository.findByIdentityNode(eq(node))).thenReturn(Arrays.asList(sensor, sensor1));
		when(meshController.requestSensor(eq(1), Mockito.anyInt())).thenReturn(24.0);

		updateMesh.updateSensors(node);
		assignmentPairHandler.run();


		verify(sensorRepository).findByIdentityNode(eq(node));
		verify(meshController, times(1)).requestSensor(eq(1), eq(0));
		verify(meshController, times(1)).requestSensor(eq(1), eq(1));
	}

	@Test
	public void test_saveSensorValue() {
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


	@Test
	public void test_updateValves() {
		Node node = new Node(1, "", "", "", 1);
		Valve valve = new Valve(new ValveIdentity(node, 0), "", "", 3);
		Valve valve1 = new Valve(new ValveIdentity(node, 1), "", "", 4);


		doReturn(Arrays.asList(valve, valve1)).when(valveRepository).findByIdentityNode(eq(node));
		doReturn(ValveState.ON).when(meshController).requestState(eq(node.getMeshNodeId()), eq(valve.getIdentity().getIndex()));
		doReturn(ValveState.OFF).when(meshController).requestState(eq(node.getMeshNodeId()), eq(valve1.getIdentity().getIndex()));

		updateMesh.updateValves(node);
		assignmentPairHandler.run();

		ArgumentCaptor<Valve> valveArgumentCaptor = ArgumentCaptor.forClass(Valve.class);

		verify(valveRepository,times(2)).save(valveArgumentCaptor.capture());

		List<Valve> valvesCaptured = valveArgumentCaptor.getAllValues();

		assertThat(valvesCaptured.size()).isEqualTo(2);

		assertThat(valvesCaptured.get(0).getLastState()).isEqualTo(ValveState.ON);
		assertThat(valvesCaptured.get(1).getLastState()).isEqualTo(ValveState.OFF);
	}
}