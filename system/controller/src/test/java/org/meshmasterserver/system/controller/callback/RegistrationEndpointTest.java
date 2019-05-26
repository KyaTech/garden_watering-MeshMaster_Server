package org.meshmasterserver.system.controller.callback;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meshmasterserver.system.controller.database.modell.Node;
import org.meshmasterserver.system.controller.database.modell.Sensor;
import org.meshmasterserver.system.controller.database.modell.SensorIdentity;
import org.meshmasterserver.system.controller.database.modell.Valve;
import org.meshmasterserver.system.controller.database.modell.ValveIdentity;
import org.meshmasterserver.system.controller.database.repository.NodeRepository;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RegistrationEndpointTest {


	private RegistrationEndpoint registrationEndpoint;

	@MockBean
	private NodeRepository nodeRepository;

	@MockBean
	private SensorRepository sensorRepository;

	@MockBean
	private ValveRepository valveRepository;

	@Before
	public void setUp() {
		registrationEndpoint = new RegistrationEndpoint(nodeRepository, sensorRepository, valveRepository);
	}

	@Test
	public void test_submit_sensor() {
		RegistrationEndpoint endpointSpy = Mockito.spy(registrationEndpoint);

		RegistrationPayloadData registrationPayloadData = new RegistrationPayloadData();
		RegistrationPayloadData.RegistrationData registrationData = new RegistrationPayloadData.RegistrationData();
		registrationData.setModule(ModuleType.Sensor);
		registrationPayloadData.setRegistration(registrationData);

		Node node = new Node();
		Sensor sensor = new Sensor();
		doReturn(node).when(endpointSpy).findNodeOrCreate(registrationData);
		doReturn(sensor).when(endpointSpy).findSensorOrCreate(eq(node), anyInt(), anyInt());

		endpointSpy.submit(registrationPayloadData);

		verify(endpointSpy, times(1)).findNodeOrCreate(eq(registrationData));
		verify(endpointSpy, times(1)).findSensorOrCreate(eq(node), anyInt(), anyInt());
		verify(endpointSpy, times(0)).findValveOrCreate(any(Node.class), anyInt(), anyInt());
	}

	@Test
	public void test_submit_valve() {
		RegistrationEndpoint endpointSpy = Mockito.spy(registrationEndpoint);

		RegistrationPayloadData registrationPayloadData = new RegistrationPayloadData();
		RegistrationPayloadData.RegistrationData registrationData = new RegistrationPayloadData.RegistrationData();
		registrationData.setModule(ModuleType.Valve);
		registrationPayloadData.setRegistration(registrationData);

		Node node = new Node();
		Valve valve = new Valve();
		doReturn(node).when(endpointSpy).findNodeOrCreate(registrationData);
		doReturn(valve).when(endpointSpy).findValveOrCreate(eq(node), anyInt(), anyInt());

		endpointSpy.submit(registrationPayloadData);

		verify(endpointSpy, times(1)).findNodeOrCreate(eq(registrationData));
		verify(endpointSpy, times(0)).findSensorOrCreate(eq(node), anyInt(), anyInt());
		verify(endpointSpy, times(1)).findValveOrCreate(any(Node.class), anyInt(), anyInt());
	}

	@Test
	public void test_findNodeOrCreate() {
		RegistrationPayloadData.RegistrationData registrationData = new RegistrationPayloadData.RegistrationData();
		registrationData.setModule(ModuleType.Sensor);
		int node_id = 1;
		registrationData.setNode(node_id);

		doReturn(Collections.emptyList()).when(nodeRepository).findByMeshNodeId(node_id);
		doReturn(null).when(nodeRepository).save(any());

		ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);

		registrationEndpoint.findNodeOrCreate(registrationData);

		verify(nodeRepository, times(1)).findByMeshNodeId(node_id);
		verify(nodeRepository, times(1)).save(nodeArgumentCaptor.capture());

		Node capturedNode = nodeArgumentCaptor.getValue();

		assertThat(capturedNode.getMeshNodeId()).isEqualTo(node_id);
	}

	@Test
	public void test_findSensorOrCreate() {

		Node node = new Node(1, "", "", "", 1);

		int index = 0;
		int pin = 1;

		when(sensorRepository.findByIdentityNodeAndIdentityIndex(any(), anyInt())).thenReturn(null);
		doReturn(null).when(sensorRepository).save(any());

		registrationEndpoint.findSensorOrCreate(node, index, pin);


		ArgumentCaptor<Sensor> sensorArgumentCaptor = ArgumentCaptor.forClass(Sensor.class);
		verify(sensorRepository, times(1)).findByIdentityNodeAndIdentityIndex(eq(node), eq(index));
		verify(sensorRepository, times(1)).save(sensorArgumentCaptor.capture());

		Sensor sensor = sensorArgumentCaptor.getValue();
		assertThat(sensor.getIdentity().getIndex()).isEqualTo(index);
		assertThat(sensor.getPin()).isEqualTo(pin);

	}

	@Test
	public void test_findSensorOrCreate_update() {

		Node node = new Node(1, "", "", "", 1);
		int index = 0;

		Sensor sensor = new Sensor();
		sensor.setIdentity(new SensorIdentity(node, index));
		sensor.setPin(3);

		int pin = 1;

		when(sensorRepository.findByIdentityNodeAndIdentityIndex(any(), anyInt())).thenReturn(sensor);
		doReturn(null).when(sensorRepository).save(any());

		registrationEndpoint.findSensorOrCreate(node, index, pin);


		ArgumentCaptor<Sensor> sensorArgumentCaptor = ArgumentCaptor.forClass(Sensor.class);
		verify(sensorRepository, times(1)).findByIdentityNodeAndIdentityIndex(eq(node), eq(index));
		verify(sensorRepository, times(1)).save(sensorArgumentCaptor.capture());

		Sensor newSensor = sensorArgumentCaptor.getValue();
		assertThat(newSensor.getIdentity().getIndex()).isEqualTo(index);
		assertThat(newSensor.getPin()).isEqualTo(pin);

	}

	@Test
	public void test_findValveOrCreate() {

		Node node = new Node(1, "", "", "", 1);

		int index = 0;
		int pin = 1;

		when(valveRepository.findByIdentityNodeAndIdentityIndex(any(), anyInt())).thenReturn(null);
		doReturn(null).when(valveRepository).save(any());

		registrationEndpoint.findValveOrCreate(node, index, pin);


		ArgumentCaptor<Valve> valveArgumentCaptor = ArgumentCaptor.forClass(Valve.class);
		verify(valveRepository, times(1)).findByIdentityNodeAndIdentityIndex(eq(node), eq(index));
		verify(valveRepository, times(1)).save(valveArgumentCaptor.capture());

		Valve valve = valveArgumentCaptor.getValue();
		assertThat(valve.getIdentity().getIndex()).isEqualTo(index);
		assertThat(valve.getPin()).isEqualTo(pin);

	}

	@Test
	public void test_findValveOrCreate_update() {

		Node node = new Node(1, "", "", "", 1);
		Valve valve = new Valve();
		int index = 0;
		valve.setIdentity(new ValveIdentity(node, index));
		valve.setPin(3);

		int pin = 1;

		when(valveRepository.findByIdentityNodeAndIdentityIndex(any(), anyInt())).thenReturn(valve);
		doReturn(null).when(valveRepository).save(any());

		registrationEndpoint.findValveOrCreate(node, index, pin);

		ArgumentCaptor<Valve> valveArgumentCaptor = ArgumentCaptor.forClass(Valve.class);
		verify(valveRepository, times(1)).findByIdentityNodeAndIdentityIndex(eq(node), eq(index));
		verify(valveRepository, times(1)).save(valveArgumentCaptor.capture());

		Valve newValve = valveArgumentCaptor.getValue();
		assertThat(newValve.getIdentity().getIndex()).isEqualTo(index);
		assertThat(newValve.getPin()).isEqualTo(pin);

	}
}