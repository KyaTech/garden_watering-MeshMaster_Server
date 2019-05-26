package org.meshmasterserver.system.controller.meshcontroller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meshmasterserver.system.controller.config.Config;
import org.meshmasterserver.system.controller.meshcontroller.http.Request;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MeshControllerApiTest {

	@MockBean
	private Request request;

	private MeshControllerApi meshControllerApi;

	@Autowired
	private Config config;

	@Before
	public void setUp() {
		meshControllerApi = new MeshControllerApi(config, request);
	}

	@Test
	public void test_requestState_stateON() throws IOException {
		

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"state\": \"ON\"\n" +
			"}"));
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestState(1, 0)).isEqualTo(ValveState.ON);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/valves/0", buildApiUri())), eq(Request.MethodType.GET));


	}

	@Test
	public void test_requestSensor_withoutIndex() throws IOException {
		

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"value\": 24\n" +
			"}"));
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestSensor(1)).isEqualTo(24);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/sensors", buildApiUri())), eq(Request.MethodType.GET));
	}

	@Test
	public void test_requestSensor() throws IOException {
		

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"value\": 24\n" +
			"}"));
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestSensor(1, 0)).isEqualTo(24);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/sensors/0", buildApiUri())), eq(Request.MethodType.GET));
	}

	@Test
	public void test_requestBattery() throws IOException {
		

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"battery\": 89\n" +
			"}"));
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestBattery(1)).isEqualTo(89);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/battery", buildApiUri())), eq(Request.MethodType.GET));
	}

	@Test
	public void test_changeValveState() throws IOException {
		

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"message\": \"OK\"\n" +
			"}"));
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.POST))).thenReturn(response);

		assertThat(meshControllerApi.changeValveState(1, 0, ValveState.ON)).isEqualTo(CommandStatus.OK);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/valves/0/ON", buildApiUri())), eq(Request.MethodType.POST));
	}

	@Test
	public void test_turnOnValve() throws IOException {
		

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"message\": \"OK\"\n" +
			"}"));
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.POST))).thenReturn(response);

		assertThat(meshControllerApi.turnOnValve(1, 0)).isEqualTo(CommandStatus.OK);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/valves/0/ON", buildApiUri())), eq(Request.MethodType.POST));
	}

	@Test
	public void test_turnOffValve() throws IOException {
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"message\": \"OK\"\n" +
			"}"));
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.POST))).thenReturn(response);

		assertThat(meshControllerApi.turnOffValve(1, 0)).isEqualTo(CommandStatus.OK);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/valves/0/OFF", buildApiUri())), eq(Request.MethodType.POST));
	}

	@Test
	public void test_throwExceptions_InvalidNode() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree("{\n" +
			"  \"error\": {\n" +
			"    \"type\": \"InvalidNode\"\n" +
			"  }\n" +
			"}");

		try {
			meshControllerApi.throwExceptionIfNecessary(root);
			assertThat(true).withFailMessage("This should throw an InvalidNodeException").isFalse();
		} catch (InvalidNodeException e) {
			assertThat(true).isTrue();
		} catch (Exception e) {
			assertThat(true).withFailMessage("This should throw an InvalidNodeException").isFalse();
		}
	}

	@Test
	public void test_throwExceptions_InvalidIndex() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree("{\n" +
			"  \"error\": {\n" +
			"    \"type\": \"InvalidIndex\"\n" +
			"  }\n" +
			"}");

		try {
			meshControllerApi.throwExceptionIfNecessary(root);
			assertThat(true).withFailMessage("This should throw an InvalidIndexException").isFalse();
		} catch (InvalidIndexException e) {
			assertThat(true).isTrue();
		} catch (Exception e) {
			assertThat(true).withFailMessage("This should throw an InvalidIndexException").isFalse();
		}
	}

	@Test
	public void test_entityToJson() throws IOException {
		String jsonString = "{\n" +
			"  \"error\": {\n" +
			"    \"type\": \"InvalidIndex\"\n" +
			"  }\n" +
			"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNodeValidation = mapper.readTree(jsonString);


		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		StringEntity stringEntity = new StringEntity(jsonNodeValidation.toString());
		response.setEntity(stringEntity);

		JsonNode jsonNode = meshControllerApi.entityToJson(response);

		assertThat(jsonNode).isEqualTo(jsonNodeValidation);
	}

	@Test
	public void test_makeRequestUri() throws URISyntaxException {
		String path = "/test/test/0/1/100";

		String uri = meshControllerApi.makeRequestUri(path);

		assertThat(uri).contains(path).startsWith("http://").contains("/api/v1/");
	}

	@Test
	public void test_makeApiRequest() throws IOException {
		String template = "/nodes/%d/tests/%d";
		String searchedJson = "value";

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"value\": 24.78\n" +
			"}"));

		
		when(request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		JsonNode jsonNode = meshControllerApi.makeApiRequest(searchedJson, template, 1, 0);

		assertThat(jsonNode.asDouble()).isEqualTo(24.78);

		verify(request).makeRequest(eq(String.format("%s/nodes/1/tests/0", buildApiUri())), eq(Request.MethodType.GET));
	}

	private String buildApiUri() {
		return String.format("http://%s:%d/%s", config.getMeshcontroller().getApi().getHost(), config.getMeshcontroller().getApi().getPort(), config.getMeshcontroller().getApi().getPrefix());
	}
}