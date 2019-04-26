package systemcontroller.meshcontroller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import systemcontroller.meshcontroller.http.Request;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Request.class)
public class MeshControllerApiTest {

	@Mock
	private MeshControllerApi meshControllerApi;

	@Before
	public void setUp() {
		meshControllerApi = new MeshControllerApi();
	}

	@Test
	public void test_requestState_stateON() throws IOException {
		PowerMockito.mockStatic(Request.class);

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"state\": \"ON\"\n" +
			"}"));
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestState(1, 0)).isEqualTo(ValveState.ON);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/valves/0"), eq(Request.MethodType.GET));
	}

	@Test
	public void test_requestSensor_withoutIndex() throws IOException {
		PowerMockito.mockStatic(Request.class);

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"value\": 24\n" +
			"}"));
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestSensor(1)).isEqualTo(24);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/sensors"), eq(Request.MethodType.GET));
	}

	@Test
	public void test_requestSensor() throws IOException {
		PowerMockito.mockStatic(Request.class);

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"value\": 24\n" +
			"}"));
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestSensor(1, 0)).isEqualTo(24);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/sensors/0"), eq(Request.MethodType.GET));
	}

	@Test
	public void test_requestBattery() throws IOException {
		PowerMockito.mockStatic(Request.class);

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"battery\": 89\n" +
			"}"));
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		assertThat(meshControllerApi.requestBattery(1)).isEqualTo(89);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/battery"), eq(Request.MethodType.GET));
	}

	@Test
	public void test_changeValveState() throws IOException {
		PowerMockito.mockStatic(Request.class);

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"message\": \"OK\"\n" +
			"}"));
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.POST))).thenReturn(response);

		assertThat(meshControllerApi.changeValveState(1, 0, ValveState.ON)).isEqualTo(CommandStatus.OK);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/valves/0/ON"), eq(Request.MethodType.POST));
	}

	@Test
	public void test_turnOnValve() throws IOException {
		PowerMockito.mockStatic(Request.class);

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"message\": \"OK\"\n" +
			"}"));
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.POST))).thenReturn(response);

		assertThat(meshControllerApi.turnOnValve(1, 0)).isEqualTo(CommandStatus.OK);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/valves/0/ON"), eq(Request.MethodType.POST));
	}

	@Test
	public void test_turnOffValve() throws IOException {
		PowerMockito.mockStatic(Request.class);

		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("Test", 0, 0), 200, "OK"));
		response.setEntity(new StringEntity("{\n" +
			"  \"request_id\": 100,\n" +
			"  \"message\": \"OK\"\n" +
			"}"));
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.POST))).thenReturn(response);

		assertThat(meshControllerApi.turnOffValve(1, 0)).isEqualTo(CommandStatus.OK);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/valves/0/OFF"), eq(Request.MethodType.POST));
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

		String uri = MeshControllerApi.makeRequestUri(path);


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

		PowerMockito.mockStatic(Request.class);
		when(Request.makeRequest(Mockito.anyString(), eq(Request.MethodType.GET))).thenReturn(response);

		JsonNode jsonNode = meshControllerApi.makeApiRequest(searchedJson, template, 1, 0);

		assertThat(jsonNode.asDouble()).isEqualTo(24.78);

		PowerMockito.verifyStatic(Request.class);
		Request.makeRequest(eq("http://192.168.176.6:8080/api/v1/nodes/1/tests/0"), eq(Request.MethodType.GET));

	}
}